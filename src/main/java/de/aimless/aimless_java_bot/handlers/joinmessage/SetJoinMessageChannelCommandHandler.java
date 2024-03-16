package de.aimless.aimless_java_bot.handlers.joinmessage;

import de.aimless.aimless_java_bot.command.CommandName;
import de.aimless.aimless_java_bot.entity.GuildEntity;
import de.aimless.aimless_java_bot.handlers.AbstractCommandHandler;
import de.aimless.aimless_java_bot.mapper.GuildEntityMapper;
import de.aimless.aimless_java_bot.repository.GuildRepository;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class SetJoinMessageChannelCommandHandler extends AbstractCommandHandler {

    private final GuildRepository guildRepository;
    private final GuildEntityMapper guildEntityMapper;

    public SetJoinMessageChannelCommandHandler(GuildRepository guildRepository) {
        this.guildRepository = guildRepository;
        this.guildEntityMapper = new GuildEntityMapper();
    }

    public void handleCommand(SlashCommandInteractionEvent event) {
        if (event.isAcknowledged()) {
            return;
        }

        event.deferReply(true).queue();

        Channel channel = Objects.requireNonNull(event.getOption("channel")).getAsChannel();

        Guild guild = event.getGuild();
        if(Objects.isNull(guild)) {
            sendMessage(event, "Failed to set channel. Command must be used inside a guild");
            return;
        }

        long guildId = guild.getIdLong();
        long channelId = channel.getIdLong();
        setJoinMessageChannel(guildId, channelId);

        String replyMessage = String.format("Channel for the join message has been set to %s", channel.getName());
        sendMessage(event, replyMessage);
    }

    // Method to set the join message channel for a guild
    private void setJoinMessageChannel(long guildId, long channelId) {
        // set channel for guild. If it exists -> update, else create
        guildRepository.findById(guildId).ifPresentOrElse(
                guildEntity -> {
                    guildEntity.setJoinMessageChannelId(channelId);
                    guildRepository.save(guildEntity);
                },
                () -> {
                    GuildEntity guildEntity = guildEntityMapper.mapWithJoinMessageChannel(guildId, channelId);
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
        return commandName == CommandName.JOINMESSAGE && subCommandName == CommandName.SET;
    }

}