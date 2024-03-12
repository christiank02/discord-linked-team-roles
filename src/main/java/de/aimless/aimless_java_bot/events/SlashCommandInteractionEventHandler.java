package de.aimless.aimless_java_bot.events;

import de.aimless.aimless_java_bot.command.CommandName;
import de.aimless.aimless_java_bot.handlers.AbstractCommandHandler;
import de.aimless.aimless_java_bot.handlers.registermetadata.RegisterMetaDataCommandHandler;
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

        switch (command) {
            case LINKEDROLE, BOOSTERROLE, RANDOMCHARACTER -> handleSubCommand(event, command);
            case REGISTERMETADATACOMMAND -> new RegisterMetaDataCommandHandler().handleCommand(event);
            default -> event.reply("Unknown command").setEphemeral(true).queue();
        }
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
