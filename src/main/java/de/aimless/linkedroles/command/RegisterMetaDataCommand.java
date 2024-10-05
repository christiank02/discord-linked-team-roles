package de.aimless.linkedroles.command;


import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.springframework.stereotype.Component;

@Component
public class RegisterMetaDataCommand implements SlashCommand {

    @Override
    public CommandData getCommandData() {
        return Commands.slash(CommandName.REGISTERMETADATACOMMAND.getName().toLowerCase(), "Registers the metadata for the connection role")
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
    }
}
