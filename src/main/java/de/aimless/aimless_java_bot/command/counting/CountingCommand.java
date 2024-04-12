package de.aimless.aimless_java_bot.command.counting;

import de.aimless.aimless_java_bot.command.CommandName;
import de.aimless.aimless_java_bot.command.SlashCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.springframework.stereotype.Component;

@Component
public class CountingCommand implements SlashCommand {

    // add command to show all abilites for the servers counting game the user has
    // name + description + count for same abilities

    @Override
    public CommandData getCommandData() {
        SubcommandData setChannelCommand = new SetChannelCommand().roleCommandData();
        SubcommandData removeChannelCommand = new RemoveChannelCommand().roleCommandData();
        SubcommandData listCommand = new ListCommand().roleCommandData();
        SubcommandData meCommand = new MeCommand().roleCommandData();

        return Commands.slash(CommandName.COUNTING.getName().toLowerCase(), "Set the channel for the counting game.")
                .addSubcommands(setChannelCommand, removeChannelCommand)
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
                .addSubcommands(listCommand, meCommand);
    }
}
