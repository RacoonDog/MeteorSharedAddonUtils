package io.github.racoondog.meteorsharedaddonutils.mixin.modmenu;

import com.terraformersmc.modmenu.util.mod.fabric.FabricMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Environment(EnvType.CLIENT)
@Mixin(value = FabricMod.class, remap = false)
public interface IFabricMod {
    @Accessor("metadata")
    @NotNull ModMetadata getMetadata();
}
