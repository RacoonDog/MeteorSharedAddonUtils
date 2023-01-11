package io.github.racoondog.meteorsharedaddonutils.mixin.mixin.modmenu;

import com.terraformersmc.modmenu.util.mod.Mod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Function;
import java.util.stream.Stream;

@Environment(EnvType.CLIENT)
@Mixin(value = Mod.Badge.class, remap = false)
public abstract class BadgeMixin {
    @Redirect(method = "convert", at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;map(Ljava/util/function/Function;)Ljava/util/stream/Stream;"))
    private static Stream<Mod.Badge> preventNpe(Stream<String> instance, Function<? super String, ? extends Mod.Badge> function) {
        return instance.filter(key -> !key.equals("meteor")).map(function);
    }
}
