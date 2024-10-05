package de.aimless.linkedroles.handlers;

import de.aimless.linkedroles.command.CommandName;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public abstract class AbstractCommandHandler {

    public abstract void handleCommand(SlashCommandInteractionEvent event);
    public abstract boolean canHandle(CommandName commandName, CommandName subCommandName);
}
