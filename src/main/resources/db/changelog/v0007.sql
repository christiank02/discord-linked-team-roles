ALTER TABLE guildentity
    ADD COLUMN countingChannelId BIGINT DEFAULT NULL,
    ADD COLUMN countingNumber int DEFAULT 0;