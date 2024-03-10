package de.aimless.aimless_java_bot.tasks;

import de.aimless.aimless_java_bot.entity.GuildEntity;
import de.aimless.aimless_java_bot.http.RandomAnimeCharacterWrapper;
import de.aimless.aimless_java_bot.http.model.AnimeCharacter;
import de.aimless.aimless_java_bot.repository.GuildRepository;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class RandomAnimeCharacterTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(RandomAnimeCharacterTask.class);

    private final RandomAnimeCharacterWrapper randomAnimeCharacterWrapper;
    private final GuildRepository guildRepository;
    private final JDA jda;

    public RandomAnimeCharacterTask(JDA jda, RandomAnimeCharacterWrapper randomAnimeCharacterWrapper, GuildRepository guildRepository) {
        this.jda = jda;
        this.randomAnimeCharacterWrapper = randomAnimeCharacterWrapper;
        this.guildRepository = guildRepository;
    }

    //@Scheduled(cron = "0 0 */2 * * ?") // every 2 hours
    @Scheduled(cron = "0 * * * * ?") // every minute
    public void sendRandomAnimeCharacters() {
        List<AnimeCharacter> characters = randomAnimeCharacterWrapper.getRandomAnimeCharacters(3);

        // send it to the discord channel which is set in the database
        List<GuildEntity> guildEntityList = guildRepository.findAllByRandomAnimeCharacterChannelIdNotNull();

        guildEntityList.forEach(guildEntity -> {
            Guild guild = jda.getGuildById(guildEntity.getGuildId());

            if (Objects.isNull(guild)) {
                LOGGER.warn("Guild with id {} not found", guildEntity.getGuildId());
                return;
            }

            TextChannel randomCharacterTextChannel = guild.getTextChannelById(guildEntity.getRandomAnimeCharacterChannelId());

            if(Objects.isNull(randomCharacterTextChannel)) {
                LOGGER.warn("TextChannel with id {} not found", guildEntity.getRandomAnimeCharacterChannelId());
                return;
            }


            randomCharacterTextChannel.sendMessage("Random Anime Characters: " + characters).queue();
        });

        characters.forEach(character -> LOGGER.info("Random Anime Character: {}", character));
    }
}
