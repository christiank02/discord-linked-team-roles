package de.aimless.aimless_java_bot.command.randomcharacter;

import de.aimless.aimless_java_bot.command.CommandName;
import de.aimless.aimless_java_bot.command.linkedrole.AddRoleCommand;
import de.aimless.aimless_java_bot.command.linkedrole.ListRoleCommand;
import de.aimless.aimless_java_bot.command.linkedrole.RemoveRoleCommand;
import de.aimless.aimless_java_bot.command.linkedrole.SetRoleLevelCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class RandomCharacterCommand {
    public CommandData roleCommandData() {
        SubcommandData addRoleSubcommand = new AddRoleCommand().roleCommandData();
        SubcommandData removeRoleSubcommand = new RemoveRoleCommand().roleCommandData();
        SubcommandData setRoleLevelSubcommand = new SetRoleLevelCommand().roleCommandData();
        SubcommandData listRoleSubcommand = new ListRoleCommand().roleCommandData();

        return Commands.slash(CommandName.RANDOMCHARACTER.getName().toLowerCase(), "Random Character Command")
                .addSubcommands(addRoleSubcommand, removeRoleSubcommand, setRoleLevelSubcommand, listRoleSubcommand)
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
    }
}
