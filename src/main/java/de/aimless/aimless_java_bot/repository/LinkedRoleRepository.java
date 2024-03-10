package de.aimless.aimless_java_bot.repository;

import de.aimless.aimless_java_bot.entity.LinkedRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LinkedRoleRepository extends JpaRepository<LinkedRole, Long> {

    List<LinkedRole> findAllByIdInOrderByLevelDesc(List<Long> id);

    List<LinkedRole> findAllByGuildId(long guildId);
}
