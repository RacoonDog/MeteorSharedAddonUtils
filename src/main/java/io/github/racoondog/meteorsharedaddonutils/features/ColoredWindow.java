package io.github.racoondog.meteorsharedaddonutils.features;

import io.github.racoondog.meteorsharedaddonutils.utils.ColorUtils;
import meteordevelopment.meteorclient.gui.renderer.GuiRenderer;
import meteordevelopment.meteorclient.gui.themes.meteor.widgets.WMeteorWindow;
import meteordevelopment.meteorclient.gui.widgets.WWidget;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/**
 * Allows making windows with a custom accent color instead of the one defined by the currently selected theme.
 *
 * <pre>{@code
 * new ColoredWindow(null, tab.name, 255, 201, 58);
 * }</pre>
 */
@Environment(EnvType.CLIENT)
public class ColoredWindow extends WMeteorWindow {
    private final SettingColor accentColor;

    public ColoredWindow(WWidget icon, String title, int accentColor) {
        super(icon, title);
        this.accentColor = ColorUtils.fromPackedRGB(accentColor);
    }

    public ColoredWindow(WWidget icon, String title, SettingColor accentColor) {
        super(icon, title);
        this.accentColor = accentColor;
    }

    public ColoredWindow(WWidget icon, String title, int r, int g, int b) {
        super(icon, title);
        this.accentColor = new SettingColor(r, g, b);
    }

    @Override
    protected WHeader header(WWidget icon) {
        return new WHeader(icon) {
            @Override
            protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
                renderer.quad(this, accentColor);
            }
        };
    }
}
