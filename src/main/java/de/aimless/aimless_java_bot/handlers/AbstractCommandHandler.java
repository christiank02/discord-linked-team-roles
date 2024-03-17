package de.aimless.aimless_java_bot.handlers;

import de.aimless.aimless_java_bot.command.CommandName;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public abstract class AbstractCommandHandler {

    public abstract void handleCommand(SlashCommandInteractionEvent event);
    public abstract boolean canHandle(CommandName commandName, CommandName subCommandName);
}
