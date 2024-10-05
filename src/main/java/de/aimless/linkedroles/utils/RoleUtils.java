package de.aimless.linkedroles.utils;

import de.aimless.linkedroles.http.RoleConnectionWrapper;
import de.aimless.linkedroles.entity.LinkedRole;
import de.aimless.linkedroles.repository.LinkedRoleRepository;
import net.dv8tion.jda.api.entities.Role;

import java.util.List;
import java.util.Map;

public class RoleUtils {

    private RoleUtils() {
        throw new IllegalArgumentException("Utility class");
    }

    public static List<LinkedRole> getLinkedRoles(List<Role> oldUserRoles, LinkedRoleRepository linkedRoleRepository) {
        List<Long> userRoleIds = oldUserRoles.stream()
                .map(Role::getIdLong)
                .toList();
        return linkedRoleRepository.findAllByIdInOrderByLevelDesc(userRoleIds);
    }

    public static void updateRoleConnection(String applicationId, String accessToken, RoleConnectionWrapper roleConnectionWrapper, int roleLevel) {
        Map<String, Object> metaData = Map.of("rolelevel", roleLevel);
        roleConnectionWrapper.updateCurrentUserApplicationRoleConnection(applicationId, accessToken, metaData);
    }
}
