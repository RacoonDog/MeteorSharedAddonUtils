package io.github.racoondog.meteorsharedaddonutils;

import com.mojang.logging.LogUtils;
import io.github.racoondog.meteorsharedaddonutils.features.TitleScreenCredits;
import meteordevelopment.meteorclient.addons.GithubRepo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class MeteorSharedAddonUtils extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();

    @Override
    public void onInitialize() {
        TitleScreenCredits.registerEmptyDrawFunction(this);
    }

    @Override
    public String getPackage() {
        return "io.github.racoondog.meteorsharedaddonutils";
    }

    @Override
    public GithubRepo getRepo() {
        return new GithubRepo("RacoonDog", "MeteorSharedAddonUtils");
    }
}
