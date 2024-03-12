package de.aimless.aimless_java_bot.handlers.randomcharacter;

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
public class SetChannelCommandHandler extends AbstractCommandHandler {

    private final GuildRepository guildRepository;
    private final GuildEntityMapper guildEntityMapper;

    public SetChannelCommandHandler(GuildRepository guildRepository) {
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
            event.getHook().sendMessage("Failed to set channel. Command must be used inside a guild").queue();
            return;
        }

        long guildId = guild.getIdLong();
        long channelId = channel.getIdLong();
        // set channel for guild. If it exists -> update, else create
        guildRepository.findById(guildId).ifPresentOrElse(
                guildEntity -> {
                    guildEntity.setRandomAnimeCharacterChannelId(channel.getIdLong());
                    guildRepository.save(guildEntity);
                },
                () -> {
                    GuildEntity guildEntity = guildEntityMapper.map(guildId, channelId);
                    guildRepository.save(guildEntity);
                }
        );

        String replyMessage = String.format("Channel for the Kiss Marry Kill game has been set to %s", channel.getName());
        event.getHook().sendMessage(replyMessage).queue();
    }

    @Override
    public boolean canHandle(CommandName commandName, CommandName subCommandName) {
        return commandName == CommandName.RANDOMCHARACTER && subCommandName == CommandName.SET;
    }

}
