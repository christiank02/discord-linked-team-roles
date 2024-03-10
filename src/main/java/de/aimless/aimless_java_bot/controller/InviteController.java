package de.aimless.aimless_java_bot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InviteController {

    @GetMapping("/invite")
    public String invite() {
        return "Thank you for inviting the Bot. You can now close this tab.";
    }
}
