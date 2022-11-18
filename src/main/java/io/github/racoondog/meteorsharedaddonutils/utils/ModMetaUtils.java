package io.github.racoondog.meteorsharedaddonutils.utils;

import com.terraformersmc.modmenu.util.mod.Mod;
import io.github.racoondog.meteorsharedaddonutils.mixin.mixin.modmenu.IFabricMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.metadata.CustomValue;
import net.fabricmc.loader.api.metadata.ModMetadata;

@Environment(EnvType.CLIENT)
public final class ModMetaUtils {
    /**
     * Thrown when {@link CustomValue} was a different type than expected.
     */
    public static class WrongTypeException extends RuntimeException {}
    /**
     * Thrown when {@link CustomValue} was not found.
     */
    public static class NotFoundException extends RuntimeException {}

    /**
     * @param parent {@link ModMetadata} object which contains the target {@link CustomValue}.
     * @param identifier The target {@link CustomValue}'s identifier.
     * @param clazz The target {@link CustomValue}'s expected class, used for type casting.
     * @return The {@link ModMetadata}'s {@link CustomValue} object of the given identifier, cast to the target class.
     * @param <T> Can be {@link CustomValue.CvObject}, {@link CustomValue.CvArray}, {@link String}, {@link Number} and {@link Boolean}.
     * @throws WrongTypeException If the given {@link CustomValue} is not an instance of the expected class.
     * @throws NotFoundException If the {@link ModMetadata} does not contain an object of the given identifier.
     */
    public static <T> T getAs(ModMetadata parent, String identifier, Class<T> clazz) throws WrongTypeException, NotFoundException{
        CustomValue returnValue = parent.getCustomValue(identifier);
        if (returnValue == null) {
            throw new NotFoundException();
        } else {
            try {
                return clazz.cast(returnValue);
            } catch (ClassCastException e) {
                throw new WrongTypeException();
            }
        }
    }

    /**
     * @param parent {@link CustomValue.CvObject} object which contains the target {@link CustomValue}.
     * @param identifier The target {@link CustomValue}'s identifier.
     * @param clazz The target {@link CustomValue}'s expected class, used for type casting.
     * @return The {@link CustomValue.CvObject}'s {@link CustomValue} object of the given identifier, cast to the target class.
     * @param <T> Can be {@link CustomValue.CvObject}, {@link CustomValue.CvArray}, {@link String}, {@link Number} and {@link Boolean}.
     * @throws WrongTypeException If the given {@link CustomValue} is not an instance of the expected class.
     * @throws NotFoundException If the {@link CustomValue.CvObject} does not contain an object of the given identifier.
     */
    public static <T> T getAs(CustomValue.CvObject parent, String identifier, Class<T> clazz) {
        CustomValue returnValue = parent.get(identifier);
        if (returnValue == null) {
            throw new NotFoundException();
        } else {
            try {
                return clazz.cast(returnValue);
            } catch (ClassCastException e) {
                throw new WrongTypeException();
            }
        }
    }

    /**
     * Using this will crash if Mod Menu is not present.
     *
     * @param mod The target Mod Menu {@link Mod} object.
     * @return The {@link Mod} object's associated {@link ModMetadata}.
     */
    public static ModMetadata getModMeta(Mod mod) {
        if (!mod.isReal()) throw new NotFoundException();
        return ((IFabricMod) mod).getMetadata();
    }

    /**
     * Using this will crash if Mod Menu is not present.
     *
     * @param mod The target Mod Menu {@link Mod} object.
     * @param badge The target badge identifier.
     * @return Whether the target {@link Mod} has the target badge identifier in their {@code fabric.mod.json}.
     */
    public static boolean hasModBadge(Mod mod, String badge) {
        try {
            CustomValue.CvArray array = getAs(getAs(getModMeta(mod), "modmenu", CustomValue.CvObject.class), "badges", CustomValue.CvArray.class);
            for (var cv : array) {
                if (cv.getType() != CustomValue.CvType.STRING) continue;
                String str = cv.getAsString();
                if (str.equals(badge)) return true;
            }
        } catch (Exception ignored) {}
        return false;
    }
}
