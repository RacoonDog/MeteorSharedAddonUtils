package io.github.racoondog.meteorsharedaddonutils.mixin.mixin;

import meteordevelopment.meteorclient.utils.network.MeteorExecutor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Environment(EnvType.CLIENT)
@Mixin(value = MeteorExecutor.class, remap = false)
public abstract class MeteorExecutorMixin {
    /**
     * @author Crosby
     * @reason Named thread executor
     * @since 1.2.7
     */
    @Redirect(method = "init", at = @At(value = "INVOKE", target = "Ljava/util/concurrent/Executors;newSingleThreadExecutor()Ljava/util/concurrent/ExecutorService;"))
    private static ExecutorService namedThreadExecutor() {
        return Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, "Meteor-Executor-Thread");
            t.setDaemon(false);
            t.setPriority(Thread.NORM_PRIORITY);
            return t;
        });
    }
}
