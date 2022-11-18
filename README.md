<div align="center">
  <h1>Meteor Shared Addon Utils</h1>

  <!-- Fancy badges -->
  <a href="https://github.com/RacoonDog/MeteorSharedAddonUtils/commits/main"><img src="https://img.shields.io/github/last-commit/RacoonDog/MeteorSharedAddonUtils?logo=git" alt="Last commit"></a>
  <img src="https://img.shields.io/github/languages/code-size/RacoonDog/MeteorSharedAddonUtils" alt="Code Size">
  <img src="https://img.shields.io/github/repo-size/RacoonDog/MeteorSharedAddonUtils" alt="Repo Size">
</div>

## Features
- Adds a [Meteor Client Mod Menu badge](#mod-menu-badge).

### Library
- `WMeteorWindow` with custom accent color; `ColoredWindow`
- `MeteorGuiTheme` with the default widgets, recolored; `RecolorGuiTheme`
- Customizable title screen text rendering. `TitleScreenCredits.registerCustomDrawFunction()`

### Utils
- Packed RGB & ARGB to `SettingColor`; `ColorUtils.fromPackedRGB()` & `ColorUtils.fromPackedARGB()`
- `Style`/`Formatting`/`TextColor` to `SettingColor`; `ColorUtils.fromStyle()`, `ColorUtils.fromFormatting()` & `ColorUtils.fromTextColor()`
- `SettingColor` to `Style`; `ColorUtils.styleFromColor()` & `ColorUtils.styleWithColor()`
- Get `fabric.mod.json` mod id and version string from `MeteorAddon` object; `AddonUtils.getAddonId()` & `AddonUtils.getAddonVersion()`
- Check if a certain addon is present using its name or id; `AddonUtils.areAddonIdsPresent()` & `AddonUtils.areAddonNamesPresent()`
- Get `MeteorAddon` object from mod id or name; `AddonUtils.getFromName()` & `AddonUtils.getFromId()`
- Utils to parse `fabric.mod.json` custom data; `ModMetaUtils`
- Add a system; `ISystems.invokeAdd()`
- Remove a value from a `Starscript` `ValueMap`; `IValueMap.remove()`
- Get a `GuiTheme`'s `SettingsWidgetFactory`; `ThemeUtils.getSettingsFactory()`
- 2 Dimensional `int` vector. `Vec2i`

### Settings
- Primitive-based `BoolSetting`, `DoubleSetting`, and `IntSetting`. `BooleanSetting`, `DoubleSetting` & `IntegerSetting`

## Using Meteor Shared Addon Utils
```groovy
repositories {
    maven {
        url "https://jitpack.io"
    }
}

dependencies {
  modImplementation("com.github.RacoonDog:MeteorSharedAddonUtils:main-SNAPSHOT") {
    transitive = false
  }
  include("com.github.RacoonDog:MeteorSharedAddonUtils:main-SNAPSHOT") {
    transitive = false
  }
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