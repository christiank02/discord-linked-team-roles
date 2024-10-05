package de.aimless.linkedroles.events;

import de.aimless.linkedroles.command.CommandName;
import de.aimless.linkedroles.handlers.AbstractCommandHandler;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class SlashCommandInteractionEventHandler extends ListenerAdapter {

    private final List<AbstractCommandHandler> commandHandlers;

    public SlashCommandInteractionEventHandler(List<AbstractCommandHandler> commandHandlers) {
        this.commandHandlers = commandHandlers;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        CommandName command = CommandName.fromCommandName(event.getName());

        if (command == null) {
            return;
        }

        if (Objects.nonNull(event.getSubcommandName())) {
            handleSubCommand(event, command);
        } else {
            handleCommand(event, command);
        }
    }

    private void handleCommand(SlashCommandInteractionEvent event, CommandName command) {
        for (AbstractCommandHandler handler : commandHandlers) {
            if (handler.canHandle(command, null)) {
                handler.handleCommand(event);
                return;
            }
        }

        event.reply("Unknown command").setEphemeral(true).queue();
    }

    private void handleSubCommand(SlashCommandInteractionEvent event, CommandName command) {
        String subCommandName = event.getSubcommandName();
        if (Objects.isNull(subCommandName)) {
            event.reply("Please provide a subcommand").setEphemeral(true).queue();
            return;
        }

        CommandName subCommand = CommandName.fromCommandName(subCommandName);
        for (AbstractCommandHandler handler : commandHandlers) {
            if (handler.canHandle(command, subCommand)) {
                handler.handleCommand(event);
                return;
            }
        }
        event.reply("Unknown subcommand").setEphemeral(true).queue();
    }
}
