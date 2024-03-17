package de.aimless.aimless_java_bot.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class GuildEntity {

    @Id
    private long guildId;

    @Column
    private Long joinMessageChannelId;

    @Column
    private long randomAnimeCharacterChannelId;

    @OneToMany(mappedBy = "guildEntity")
    private Set<UserGuildEntity> userGuilds;

    public long getGuildId() {
        return guildId;
    }

    public void setGuildId(long guildId) {
        this.guildId = guildId;
    }

    public Long getJoinMessageChannelId() {
        return joinMessageChannelId;
    }

    public void setJoinMessageChannelId(Long joinMessageChannelId) {
        this.joinMessageChannelId = joinMessageChannelId;
    }

    public long getRandomAnimeCharacterChannelId() {
        return randomAnimeCharacterChannelId;
    }

    public void setRandomAnimeCharacterChannelId(long randomAnimeCharacterChannelId) {
        this.randomAnimeCharacterChannelId = randomAnimeCharacterChannelId;
    }

    public Set<UserGuildEntity> getUserGuilds() {
        return userGuilds;
    }

    public void setUserGuilds(Set<UserGuildEntity> userGuilds) {
        this.userGuilds = userGuilds;
    }
}
