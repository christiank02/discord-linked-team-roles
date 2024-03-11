package de.aimless.aimless_java_bot.repository;

import de.aimless.aimless_java_bot.entity.BoosterRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoosterRoleRepository extends JpaRepository<BoosterRole, Long> {

    List<BoosterRole> findAllByGuildId(long idLong);

    List<Long> findAllIdsByGuildIdAndAutoAssignableIsFalse(long idLong);

}
