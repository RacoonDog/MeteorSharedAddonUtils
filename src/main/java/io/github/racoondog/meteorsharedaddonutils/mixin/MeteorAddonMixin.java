package io.github.racoondog.meteorsharedaddonutils.mixin;

import io.github.racoondog.meteorsharedaddonutils.mixininterface.IMeteorAddon;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Environment(EnvType.CLIENT)
@Mixin(value = MeteorAddon.class, remap = false)
public abstract class MeteorAddonMixin implements IMeteorAddon {
    @Unique private String id;
    @Unique private String version;

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String string) {
        this.id = string;
    }

    @Override
    public String getVersion() {
        return this.version;
    }

    @Override
    public void setVersion(String string) {
        this.version = string;
    }
}
