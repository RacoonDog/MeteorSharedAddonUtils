package io.github.racoondog.meteorsharedaddonutils.features.settings;

import com.google.common.collect.ImmutableList;
import io.github.racoondog.meteorsharedaddonutils.utils.ColorUtils;
import io.github.racoondog.meteorsharedaddonutils.utils.ThemeUtils;
import meteordevelopment.meteorclient.gui.renderer.GuiRenderer;
import meteordevelopment.meteorclient.gui.utils.SettingsWidgetFactory;
import meteordevelopment.meteorclient.gui.widgets.WQuad;
import meteordevelopment.meteorclient.gui.widgets.containers.WHorizontalList;
import meteordevelopment.meteorclient.gui.widgets.pressable.WButton;
import meteordevelopment.meteorclient.settings.IVisible;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.utils.PreInit;
import meteordevelopment.meteorclient.utils.render.color.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Style;
import net.minecraft.text.TextColor;

import java.util.List;
import java.util.function.Consumer;

import static meteordevelopment.meteorclient.MeteorClient.mc;

/**
 * @author Crosby
 * @since 1.2
 */
@Environment(EnvType.CLIENT)
public class StyleSetting extends Setting<Style> {
    private static final List<String> SUGGESTIONS = ImmutableList.of("bold,italic,255,255,255", "0,0,0", "obfuscated,70,70,225");

    public StyleSetting(String name, String description, Style defaultValue, Consumer<Style> onChanged, Consumer<Setting<Style>> onModuleActivated, IVisible visible) {
        super(name, description, defaultValue, onChanged, onModuleActivated, visible);
    }

    @Override
    protected Style parseImpl(String str) {
        Style style = Style.EMPTY
                .withBold(str.contains("bold"))
                .withItalic(str.contains("italic"))
                .withUnderline(str.contains("underlined"))
                .withStrikethrough(str.contains("strikethrough"))
                .withObfuscated(str.contains("obfuscated"));

        String[] tokens = str.split(",");

        style.withColor(new Color(
                Integer.parseInt(tokens[tokens.length - 3]),
                Integer.parseInt(tokens[tokens.length - 2]),
                Integer.parseInt(tokens[tokens.length - 1]),
                255
        ).getPacked());

        return style;
    }

    @Override
    public List<String> getSuggestions() {
        return SUGGESTIONS;
    }

    @Override
    protected boolean isValueValid(Style value) {
        return true;
    }

    @Override
    protected NbtCompound save(NbtCompound tag) {
        if (value.isEmpty()) return tag;
        if (value.isBold()) tag.putBoolean("bold", true);
        if (value.isItalic()) tag.putBoolean("italic", true);
        if (value.isUnderlined()) tag.putBoolean("underlined", true);
        if (value.isStrikethrough()) tag.putBoolean("strikethrough", true);
        if (value.isObfuscated()) tag.putBoolean("obfuscated", true);

        TextColor color = value.getColor();
        if (color != null) tag.putInt("color", color.getRgb());

        return tag;
    }

    @Override
    protected Style load(NbtCompound tag) {
        Style style = Style.EMPTY
                .withBold(tag.contains("bold"))
                .withItalic(tag.contains("italic"))
                .withUnderline(tag.contains("underlined"))
                .withStrikethrough(tag.contains("strikethrough"))
                .withObfuscated(tag.contains("obfuscated"));

        if (tag.contains("color")) style.withColor(tag.getInt("color"));

        return style;
    }

    @PreInit
    public static void preInit() {
        SettingsWidgetFactory.registerCustomFactory(StyleSetting.class, (theme) -> (table, s) -> {
            StyleSetting setting = (StyleSetting) s;

            WHorizontalList list = table.add(theme.horizontalList()).expandX().widget();

            WQuad quad = list.add(theme.quad(ColorUtils.fromStyle(setting.get()).a(255))).widget();

            WButton edit = table.add(theme.button(GuiRenderer.EDIT)).widget();
            edit.action = () -> {
                StyleSettingScreen screen = new StyleSettingScreen(theme, setting);
                screen.action = () -> quad.color = screen.color;
                mc.setScreen(screen);
            };

            ThemeUtils.reset(theme, table, setting, () -> quad.color = ColorUtils.fromStyle(setting.get()).a(255));
        });
    }

    public static class Builder extends SettingBuilder<Builder, Style, StyleSetting> {
        public Builder() {
            super(Style.EMPTY);
        }

        @Override
        public StyleSetting build() {
            return new StyleSetting(name, description, defaultValue, onChanged, onModuleActivated, visible);
        }
    }
}
