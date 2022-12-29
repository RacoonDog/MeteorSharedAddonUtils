package io.github.racoondog.meteorsharedaddonutils.mixin.mixin;

import meteordevelopment.meteorclient.systems.accounts.Account;
import meteordevelopment.meteorclient.systems.accounts.Accounts;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

/**
 * @author Crosby
 * @since 1.2.3
 */
@Environment(EnvType.CLIENT)
@Mixin(value = Accounts.class, remap = false)
public interface IAccounts {
    @Accessor
    List<Account<?>> getAccounts();
}
