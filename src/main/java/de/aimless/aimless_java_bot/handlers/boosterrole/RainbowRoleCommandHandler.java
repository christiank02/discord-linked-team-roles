package de.aimless.aimless_java_bot.handlers.boosterrole;

import de.aimless.aimless_java_bot.command.CommandName;
import de.aimless.aimless_java_bot.entity.GuildEntity;
import de.aimless.aimless_java_bot.entity.UserEntity;
import de.aimless.aimless_java_bot.entity.UserGuildEntity;
import de.aimless.aimless_java_bot.handlers.AbstractCommandHandler;
import de.aimless.aimless_java_bot.repository.BoosterRoleRepository;
import de.aimless.aimless_java_bot.repository.GuildRepository;
import de.aimless.aimless_java_bot.repository.UserEntityRepository;
import de.aimless.aimless_java_bot.repository.UserGuildRepository;
import de.aimless.aimless_java_bot.utils.CommandUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class RainbowRoleCommandHandler extends AbstractCommandHandler {

    private final UserGuildRepository userGuildRepository;
    private final GuildRepository guildRepository;
    private final UserEntityRepository userEntityRepository;
    private final BoosterRoleRepository boosterRoleRepository;
    private final JDA jda;

    public RainbowRoleCommandHandler(UserGuildRepository userGuildRepository, GuildRepository guildRepository, UserEntityRepository userEntityRepository, BoosterRoleRepository boosterRoleRepository, JDA jda) {
        this.userGuildRepository = userGuildRepository;
        this.guildRepository = guildRepository;
        this.userEntityRepository = userEntityRepository;
        this.boosterRoleRepository = boosterRoleRepository;
        this.jda = jda;
    }

    @Override
    public void handleCommand(SlashCommandInteractionEvent event) {
        CommandUtils.acknowledgeAndDeferReply(event);

        try {
            GuildEntity guildEntity = getOrCreateGuildEntity(event);
            UserEntity userEntity = getOrCreateUserEntity(event);
            UserGuildEntity userGuildEntity = getOrCreateUserGuildEntity(userEntity, guildEntity);

            boolean rainbowRoleEnabled = !userGuildEntity.isRainbowRoleEnabled();
            userGuildEntity.setRainbowRoleEnabled(rainbowRoleEnabled);
            userGuildRepository.save(userGuildEntity);

            handleRainbowRole(event, guildEntity, userEntity, userGuildEntity, rainbowRoleEnabled);
            sendReplyMessage(event, rainbowRoleEnabled);
        } catch (IllegalStateException e) {
            event.getHook()
                    .sendMessage(e.getMessage())
                    .setEphemeral(true)
                    .queue();
        }
    }

    private GuildEntity getOrCreateGuildEntity(SlashCommandInteractionEvent event) throws IllegalStateException {
        Guild guild = CommandUtils.getGuildOrThrow(event);
        long guildId = guild.getIdLong();
        return guildRepository.findById(guildId)
                .orElseGet(() -> {
                    GuildEntity newGuildEntity = new GuildEntity();
                    newGuildEntity.setGuildId(guildId);
                    return guildRepository.save(newGuildEntity);
                });
    }

    private UserEntity getOrCreateUserEntity(SlashCommandInteractionEvent event) {
        long userId = event.getUser().getIdLong();
        return userEntityRepository.findById(userId)
                .orElseGet(() -> {
                    UserEntity newUserEntity = new UserEntity();
                    newUserEntity.setId(userId);
                    return userEntityRepository.save(newUserEntity);
                });
    }

    private UserGuildEntity getOrCreateUserGuildEntity(UserEntity userEntity, GuildEntity guildEntity) {
        return userGuildRepository.findByUserEntityIdAndGuildEntityGuildId(userEntity.getId(), guildEntity.getGuildId())
                .orElseGet(() -> {
                    UserGuildEntity newUserGuildEntity = new UserGuildEntity();
                    newUserGuildEntity.setUserEntity(userEntity);
                    newUserGuildEntity.setGuildEntity(guildEntity);
                    return userGuildRepository.save(newUserGuildEntity);
                });
    }

    private void handleRainbowRole(SlashCommandInteractionEvent event, GuildEntity guildEntity, UserEntity userEntity, UserGuildEntity userGuildEntity, boolean rainbowRoleEnabled) {
        Guild guild = jda.getGuildById(guildEntity.getGuildId());
        if (Objects.isNull(guild)) {
            sendErrorMessage(event);
            return;
        }

        Member guildMember = guild.getMemberById(userEntity.getId());
        if (Objects.isNull(guildMember)) {
            sendErrorMessage(event);
            return;
        }

        List<Long> boosterRoles = boosterRoleRepository.findAllIdsByGuildIdAndAutoAssignableIsTrue(guild.getIdLong());
        List<Role> roles = getRoles(guild, boosterRoles);

        if (rainbowRoleEnabled && userGuildEntity.getBoostCount() > 1) {
            guild.modifyMemberRoles(guildMember, roles, null).queue();
        } else {
            guild.modifyMemberRoles(guildMember, null, roles).queue();
        }
    }

    private List<Role> getRoles(Guild guild, List<Long> boosterRoles) {
        return boosterRoles.stream()
                .map(guild::getRoleById)
                .filter(Objects::nonNull)
                .filter(role -> !role.isManaged())
                .toList();
    }

    private void sendErrorMessage(SlashCommandInteractionEvent event) {
        event.getHook()
                .sendMessage("Error while handling the rainbow role. Please try again later.")
                .setEphemeral(true)
                .queue();
    }

    private void sendReplyMessage(SlashCommandInteractionEvent event, boolean rainbowRoleEnabled) {
        String replyMessage = rainbowRoleEnabled ? "You have enabled the rainbow role for yourself." : "You have disabled the rainbow role for yourself.";
        event.getHook()
                .sendMessage(replyMessage)
                .setEphemeral(true)
                .queue();
    }

    @Override
    public boolean canHandle(CommandName commandName, CommandName subCommandName) {
        return commandName == CommandName.RAINBOWROLE;
    }
}