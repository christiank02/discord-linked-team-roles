package de.aimless.aimless_java_bot.service;

import de.aimless.aimless_java_bot.http.model.AnimeCharacter;
import de.aimless.aimless_java_bot.http.model.Images;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.util.List;


@Service
public class KissMarryKillGameService {

    public void sendGameMessages(List<AnimeCharacter> animeCharacterList, TextChannel channel) {
        if (animeCharacterList.size() != 3) {
            throw new IllegalArgumentException("AnimeCharacterList must contain exactly 3 characters for the game.");
        }

        // Create the embeds
        for (AnimeCharacter character : animeCharacterList) {
            MessageEmbed embed = createEmbed(character);
            sendMessageWithReactions(channel, embed);
        }
    }


    private MessageEmbed createEmbed(AnimeCharacter character) {
        Images characterImages = character.getImages();
        String imageUrl = characterImages.getJpg().getImage_url();

        return new EmbedBuilder()
                .setTitle(character.getName())
                .setDescription("React with your choice!")
                .setImage(imageUrl)
                .setColor(Color.RED)
                .build();
    }

    private void sendMessageWithReactions(TextChannel channel, MessageEmbed embed) {
        channel.sendMessageEmbeds(embed).queue(this::addReactions);
    }

    private void addReactions(Message message) {
        message.addReaction(Emoji.fromUnicode("💋")).queue(); // Kiss emoji
        message.addReaction(Emoji.fromUnicode("💍")).queue(); // Marry emoji
        message.addReaction(Emoji.fromUnicode("💀")).queue(); // Kill emoji
    }
}
