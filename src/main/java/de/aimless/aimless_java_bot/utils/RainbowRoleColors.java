package de.aimless.aimless_java_bot.utils;

import lombok.Getter;

import java.awt.Color;


@Getter
public enum RainbowRoleColors {

    WHITE(Color.decode("#FFFFFF")),
    DARK_RED(Color.decode("#7e0000")),
    DARK_PURPLE(Color.decode("#4c00b0")),
    LIGHT_PURPLE(Color.decode("#ce94ff")),
    MEDIUM_PURPLE(Color.decode("#a756bc")),
    LIGHT_PINK(Color.decode("#ffb3db")),
    ROSE(Color.decode("#f4819f")),
    LIGHT_GREEN(Color.decode("#9fd5c0")),
    PEACH(Color.decode("#fbb1a4")),
    SKIN_TONE(Color.decode("#fbb1a4"));

    private final Color color;

    RainbowRoleColors(Color color) {
        this.color = color;
    }
}
