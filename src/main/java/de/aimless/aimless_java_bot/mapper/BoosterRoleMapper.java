package de.aimless.aimless_java_bot.mapper;

import de.aimless.aimless_java_bot.entity.BoosterRole;
import net.dv8tion.jda.api.entities.Role;

public class BoosterRoleMapper {

    public BoosterRole map(Role role, long guildId) {
        BoosterRole boosterRole = new BoosterRole();
        boosterRole.setId(role.getIdLong());
        boosterRole.setName(role.getName());
        boosterRole.setGuildId(guildId);
        return boosterRole;
    }
}
