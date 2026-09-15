package com.infotainer.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ModConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File("config/infotainer.json");
    private static ConfigData configData = new ConfigData();

    public static void load() {
        CONFIG_FILE.getParentFile().mkdirs();
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                configData = GSON.fromJson(reader, ConfigData.class);
                if (configData == null) {
                    configData = new ConfigData();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        save();
    }

    public static void save() {
        CONFIG_FILE.getParentFile().mkdirs();
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(configData, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ConfigData get() {
        return configData;
    }

    public static class ConfigData {
        // Display settings
        public boolean showDateTime = true;
        public boolean showTwitchFollowers = true;
        public boolean showWeather = true;

        // Position settings
        public int hudX = 10;
        public int hudY = 10;
        public HudAlignment alignment = HudAlignment.TOP_RIGHT;

        // Twitch settings
        public String twitchChannelName = "";
        public String twitchOAuthToken = "";

        // Weather settings
        public boolean use12HourFormat = false;
        public int updateInterval = 60; // seconds

        public enum HudAlignment {
            TOP_LEFT,
            TOP_CENTER,
            TOP_RIGHT,
            CENTER_LEFT,
            CENTER,
            CENTER_RIGHT,
            BOTTOM_LEFT,
            BOTTOM_CENTER,
            BOTTOM_RIGHT
        }
    }
}
