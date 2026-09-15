package com.infotainer;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import com.infotainer.config.ModConfig;
import com.infotainer.util.TwitchApiClient;

public class Infotainer implements ModInitializer {
    public static final String MOD_ID = "infotainer";
    public static final String MOD_NAME = "Infotainer";

    private static KeyBinding settingsKeyBinding;

    @Override
    public void onInitialize() {
        ModConfig.load();
        TwitchApiClient.initialize();
    }

    public static KeyBinding getSettingsKeyBinding() {
        if (settingsKeyBinding == null) {
            settingsKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.infotainer.settings",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_I,
                "category.infotainer"
            ));
        }
        return settingsKeyBinding;
    }
}
