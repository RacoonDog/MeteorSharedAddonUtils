package io.github.racoondog.meteorsharedaddonutils.features;

import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.gui.GuiKeyEvents;
import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.renderer.GuiDebugRenderer;
import meteordevelopment.meteorclient.gui.renderer.GuiRenderer;
import meteordevelopment.meteorclient.gui.utils.Cell;
import meteordevelopment.meteorclient.gui.widgets.WRoot;
import meteordevelopment.meteorclient.gui.widgets.WWidget;
import meteordevelopment.meteorclient.gui.widgets.containers.WContainer;
import meteordevelopment.meteorclient.gui.widgets.input.WTextBox;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.meteorclient.utils.misc.CursorStyle;
import meteordevelopment.meteorclient.utils.misc.input.Input;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static meteordevelopment.meteorclient.MeteorClient.mc;
import static org.lwjgl.glfw.GLFW.*;

/**
 * Methods which contain the comment OVERRIDE need to be manually called from the parent screen.
 */
@Environment(EnvType.CLIENT)
public abstract class ScreenContainer {
    private static final GuiRenderer RENDERER = new GuiRenderer();
    private static final GuiDebugRenderer DEBUG_RENDERER = new GuiDebugRenderer();

    public Runnable taskAfterRender;
    protected Runnable enterAction;

    public Screen parent;
    private final WContainer root;

    public final GuiTheme theme;

    public boolean locked, lockedAllowClose;
    private boolean closed;
    private boolean onClose;
    private boolean debug;

    private double lastMouseX, lastMouseY;

    public double animProgress;

    private List<Runnable> onClosed;

    protected boolean firstInit = true;

    public ScreenContainer(GuiTheme theme) {
        this.parent = mc.currentScreen;
        this.root = new WScreenRoot(theme);
        this.theme = theme;
    }

    public <W extends WWidget> Cell<W> add(W widget) {
        return root.add(widget);
    }

    public void clear() {
        root.clear();
    }

    public void invalidate() {
        root.invalidate();
    }

    //OVERRIDE
    public void init() {
        MeteorClient.EVENT_BUS.subscribe(this);

        closed = false;

        if (firstInit) {
            firstInit = false;
            initWidgets();
        }
    }

    public abstract void initWidgets();

    public void reload() {
        clear();
        initWidgets();
    }

    public void onClosed(Runnable action) {
        if (onClosed == null) onClosed = new ArrayList<>(2);
        onClosed.add(action);
    }

    //OVERRIDE
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (locked) return false;

        double s = mc.getWindow().getScaleFactor();
        mouseX *= s;
        mouseY *= s;

