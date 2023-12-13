package me.redth.optimalaimreborn.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "optimal-aim-reborn")
public class ConfigSettings implements ConfigData {
    public boolean enabled = true;

    @ConfigEntry.BoundedDiscrete(min = 1, max = 64)
    public int distance = 10;

    @ConfigEntry.ColorPicker(allowAlpha = true)
    public int color = 0xAAAA0000;
}
