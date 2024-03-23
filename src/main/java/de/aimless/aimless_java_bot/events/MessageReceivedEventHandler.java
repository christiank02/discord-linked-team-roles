package de.aimless.aimless_java_bot.events;

import de.aimless.aimless_java_bot.entity.GuildEntity;
import de.aimless.aimless_java_bot.repository.GuildRepository;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MessageReceivedEventHandler extends ListenerAdapter {

    private final GuildRepository guildRepository;

    public MessageReceivedEventHandler(GuildRepository guildRepository) {
        this.guildRepository = guildRepository;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (isBotMessage(event)) {
            return;
        }

        Optional<GuildEntity> optionalGuildEntity = getGuildEntity(event);
        optionalGuildEntity.ifPresent(guildEntity -> handleCountingEvent(event, guildEntity));
    }

    private boolean isBotMessage(MessageReceivedEvent event) {
        return event.getAuthor().isBot();
    }

    private Optional<GuildEntity> getGuildEntity(MessageReceivedEvent event) {
        long guildId = event.getGuild().getIdLong();
        return guildRepository.findById(guildId);
    }

    private void handleCountingEvent(MessageReceivedEvent event, GuildEntity guildEntity) {
        if (isCountingChannelMessage(event, guildEntity) && isNumericMessage(event)) {
            int number = getNumberFromMessage(event);
            handleNumberMessage(event, guildEntity, number);
        }
    }

    private boolean isCountingChannelMessage(MessageReceivedEvent event, GuildEntity guildEntity) {
        return event.getChannel().getIdLong() == guildEntity.getCountingChannelId();
    }

    private boolean isNumericMessage(MessageReceivedEvent event) {
        return event.getMessage().getContentRaw().matches("^-?\\d+$");
    }

    private int getNumberFromMessage(MessageReceivedEvent event) {
        return Integer.parseInt(event.getMessage().getContentRaw());
    }

    private void handleNumberMessage(MessageReceivedEvent event, GuildEntity guildEntity, int number) {
        if (number == guildEntity.getCountingNumber() + 1) {
            updateCountingNumber(guildEntity, number);
        } else {
            sendErrorMessage(event, guildEntity);
            resetCountingNumber(guildEntity);
        }
    }

    private void updateCountingNumber(GuildEntity guildEntity, int number) {
        guildEntity.setCountingNumber(number);
        guildRepository.save(guildEntity);
    }

    private void sendErrorMessage(MessageReceivedEvent event, GuildEntity guildEntity) {
        String replyMessage = String.format("%s HAT DIE REIHE ZERSTÖRT! DIE NÄCHSTE ZAHL IST *%d*. Falsche Zahl.", event.getAuthor().getAsMention(), guildEntity.getCountingNumber() + 1);
        event.getChannel().sendMessage(replyMessage).queue();
    }

    private void resetCountingNumber(GuildEntity guildEntity) {
        guildEntity.setCountingNumber(0);
        guildRepository.save(guildEntity);
    }
}
