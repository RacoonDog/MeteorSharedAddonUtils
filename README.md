<div align="center">
  <h1>Meteor Shared Addon Utils</h1>

  <!-- Fancy badges -->
  <a href="https://github.com/RacoonDog/MeteorSharedAddonUtils/commits/main"><img src="https://img.shields.io/github/last-commit/RacoonDog/MeteorSharedAddonUtils?logo=git" alt="Last commit"></a>
  <img src="https://img.shields.io/github/languages/code-size/RacoonDog/MeteorSharedAddonUtils" alt="Code Size">
  <img src="https://img.shields.io/github/repo-size/RacoonDog/MeteorSharedAddonUtils" alt="Repo Size">
  <img src="https://img.shields.io/github/issues/RacoonDog/MeteorSharedAddonUtils" alt="Issues">
  <img src="https://img.shields.io/github/stars/RacoonDog/MeteorSharedAddonUtils" alt="Stars">
</div>

## Features
- Adds a [Meteor Client Mod Menu badge](#mod-menu-badge).

### Library
- `WMeteorWindow` with custom accent color; `ColorWindow`
- `MeteorGuiTheme` with default recolored widgets; `RecolorGuiTheme`
- Customizable title screen text rendering. `TitleScreenCredits.registerCustomDrawFunction()`

### Utils
- Packed RGB & ARGB to `SettingColor`; `ColorUtils.fromPackedRGB()` & `ColorUtils.fromPackedARGB()`
- Get `fabric.mod.json` mod id from `MeteorAddon` object; `AddonUtils.getAddonId()`
- Check if a certain addon is present using it's name or id; `AddonUtils.areAddonIdsPresent()` & `AddonUtils.areAddonnamesPresent()`
- Get `MeteorAddon` object from mod id or name; `AddonUtils.getFromName()` & `AddonUtils.getFromId()`
- Utils to parse `fabric.mod.json` custom data. `ModMetaUtils`

## Using Meteor Shared Addon Utils
```groovy
repositories {
    maven {
        url "https://jitpack.io"
    }
}

dependencies {
    modImplementation("com.github.RacoonDog:MeteorSharedAddonUtils:main-SNAPSHOT")
    include("com.github.RacoonDog:MeteorSharedAddonUtils:main-SNAPSHOT")
}
```

## Mod Menu Badge
In `fabric.mod.json`
```json
"custom": {
  "modmenu": {
    "badges": [ "meteor" ]
  }
}
```