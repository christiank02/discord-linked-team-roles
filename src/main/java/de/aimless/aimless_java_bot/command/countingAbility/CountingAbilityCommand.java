package de.aimless.aimless_java_bot.command.countingAbility;

import de.aimless.aimless_java_bot.command.CommandName;
import de.aimless.aimless_java_bot.command.SlashCommand;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.springframework.stereotype.Component;

@Component
public class CountingAbilityCommand implements SlashCommand {

    @Override
    public CommandData getCommandData() {
        SubcommandData listCommand = new ListCommand().roleCommandData();
        SubcommandData meCommand = new MeCommand().roleCommandData();

        return Commands.slash(CommandName.ABILITY.getName().toLowerCase(), "Set the channel for the counting game.")
                .addSubcommands(listCommand, meCommand);
    }
}
