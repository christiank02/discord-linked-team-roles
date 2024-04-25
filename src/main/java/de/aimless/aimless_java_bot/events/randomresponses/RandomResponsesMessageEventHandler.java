package de.aimless.aimless_java_bot.events.randomresponses;

import de.aimless.aimless_java_bot.utils.RandomResponseBuzzWord;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Component
public class RandomResponsesMessageEventHandler extends ListenerAdapter {

    private static final long TIME_TO_WAIT = 1000 * 60 * 5;
    private final Map<String, Long> lastMessageTimes = new HashMap<>();

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (isBotMessage(event)) {
            return;
        }

        handleBuzzWord(event);
    }

    private boolean isBotMessage(MessageReceivedEvent event) {
        return event.getAuthor().isBot();
    }

    private void handleBuzzWord(MessageReceivedEvent event) {
        Optional<RandomResponseBuzzWord> buzzWord = RandomResponseBuzzWord.findBuzzWord(event.getMessage().getContentRaw());
        buzzWord.ifPresent(word -> {
            RandomResponseBuzzWord buzzWordKey;

            if (word == RandomResponseBuzzWord.AIMLESS) {
                String customBuzzWord = String.format("custom_%s", event.getAuthor().getId());
                buzzWordKey = RandomResponseBuzzWord.findBuzzWord(customBuzzWord).orElse(null);
                System.out.println(buzzWordKey);
                if (Objects.isNull(buzzWordKey)) {
                    return;
                }
            } else {
                buzzWordKey = word;
            }
            Long lastMessageTime = lastMessageTimes.get(buzzWordKey.getBuzzWord());
            if (lastMessageTime == null || (System.currentTimeMillis() - lastMessageTime) > TIME_TO_WAIT) {
                MessageEmbed embed = (word == RandomResponseBuzzWord.AIMLESS) ? createCustomEmbed(buzzWordKey) : createEmbed(event, buzzWordKey);
                event.getChannel()
                        .sendMessageEmbeds(embed)
                        .setMessageReference(event.getMessageId())
                        .queue();
                lastMessageTimes.put(buzzWordKey.getBuzzWord(), System.currentTimeMillis());
            }
        });
    }

    private MessageEmbed createEmbed(MessageReceivedEvent event, RandomResponseBuzzWord buzzWord) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor(buzzWord.getResponse(), buzzWord.getResponseImage(), event.getJDA().getSelfUser().getAvatarUrl());
        embedBuilder.setImage(buzzWord.getResponseImage());
        embedBuilder.setColor(Color.decode("#00FFFF"));
        embedBuilder.setFooter(buzzWord.getFooter(), "https://cdn.discordapp.com/avatars/244209930268049409/e8c5beee3db2c89771b252c0a0ae07ee.webp?size=2048");
        return embedBuilder.build();
    }

    private MessageEmbed createCustomEmbed(RandomResponseBuzzWord buzzWord) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("Aimless", "https://cdn.discordapp.com/avatars/244209930268049409/e8c5beee3db2c89771b252c0a0ae07ee.webp?size=2048", "https://cdn.discordapp.com/avatars/244209930268049409/e8c5beee3db2c89771b252c0a0ae07ee.webp?size=2048");
        embedBuilder.setTitle(buzzWord.getResponse());
        embedBuilder.setImage(buzzWord.getResponseImage());
        embedBuilder.setColor(Color.decode("#FF80ED"));
        embedBuilder.setFooter(buzzWord.getFooter(), "https://cdn.discordapp.com/avatars/244209930268049409/e8c5beee3db2c89771b252c0a0ae07ee.webp?size=2048");
        return embedBuilder.build();
    }
}
