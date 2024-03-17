package de.aimless.aimless_java_bot.repository;

import de.aimless.aimless_java_bot.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
}
