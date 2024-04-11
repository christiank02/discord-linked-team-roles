CREATE TABLE UserGuildEntity (
                                 id SERIAL PRIMARY KEY,
                                 user_id BIGINT,
                                 guild_id BIGINT,
                                 rainbowRoleEnabled BOOLEAN DEFAULT TRUE,
                                 boost_count INTEGER DEFAULT 0,
                                 FOREIGN KEY (user_id) REFERENCES UserEntity(id),
                                 FOREIGN KEY (guild_id) REFERENCES GuildEntity(guildId)
);