package io.github.racoondog.meteorsharedaddonutils.utils;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
@FunctionalInterface
public interface ObjectObjectIntConsumer<T, E> {
    void accept(T t, E e, int i);
}
