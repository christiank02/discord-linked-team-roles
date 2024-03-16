package de.aimless.aimless_java_bot.command.joinmessage;

import de.aimless.aimless_java_bot.command.CommandName;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class RemoveChannelCommand {

    public SubcommandData roleCommandData() {
        return new SubcommandData(CommandName.REMOVE.getName(), "Disables the join message");
    }
}
