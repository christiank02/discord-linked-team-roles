package de.aimless.aimless_java_bot.command.boosterrole;

import de.aimless.aimless_java_bot.command.CommandName;
import de.aimless.aimless_java_bot.command.SlashCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.springframework.stereotype.Component;

@Component
public class BoosterRoleCommand implements SlashCommand {
    @Override
    public CommandData getCommandData() {
        SubcommandData addRoleSubcommand = new AddRoleCommand().roleCommandData();
        SubcommandData removeRoleSubcommand = new RemoveRoleCommand().roleCommandData();
        SubcommandData listRoleSubcommand = new ListRoleCommand().roleCommandData();
        SubcommandData setRoleSubcommand = new SetBoosterRoleCommand().roleCommandData();

        return Commands.slash(CommandName.BOOSTERROLE.getName().toLowerCase(), "Manage booster roles")
                .addSubcommands(addRoleSubcommand, removeRoleSubcommand, listRoleSubcommand, setRoleSubcommand)
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
    }
}
