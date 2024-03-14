package de.aimless.aimless_java_bot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class GuildEntity {

    @Id
    private long guildId;

    @Column
    private long joinMessageChannelId;

    @Column
    private long randomAnimeCharacterChannelId;

    public long getGuildId() {
        return guildId;
    }

    public void setGuildId(long guildId) {
        this.guildId = guildId;
    }

    public long getJoinMessageChannelId() {
        return joinMessageChannelId;
    }

    public void setJoinMessageChannelId(long joinMessageChannelId) {
        this.joinMessageChannelId = joinMessageChannelId;
    }

    public long getRandomAnimeCharacterChannelId() {
        return randomAnimeCharacterChannelId;
    }

    public void setRandomAnimeCharacterChannelId(long randomAnimeCharacterChannelId) {
        this.randomAnimeCharacterChannelId = randomAnimeCharacterChannelId;
    }
}
