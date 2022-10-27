package io.github.racoondog.meteorsharedaddonutils.utils;

import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class ColorUtils {
    /**
     * @param packedRGB Packed {@code int} form of RGB {@code #RRGGBB}.
     * @return {@link SettingColor} object.
     */
    public static SettingColor fromPackedRGB(int packedRGB) {
        return new SettingColor((packedRGB >> 16) & 0xFF, (packedRGB >> 8) & 0xFF, packedRGB & 0xFF);
    }

    /**
     * @param packedARGB Packed {@code int} form of ARGB {@code #AARRGGBB}.
     * @return {@link SettingColor} object.
     */
    public static SettingColor fromPackedARGB(int packedARGB) {
        return new SettingColor((packedARGB >> 16) & 0xFF, (packedARGB >> 8) & 0xFF, packedARGB & 0xFF, (packedARGB >> 24) & 0xFF);
    }

    /**
     * @param packedRGBA Packed {@code int} form of RGBA {@code #RRGGBBAA}.
     * @return {@link SettingColor} object.
     */
    public static SettingColor fromPackedRGBA(int packedRGBA) {
        return new SettingColor((packedRGBA >> 24) & 0xFF, (packedRGBA >> 16) & 0xFF, (packedRGBA >> 8) & 0xFF, packedRGBA & 0xFF);
    }
}
