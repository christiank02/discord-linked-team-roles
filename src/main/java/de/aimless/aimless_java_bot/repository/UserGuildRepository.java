package de.aimless.aimless_java_bot.repository;

import de.aimless.aimless_java_bot.entity.GuildEntity;
import de.aimless.aimless_java_bot.entity.UserEntity;
import de.aimless.aimless_java_bot.entity.UserGuildEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserGuildRepository extends JpaRepository<UserGuildEntity, Long> {

    Optional<UserGuildEntity> findByUserEntityAndGuildEntity(UserEntity user, GuildEntity guild);

}
