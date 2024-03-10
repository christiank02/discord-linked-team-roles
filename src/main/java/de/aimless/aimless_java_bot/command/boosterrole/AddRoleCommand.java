package de.aimless.aimless_java_bot.command.boosterrole;

import de.aimless.aimless_java_bot.command.CommandName;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class AddRoleCommand {

    public SubcommandData roleCommandData() {
        return new SubcommandData(CommandName.ADD.getName(), "Adds a new booster role to the bots database for this guild")
                .addOption(OptionType.ROLE, "role", "The role you want to add as a booster role", true);
    }
}
