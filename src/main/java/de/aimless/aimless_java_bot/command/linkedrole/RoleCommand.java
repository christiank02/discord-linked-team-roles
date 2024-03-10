package de.aimless.aimless_java_bot.command.linkedrole;

import de.aimless.aimless_java_bot.command.CommandName;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class RoleCommand {

    public CommandData roleCommandData() {
        SubcommandData addRoleSubcommand = new AddRoleCommand().roleCommandData();
        SubcommandData removeRoleSubcommand = new RemoveRoleCommand().roleCommandData();
        SubcommandData setRoleLevelSubcommand = new SetRoleLevelCommand().roleCommandData();
        SubcommandData listRoleSubcommand = new ListRoleCommand().roleCommandData();

        return Commands.slash(CommandName.LINKEDROLE.getName().toLowerCase(), "Manage linked roles level")
                .addSubcommands(addRoleSubcommand, removeRoleSubcommand, setRoleLevelSubcommand, listRoleSubcommand)
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
    }
}
