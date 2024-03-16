package de.aimless.aimless_java_bot.handlers.joinmessage;

import de.aimless.aimless_java_bot.command.CommandName;
import de.aimless.aimless_java_bot.handlers.AbstractCommandHandler;
import de.aimless.aimless_java_bot.repository.GuildRepository;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class RemoveJoinMessageChannelCommandHandler extends AbstractCommandHandler {

    private final GuildRepository guildRepository;
    private static final Long ABSENCE_OF_CHANNEL = null; // Represents the absence of a join message channel

    public RemoveJoinMessageChannelCommandHandler(GuildRepository guildRepository) {
        this.guildRepository = guildRepository;
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
        removeJoinMessageChannel(guildId);

        sendMessage(event, "Channel for the join message has been removed.");
    }

    // Method to remove the join message channel for a guild
    private void removeJoinMessageChannel(long guildId) {
        guildRepository.findById(guildId).ifPresent(
                guildEntity -> {
                    guildEntity.setJoinMessageChannelId(ABSENCE_OF_CHANNEL);
                    guildRepository.save(guildEntity);
                }
        );
    }

    // Method to send a message
    private void sendMessage(SlashCommandInteractionEvent event, String message) {
        event.getHook().sendMessage(message).queue();
    }

    @Override
    public boolean canHandle(CommandName commandName, CommandName subCommandName) {
        return commandName == CommandName.JOINMESSAGE && subCommandName == CommandName.REMOVE;
    }

}
