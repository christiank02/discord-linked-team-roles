package de.aimless.aimless_java_bot.repository;

import de.aimless.aimless_java_bot.entity.BoosterRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoosterRoleRepository extends JpaRepository<BoosterRole, Long> {

    List<BoosterRole> findAllByGuildId(long idLong);

    @Query("SELECT br.id FROM BoosterRole br WHERE br.guildId = ?1 AND br.autoAssignable = true")
    List<Long> findAllIdsByGuildIdAndAutoAssignableIsTrue(long idLong);

    @Query("SELECT br.id FROM BoosterRole br WHERE br.guildId = ?1")
    List<Long> findAllIdsByGuildId(long idLong);
}
