package io.github.racoondog.meteorsharedaddonutils.utils;

import io.github.racoondog.meteorsharedaddonutils.mixininterface.IMeteorAddon;
import meteordevelopment.meteorclient.addons.AddonManager;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

@Environment(EnvType.CLIENT)
public final class AddonUtils {
    /**
     * @param addon {@link MeteorAddon} object.
     * @return The addon's id defined in it's {@code fabric.mod.json} file.
     */
    public static String getAddonId(MeteorAddon addon) {
        return ((IMeteorAddon) addon).getId();
    }

    /**
     * @param addon {@link MeteorAddon} object.
     * @return The addon's version string defined in it's {@code fabric.mod.json} file.
     */
    public static String getAddonVersion(MeteorAddon addon) {
        return ((IMeteorAddon) addon).getVersion();
    }

    /**
     * @param addonNames One or more addon names.
     * @return {@code true} if any one of the specified addons is present.
     */
    public static boolean areAddonNamesPresent(String... addonNames) {
        if (cachedNameAddons == null) generateNameCache();
        for (var id : addonNames) {
            if (cachedNameAddons.containsKey(id)) return true;
        }
        return false;
    }

    /**
     * @param addonIds One or more addon ids. See {@link #getAddonId(MeteorAddon)} for how to get a {@link MeteorAddon}'s id.
     * @return {@code true} if any one of the specified addons is present.
     */
    public static boolean areAddonIdsPresent(String... addonIds) {
        if (cachedIdAddons == null) generateIdCache();
        for (var id : addonIds) {
            if (cachedIdAddons.containsKey(id)) return true;
        }
        return false;
    }

    /**
     * @param addonName The target {@link MeteorAddon}'s name.
     * @return A {@link MeteorAddon} object associated with the target name, if found.
     */
    @Nullable
    public static MeteorAddon getFromName(String addonName) {
        if (cachedNameAddons == null) generateNameCache();
        return cachedNameAddons.getOrDefault(addonName, null);
    }


    /**
     * @param addonId The target {@link MeteorAddon}'s id.
     * @return A {@link MeteorAddon} object associated with the target id, if found.
     */
    @Nullable
    public static MeteorAddon getFromId(String addonId) {
        if (cachedIdAddons == null) generateIdCache();
        return cachedIdAddons.getOrDefault(addonId, null);
    }

    private static HashMap<String, MeteorAddon> cachedNameAddons;
    private static HashMap<String, MeteorAddon> cachedIdAddons;
    private static void generateNameCache() {
        cachedNameAddons = new HashMap<>();
        for (var addon : AddonManager.ADDONS) {
            cachedNameAddons.put(addon.name, addon);
        }
    }
    private static void generateIdCache() {
        cachedIdAddons = new HashMap<>();
        for (var addon : AddonManager.ADDONS) {
            cachedIdAddons.put(getAddonId(addon), addon);
        }
    }
}
