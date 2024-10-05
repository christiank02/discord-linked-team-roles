package de.aimless.linkedroles.repository;

import de.aimless.linkedroles.entity.LinkedRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LinkedRoleRepository extends JpaRepository<LinkedRole, Long> {

    List<LinkedRole> findAllByIdInOrderByLevelDesc(List<Long> id);

    List<LinkedRole> findAllByGuildId(long guildId);
}
