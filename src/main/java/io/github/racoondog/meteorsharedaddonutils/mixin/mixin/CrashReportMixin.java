package io.github.racoondog.meteorsharedaddonutils.mixin.mixin;

import io.github.racoondog.meteorsharedaddonutils.utils.AddonUtils;
import meteordevelopment.meteorclient.addons.AddonManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.crash.CrashReport;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(CrashReport.class)
public abstract class CrashReportMixin {
    /**
     * @author Crosby
     * @since 1.2.1
     */
    @Inject(method = "addStackTrace", at = @At("TAIL"))
    private void appendAddonsToStackTrace(StringBuilder sb, CallbackInfo ci) {
        if (AddonManager.ADDONS.isEmpty()) return;

        sb.append("\n\n-- Meteor Client Addons --\n\n");

        for (var addon : AddonManager.ADDONS) {
            sb.append(addon.name);

            List<String> authors = new ArrayList<>(List.of(addon.authors));
            authors.removeIf(s -> s.equals("seasnail") || s.equals("seasnail8169"));

            if (!authors.isEmpty()) {
                sb.append(" (");
                for (int i = 0; i < authors.size(); i++) {
                    if (i > 0) sb.append(i == authors.size() - 1 ? " & " : ", ");
                    sb.append(authors.get(i));
                }
                sb.append(')');
            }

            sb.append("; v").append(AddonUtils.getAddonVersion(addon));

            String commit = addon.getCommit();
            if (commit != null && !commit.isBlank()) sb.append("; commit ").append(commit);
            sb.append('\n');
        }
    }
}
