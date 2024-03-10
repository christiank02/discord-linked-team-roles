CREATE TABLE linkedrole (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    level INT NOT NULL,
    guildId BIGINT NOT NULL
);