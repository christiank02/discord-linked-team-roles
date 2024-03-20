package de.aimless.aimless_java_bot.events;

import de.aimless.aimless_java_bot.repository.UserGuildRepository;
import lombok.NonNull;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class GuildMemberRemoveEventHandler extends ListenerAdapter {

    private final UserGuildRepository userGuildRepository;

    public GuildMemberRemoveEventHandler(UserGuildRepository userGuildRepository) {
        this.userGuildRepository = userGuildRepository;
    }

    @Override
    public void onGuildMemberRemove(@NonNull GuildMemberRemoveEvent event) {
        // set the boost count back to 3 on leave
        if (Objects.nonNull(event.getMember())) {
            long memberId = event.getMember().getIdLong();
            long guildId = event.getGuild().getIdLong();

            userGuildRepository.findByUserEntityIdAndGuildEntityGuildId(memberId, guildId).ifPresent(userGuildEntity -> {
                userGuildEntity.setBoostCount(0);
                userGuildRepository.save(userGuildEntity);
            });
        }
    }
}
