package de.aimless.aimless_java_bot.events;

import de.aimless.aimless_java_bot.entity.CountingGameEntity;
import de.aimless.aimless_java_bot.entity.UserGuildEntity;
import de.aimless.aimless_java_bot.repository.CountingGameRepository;
import de.aimless.aimless_java_bot.repository.UserGuildRepository;
import de.aimless.aimless_java_bot.utils.CountingAbility;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class ButtonInteractionEventHandler extends ListenerAdapter {

    private final UserGuildRepository userGuildRepository;
    private final CountingGameRepository countingGameRepository;

    public ButtonInteractionEventHandler(UserGuildRepository userGuildRepository, CountingGameRepository countingGameRepository) {
        this.userGuildRepository = userGuildRepository;
        this.countingGameRepository = countingGameRepository;
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        event.deferReply().queue();
        if (isUseAbilityButton(event)) {
            handleUseAbilityButton(event);
        } else {
            event.getHook().sendMessage("Unknown button clicked").queue();
        }
    }

    private boolean isUseAbilityButton(ButtonInteractionEvent event) {
        return event.getComponentId().equals(CountingAbility.StreakSaver.getButtonId());
    }

    private void handleUseAbilityButton(ButtonInteractionEvent event) {
        Guild guild = event.getGuild();

        if (Objects.isNull(guild)) {
            sendDmErrorMessage(event);
            return;
        }

        long guildId = guild.getIdLong();
        Optional<CountingGameEntity> optionalCountingGameEntity = countingGameRepository.findByGuildGuildId(guildId);
        if (optionalCountingGameEntity.isEmpty()) {
            sendNoCountingGameErrorMessage(event);
            return;
        }

        CountingGameEntity countingGameEntity = optionalCountingGameEntity.get();
        handleCountingGameEntity(event, guildId, countingGameEntity);
    }

    private void handleCountingGameEntity(ButtonInteractionEvent event, long guildId, CountingGameEntity countingGameEntity) {
        boolean hasPendingDecision = countingGameEntity.getPendingDecision();
        long userId = event.getUser().getIdLong();

        Optional<UserGuildEntity> optionalUserGuildEntity = userGuildRepository.findByUserEntityIdAndGuildEntityGuildId(userId, guildId);
        optionalUserGuildEntity.ifPresent(userGuildEntity -> handleUserGuildEntity(event, countingGameEntity, hasPendingDecision, userGuildEntity));
    }

    private void sendDmErrorMessage(ButtonInteractionEvent event) {
        event.getHook().sendMessage("You can't use abilities in DMs!").setEphemeral(true).queue();
    }

    private void sendNoCountingGameErrorMessage(ButtonInteractionEvent event) {
        event.getHook().sendMessage("No counting game found for this guild!").setEphemeral(true).queue();
    }

    private void handleUserGuildEntity(ButtonInteractionEvent event, CountingGameEntity countingGameEntity, boolean hasPendingDecision, UserGuildEntity userGuildEntity) {
        if (userGuildEntity.getCountingGameAbilities().contains(CountingAbility.StreakSaver) && hasPendingDecision) {
            handleStreakSaver(event, countingGameEntity, userGuildEntity);
        } else if (!hasPendingDecision) {
            event.getHook().sendMessage("Too late! The pending decision has already been handled or expired").setEphemeral(true).queue();
        } else {
            event.getHook().sendMessage("You don't have the Streak Saver ability!").setEphemeral(true).queue();
        }
    }

    private void handleStreakSaver(ButtonInteractionEvent event, CountingGameEntity countingGameEntity, UserGuildEntity userGuildEntity) {
        countingGameEntity.setPendingDecision(false);
        countingGameEntity.setLastNumber(countingGameEntity.getLastNumberBeforeReset());
        countingGameRepository.save(countingGameEntity);

        userGuildEntity.getCountingGameAbilities().remove(CountingAbility.StreakSaver);
        userGuildRepository.save(userGuildEntity);

        String replyMessage = String.format("Streak saved! Next number is %d", countingGameEntity.getLastNumberBeforeReset() + 1);
        event.getHook().sendMessage(replyMessage).queue();
    }
}
