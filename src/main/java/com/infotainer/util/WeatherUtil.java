package com.infotainer.util;

import net.minecraft.client.MinecraftClient;

public class WeatherUtil {
    public static String getWeatherInfo() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) {
            return "No weather";
        }

        boolean raining = client.world.isRaining();
        boolean thundering = client.world.isThundering();

        if (thundering) {
            return "⚡ Thunderstorm";
        } else if (raining) {
            return "🌧️ Raining";
        } else {
            return "☀️ Clear";
        }
    }

    public static float getTemperature() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) {
            return 15.0f;
        }

        net.minecraft.util.math.BlockPos pos = client.player.getBlockPos();
        return client.world.getBiome(pos).value().getTemperature();
    }
}
