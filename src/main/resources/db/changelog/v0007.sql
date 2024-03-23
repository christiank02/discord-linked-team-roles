CREATE TABLE IF NOT EXISTS countingGameEntity (
                                                  id BIGSERIAL PRIMARY KEY,
                                                  channelId BIGINT DEFAULT NULL,
                                                  lastUserId BIGINT DEFAULT NULL,
                                                  lastNumber INT DEFAULT 0,
                                                  guild_id BIGINT,
                                                  FOREIGN KEY (guild_id) REFERENCES GuildEntity(guildid)
);