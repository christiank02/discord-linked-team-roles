package de.aimless.aimless_java_bot.command.linkedrole;

import de.aimless.aimless_java_bot.command.CommandName;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class RemoveRoleCommand {

    public SubcommandData roleCommandData() {
        return new SubcommandData(CommandName.REMOVE.getName(), "Removes the given role from the bots database")
                .addOption(OptionType.ROLE, "role", "The role you want to remove", true);
    }
}
