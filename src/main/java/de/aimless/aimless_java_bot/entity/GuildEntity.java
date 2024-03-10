package de.aimless.aimless_java_bot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class GuildEntity {

    @Id
    private long guildId;

    private long randomAnimeCharacterChannelId;

    public long getGuildId() {
        return guildId;
    }

    public void setGuildId(long guildId) {
        this.guildId = guildId;
    }

    public long getRandomAnimeCharacterChannelId() {
        return randomAnimeCharacterChannelId;
    }

    public void setRandomAnimeCharacterChannelId(long randomAnimeCharacterChannelId) {
        this.randomAnimeCharacterChannelId = randomAnimeCharacterChannelId;
    }
}
