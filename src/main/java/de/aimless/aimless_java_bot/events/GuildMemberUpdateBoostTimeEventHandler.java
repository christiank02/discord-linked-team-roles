package de.aimless.aimless_java_bot.events;

import de.aimless.aimless_java_bot.repository.BoosterRoleRepository;
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

    public GuildMemberUpdateBoostTimeEventHandler(BoosterRoleRepository boosterRoleRepository) {
        this.boosterRoleRepository = boosterRoleRepository;
    }

    // TODO: Before assigning the booster role to the user, we have to check if the user has disabled the rainbow role
    // TODO: Update the command handler for Rainbow role enabling to remove the booster role if the user has disabled the rainbow role

    @Override
    public void onGuildMemberUpdateBoostTime(GuildMemberUpdateBoostTimeEvent event) {
        OffsetDateTime newTimeBoosted = event.getNewTimeBoosted();
        OffsetDateTime oldTimeBoosted = event.getOldTimeBoosted();

        if (Objects.isNull(oldTimeBoosted)) {
            return; // User is boosting for the first time
        }

        Guild guild = event.getGuild();
        List<Long> boosterRoles = boosterRoleRepository.findAllIdsByGuildIdAndAutoAssignableIsFalse(guild.getIdLong());
        List<Role> roles = boosterRoles.stream()
                .map(guild::getRoleById)
                .filter(Objects::nonNull)
                .toList();

        Member eventMember = event.getMember();
        if (Objects.isNull(newTimeBoosted)) {
            guild.modifyMemberRoles(eventMember, null, roles).queue();
            return; // User is not boosting anymore
        }


        OffsetDateTime thirtyDaysBeforeNewBoost = newTimeBoosted.minusDays(30);
        if (oldTimeBoosted.isAfter(thirtyDaysBeforeNewBoost) && eventMember.isBoosting()) {
            guild.modifyMemberRoles(eventMember, roles, null).queue();
        }
    }
}