        return root.mouseClicked(mouseX, mouseY, button, false);
    }

    //OVERRIDE
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (locked) return false;

        double s = mc.getWindow().getScaleFactor();
        mouseX *= s;
        mouseY *= s;

        return root.mouseReleased(mouseX, mouseY, button);
    }

    //OVERRIDE
    public void mouseMoved(double mouseX, double mouseY) {
        if (locked) return;

        double s = mc.getWindow().getScaleFactor();
        mouseX *= s;
        mouseY *= s;

        root.mouseMoved(mouseX, mouseY, lastMouseX, lastMouseY);

        lastMouseX = mouseX;
        lastMouseY = mouseY;
    }

    //OVERRIDE
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (locked) return false;

        root.mouseScrolled(amount);

        return false;
    }

    //OVERRIDE
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (locked) return false;

        if ((modifiers == GLFW_MOD_CONTROL || modifiers == GLFW_MOD_SUPER) && keyCode == GLFW_KEY_9) {
            debug = !debug;
            return true;
        }

        if ((keyCode == GLFW_KEY_ENTER || keyCode == GLFW_KEY_KP_ENTER) && enterAction != null) {
            enterAction.run();
            return true;
        }

        return false;
    }

    //OVERRIDE
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (locked) return false;

        if (root.keyPressed(keyCode, modifiers)) return true;

        // Select next text box if TAB was pressed
        if (keyCode == GLFW_KEY_TAB) {
            AtomicReference<WTextBox> firstTextBox = new AtomicReference<>(null);
            AtomicBoolean done = new AtomicBoolean(false);
            AtomicBoolean foundFocused = new AtomicBoolean(false);

            loopWidgets(root, wWidget -> {
                if (done.get() || !(wWidget instanceof WTextBox textBox)) return;

                if (foundFocused.get()) {
                    textBox.setFocused(true);
                    textBox.setCursorMax();

                    done.set(true);
                }
                else {
                    if (textBox.isFocused()) {
                        textBox.setFocused(false);
                        foundFocused.set(true);
                    }
                }

                if (firstTextBox.get() == null) firstTextBox.set(textBox);
            });

            if (!done.get() && firstTextBox.get() != null) {
                firstTextBox.get().setFocused(true);
                firstTextBox.get().setCursorMax();
            }

            return true;
        }

        boolean control = MinecraftClient.IS_SYSTEM_MAC ? modifiers == GLFW_MOD_SUPER : modifiers == GLFW_MOD_CONTROL;

        if (control && keyCode == GLFW_KEY_C && toClipboard()) {
            return true;
        }
        else if (control && keyCode == GLFW_KEY_V && fromClipboard()) {
            reload();
            return true;
        }

        return false;
    }

    public void keyRepeated(int key, int modifiers) {
        if (locked) return;

        root.keyRepeated(key, modifiers);
    }

    //OVERRIDE
    public boolean charTyped(char chr, int keyCode) {
        if (locked) return false;

        return root.charTyped(chr);
    }

    //OVERRIDE
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        double s = mc.getWindow().getScaleFactor();
        mouseX *= s;
        mouseY *= s;

        animProgress += delta / 20 * 14;
        animProgress = Utils.clamp(animProgress, 0, 1);

        GuiKeyEvents.canUseKeys = true;

        // Apply projection without scaling
        Utils.unscaledProjection();

        onRenderBefore(delta);

        RENDERER.theme = theme;
        theme.beforeRender();

        RENDERER.begin(matrices);
        RENDERER.setAlpha(animProgress);
        root.render(RENDERER, mouseX, mouseY, delta / 20);
        RENDERER.setAlpha(1);
        RENDERER.end(matrices);

        boolean tooltip = RENDERER.renderTooltip(mouseX, mouseY, delta / 20, matrices);

        if (debug) {
            DEBUG_RENDERER.render(root, matrices);
            if (tooltip) DEBUG_RENDERER.render(RENDERER.tooltipWidget, matrices);
        }

        Utils.scaledProjection();

        runAfterRenderTasks();
    }

    protected void runAfterRenderTasks() {
        if (taskAfterRender != null) {
            taskAfterRender.run();
            taskAfterRender = null;
        }
    }

    protected void onRenderBefore(float delta) {}

    //OVERRIDE
    public void resize(MinecraftClient client, int width, int height) {
        root.invalidate();
    }

    //OVERRIDE
    public void close() {
        if (!locked || lockedAllowClose) {
            boolean preOnClose = onClose;
            onClose = true;

            removed();

            onClose = preOnClose;
        }
    }

    //OVERRIDE
    public void removed() {
        if (!closed || lockedAllowClose) {
            closed = true;
            onClosed();

            Input.setCursorStyle(CursorStyle.Default);

            loopWidgets(root, widget -> {
                if (widget instanceof WTextBox textBox) {

                    if (textBox.isFocused()) textBox.setFocused(false);
                }
            });

            MeteorClient.EVENT_BUS.unsubscribe(this);
            GuiKeyEvents.canUseKeys = true;

            if (onClosed != null) {
                for (Runnable action : onClosed) action.run();
            }
        }
    }

    private void loopWidgets(WWidget widget, Consumer<WWidget> action) {
        action.accept(widget);

        if (widget instanceof WContainer) {
            for (Cell<?> cell : ((WContainer) widget).cells) loopWidgets(cell.widget(), action);
        }
    }

    protected void onClosed() {}

    public boolean toClipboard() {
        return false;
    }

    public boolean fromClipboard() {
        return false;
    }

    public static class WScreenRoot extends WContainer implements WRoot {
        private boolean valid = false;

        public WScreenRoot(GuiTheme theme) {
            this.theme = theme;
        }

        @Override
        public void invalidate() {
            valid = false;
        }

        @Override
        protected void onCalculateSize() {
            width = Utils.getWindowWidth();
            height = Utils.getWindowHeight();
        }

        @Override
        protected void onCalculateWidgetPositions() {
            for (var cell : cells) {
                cell.x = 0;
                cell.y = 0;

                cell.width = width;
                cell.height = height;

                cell.alignWidget();
            }
        }

        @Override
        public boolean render(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
            if (!valid) {
                calculateSize();
                calculateWidgetPositions();

                valid = true;

                mouseMoved(mc.mouse.getX(), mc.mouse.getY(), mc.mouse.getX(), mc.mouse.getY());
            }

            return super.render(renderer, mouseX, mouseY, delta);
        }
    }
}
