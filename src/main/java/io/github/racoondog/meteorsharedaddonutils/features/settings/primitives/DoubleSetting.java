package io.github.racoondog.meteorsharedaddonutils.features.settings.primitives;

import com.google.common.collect.ImmutableList;
import io.github.racoondog.meteorsharedaddonutils.utils.ThemeUtils;
import it.unimi.dsi.fastutil.doubles.DoubleConsumer;
import meteordevelopment.meteorclient.gui.utils.SettingsWidgetFactory;
import meteordevelopment.meteorclient.gui.widgets.input.WDoubleEdit;
import meteordevelopment.meteorclient.settings.IVisible;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.utils.PreInit;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.NbtCompound;

import java.util.List;
import java.util.function.Consumer;

/**
 * {@link Setting} implementation using primitive types unlike {@link meteordevelopment.meteorclient.settings.DoubleSetting}.
 * Does not extend {@link meteordevelopment.meteorclient.settings.DoubleSetting} due to its private constructor.
 * Variable type should be {@link DoubleSetting} when using this implementation in order to access the new methods.
 * Using a primitive type instead of the wrapper object allows us to prevent unnecessary boxing/unboxing and use {@link DoubleConsumer}'s which also use primitive types.
 * Only drawback with this implementation is the presence of unused {@code defaultValue}, {@code value}, and {@code onChanged} superclass variables.
 * @author Crosby
 */
@Environment(EnvType.CLIENT)
public class DoubleSetting extends Setting<Double> {
    private static final List<String> SUGGESTIONS = ImmutableList.of("0.0D, 3.5D, 10.75D");

    public final double min, max, sliderMin, sliderMax, defaultValue;
    public final int decimalPlaces;
    public final boolean onSliderRelease, noSlider;
    protected double value;
    protected final DoubleConsumer onChanged;

    public DoubleSetting(String name, String description, double defaultValue, DoubleConsumer onChanged, Consumer<Setting<Double>> onModuleActivated, IVisible visible, double min, double max, double sliderMin, double sliderMax, boolean onSliderRelease, int decimalPlaces, boolean noSlider) {
        super(name, description, defaultValue, onChanged, onModuleActivated, visible);
        this.defaultValue = defaultValue;
        this.onChanged = onChanged;

        this.min = min;
        this.max = max;
        this.sliderMin = sliderMin;
        this.sliderMax = sliderMax;
        this.decimalPlaces = decimalPlaces;
        this.onSliderRelease = onSliderRelease;
        this.noSlider = noSlider;
    }

    public double getDouble() {
        return value;
    }

    /**
     * @deprecated Use {@link DoubleSetting#getDouble()} instead.
     * @author Crosby
     */
    @Deprecated
    @Override
    public Double get() {
        return value;
    }
    
    public boolean setDouble(double value) {
        this.value = value;
        onChanged();
        return true;
    }

    /**
     * @deprecated Use {@link DoubleSetting#setDouble(double)} instead.
     * @author Crosby
     */
    @Deprecated
    @Override
    public boolean set(Double value) {
        this.value = value;
        onChanged();
        return true;
    }
    
    @Override
    protected void resetImpl() {
        value = defaultValue;
    }
    
    public double getDefaultDoubleValue() {
        return this.defaultValue;
    }

    /**
     * @deprecated Use {@link DoubleSetting#getDefaultDoubleValue()} instead.
     * @author Crosby
     */
    @Deprecated
    @Override
    public Double getDefaultValue() {
        return this.defaultValue;
    }

    @Override
    public boolean parse(String str) {
        Double newValue = parseImpl(str);

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
     * Using the {@link Double} wrapper object for nullability
     */
    @Override
    protected Double parseImpl(String str) {
        try {
            return Double.parseDouble(str.trim());
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    @Override
    protected boolean isValueValid(Double value) {
        return value >= min && value <= max;
    }

    @Override
    public List<String> getSuggestions() {
        return SUGGESTIONS;
    }

    @Override
    protected NbtCompound save(NbtCompound tag) {
        tag.putDouble("value", getDouble());
        
        return tag;
    }

    @Override
    protected Double load(NbtCompound tag) {
        setDouble(tag.getDouble("value"));
        
        return getDouble();
    }

    /**
     * Registers the setting factory.
     * @author Crosby
     * @todo make better description
     */
    @PreInit
    public static void registerSetting() {
        SettingsWidgetFactory.registerCustomFactory(DoubleSetting.class, (theme) -> (table, s) -> {
            DoubleSetting setting = (DoubleSetting) s;
            WDoubleEdit edit = theme.doubleEdit(setting.getDouble(), setting.min, setting.max, setting.sliderMin, setting.sliderMax, setting.decimalPlaces, setting.noSlider);
            table.add(edit).expandX();

            Runnable action = () -> {
                if (!setting.setDouble(edit.get())) edit.set(setting.getDouble());
            };

            if (setting.onSliderRelease) edit.actionOnRelease = action;
            else edit.action = action;

            ThemeUtils.reset(theme, table, setting, () -> edit.set(setting.getDouble()));
        });
    }

    public static class Builder extends SettingBuilder<Builder, Double, DoubleSetting> {
        public double min = Double.NEGATIVE_INFINITY, max = Double.POSITIVE_INFINITY;
        public double sliderMin = 0, sliderMax = 10, defaultValue = 0D;
        public boolean onSliderRelease = false;
        public int decimalPlaces = 3;
        public boolean noSlider = false;
        protected DoubleConsumer onChanged;

        public Builder() {
            super(0D);
        }

        public Builder min(double min) {
            this.min = min;
            return this;
        }

        public Builder max(double max) {
            this.max = max;
            return this;
        }

        public Builder range(double min, double max) {
            this.min = Math.min(min, max);
            this.max = Math.max(min, max);
            return this;
        }

        public Builder sliderMin(double min) {
            sliderMin = min;
            return this;
        }

        public Builder sliderMax(double max) {
            sliderMax = max;
            return this;
        }

        public Builder sliderRange(double min, double max) {
            sliderMin = min;
            sliderMax = max;
            return this;
        }

        public Builder onSliderRelease() {
            onSliderRelease = true;
            return this;
        }

        public Builder decimalPlaces(int decimalPlaces) {
            this.decimalPlaces = decimalPlaces;
            return this;
        }

        public Builder noSlider() {
            noSlider = true;
            return this;
        }

        public DoubleSetting build() {
            return new DoubleSetting(name, description, defaultValue, onChanged, onModuleActivated, visible, min, max, Math.max(sliderMin, min), Math.min(sliderMax, max), onSliderRelease, decimalPlaces, noSlider);
        }

        /**
         * Replaces the {@link Consumer} with primitive-based {@link DoubleConsumer}.
         * @author Crosby
         */
        public Builder onChanged(DoubleConsumer onChanged) {
            this.onChanged = onChanged;
            return this;
        }

        /**
         * Replaces the {@link Double} with double primitive.
         * @author Crosby
         */
        public Builder defaultValue(double defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }
    }
}
