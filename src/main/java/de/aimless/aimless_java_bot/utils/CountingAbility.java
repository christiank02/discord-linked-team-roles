package de.aimless.aimless_java_bot.utils;

import lombok.Getter;

@Getter
public enum CountingAbility {
    StreakSaver("Streak Saver", "Rette die Serie, wenn ein Fehler gemacht wird","streak-saver");

    private final String displayName;
    private final String description;
    private final String buttonId;

    CountingAbility(String displayName, String description, String buttonId) {
        this.displayName = displayName;
        this.description = description;
        this.buttonId = buttonId;
    }
}
