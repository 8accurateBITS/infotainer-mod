package com.infotainer.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import com.infotainer.config.ModConfig;
import com.infotainer.util.DateTimeUtil;
import com.infotainer.util.TwitchApiClient;
import com.infotainer.util.WeatherUtil;
import net.minecraft.client.gui.DrawContext;

@Mixin(InGameHud.class)
public class HudRenderMixin {

    @Inject(method = "render", at = @At("TAIL"))
    private void onHudRender(DrawContext context, float tickDelta, CallbackInfo ci) {
        renderInfotainerHud(context);
    }

    private void renderInfotainerHud(DrawContext context) {
        ModConfig.ConfigData config = ModConfig.get();
        int textColor = 0xFFFFFF;
        int lineHeight = 12;
        int x = config.hudX;
        int y = config.hudY;
        int maxWidth = 0;

        // Calculate position based on alignment
        String[] lines = getHudLines();
        
        // Render each line
        for (int i = 0; i < lines.length; i++) {
            if (lines[i] != null && !lines[i].isEmpty()) {
                context.drawTextWithBackground(
                    net.minecraft.client.MinecraftClient.getInstance().textRenderer,
                    lines[i],
                    x,
                    y + (i * lineHeight),
                    textColor,
                    0x00000000
                );
            }
        }
    }

    private String[] getHudLines() {
        ModConfig.ConfigData config = ModConfig.get();
        java.util.List<String> lines = new java.util.ArrayList<>();

        if (config.showDateTime) {
            lines.add(DateTimeUtil.getCurrentDateTime());
        }

        if (config.showTwitchFollowers && !config.twitchChannelName.isEmpty()) {
            long followers = TwitchApiClient.getFollowerCount(config.twitchChannelName);
            if (followers >= 0) {
                lines.add("Followers: " + followers);
            }
        }

        if (config.showWeather) {
            lines.add(WeatherUtil.getWeatherInfo());
        }

        return lines.toArray(new String[0]);
    }
}
