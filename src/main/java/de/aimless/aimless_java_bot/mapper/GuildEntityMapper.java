package de.aimless.aimless_java_bot.mapper;

import de.aimless.aimless_java_bot.entity.GuildEntity;

public class GuildEntityMapper {

    public GuildEntity map(long guildId, long randomAnimeCharacterChannelId) {
        GuildEntity guildEntity = new GuildEntity();
        guildEntity.setGuildId(guildId);
        guildEntity.setRandomAnimeCharacterChannelId(randomAnimeCharacterChannelId);
        return guildEntity;
    }

    public GuildEntity mapWithJoinMessageChannel(long guildId, long joinMessageChannelId) {
        GuildEntity guildEntity = new GuildEntity();
        guildEntity.setGuildId(guildId);
        guildEntity.setJoinMessageChannelId(joinMessageChannelId);
        return guildEntity;
    }

    public GuildEntity mapWithCountingChannel(long guildId, long countingChannelId) {
        GuildEntity guildEntity = new GuildEntity();
        guildEntity.setGuildId(guildId);
        guildEntity.setCountingChannelId(countingChannelId);
        return guildEntity;
    }
}
