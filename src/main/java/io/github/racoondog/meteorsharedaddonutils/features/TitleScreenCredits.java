package io.github.racoondog.meteorsharedaddonutils.features;

import io.github.racoondog.meteorsharedaddonutils.MeteorSharedAddonUtils;
import it.unimi.dsi.fastutil.objects.ObjectBooleanImmutablePair;
import it.unimi.dsi.fastutil.objects.ObjectBooleanPair;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.addons.AddonManager;
import meteordevelopment.meteorclient.addons.GithubRepo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.gui.GuiThemes;
import meteordevelopment.meteorclient.gui.screens.CommitsScreen;
import meteordevelopment.meteorclient.utils.network.Http;
import meteordevelopment.meteorclient.utils.network.MeteorExecutor;
import meteordevelopment.meteorclient.utils.render.color.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

import static meteordevelopment.meteorclient.MeteorClient.mc;

@Environment(EnvType.CLIENT)
public class TitleScreenCredits {
    @FunctionalInterface
    public interface DrawFunction {
        void accept(MatrixStack matrices, Credit credit, int y);
    }

    public static final int WHITE = Color.fromRGBA(255, 255, 255, 255);
    public static final int GRAY = Color.fromRGBA(175, 175, 175, 255);
    public static final int RED = Color.fromRGBA(225, 25, 25, 255);

    private static final HashMap<MeteorAddon, ObjectBooleanPair<DrawFunction>> customTitleScreenDrawFunctions = new HashMap<>();
    private static final List<Credit> credits = new ArrayList<>();
    private static boolean initialized = false;
    private static final List<Pair<MeteorAddon, Consumer<Credit>>> creditModificationQueue = new ArrayList<>();

    /**
     * Registers a custom title screen credit drawing function.
     *
     * <pre>{@code
     * TitleScreenCredits.registerCustomDrawFunction(this, (matrices, credit, y) -> {
     *     int newY = mc.currentScreen.height - 22;
     *     String text = "Powered by MeteorSharedAddonUtils!";
     *     int width = mc.textRenderer.getWidth(text);
     *     int x = mc.currentScreen.width - 3 - width;
     *
     *     mc.textRenderer.drawWithShadow(matrices, text, x, newY, Color.fromRGBA(255, 255, 0, 255));
     * }, false);
     * }</pre>
     *
     * @param addon The {@link MeteorAddon}` object. Usually {@code this} if used in the addon's {@link MeteorAddon#onInitialize()} method. Can also be another addon to override their title screen credit.
     * @param function A TriConsumer which gives the {@link MatrixStack}, the addon's {@link TitleScreenCredits.Credit} object and the current Y position on the normal title screen credit list. Y position is based on the {@link TitleScreenCredits.Credit} object's width.
     * @param updateY Should be {@code true} if the custom title screen credit appears in the normal credit list and {@code false} if credit is removed or moved somewhere else.
     */
    @SuppressWarnings("SuspiciousNameCombination")
    public static void registerCustomDrawFunction(MeteorAddon addon, DrawFunction function, boolean updateY) {
        customTitleScreenDrawFunctions.put(addon, new ObjectBooleanImmutablePair<>(function, updateY));
    }

    /**
     * Removes the selected addon's title screen credit.
     *
     * <pre>{@code TitleScreenCredits.registerEmptyDrawFunction(this);}</pre>
     *
     * @param addon The {@link MeteorAddon} who's title screen credit is to be removed. Usually {@code this} if used in the addon's {@link MeteorAddon#onInitialize()} method. Can also be another addon to remove their title screen credit.
     */
    public static void registerEmptyDrawFunction(MeteorAddon addon) {
        customTitleScreenDrawFunctions.put(addon, new ObjectBooleanImmutablePair<>(((matrixStack, credit, i) -> {}), false));
    }

    /**
     * Unregisters the given {@link MeteorAddon}'s custom drawing function.
     *
     * <pre>{@code TitleScreenCredits.unregisterCustomDrawFunction(this);}</pre>
     *
     * @param addon Target {@link MeteorAddon}.
     */
    public static void unregisterCustomDrawFunction(MeteorAddon addon) {
        customTitleScreenDrawFunctions.remove(addon);
    }

    /**
     * Modifies the given {@link MeteorAddon}'s credit.
     *
     * <pre>{@code TitleScreenCredits.modifyAddonCredit(this, credit -> credit.append("!", TitleScreenCredits.WHITE));}</pre>
     *
     * @param addon Target {@link MeteorAddon}.
     * @param creditConsumer The {@link Credit} object to modify.
     */
    public static void modifyAddonCredit(MeteorAddon addon, Consumer<Credit> creditConsumer) {
        if (!initialized) {
            creditModificationQueue.add(new Pair(addon, creditConsumer));
            return;
        }

        for (var credit : credits) {
            if (credit.addon.equals(addon)) {
                creditConsumer.accept(credit);
                if (credit.outdated && !hasOutdated(credit)) credit.sections.add(1, new OutdatedMarker());
                if (initialized) sortCredits(); //Sorting will happen during initialization, so don't sort if not initialized
                return;
            }
        }
        MeteorSharedAddonUtils.LOG.warn("A credit could not be found for addon {}.", addon.name);
    }

