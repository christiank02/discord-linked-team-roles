package de.aimless.aimless_java_bot.handlers.counting;

import de.aimless.aimless_java_bot.command.CommandName;
import de.aimless.aimless_java_bot.handlers.AbstractCommandHandler;
import de.aimless.aimless_java_bot.repository.CountingGameRepository;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class RemoveCountingChannelCommandHandler extends AbstractCommandHandler {

    private final CountingGameRepository countingGameRepository;

    private static final Long ABSENCE_OF_CHANNEL = null; // Represents the absence of a join message channel

    public RemoveCountingChannelCommandHandler(CountingGameRepository countingGameRepository) {
        this.countingGameRepository = countingGameRepository;
    }

    public void handleCommand(SlashCommandInteractionEvent event) {
        if (event.isAcknowledged()) {
            return;
        }

        event.deferReply(true).queue();

        Guild guild = event.getGuild();
        if(Objects.isNull(guild)) {
            sendMessage(event, "Failed to remove channel. Command must be used inside a guild");
            return;
        }

        long guildId = guild.getIdLong();
        removeCountingChannel(guildId);

        sendMessage(event, "Channel for the counting game has been removed.");
    }

    // Method to remove the counting channel for a guild
    private void removeCountingChannel(long guildId) {
        countingGameRepository.findById(guildId).ifPresent(
                countingGameEntity -> {
                    countingGameEntity.setChannelId(ABSENCE_OF_CHANNEL);
                    countingGameRepository.save(countingGameEntity);
                }
        );
    }

    // Method to send a message
    private void sendMessage(SlashCommandInteractionEvent event, String message) {
        event.getHook().sendMessage(message).queue();
    }

    @Override
    public boolean canHandle(CommandName commandName, CommandName subCommandName) {
        return commandName == CommandName.COUNTING && subCommandName == CommandName.REMOVE;
    }

}
