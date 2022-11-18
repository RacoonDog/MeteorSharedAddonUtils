package io.github.racoondog.meteorsharedaddonutils.features.settings.primitives;

import com.google.common.collect.ImmutableList;
import io.github.racoondog.meteorsharedaddonutils.utils.ThemeUtils;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import meteordevelopment.meteorclient.gui.utils.SettingsWidgetFactory;
import meteordevelopment.meteorclient.gui.widgets.pressable.WCheckbox;
import meteordevelopment.meteorclient.settings.IVisible;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.utils.PreInit;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

/**
 * {@link Setting} implementation using primitive types unlike {@link meteordevelopment.meteorclient.settings.BoolSetting}.
 * Does not extend {@link meteordevelopment.meteorclient.settings.BoolSetting} due to its private constructor.
 * Variable type should be {@link BooleanSetting} instead of {@link Setting} when using this implementation in order to access the new methods.
 * Using a primitive type instead of the wrapper object allows us to prevent unnecessary boxing/unboxing, remove null-checks and use {@link BooleanConsumer}'s which also use primitive types.
 * Only drawback with this implementation is the presence of unused {@code defaultValue}, {@code value}, and {@code onChanged} superclass variables.
 * @author Crosby
 */
@Environment(EnvType.CLIENT)
public class BooleanSetting extends Setting<Boolean> {
    private static final List<String> SUGGESTIONS = ImmutableList.of("true", "false", "toggle");

    protected final boolean defaultValue;
    protected boolean value;
    protected final BooleanConsumer onChanged;

    protected BooleanSetting(String name, String description, boolean defaultValue, BooleanConsumer onChanged, Consumer<Setting<Boolean>> onModuleActivated, IVisible visible) {
        super(name, description, defaultValue, onChanged, onModuleActivated, visible);
        this.defaultValue = defaultValue;
        this.onChanged = onChanged;
    }

    public boolean getBoolean() {
        return value;
    }

    /**
     * @deprecated Use {@link BooleanSetting#getBoolean()} instead.
     * @author Crosby
     */
    @Deprecated
    @Override
    public Boolean get() {
        return value;
    }

    public boolean setBoolean(boolean value) {
        this.value = value;
        onChanged();
        return true;
    }

    /**
     * @deprecated Use {@link BooleanSetting#setBoolean(boolean)} instead.
     * @author Crosby
     */
    @Deprecated
    @Override
    public boolean set(Boolean value) {
        this.value = value;
        onChanged();
        return true;
    }

    @Override
    protected void resetImpl() {
        value = defaultValue;
    }

    public boolean getDefaultBooleanValue() {
        return this.defaultValue;
    }

    /**
     * @deprecated Use {@link BooleanSetting#getDefaultBooleanValue()} instead.
     * @author Crosby
     */
    @Deprecated
    @Override
    public Boolean getDefaultValue() {
        return this.defaultValue;
    }

    @Override
    public boolean parse(String str) {
        Boolean newValue = parseImpl(str);

        if (newValue != null) {
            value = newValue;
            onChanged();
            return true;
        }

        return false;
    }

    @Override
    public boolean wasChanged() {
        return value != defaultValue;
    }

    @Override
    public void onChanged() {
        if (onChanged != null) onChanged.accept(value);
    }

    /**
     * Using the {@link Boolean} wrapper object for nullability
     */
    @Nullable
    @Override
    protected Boolean parseImpl(String str) {
        if (str.equalsIgnoreCase("true") || str.equalsIgnoreCase("1")) return true;
        else if (str.equalsIgnoreCase("false") || str.equalsIgnoreCase("0")) return false;
        else if (str.equalsIgnoreCase("toggle")) return !getBoolean();
        return null;
    }

    @Override
    protected boolean isValueValid(Boolean value) {
        return true;
    }

    @Override
    public List<String> getSuggestions() {
        return SUGGESTIONS;
    }

    @Override
    public NbtCompound save(NbtCompound tag) {
        tag.putBoolean("value", getBoolean());

        return tag;
    }

    @Override
    public Boolean load(NbtCompound tag) {
        setBoolean(tag.getBoolean("value"));

        return getBoolean();
    }

    /**
     * Registers the setting factory.
     * @author Crosby
     * @todo make better description
     */
    @PreInit
    public static void registerSetting() {
        SettingsWidgetFactory.registerCustomFactory(BooleanSetting.class, (theme) -> (table, s) -> {
            BooleanSetting setting = (BooleanSetting) s;
            WCheckbox checkbox = table.add(theme.checkbox(setting.getBoolean())).expandCellX().widget();
            checkbox.action = () -> setting.setBoolean(checkbox.checked);

            ThemeUtils.reset(theme, table, setting, () -> checkbox.checked = setting.getBoolean());
        });
    }

    public static class Builder extends SettingBuilder<Builder, Boolean, BooleanSetting> {
        protected BooleanConsumer onChanged;
        protected boolean defaultValue = false;

        public Builder() {
            super(false);
        }

        @Override
        public BooleanSetting build() {
            return new BooleanSetting(name, description, defaultValue, onChanged, onModuleActivated, visible);
        }

        /**
         * Replaces {@link Consumer} with primitive-based {@link BooleanConsumer}.
         * @author Crosby
         */
        public Builder onChanged(BooleanConsumer onChanged) {
            this.onChanged = onChanged;
            return this;
        }

        /**
         * Replaces {@link Boolean} with boolean primitive.
         * @author Crosby
         */
        public Builder defaultValue(boolean defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }
    }
}

