package de.aimless.aimless_java_bot.events;

import de.aimless.aimless_java_bot.entity.CountingGameEntity;
import de.aimless.aimless_java_bot.repository.CountingGameRepository;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class MessageReceivedEventHandler extends ListenerAdapter {

    private final CountingGameRepository countingGameRepository;

    public MessageReceivedEventHandler(CountingGameRepository countingGameRepository) {
        this.countingGameRepository = countingGameRepository;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (isBotMessage(event)) {
            return;
        }

        Optional<CountingGameEntity> optionalGuildEntity = getGuildEntity(event);
        optionalGuildEntity.ifPresent(countingGameEntity -> handleCountingEvent(event, countingGameEntity));
    }

    private boolean isBotMessage(MessageReceivedEvent event) {
        return event.getAuthor().isBot();
    }

    private Optional<CountingGameEntity> getGuildEntity(MessageReceivedEvent event) {
        long guildId = event.getGuild().getIdLong();
        return countingGameRepository.findByGuildGuildId(guildId);
    }

    private void handleCountingEvent(MessageReceivedEvent event, CountingGameEntity countingGameEntity) {
        if (isCountingChannelMessage(event, countingGameEntity) && isNumericMessage(event)) {
            int number = getNumberFromMessage(event);
            handleNumberMessage(event, countingGameEntity, number);
        }
    }

    private boolean isCountingChannelMessage(MessageReceivedEvent event, CountingGameEntity countingGameEntity) {
        return event.getChannel().getIdLong() == countingGameEntity.getChannelId();
    }

    private boolean isNumericMessage(MessageReceivedEvent event) {
        return event.getMessage().getContentRaw().matches("^-?\\d+$");
    }

    private int getNumberFromMessage(MessageReceivedEvent event) {
        return Integer.parseInt(event.getMessage().getContentRaw());
    }

    private void handleNumberMessage(MessageReceivedEvent event, CountingGameEntity countingGameEntity, int number) {
        if (isNextNumberAndDifferentUser(event, countingGameEntity, number)) {
            handleValidNumber(event, countingGameEntity, number);
        } else {
            handleError(event, countingGameEntity);
        }
    }

    private boolean isNextNumberAndDifferentUser(MessageReceivedEvent event, CountingGameEntity countingGameEntity, int number) {
        return number == countingGameEntity.getLastNumber() + 1 && !isMessageFromSameUser(event, countingGameEntity);
    }

    private void handleValidNumber(MessageReceivedEvent event, CountingGameEntity countingGameEntity, int number) {
        updateCountingInfo(countingGameEntity, number, event);
        approveCountingNumber(event);
    }

    private void handleError(MessageReceivedEvent event, CountingGameEntity countingGameEntity) {
        addErrorReaction(event);
        sendErrorMessage(event, countingGameEntity);
        resetCountingNumber(countingGameEntity);
    }

    private boolean isMessageFromSameUser(MessageReceivedEvent event, CountingGameEntity countingGameEntity) {
        if (Objects.isNull(countingGameEntity.getLastUserId())) {
            return false;
        }
        return event.getAuthor().getIdLong() == countingGameEntity.getLastUserId();
    }

    private void updateCountingInfo(CountingGameEntity countingGameEntity, int number, MessageReceivedEvent event) {
        countingGameEntity.setLastNumber(number);
        countingGameEntity.setLastUserId(event.getAuthor().getIdLong());
        countingGameRepository.save(countingGameEntity);
    }

    private void approveCountingNumber(MessageReceivedEvent event) {
        event.getMessage()
                .addReaction(Emoji.fromUnicode("U+2705"))
                .queue();
    }

    private void addErrorReaction(MessageReceivedEvent event) {
        event.getMessage()
                .addReaction(Emoji.fromUnicode("U+274C"))
                .queue();
    }

    private void sendErrorMessage(MessageReceivedEvent event, CountingGameEntity countingGameEntity) {
        String replyMessage;
        if (isMessageFromSameUser(event, countingGameEntity)) {
            replyMessage = String.format("%s HAT DIE REIHE BEI %d ZERSTÖRT!! Die nächste Zahl ist *1. Du darfst nicht zweimal hintereinander zählen.*", event.getAuthor().getAsMention(), countingGameEntity.getLastNumber());
        } else {
            replyMessage = String.format("%s HAT DIE REIHE ZERSTÖRT! DIE NÄCHSTE ZAHL IST *1. Falsche Zahl.*", event.getAuthor().getAsMention());
        }
        event.getChannel().sendMessage(replyMessage).queue();
    }

    private void resetCountingNumber(CountingGameEntity countingGameEntity) {
        countingGameEntity.setLastNumber(0);
        countingGameRepository.save(countingGameEntity);
    }
}
