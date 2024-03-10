package de.aimless.aimless_java_bot.oauth2;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuth2InviteController {

    @GetMapping("/invite")
    public String invite() {
        return "Thank you for inviting the Bot. You can now close this tab.";
    }
}