    private static void init() {
        initialized = true;

        add(MeteorClient.ADDON);
        AddonManager.ADDONS.forEach(TitleScreenCredits::add);

        creditModificationQueue.forEach(pair -> modifyAddonCredit(pair.getLeft(), pair.getRight()));

        sortCredits();

        MeteorExecutor.execute(() -> {
            for (var credit : credits) {
                GithubRepo repo = credit.addon.getRepo();
                String commit = credit.addon.getCommit();
                if (repo == null || commit == null) continue;

                Response res = Http.get(String.format("https://api.github.com/repos/%s/branches/%s", repo.getOwnerName(), repo.branch())).sendJson(Response.class);

                if (res != null && !commit.equals(res.commit.sha)) {
                    credit.outdated = true;
                    synchronized (credit.sections) {
                        credit.sections.add(1, new OutdatedMarker());
                        credit.calculateWidth();
                    }
                }
            }
        });
    }

    private static void add(MeteorAddon addon) {
        if (addon.name == null) return;
        Credit credit = new Credit(addon);
        Color color = addon.color;

        credit.sections.add(new Section(addon.name, color == null ? RED : color.getPacked()));

        if (addon.authors != null && addon.authors.length >= 1) {
            credit.sections.add(new Section(" by ", GRAY));
            for (int i = 0; i < addon.authors.length; i++) {
                if (i > 0) {
                    credit.sections.add(new Section(i == addon.authors.length - 1 ? " & " : ", ", GRAY));
                }

                credit.sections.add(new Section(addon.authors[i], WHITE));
            }
        }

        credit.calculateWidth();
        credits.add(credit);
    }

    public static void render(MatrixStack matrixStack) {
        if (credits.isEmpty()) init();

        int y = 3;
        for (var credit : credits) {
            if (customTitleScreenDrawFunctions.containsKey(credit.addon)) {
                ObjectBooleanPair<DrawFunction> pair = customTitleScreenDrawFunctions.get(credit.addon);
                pair.left().accept(matrixStack, credit, y);
                if (pair.rightBoolean()) y += mc.textRenderer.fontHeight + 2;
            } else {
                int x = mc.currentScreen.width - 3 - credit.width;

                synchronized (credit.sections) {
                    for (var section : credit.sections) {
                        mc.textRenderer.drawWithShadow(matrixStack, section.text, x, y, section.color);
                        x += section.width;
                    }
                }

                y += mc.textRenderer.fontHeight + 2;
            }
        }
    }

    public static boolean onClicked(double mouseX, double mouseY) {
        int y = 3;
        for (var credit : credits) {
            if (customTitleScreenDrawFunctions.containsKey(credit.addon) && !customTitleScreenDrawFunctions.get(credit.addon).rightBoolean()) continue;

            int x = mc.currentScreen.width - 3 - credit.width;

            if (mouseX >= x && mouseX <= x + credit.width && mouseY >= y && mouseY <= y + mc.textRenderer.fontHeight + 2) {
                if (credit.addon.getRepo() != null && credit.addon.getCommit() != null) {
                    mc.setScreen(new CommitsScreen(GuiThemes.get(), credit.addon));
                    return true;
                }
            }

            y += mc.textRenderer.fontHeight + 2;
        }

        return false;
    }

    private static void sortCredits() {
        credits.sort(Comparator.comparingInt(value -> value.sections.get(0).text.equals("Meteor Client ") ? Integer.MIN_VALUE : -value.width));
    }

    private static boolean hasOutdated(Credit credit) {
        for (var section : credit.sections) {
            if (section instanceof OutdatedMarker) return true;
        }
        return false;
    }

    public static class Credit {
        public final MeteorAddon addon;
        public final List<Section> sections = new ArrayList<>();
        public int width = 0;
        public boolean outdated = false;

        public Credit(MeteorAddon addon) {
            this.addon = addon;
        }

        public void calculateWidth() {
            width = 0;
            for (var section : sections) width += section.width;
        }

        public void append(String text, int color) {
            this.sections.add(new Section(text, color));
            this.calculateWidth();
        }
    }

    public static class Section {
        public final String text;
        public final int color, width;

        public Section(String text, int color) {
            this.text = text;
            this.color = color;
            this.width = mc.textRenderer.getWidth(text);
        }
    }

    public static class OutdatedMarker extends Section {

        public OutdatedMarker() {
            super("*", RED);
        }
    }

    public static class Response {
        public Commit commit;
    }

    public static class Commit {
        public String sha;
    }
}
