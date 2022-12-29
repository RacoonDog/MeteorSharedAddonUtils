package io.github.racoondog.meteorsharedaddonutils.features.settings;

import io.github.racoondog.meteorsharedaddonutils.utils.ColorUtils;
import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.WindowScreen;
import meteordevelopment.meteorclient.gui.renderer.GuiRenderer;
import meteordevelopment.meteorclient.gui.widgets.WQuad;
import meteordevelopment.meteorclient.gui.widgets.containers.WHorizontalList;
import meteordevelopment.meteorclient.gui.widgets.containers.WTable;
import meteordevelopment.meteorclient.gui.widgets.input.WIntEdit;
import meteordevelopment.meteorclient.gui.widgets.pressable.WButton;
import meteordevelopment.meteorclient.gui.widgets.pressable.WCheckbox;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.utils.render.color.Color;
import net.minecraft.text.Style;

import java.util.ArrayList;
import java.util.List;

import static meteordevelopment.meteorclient.MeteorClient.mc;

public class StyleSettingScreen extends WindowScreen {
    private final Setting<Style> setting;
    public final Color color;
    private WQuad displayQuad;
    private WIntEdit rItb, gItb, bItb;
    private WCheckbox bold, italic, underlined, strikethrough, obfuscated;
    public Runnable action;

    public StyleSettingScreen(GuiTheme theme, Setting<Style> setting) {
        super(theme, "Select Style");

        this.setting = setting;
        this.color = ColorUtils.fromStyle(setting.get()).a(255);
    }

    @Override
    public boolean toClipboard() {
        List<String> tokens = new ArrayList<>();
        if (setting.get().isBold()) tokens.add("bold");
        if (setting.get().isItalic()) tokens.add("italic");
        if (setting.get().isUnderlined()) tokens.add("underlined");
        if (setting.get().isStrikethrough()) tokens.add("strikethrough");
        if (setting.get().isObfuscated()) tokens.add("obfuscated");

        tokens.add(color.r + "," + color.g + "," + color.b);

        String string = String.join(",", tokens);
        mc.keyboard.setClipboard(string);
        return mc.keyboard.getClipboard().equals(string);
    }

    @Override
    public boolean fromClipboard() {
        String str = mc.keyboard.getClipboard().trim();

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

        setting.set(style);

        return true;
    }

    private void callAction() {
        if (action != null) action.run();
    }

    private void updateColor() {
        color.r = rItb.get();
        color.g = gItb.get();
        color.b = bItb.get();

        color.validate();

        if (color.r != rItb.get()) rItb.set(color.r);
        if (color.g != gItb.get()) gItb.set(color.g);
        if (color.b != bItb.get()) bItb.set(color.b);

        displayQuad.color.set(color);

        setting.set(setting.get().withColor(color.getPacked()));
        setting.onChanged();
        callAction();
    }

    private void setFromSetting() {
        color.set(ColorUtils.fromStyle(setting.get()));

        if (color.r != rItb.get()) rItb.set(color.r);
        if (color.g != gItb.get()) gItb.set(color.g);
        if (color.b != bItb.get()) bItb.set(color.b);

        bold.checked = setting.get().isBold();
        italic.checked = setting.get().isItalic();
        underlined.checked = setting.get().isUnderlined();
        strikethrough.checked = setting.get().isStrikethrough();
        obfuscated.checked = setting.get().isObfuscated();

        displayQuad.color.set(color);
    }

    @Override
    public void initWidgets() {
        displayQuad = add(theme.quad(color)).expandX().widget();

        WTable rgbTable = add(theme.table()).expandX().widget();

        rgbTable.add(theme.label("R:"));
        rItb = rgbTable.add(theme.intEdit(color.r, 0, 255, 0, 255, false)).expandX().widget();
        rItb.action = this::updateColor;
        rgbTable.row();

        rgbTable.add(theme.label("G:"));
        gItb = rgbTable.add(theme.intEdit(color.g, 0, 255, 0, 255, false)).expandX().widget();
        gItb.action = this::updateColor;
        rgbTable.row();

        rgbTable.add(theme.label("B:"));
        bItb = rgbTable.add(theme.intEdit(color.b, 0, 255, 0, 255, false)).expandX().widget();
        bItb.action = this::updateColor;
        rgbTable.row();

        WTable modifierTable = add(theme.table()).expandX().widget();

        WHorizontalList boldList = modifierTable.add(theme.horizontalList()).expandX().widget();
        boldList.add(theme.label("Bold: "));
        bold = boldList.add(theme.checkbox(setting.get().isBold())).expandCellX().right().widget();
        bold.action = () -> {
            setting.set(setting.get().withBold(bold.checked));
            setting.onChanged();
        };
        modifierTable.row();

        WHorizontalList italicList = modifierTable.add(theme.horizontalList()).expandX().widget();
        italicList.add(theme.label("Italic: "));
        italic = italicList.add(theme.checkbox(setting.get().isItalic())).expandCellX().right().widget();
        italic.action = () -> {
            setting.set(setting.get().withItalic(italic.checked));
            setting.onChanged();
        };
        modifierTable.row();

        WHorizontalList underlinedList = modifierTable.add(theme.horizontalList()).expandX().widget();
        underlinedList.add(theme.label("Underlined: "));
        underlined = underlinedList.add(theme.checkbox(setting.get().isUnderlined())).expandCellX().right().widget();
        underlined.action = () -> {
            setting.set(setting.get().withUnderline(underlined.checked));
            setting.onChanged();
        };
        modifierTable.row();

        WHorizontalList strikethroughList = modifierTable.add(theme.horizontalList()).expandX().widget();
        strikethroughList.add(theme.label("Strikethrough: "));
        strikethrough = strikethroughList.add(theme.checkbox(setting.get().isStrikethrough())).expandCellX().right().widget();
        strikethrough.action = () -> {
            setting.set(setting.get().withStrikethrough(strikethrough.checked));
            setting.onChanged();
        };
        modifierTable.row();

        WHorizontalList obfuscatedList = modifierTable.add(theme.horizontalList()).expandX().widget();
        obfuscatedList.add(theme.label("Obfuscated: "));
        obfuscated = obfuscatedList.add(theme.checkbox(setting.get().isObfuscated())).expandCellX().right().widget();
        obfuscated.action = () -> {
            setting.set(setting.get().withObfuscated(obfuscated.checked));
            setting.onChanged();
        };

        // Bottom
        WHorizontalList bottomList = add(theme.horizontalList()).expandX().widget();

        WButton backButton = bottomList.add(theme.button("Back")).expandX().widget();
        backButton.action = this::close;

        WButton resetButton = bottomList.add(theme.button(GuiRenderer.RESET)).widget();
        resetButton.action = () -> {
            setting.reset();
            setFromSetting();
            callAction();
        };
    }
}
