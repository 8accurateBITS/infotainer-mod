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

        // Twitch Channel
        this.addDrawableChild(new ButtonWidget.Builder(
            Text.literal("Twitch Channel: " + config.twitchChannelName),
            button -> {}
        ).position(10, y).width(200).build());

        twitchChannelInput = new TextFieldWidget(this.textRenderer, 220, y, 150, 20, Text.literal("Twitch Channel"));
        twitchChannelInput.setText(config.twitchChannelName);
        this.addSelectableChild(twitchChannelInput);
        this.setFocused(twitchChannelInput);

        y += spacing;

        // Twitch OAuth Token
        this.addDrawableChild(new ButtonWidget.Builder(
            Text.literal("OAuth Token"),
            button -> {}
        ).position(10, y).width(200).build());

        twitchTokenInput = new TextFieldWidget(this.textRenderer, 220, y, 150, 20, Text.literal("OAuth Token"));
        twitchTokenInput.setText(config.twitchOAuthToken);
        this.addSelectableChild(twitchTokenInput);

        y += spacing;

        // HUD Position X
        this.addDrawableChild(new ButtonWidget.Builder(
            Text.literal("HUD X Position: " + config.hudX),
            button -> {}
        ).position(10, y).width(200).build());

        hudXInput = new TextFieldWidget(this.textRenderer, 220, y, 150, 20, Text.literal("X Position"));
        hudXInput.setText(String.valueOf(config.hudX));
        this.addSelectableChild(hudXInput);

        y += spacing;

        // HUD Position Y
        this.addDrawableChild(new ButtonWidget.Builder(
            Text.literal("HUD Y Position: " + config.hudY),
            button -> {}
        ).position(10, y).width(200).build());

        hudYInput = new TextFieldWidget(this.textRenderer, 220, y, 150, 20, Text.literal("Y Position"));
        hudYInput.setText(String.valueOf(config.hudY));
        this.addSelectableChild(hudYInput);

        y += spacing;

        // Alignment
        this.addDrawableChild(new CyclingButtonWidget.Builder<>(ModConfig.ConfigData.HudAlignment.class)
            .omitKeyText()
            .build(10, y, 200, 20, Text.literal("Alignment: "), (button, alignment) -> {
                config.alignment = alignment;
            }, config.alignment));

        y += spacing + 10;

        // Display toggles
        this.addDrawableChild(new ButtonWidget.Builder(
            Text.literal(config.showDateTime ? "✓ Show Date/Time" : "✗ Show Date/Time"),
            button -> config.showDateTime = !config.showDateTime
        ).position(10, y).width(200).build());

        this.addDrawableChild(new ButtonWidget.Builder(
            Text.literal(config.showTwitchFollowers ? "✓ Show Twitch Followers" : "✗ Show Twitch Followers"),
            button -> config.showTwitchFollowers = !config.showTwitchFollowers
        ).position(220, y).width(200).build());

        y += spacing;

        this.addDrawableChild(new ButtonWidget.Builder(
            Text.literal(config.showWeather ? "✓ Show Weather" : "✗ Show Weather"),
            button -> config.showWeather = !config.showWeather
        ).position(10, y).width(200).build());

        y += spacing + 20;

        // Save button
        this.addDrawableChild(new ButtonWidget.Builder(
            Text.literal("Save"),
            button -> saveSettings()
        ).position(this.width / 2 - 105, y).width(100).build());

        // Done button
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
