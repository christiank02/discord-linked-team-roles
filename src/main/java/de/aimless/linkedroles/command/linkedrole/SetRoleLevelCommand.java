package de.aimless.linkedroles.command.linkedrole;

import de.aimless.linkedroles.command.CommandName;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class SetRoleLevelCommand {

    public SubcommandData roleCommandData() {
        return new SubcommandData(CommandName.SET.getName(), "Sets the permission level for the given role in the bots database")
                .addOption(OptionType.ROLE, "role", "The role you want to modify", true)
                .addOption(OptionType.INTEGER, "level", "the new permission level of the role", true);
    }
}
