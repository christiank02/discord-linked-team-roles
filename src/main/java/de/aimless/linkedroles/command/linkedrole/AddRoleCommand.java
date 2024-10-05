package de.aimless.linkedroles.command.linkedrole;

import de.aimless.linkedroles.command.CommandName;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class AddRoleCommand {

    public SubcommandData roleCommandData() {
        return new SubcommandData(CommandName.ADD.getName(), "Adds a new role to the bots database with a permission level for the linked roles")
                .addOption(OptionType.ROLE, "role", "The role you want to add", true)
                .addOption(OptionType.INTEGER, "level", "The permission level of the role", true);
    }
}
