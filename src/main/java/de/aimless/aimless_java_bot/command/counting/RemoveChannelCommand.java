package de.aimless.aimless_java_bot.command.counting;

import de.aimless.aimless_java_bot.command.CommandName;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class RemoveChannelCommand {

    public SubcommandData roleCommandData() {
        return new SubcommandData(CommandName.REMOVE.getName(), "Removes the counting channel/Disables the bot to listen");
    }
}
