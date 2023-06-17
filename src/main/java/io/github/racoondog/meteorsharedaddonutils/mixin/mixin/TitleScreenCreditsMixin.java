package io.github.racoondog.meteorsharedaddonutils.mixin.mixin;

import meteordevelopment.meteorclient.utils.player.TitleScreenCredits;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(value = TitleScreenCredits.class, remap = false)
public abstract class TitleScreenCreditsMixin {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private static void injectRender(DrawContext context, CallbackInfo ci) {
        io.github.racoondog.meteorsharedaddonutils.features.TitleScreenCredits.render(context);
        ci.cancel();
    }

    @Inject(method = "onClicked", at = @At("HEAD"), cancellable = true)
    private static void injectOnClicked(double mouseX, double mouseY, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(io.github.racoondog.meteorsharedaddonutils.features.TitleScreenCredits.onClicked(mouseX, mouseY));
        cir.cancel();
    }
}
