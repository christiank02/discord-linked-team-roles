package de.aimless.aimless_java_bot.repository;

import de.aimless.aimless_java_bot.entity.GuildEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GuildRepository extends JpaRepository<GuildEntity, Long> {

    List<GuildEntity> findAllByRandomAnimeCharacterChannelIdNotNull();
}
