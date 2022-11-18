package io.github.racoondog.meteorsharedaddonutils.utils;

import meteordevelopment.meteorclient.utils.render.color.Color;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Style;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public final class ColorUtils {
    /**
     * @param packedRgb Packed {@code int} form of RGB {@code #RRGGBB}.
     * @return {@link SettingColor} object.
     */
    public static SettingColor fromPackedRgb(int packedRgb) {
        return new SettingColor((packedRgb >> 16) & 0xFF, (packedRgb >> 8) & 0xFF, packedRgb & 0xFF);
    }

    /**
     * @param packedArgb Packed {@code int} form of ARGB {@code #AARRGGBB}.
     * @return {@link SettingColor} object.
     */
    public static SettingColor fromPackedArgb(int packedArgb) {
        return new SettingColor((packedArgb >> 16) & 0xFF, (packedArgb >> 8) & 0xFF, packedArgb & 0xFF, (packedArgb >> 24) & 0xFF);
    }

    /**
     * @param packedRgba Packed {@code int} form of RGBA {@code #RRGGBBAA}.
     * @return {@link SettingColor} object.
     */
    public static SettingColor fromPackedRgba(int packedRgba) {
        return new SettingColor((packedRgba >> 24) & 0xFF, (packedRgba >> 16) & 0xFF, (packedRgba >> 8) & 0xFF, packedRgba & 0xFF);
    }

    /**
     * @param formatting {@link Formatting} color object.
     * @return {@link SettingColor} object that is {@link Color#WHITE} if {@link Formatting} does not contain a color value.
     */
    public static SettingColor fromFormatting(Formatting formatting) {
        return fromFormatting(formatting, (SettingColor) Color.WHITE);
    }

    /**
     * @param formatting {@link Formatting} color object.
     * @param fallback {@link SettingColor} object that is returned if {@link Formatting} does not contain a color value.
     * @return {@link SettingColor} object.
     */
    public static SettingColor fromFormatting(Formatting formatting, SettingColor fallback) {
        return formatting.isColor() ? fromPackedRgb(formatting.getColorValue()) : fallback;
    }

    /**
     * @param textColor {@link TextColor} color object.
     * @return {@link SettingColor} object that is {@link Color#WHITE} if {@link TextColor} is null.
     */
    public static SettingColor fromTextColor(@Nullable TextColor textColor) {
        return fromTextColor(textColor, (SettingColor) Color.WHITE);
    }

    /**
     * @param textColor {@link TextColor} color object.
     * @param fallback {@link SettingColor} object that is returned if {@link TextColor} is null.
     * @return {@link SettingColor} object.
     */
    public static SettingColor fromTextColor(@Nullable TextColor textColor, SettingColor fallback) {
        return textColor != null ? fromPackedRgb(textColor.getRgb()) : fallback;
    }

    /**
     * @param style {@link Style} object.
     * @return {@link SettingColor} object that is {@link Color#WHITE} if {@link Style} does not contain a color value.
     */
    public static SettingColor fromStyle(Style style) {
        return fromStyle(style, (SettingColor) Color.WHITE);
    }

    /**
     * @param style {@link Style} object.
     * @param fallback {@link SettingColor} object that is returned if {@link Style} does not contain a color value.
     * @return {@link SettingColor} object.
     */
    public static SettingColor fromStyle(Style style, SettingColor fallback) {
        return fromTextColor(style.getColor(), fallback);
    }

    /**
     * @param color {@link Color} color object.
     * @return {@link TextColor} object.
     */
    public static TextColor textColorFromColor(Color color) {
        return TextColor.fromRgb(color.getPacked());
    }

    /**
     * @param color {@link Color} color object.
     * @return {@link Style} object with only the {@link Color} color value.
     */
    public static Style styleFromColor(Color color) {
        return Style.EMPTY.withColor(color.getPacked());
    }

    /**
     * @param style base {@link Style} object.
     * @param color {@link Color} color object.
     * @return {@link Style} param with {@link Color} color object added as color value.
     */
    public static Style styleWithColor(Style style, Color color) {
        return style.withColor(color.getPacked());
    }
}
