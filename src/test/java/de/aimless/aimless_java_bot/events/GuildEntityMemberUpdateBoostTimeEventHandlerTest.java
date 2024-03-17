package de.aimless.aimless_java_bot.events;

import de.aimless.aimless_java_bot.entity.UserGuildEntity;
import de.aimless.aimless_java_bot.repository.BoosterRoleRepository;
import de.aimless.aimless_java_bot.repository.UserGuildRepository;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateBoostTimeEvent;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class GuildEntityMemberUpdateBoostTimeEventHandlerTest {

    @Mock
    private BoosterRoleRepository boosterRoleRepository;

    @Mock
    private UserGuildRepository userGuildRepository;

    @InjectMocks
    private GuildMemberUpdateBoostTimeEventHandler handler;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("Should not add roles when user boosts for the first time")
    @Test
    void shouldNotAddRolesWhenUserBoostsForTheFirstTime() {
        GuildMemberUpdateBoostTimeEvent event = mock(GuildMemberUpdateBoostTimeEvent.class);
        Guild guild = mock(Guild.class);
        Member member = mock(Member.class);

        when(event.getGuild()).thenReturn(guild);
        when(event.getMember()).thenReturn(member);
        when(event.getNewTimeBoosted()).thenReturn(OffsetDateTime.now());
        when(event.getOldTimeBoosted()).thenReturn(null);

        List<Long> boosterRoles = Arrays.asList(123L, 456L, 789L);
        when(boosterRoleRepository.findAllIdsByGuildIdAndAutoAssignableIsTrue(anyLong())).thenReturn(boosterRoles);

        handler.onGuildMemberUpdateBoostTime(event);

        verify(guild, never()).modifyMemberRoles(eq(member), anyList(), eq(null));
    }

    @DisplayName("Should add roles when user has boosted twice or more within a month")
    @Test
    void shouldAddRolesWhenUserBoostsHasBoostedTwiceOrMoreInAMonth() {
        GuildMemberUpdateBoostTimeEvent event = mock(GuildMemberUpdateBoostTimeEvent.class);
        Guild guild = mock(Guild.class);
        Member member = mock(Member.class);

        when(event.getGuild()).thenReturn(guild);
        when(event.getMember()).thenReturn(member);
        when(event.getNewTimeBoosted()).thenReturn(OffsetDateTime.now());
        when(event.getOldTimeBoosted()).thenReturn(OffsetDateTime.now().minusDays(15));

        List<Long> boosterRoles = Arrays.asList(123L, 456L, 789L);
        when(boosterRoleRepository.findAllIdsByGuildIdAndAutoAssignableIsTrue(anyLong())).thenReturn(boosterRoles);
        when(member.isBoosting()).thenReturn(true);

        // Mock the AuditableRestAction object
        AuditableRestAction<Void> auditableRestAction = mock(AuditableRestAction.class);
        when(guild.modifyMemberRoles(eq(member), anyList(), eq(null))).thenReturn(auditableRestAction);
        doNothing().when(auditableRestAction).queue();

        handler.onGuildMemberUpdateBoostTime(event);

        verify(guild, times(1)).modifyMemberRoles(eq(member), anyList(), eq(null));
    }

    @DisplayName("Should remove roles when user has stopped boosting")
    @Test
    void shouldRemoveRolesWhenUserBoostsHasStoppedBoosting() {
        GuildMemberUpdateBoostTimeEvent event = mock(GuildMemberUpdateBoostTimeEvent.class);
        Guild guild = mock(Guild.class);
        Member member = mock(Member.class);

        when(event.getGuild()).thenReturn(guild);
        when(event.getMember()).thenReturn(member);
        when(event.getNewTimeBoosted()).thenReturn(null);
        when(event.getOldTimeBoosted()).thenReturn(OffsetDateTime.now().minusDays(15));

        List<Long> boosterRoles = Arrays.asList(123L, 456L, 789L);
        when(boosterRoleRepository.findAllIdsByGuildIdAndAutoAssignableIsTrue(anyLong())).thenReturn(boosterRoles);
        when(member.isBoosting()).thenReturn(true);

        // Mock the AuditableRestAction object
        AuditableRestAction<Void> auditableRestAction = mock(AuditableRestAction.class);
        when(guild.modifyMemberRoles(eq(member), eq(null), anyList())).thenReturn(auditableRestAction);
        doNothing().when(auditableRestAction).queue();

        handler.onGuildMemberUpdateBoostTime(event);

        verify(guild, times(1)).modifyMemberRoles(eq(member), eq(null), anyList());
    }

    @DisplayName("Should not add roles when user has disabled the rainbow role")
    @Test
    void shouldNotAddRolesWhenUserHasDisabledTheRainbowRole() {
        GuildMemberUpdateBoostTimeEvent event = mock(GuildMemberUpdateBoostTimeEvent.class);
        Guild guild = mock(Guild.class);
        Member member = mock(Member.class);

        when(event.getGuild()).thenReturn(guild);
        when(event.getMember()).thenReturn(member);
        when(event.getNewTimeBoosted()).thenReturn(OffsetDateTime.now());
        when(event.getOldTimeBoosted()).thenReturn(OffsetDateTime.now().minusDays(15));

        List<Long> boosterRoles = Arrays.asList(123L, 456L, 789L);
        when(boosterRoleRepository.findAllIdsByGuildIdAndAutoAssignableIsTrue(anyLong())).thenReturn(boosterRoles);
        when(member.isBoosting()).thenReturn(true);

        // Mock the UserGuildRepository object
        UserGuildEntity userGuildEntity = mock(UserGuildEntity.class);
        when(userGuildEntity.isRainbowRoleEnabled()).thenReturn(false);
        when(userGuildRepository.findByUserEntityIdAndGuildEntityGuildId(anyLong(), anyLong())).thenReturn(Optional.of(userGuildEntity));

        // Mock the AuditableRestAction object
        AuditableRestAction<Void> auditableRestAction = mock(AuditableRestAction.class);
        when(guild.modifyMemberRoles(eq(member), anyList(), eq(null))).thenReturn(auditableRestAction);
        doNothing().when(auditableRestAction).queue();

        handler.onGuildMemberUpdateBoostTime(event);

        verify(guild, never()).modifyMemberRoles(eq(member), anyList(), eq(null));
    }
}