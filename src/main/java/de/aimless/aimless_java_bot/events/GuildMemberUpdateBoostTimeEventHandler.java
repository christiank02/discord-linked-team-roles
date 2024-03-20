package de.aimless.aimless_java_bot.events;

import de.aimless.aimless_java_bot.entity.GuildEntity;
import de.aimless.aimless_java_bot.entity.UserEntity;
import de.aimless.aimless_java_bot.entity.UserGuildEntity;
import de.aimless.aimless_java_bot.repository.BoosterRoleRepository;
import de.aimless.aimless_java_bot.repository.GuildRepository;
import de.aimless.aimless_java_bot.repository.UserEntityRepository;
import de.aimless.aimless_java_bot.repository.UserGuildRepository;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateBoostTimeEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

@Component
public class GuildMemberUpdateBoostTimeEventHandler extends ListenerAdapter {

    private final BoosterRoleRepository boosterRoleRepository;
    private final UserGuildRepository userGuildRepository;
    private final UserEntityRepository userEntityRepository;
    private final GuildRepository guildRepository;

    public GuildMemberUpdateBoostTimeEventHandler(BoosterRoleRepository boosterRoleRepository, UserGuildRepository userGuildRepository, UserEntityRepository userEntityRepository, GuildRepository guildRepository) {
        this.boosterRoleRepository = boosterRoleRepository;
        this.userGuildRepository = userGuildRepository;
        this.userEntityRepository = userEntityRepository;
        this.guildRepository = guildRepository;
    }

    @Override
    public void onGuildMemberUpdateBoostTime(GuildMemberUpdateBoostTimeEvent event) {
        OffsetDateTime newTimeBoosted = event.getNewTimeBoosted();
        OffsetDateTime oldTimeBoosted = event.getOldTimeBoosted();

        Guild guild = event.getGuild();

        List<Long> boosterRoles = boosterRoleRepository.findAllIdsByGuildIdAndAutoAssignableIsTrue(guild.getIdLong());
        List<Role> roles = boosterRoles.stream()
                .map(guild::getRoleById)
                .filter(Objects::nonNull)
                .toList();

        Member eventMember = event.getMember();

        // get guild Entity
        GuildEntity guildEntity = guildRepository.findById(guild.getIdLong()).orElseGet(() -> {
            GuildEntity newGuildEntity = new GuildEntity();
            newGuildEntity.setGuildId(guild.getIdLong());
            return guildRepository.save(newGuildEntity);
        });

        UserEntity userEntity = userEntityRepository.findById(eventMember.getIdLong()).orElseGet(() -> {
            UserEntity newUserEntity = new UserEntity();
            newUserEntity.setId(eventMember.getIdLong());
            return userEntityRepository.save(newUserEntity);
        });

        UserGuildEntity userGuildEntity = userGuildRepository.findByUserEntityIdAndGuildEntityGuildId(eventMember.getIdLong(), guild.getIdLong()).orElseGet(
                () -> {
                    UserGuildEntity newUserGuildEntity = new UserGuildEntity();
                    newUserGuildEntity.setUserEntity(userEntity);
                    newUserGuildEntity.setGuildEntity(guildEntity);
                    return userGuildRepository.save(newUserGuildEntity);
                }
        );

        if (Objects.isNull(newTimeBoosted)) {
            guild.modifyMemberRoles(eventMember, null, roles).queue();
            userGuildEntity.setBoostCount(0); // User is not boosting anymore
            return;
        }

        if (Objects.isNull(oldTimeBoosted)) {
            userGuildEntity.setBoostCount(1); // User is boosting for the first time
            return;
        }

        boolean hasUserRainbowRoleEnabled = userGuildEntity.isRainbowRoleEnabled();

        OffsetDateTime thirtyDaysBeforeNewBoost = newTimeBoosted.minusDays(30);
        if (oldTimeBoosted.isAfter(thirtyDaysBeforeNewBoost) && eventMember.isBoosting() && hasUserRainbowRoleEnabled) {
            guild.modifyMemberRoles(eventMember, roles, null).queue();
            userGuildEntity.setBoostCount(2); // User is boosting more than once in 30 days
        }

        userGuildRepository.save(userGuildEntity);
    }
}
