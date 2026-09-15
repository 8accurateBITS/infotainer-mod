package com.infotainer.config;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.client.gui.DrawContext;

public class InfotainerConfigScreen extends Screen {
    private final Screen parent;
    private TextFieldWidget twitchChannelInput;
    private TextFieldWidget twitchTokenInput;
    private TextFieldWidget hudXInput;
    private TextFieldWidget hudYInput;

    public InfotainerConfigScreen(Screen parent) {
        super(Text.literal("Infotainer Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        ModConfig.ConfigData config = ModConfig.get();

        int y = 30;
        int spacing = 30;

        twitchChannelInput = new TextFieldWidget(this.textRenderer, 220, y, 150, 20, Text.literal("Twitch Channel"));
        twitchChannelInput.setText(config.twitchChannelName);
        this.addSelectableChild(twitchChannelInput);
        this.setFocused(twitchChannelInput);

        y += spacing;

        twitchTokenInput = new TextFieldWidget(this.textRenderer, 220, y, 150, 20, Text.literal("OAuth Token"));
        twitchTokenInput.setText(config.twitchOAuthToken);
        this.addSelectableChild(twitchTokenInput);

        y += spacing;

        hudXInput = new TextFieldWidget(this.textRenderer, 220, y, 150, 20, Text.literal("X Position"));
        hudXInput.setText(String.valueOf(config.hudX));
        this.addSelectableChild(hudXInput);

        y += spacing;

        hudYInput = new TextFieldWidget(this.textRenderer, 220, y, 150, 20, Text.literal("Y Position"));
        hudYInput.setText(String.valueOf(config.hudY));
        this.addSelectableChild(hudYInput);

        y += spacing + 10;

        this.addDrawableChild(new ButtonWidget.Builder(
            Text.literal(config.showDateTime ? "✓ Show Date/Time" : "✗ Show Date/Time"),
            button -> config.showDateTime = !config.showDateTime
        ).position(10, y).width(200).build());

        this.addDrawableChild(new ButtonWidget.Builder(
            Text.literal(config.showTwitchFollowers ? "✓ Show Twitch" : "✗ Show Twitch"),
            button -> config.showTwitchFollowers = !config.showTwitchFollowers
        ).position(220, y).width(200).build());

        y += spacing;

        this.addDrawableChild(new ButtonWidget.Builder(
            Text.literal(config.showWeather ? "✓ Show Weather" : "✗ Show Weather"),
            button -> config.showWeather = !config.showWeather
        ).position(10, y).width(200).build());

        y += spacing + 20;

        this.addDrawableChild(new ButtonWidget.Builder(
            Text.literal("Save"),
            button -> saveSettings()
        ).position(this.width / 2 - 105, y).width(100).build());

        this.addDrawableChild(new ButtonWidget.Builder(
            Text.literal("Done"),
            button -> this.close()
        ).position(this.width / 2 + 5, y).width(100).build());
    }

    private void saveSettings() {
        ModConfig.ConfigData config = ModConfig.get();
        config.twitchChannelName = twitchChannelInput.getText();
        config.twitchOAuthToken = twitchTokenInput.getText();
        try {
            config.hudX = Integer.parseInt(hudXInput.getText());
            config.hudY = Integer.parseInt(hudYInput.getText());
        } catch (NumberFormatException e) {
            // Keep old values
        }
        ModConfig.save();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    @Override
    public void close() {
        saveSettings();
        this.client.setScreen(this.parent);
    }
}
