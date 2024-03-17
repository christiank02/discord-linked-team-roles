CREATE TABLE userentity (
                            id BIGSERIAL PRIMARY KEY

);

CREATE TABLE user_guild (
                            id BIGSERIAL PRIMARY KEY,
                            user_id BIGINT,
                            guild_id BIGINT,
                            rainbow_role_enabled BOOLEAN,
                            FOREIGN KEY (user_id) REFERENCES userentity(id),
                            FOREIGN KEY (guild_id) REFERENCES guildentity(guildid)
);