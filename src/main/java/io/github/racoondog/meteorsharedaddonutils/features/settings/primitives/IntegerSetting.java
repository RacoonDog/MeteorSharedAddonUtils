package io.github.racoondog.meteorsharedaddonutils.features.settings.primitives;

import com.google.common.collect.ImmutableList;
import io.github.racoondog.meteorsharedaddonutils.utils.ThemeUtils;
import it.unimi.dsi.fastutil.ints.IntConsumer;
import meteordevelopment.meteorclient.gui.utils.SettingsWidgetFactory;
import meteordevelopment.meteorclient.gui.widgets.input.WIntEdit;
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
 * {@link Setting} implementation using primitive types unlike {@link meteordevelopment.meteorclient.settings.IntSetting}.
 * Does not extend {@link meteordevelopment.meteorclient.settings.IntSetting} due to its private constructor.
 * Variable type should be {@link IntegerSetting} when using this implementation in order to access the new methods.
 * Using a primitive type instead of the wrapper object allows us to prevent unnecessary boxing/unboxing and use {@link IntConsumer}'s which also use primitive types.
 * Only drawback with this implementation is the presence of unused {@code defaultValue}, {@code value}, and {@code onChanged} superclass variables.
 * @author Crosby
 */
@Environment(EnvType.CLIENT)
public class IntegerSetting extends Setting<Integer> {
    private static final List<String> SUGGESTIONS = ImmutableList.of("0", "3", "10");

    protected final int min, max, sliderMin, sliderMax, defaultValue;
    protected int value;
    public final boolean noSlider;
    protected final IntConsumer onChanged;

    protected IntegerSetting(String name, String description, int defaultValue, IntConsumer onChanged, Consumer<Setting<Integer>> onModuleActivated, IVisible visible, int min, int max, int sliderMin, int sliderMax, boolean noSlider) {
        super(name, description, defaultValue, onChanged, onModuleActivated, visible);
        this.defaultValue = defaultValue;
        this.onChanged = onChanged;

        this.min = min;
        this.max = max;
        this.sliderMin = sliderMin;
        this.sliderMax = sliderMax;
        this.noSlider = noSlider;
    }

    public int getInteger() {
        return value;
    }

    /**
     * @deprecated Use {@link IntegerSetting#getInteger()} instead.
     * @author Crosby
     */
    @Deprecated
    @Override
    public Integer get() {
        return value;
    }

    public boolean setInteger(int value) {
        this.value = value;
        onChanged();
        return true;
    }

    /**
     * @deprecated Use {@link IntegerSetting#setInteger(int)} instead.
     * @author Crosby
     */
    @Deprecated
    @Override
    public boolean set(Integer value) {
        this.value = value;
        onChanged();
        return true;
    }

    @Override
    protected void resetImpl() {
        value = defaultValue;
    }

    public int getDefaultIntegerValue() {
        return this.defaultValue;
    }

    /**
     * @deprecated Use {@link IntegerSetting#getDefaultIntegerValue()} instead.
     * @author Crosby
     */
    @Deprecated
    @Override
    public Integer getDefaultValue() {
        return this.defaultValue;
    }

    @Override
    public boolean parse(String str) {
        Integer newValue = parseImpl(str);

        if (newValue != null) {
            if (isValueValid(newValue)) {
                value = newValue;
                onChanged();
            }
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
     * Using the {@link Integer} wrapper object for nullability
     */
    @Nullable
    @Override
    protected Integer parseImpl(String str) {
        try {
            return Integer.parseInt(str.trim());
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    @Override
    protected boolean isValueValid(Integer value) {
        return value >= min && value <= max;
    }

    @Override
    public List<String> getSuggestions() {
        return SUGGESTIONS;
    }

    @Override
    protected NbtCompound save(NbtCompound tag) {
        tag.putInt("value", getInteger());

        return tag;
    }

    @Override
    protected Integer load(NbtCompound tag) {
        setInteger(tag.getInt("value"));

        return getInteger();
    }

    /**
     * Registers the setting factory.
     * @author Crosby
     * @todo make better description
     */
    @PreInit
    public static void registerSetting() {
        SettingsWidgetFactory.registerCustomFactory(IntegerSetting.class, (theme) -> (table, s) -> {
            IntegerSetting setting = (IntegerSetting) s;
            WIntEdit edit = table.add(theme.intEdit(setting.getInteger(), setting.min, setting.max, setting.sliderMin, setting.sliderMax, setting.noSlider)).expandX().widget();

            edit.action = () -> {
                if (!setting.setInteger(edit.get())) edit.set(setting.getInteger());
            };

            ThemeUtils.reset(theme, table, setting, () -> edit.set(setting.getInteger()));
        });
    }

    public static class Builder extends SettingBuilder<Builder, Integer, IntegerSetting> {
        private int min = Integer.MIN_VALUE, max = Integer.MAX_VALUE;
        private int sliderMin = 0, sliderMax = 10, defaultValue = 0;
        private boolean noSlider = false;
        protected IntConsumer onChanged;

        public Builder() {
            super(0);
        }

        public Builder min(int min) {
            this.min = min;
            return this;
        }

        public Builder max(int max) {
            this.max = max;
            return this;
        }

        public Builder range(int min, int max) {
            this.min = Math.min(min, max);
            this.max = Math.max(min, max);
            return this;
        }

        public Builder sliderMin(int min) {
            this.sliderMin = min;
            return this;
        }

        public Builder sliderMax(int max) {
            this.sliderMax = max;
            return this;
        }

        public Builder sliderRange(int min, int max) {
            this.sliderMin = min;
            this.sliderMax = max;
            return this;
        }

        public Builder noSlider() {
            noSlider = true;
            return this;
        }

        @Override
        public IntegerSetting build() {
            return new IntegerSetting(name, description, defaultValue, onChanged, onModuleActivated, visible, min, max, Math.max(sliderMin, min), Math.min(sliderMax, max), noSlider);
        }

        /**
         * Replaces {@link Consumer} with primitive-based {@link IntConsumer}.
         * @author Crosby
         */
        public Builder onChanged(IntConsumer onChanged) {
            this.onChanged = onChanged;
            return this;
        }

        /**
         * Replaces {@link Integer} with int primitive.
         * @author Crosby
         */
        public Builder defaultValue(int defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }
    }
}
