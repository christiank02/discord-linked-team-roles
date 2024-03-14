package de.aimless.aimless_java_bot.events;

import de.aimless.aimless_java_bot.entity.GuildEntity;
import de.aimless.aimless_java_bot.repository.GuildRepository;
import lombok.NonNull;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GuildMemberJoinEventHandler extends ListenerAdapter {

    private final GuildRepository guildRepository;

    public GuildMemberJoinEventHandler(GuildRepository guildRepository) {
        this.guildRepository = guildRepository;
    }

    @Override
    public void onGuildMemberJoin(@NonNull GuildMemberJoinEvent event) {
        // join message channel
        Optional<GuildEntity> optionalGuildEntity = guildRepository.findById(event.getGuild().getIdLong());

        if (optionalGuildEntity.isPresent()) {
            GuildEntity guildEntity = optionalGuildEntity.get();

            if (guildEntity.getJoinMessageChannelId() != 0) {
                TextChannel joinChannel = event.getGuild().getTextChannelById(guildEntity.getJoinMessageChannelId());
                joinChannel.sendMessage("Welcome to the server!").queue();
            }
        }
    }
}
