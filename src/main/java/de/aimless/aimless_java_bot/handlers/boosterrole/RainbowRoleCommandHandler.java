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

/**
 * This class is responsible for handling the enabling and disabling of the rainbow role for the user.
 */
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

        GuildEntity guildEntity = getOrCreateGuildEntity(event);
        if(Objects.isNull(guildEntity)) {
            return;
        }

        UserEntity userEntity = getOrCreateUserEntity(event);
        UserGuildEntity userGuildEntity = getOrCreateUserGuildEntity(userEntity, guildEntity);

        boolean rainbowRoleEnabled = !userGuildEntity.isRainbowRoleEnabled();
        userGuildEntity.setRainbowRoleEnabled(rainbowRoleEnabled);
        userGuildRepository.save(userGuildEntity);

        if (!rainbowRoleEnabled) {
            handleRainbowRoleDisabled(event, guildEntity, userEntity);
        }

        sendReplyMessage(event, rainbowRoleEnabled);
    }

    private GuildEntity getOrCreateGuildEntity(SlashCommandInteractionEvent event) {
        try {
            Guild guild = CommandUtils.getGuildOrThrow(event);
            return guildRepository.findById(guild.getIdLong())
                    .orElseGet(() -> {
                        GuildEntity newGuildEntity = new GuildEntity();
                        newGuildEntity.setGuildId(guild.getIdLong());
                        return guildRepository.save(newGuildEntity);
                    });
        } catch (IllegalStateException e) {
            event.getHook().sendMessage(e.getMessage()).queue();
            return null;
        }
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
        return userGuildRepository.findByUserEntityAndGuildEntity(userEntity, guildEntity)
                .orElseGet(() -> {
                    UserGuildEntity newUserGuild = new UserGuildEntity();
                    newUserGuild.setGuildEntity(guildEntity);
                    newUserGuild.setUserEntity(userEntity);
                    newUserGuild.setRainbowRoleEnabled(true);
                    return newUserGuild;
                });
    }

    private void handleRainbowRoleDisabled(SlashCommandInteractionEvent event, GuildEntity guildEntity, UserEntity userEntity) {
        Guild guild = jda.getGuildById(guildEntity.getGuildId());

        if (Objects.isNull(guild)) {
            event.getHook()
                    .sendMessage("Error while disabling the rainbow role. Please try again later.")
                    .setEphemeral(true)
                    .queue();
            return;
        }

        Member guildMember = guild.getMemberById(userEntity.getId());
        if (Objects.isNull(guildMember)) {
            event.getHook()
                    .sendMessage("Error while disabling the rainbow role. Please try again later.")
                    .setEphemeral(true)
                    .queue();
            return;
        }

        List<Long> boosterRoles = boosterRoleRepository.findAllIdsByGuildId(guild.getIdLong());
        List<Role> roles = boosterRoles.stream()
                .map(guild::getRoleById)
                .filter(Objects::nonNull)
                .toList();

        guild.modifyMemberRoles(guildMember, null, roles).queue();
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
