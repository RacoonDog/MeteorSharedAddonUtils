package io.github.racoondog.meteorsharedaddonutils.mixin.mixin.modmenu;

import com.terraformersmc.modmenu.util.mod.Mod;
import com.terraformersmc.modmenu.util.mod.ModBadgeRenderer;
import io.github.racoondog.meteorsharedaddonutils.utils.ModMetaUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = ModBadgeRenderer.class, remap = false)
public abstract class ModBadgeRendererMixin {
    @Shadow protected Mod mod;
    @Shadow public abstract void drawBadge(DrawContext context, OrderedText text, int outlineColor, int fillColor, int mouseX, int mouseY);

    @Inject(method = "draw", at = @At("TAIL"))
    private void addMeteorBadge(DrawContext context, int mouseX, int mouseY, CallbackInfo ci) {
        if (ModMetaUtils.hasModBadge(this.mod, "meteor")) drawBadge(context, Text.literal("Meteor").asOrderedText(), 0xFF913DE2, 0x99913DE2, mouseX, mouseY);
    }
}
