package io.github.racoondog.meteorsharedaddonutils.mixin.mixininterface;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface IMeteorAddon {
    String getId();
    void setId(String string);

    String getVersion();
    void setVersion(String string);
}
