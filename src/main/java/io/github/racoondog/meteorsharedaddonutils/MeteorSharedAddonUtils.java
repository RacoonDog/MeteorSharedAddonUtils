package io.github.racoondog.meteorsharedaddonutils;

import io.github.racoondog.meteorsharedaddonutils.features.TitleScreenCredits;
import meteordevelopment.meteorclient.addons.GithubRepo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class MeteorSharedAddonUtils extends MeteorAddon {
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
