package de.aimless.aimless_java_bot.command.randomcharacter;

import de.aimless.aimless_java_bot.command.CommandName;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class RandomCharacterCommand {
    public CommandData roleCommandData() {
        SubcommandData setChannelCommand = new SetChannelCommand().roleCommandData();

        return Commands.slash(CommandName.RANDOMCHARACTER.getName().toLowerCase(), "Random Character Command")
                .addSubcommands(setChannelCommand)
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
    }
}
