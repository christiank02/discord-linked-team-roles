package de.aimless.aimless_java_bot.mapper;

import de.aimless.aimless_java_bot.entity.GuildEntity;

public class GuildEntityMapper {

    public GuildEntity map(long guildId, long randomAnimeCharacterChannelId) {
        GuildEntity guildEntity = new GuildEntity();
        guildEntity.setGuildId(guildId);
        guildEntity.setRandomAnimeCharacterChannelId(randomAnimeCharacterChannelId);
        return guildEntity;
    }
}
