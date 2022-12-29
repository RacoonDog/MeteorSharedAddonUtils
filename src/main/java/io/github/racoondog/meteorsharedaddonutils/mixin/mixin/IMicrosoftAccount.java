package io.github.racoondog.meteorsharedaddonutils.mixin.mixin;

import meteordevelopment.meteorclient.systems.accounts.types.MicrosoftAccount;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * @author Crosby
 * @since 1.2.3
 */
@Environment(EnvType.CLIENT)
@Mixin(value = MicrosoftAccount.class, remap = false)
public interface IMicrosoftAccount {
    @Invoker String invokeAuth();
}
