package de.aimless.aimless_java_bot.command.joinmessage;

import de.aimless.aimless_java_bot.command.CommandName;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class JoinMessageCommand {

    public CommandData roleCommandData() {
        SubcommandData setChannelCommand = new SetChannelCommand().roleCommandData();
        SubcommandData removeChannelCommand = new RemoveChannelCommand().roleCommandData();

        return Commands.slash(CommandName.JOINMESSAGE.getName().toLowerCase(), "Set the channel for the join message.")
                .addSubcommands(setChannelCommand, removeChannelCommand)
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
    }
}
