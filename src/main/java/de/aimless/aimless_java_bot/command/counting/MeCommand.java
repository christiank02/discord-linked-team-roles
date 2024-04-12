package de.aimless.aimless_java_bot.command.counting;

import de.aimless.aimless_java_bot.command.CommandName;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class MeCommand {
    public SubcommandData roleCommandData() {
        return new SubcommandData(CommandName.ME.getName(), "Lists all abilities you have for the counting game.");
    }
}
