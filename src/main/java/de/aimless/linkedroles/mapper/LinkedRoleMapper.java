package de.aimless.linkedroles.mapper;

import de.aimless.linkedroles.entity.LinkedRole;
import net.dv8tion.jda.api.entities.Role;

public class LinkedRoleMapper {

    public LinkedRole map(Role role, int roleLevel) {
        LinkedRole linkedRole = new LinkedRole();
        linkedRole.setId(role.getIdLong());
        linkedRole.setName(role.getName());
        linkedRole.setLevel(roleLevel);
        return linkedRole;
    }
}
