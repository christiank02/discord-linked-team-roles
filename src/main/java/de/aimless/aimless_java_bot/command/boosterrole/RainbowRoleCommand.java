package de.aimless.aimless_java_bot.command.boosterrole;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class RainbowRoleCommand {

    public CommandData roleCommandData() {
        return Commands.slash("rainbowrole", "Disables/Enables the rainbow role for yourself if you have the permissions for the role.");
    }
}
