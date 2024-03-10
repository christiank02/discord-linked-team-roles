package de.aimless.aimless_java_bot.command;


import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class RegisterMetaDataCommand {

    public CommandData registerMetaDataCommandData() {
        return Commands.slash(CommandName.REGISTERMETADATACOMMAND.getName().toLowerCase(), "Registers the metadata for the connection role")
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
    }
}
