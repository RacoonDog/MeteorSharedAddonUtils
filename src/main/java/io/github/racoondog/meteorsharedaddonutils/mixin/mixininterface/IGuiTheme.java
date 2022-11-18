package io.github.racoondog.meteorsharedaddonutils.mixin.mixininterface;

import meteordevelopment.meteorclient.gui.utils.SettingsWidgetFactory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface IGuiTheme {
    SettingsWidgetFactory getSettingsWidgetFactory();
}
