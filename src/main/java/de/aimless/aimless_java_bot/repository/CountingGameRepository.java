package de.aimless.aimless_java_bot.repository;

import de.aimless.aimless_java_bot.entity.CountingGameEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CountingGameRepository extends JpaRepository<CountingGameEntity, Long> {

    Optional<CountingGameEntity> findByGuildGuildId(Long guildId);
}
