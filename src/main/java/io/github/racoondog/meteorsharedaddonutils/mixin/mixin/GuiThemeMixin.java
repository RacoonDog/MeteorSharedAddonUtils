package io.github.racoondog.meteorsharedaddonutils.mixin.mixin;

import io.github.racoondog.meteorsharedaddonutils.features.RecolorGuiTheme;
import io.github.racoondog.meteorsharedaddonutils.mixin.mixininterface.IGuiTheme;
import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.utils.SettingsWidgetFactory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(EnvType.CLIENT)
@Mixin(value = GuiTheme.class, remap = false)
public abstract class GuiThemeMixin implements IGuiTheme {
    @Shadow @Final @Mutable public String name;

    @Shadow protected SettingsWidgetFactory settingsFactory;

    @Redirect(method = "<init>", at = @At(value = "FIELD", target = "Lmeteordevelopment/meteorclient/gui/GuiTheme;name:Ljava/lang/String;"))
    private void rename(GuiTheme instance, String value) {
        if (instance instanceof RecolorGuiTheme recolorGuiTheme) name = recolorGuiTheme.getName();
        else name = value;
    }

    @Override
    public SettingsWidgetFactory getSettingsWidgetFactory() {
        return this.settingsFactory;
    }
}
