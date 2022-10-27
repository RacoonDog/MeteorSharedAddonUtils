package io.github.racoondog.meteorsharedaddonutils.mixin;

import io.github.racoondog.meteorsharedaddonutils.mixininterface.IMeteorAddon;
import meteordevelopment.meteorclient.addons.AddonManager;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = AddonManager.class, remap = false)
public abstract class AddonManagerMixin {
    @Inject(method = "init", at = @At("TAIL"))
    private static void injectTail(CallbackInfo ci) {
        for (var entrypoint : FabricLoader.getInstance().getEntrypointContainers("meteor", MeteorAddon.class)) {
            ModMetadata metadata = entrypoint.getProvider().getMetadata();
            MeteorAddon addon = entrypoint.getEntrypoint();

            ((IMeteorAddon) addon).setId(metadata.getId());
        }
    }
}
