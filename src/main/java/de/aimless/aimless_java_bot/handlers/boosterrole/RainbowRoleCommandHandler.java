package de.aimless.aimless_java_bot.handlers.boosterrole;

import de.aimless.aimless_java_bot.command.CommandName;
import de.aimless.aimless_java_bot.entity.GuildEntity;
import de.aimless.aimless_java_bot.entity.UserEntity;
import de.aimless.aimless_java_bot.entity.UserGuildEntity;
import de.aimless.aimless_java_bot.handlers.AbstractCommandHandler;
import de.aimless.aimless_java_bot.repository.GuildRepository;
import de.aimless.aimless_java_bot.repository.UserEntityRepository;
import de.aimless.aimless_java_bot.repository.UserGuildRepository;
import de.aimless.aimless_java_bot.utils.CommandUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

/**
 * This class is responsible for handling the enabling and disabling of the rainbow role for the user.
 */
@Component
public class RainbowRoleCommandHandler extends AbstractCommandHandler {

    private final UserGuildRepository userGuildRepository;
    private final GuildRepository guildRepository;
    private final UserEntityRepository userEntityRepository;

    public RainbowRoleCommandHandler(UserGuildRepository userGuildRepository, GuildRepository guildRepository, UserEntityRepository userEntityRepository) {
        this.userGuildRepository = userGuildRepository;
        this.guildRepository = guildRepository;
        this.userEntityRepository = userEntityRepository;
    }

    @Override
    public void handleCommand(SlashCommandInteractionEvent event) {
        CommandUtils.acknowledgeAndDeferReply(event);

        GuildEntity guildEntity;
        try {
            Guild guild = CommandUtils.getGuildOrThrow(event);
            guildEntity = guildRepository.findById(guild.getIdLong())
                    .orElseGet(() -> {
                        GuildEntity newGuildEntity = new GuildEntity();
                        newGuildEntity.setGuildId(guild.getIdLong());
                        return guildRepository.save(newGuildEntity);
                    });
        } catch (IllegalStateException e) {
            event.getHook().sendMessage(e.getMessage()).queue();
            return;
        }

        long userId = event.getUser().getIdLong();
        UserEntity userEntity = userEntityRepository.findById(userId)
                .orElseGet(() -> {
                    UserEntity newUserEntity = new UserEntity();
                    newUserEntity.setId(userId);
                    return userEntityRepository.save(newUserEntity);
                });

        // we need an user repository to get the user
        UserGuildEntity userGuildEntity = userGuildRepository.findByUserEntityAndGuildEntity(userEntity, guildEntity)
                .orElseGet(() -> {
                    UserGuildEntity newUserGuild = new UserGuildEntity();
                    newUserGuild.setGuildEntity(guildEntity);
                    newUserGuild.setUserEntity(userEntity);
                    newUserGuild.setRainbowRoleEnabled(true);
                    return newUserGuild;
                });

        boolean rainbowRoleEnabled = !userGuildEntity.isRainbowRoleEnabled();
        userGuildEntity.setRainbowRoleEnabled(rainbowRoleEnabled);
        userGuildRepository.save(userGuildEntity);

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
