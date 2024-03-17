package de.aimless.aimless_java_bot.tasks;

import de.aimless.aimless_java_bot.entity.BoosterRole;
import de.aimless.aimless_java_bot.repository.BoosterRoleRepository;
import de.aimless.aimless_java_bot.utils.RainbowRoleColors;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class RoleColorChanger {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleColorChanger.class);
    private static final long MINUTES_IN_MILLIS = 300000;

    private final BoosterRoleRepository boosterRoleRepository;
    private final JDA jda;
    private final Random random = new Random();

    public RoleColorChanger(BoosterRoleRepository boosterRoleRepository, JDA jda) {
        this.boosterRoleRepository = boosterRoleRepository;
        this.jda = jda;
    }

    // Maybe change this to a more efficient way of changing the color of the roles
    @Scheduled(fixedRate = MINUTES_IN_MILLIS)
    public void changeColor() {
        List<BoosterRole> boosterRoleList = boosterRoleRepository.findAll();
        for (BoosterRole boosterRole : boosterRoleList) {
            Optional<Role> optionalRole = processBoosterRole(boosterRole);
            if (optionalRole.isPresent()) {
                Role role = optionalRole.get();
                try {
                    changeColor(role);
                } catch (InsufficientPermissionException | HierarchyException ex) {
                    LOGGER.warn("Error {} while changing color for role {} with id {}", ex.getClass(), role.getName(), role.getId());
                }
            }
        }
    }

    private Optional<Role> processBoosterRole(BoosterRole boosterRole) {
        Guild guild = jda.getGuildById(boosterRole.getGuildId());
        if (Objects.isNull(guild)) {
            return Optional.empty(); // Guild not found or not available anymore
        }

        Role role = guild.getRoleById(boosterRole.getId());
        if (Objects.isNull(role)) {
            return Optional.empty(); // Role not found or not available anymore
        }

        return Optional.of(role);
    }

    private void changeColor(Role role) throws InsufficientPermissionException, HierarchyException {
        RainbowRoleColors color = getRandomColor();
        try {
            role.getManager().setColor(color.getColor().getRGB()).timeout(30, TimeUnit.MINUTES).completeAfter(1, TimeUnit.MINUTES);
        } catch (Exception e) {
            LOGGER.warn("Error while changing color for role {} with id {}. Error: {}", role.getName(), role.getId(), e.getMessage());
        }
    }

    private RainbowRoleColors getRandomColor() {
        RainbowRoleColors[] colors = RainbowRoleColors.values();
        return colors[random.nextInt(colors.length)];
    }
}
