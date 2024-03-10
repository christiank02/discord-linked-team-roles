package de.aimless.aimless_java_bot.command.linkedrole;

import de.aimless.aimless_java_bot.command.CommandName;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class ListRoleCommand {

    public SubcommandData roleCommandData() {
        return new SubcommandData(CommandName.LIST.getName(), "Lists all linked roles and their permission levels for this guild");
    }
}
