package de.aimless.aimless_java_bot.mapper;

import de.aimless.aimless_java_bot.entity.LinkedRole;
import net.dv8tion.jda.api.entities.Role;

public class LinkedRoleMapper {

    public LinkedRole map(Role role, int roleLevel, long guildId) {
        LinkedRole linkedRole = new LinkedRole();
        linkedRole.setId(role.getIdLong());
        linkedRole.setName(role.getName());
        linkedRole.setLevel(roleLevel);
        linkedRole.setGuildId(guildId);
        return linkedRole;
    }
}
