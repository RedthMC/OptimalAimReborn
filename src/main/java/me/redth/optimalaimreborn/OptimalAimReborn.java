package me.redth.optimalaimreborn;

import me.redth.optimalaimreborn.config.ConfigSettings;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;

public class OptimalAimReborn implements ClientModInitializer {
    public static final MinecraftClient mc = MinecraftClient.getInstance();
    public static ConfigSettings config;

    @Override
    public void onInitializeClient() {
        config = AutoConfig.register(ConfigSettings.class, GsonConfigSerializer::new).getConfig();
    }
}
