package de.aimless.aimless_java_bot.command.boosterrole;

import de.aimless.aimless_java_bot.command.CommandName;
import de.aimless.aimless_java_bot.command.SlashCommand;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.springframework.stereotype.Component;

@Component
public class RainbowRoleCommand implements SlashCommand {

    @Override
    public CommandData getCommandData() {
        return Commands.slash(CommandName.RAINBOWROLE.getName(), "Disables/Enables the rainbow role for yourself if you have the permissions for the role.");
    }
}
