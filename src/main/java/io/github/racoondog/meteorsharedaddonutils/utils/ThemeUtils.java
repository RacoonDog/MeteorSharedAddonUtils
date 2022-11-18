package io.github.racoondog.meteorsharedaddonutils.utils;

import io.github.racoondog.meteorsharedaddonutils.mixin.mixininterface.IGuiTheme;
import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.renderer.GuiRenderer;
import meteordevelopment.meteorclient.gui.utils.SettingsWidgetFactory;
import meteordevelopment.meteorclient.gui.widgets.containers.WContainer;
import meteordevelopment.meteorclient.gui.widgets.pressable.WButton;
import meteordevelopment.meteorclient.settings.Setting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class ThemeUtils {
    /**
     * Returns a {@link GuiTheme}'s {@link SettingsWidgetFactory} object.
     */
    public static SettingsWidgetFactory getSettingsFactory(GuiTheme theme) {
        return ((IGuiTheme) theme).getSettingsWidgetFactory();
    }

    // DefaultSettingsWidgetFactory methods, but public

    public static void selectW(GuiTheme theme, WContainer c, Setting<?> setting, Runnable action) {
        WButton button = c.add(theme.button("Select")).expandCellX().widget();
        button.action = action;

        reset(theme, c, setting, null);
    }
    public static void reset(GuiTheme theme, WContainer c, Setting<?> setting, Runnable action) {
        WButton reset = c.add(theme.button(GuiRenderer.RESET)).widget();
        reset.action = () -> {
            setting.reset();
            if (action != null) action.run();
        };
    }
}
