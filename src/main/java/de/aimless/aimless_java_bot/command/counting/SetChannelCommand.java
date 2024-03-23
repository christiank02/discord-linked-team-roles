package de.aimless.aimless_java_bot.command.counting;

import de.aimless.aimless_java_bot.command.CommandName;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class SetChannelCommand {

    public SubcommandData roleCommandData() {
        return new SubcommandData(CommandName.SET.getName(), "Sets the channel where the bot should listen to the counting")
                .addOption(OptionType.CHANNEL, "channel", "The channel where the counting should take place", true);
    }
}
