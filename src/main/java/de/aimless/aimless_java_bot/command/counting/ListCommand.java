package de.aimless.aimless_java_bot.command.counting;

import de.aimless.aimless_java_bot.command.CommandName;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class ListCommand {
    public SubcommandData roleCommandData() {
        return new SubcommandData(CommandName.LIST.getName(), "Lists all abilities for the counting game.");
    }
}
