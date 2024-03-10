CREATE TABLE BoosterRole
(
    id      BIGINT PRIMARY KEY,
    name    VARCHAR(255) NOT NULL,
    guildId BIGINT       NOT NULL
);

CREATE TABLE BoosterUser
(
    userId         BIGINT PRIMARY KEY,
    amountOfBoosts INT,
    guildId        BIGINT
);