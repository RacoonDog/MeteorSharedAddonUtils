package io.github.racoondog.meteorsharedaddonutils.mixin.mixin;

import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.systems.modules.misc.swarm.Swarm;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * @author Crosby
 * @since 1.2.3
 */
@Environment(EnvType.CLIENT)
@Mixin(value = Swarm.class, remap = false)
public interface ISwarm {
    @Accessor Setting<String> getIpAddress();
    @Accessor Setting<Integer> getServerPort();
}
