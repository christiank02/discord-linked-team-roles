package de.aimless.aimless_java_bot.command.boosterrole;

import de.aimless.aimless_java_bot.command.CommandName;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class SetBoosterRoleCommand {

    public SubcommandData roleCommandData() {
        return new SubcommandData(CommandName.SET.getName(), "Sets the auto assignable status of a booster role.")
                .addOption(OptionType.ROLE, "role", "The role you want to modify", true)
                .addOption(OptionType.BOOLEAN, "auto_assignable", "Set whether this role should be automatically granted when a user boosts twice.", true);
    }
}
