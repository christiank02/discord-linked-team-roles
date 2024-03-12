package de.aimless.aimless_java_bot.utils;

import lombok.Getter;

import java.awt.Color;


@Getter
public enum RainbowRoleColors {

    COLOR1(Color.decode("#F88FB0")),
    COLOR2(Color.decode("#E1CEFF")),
    COLOR3(Color.decode("#ceffdd")),
    COLOR4(Color.decode("#c7e1f8")),
    COLOR5(Color.decode("#d28080")),
    COLOR6(Color.decode("#b8ffba")),
    COLOR7(Color.decode("#f7cfa6")),
    COLOR8(Color.decode("#ecfab3")),
    COLOR9(Color.decode("#dadada")),
    COLOR10(Color.decode("#c0f3f4"));

    private final Color color;

    RainbowRoleColors(Color color) {
        this.color = color;
    }
}
