package io.github.racoondog.meteorsharedaddonutils.mixin.mixin;

import io.github.racoondog.meteorsharedaddonutils.mixin.mixininterface.IValueMap;
import meteordevelopment.starscript.value.Value;
import meteordevelopment.starscript.value.ValueMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
@Mixin(value = ValueMap.class, remap = false)
public class ValueMapMixin implements IValueMap {
    @Shadow @Final private Map<String, Supplier<Value>> values;

    @Override
    public void remove(String key) {
        this.values.remove(key);
    }
}
