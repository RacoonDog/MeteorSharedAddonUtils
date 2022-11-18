package io.github.racoondog.meteorsharedaddonutils.mixin.mixininterface;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface IValueMap {
    void remove(String key);
}
