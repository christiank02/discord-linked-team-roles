package de.aimless.aimless_java_bot.handlers.boosterrole;

import de.aimless.aimless_java_bot.command.CommandName;
import de.aimless.aimless_java_bot.handlers.AbstractCommandHandler;
import de.aimless.aimless_java_bot.entity.BoosterRole;
import de.aimless.aimless_java_bot.mapper.BoosterRoleMapper;
import de.aimless.aimless_java_bot.repository.BoosterRoleRepository;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class AddBoosterRoleCommandHandler extends AbstractCommandHandler {

    private final BoosterRoleRepository boosterRoleRepository;
    private final BoosterRoleMapper boosterRoleMapper;

    public AddBoosterRoleCommandHandler(BoosterRoleRepository boosterRoleRepository) {
        this.boosterRoleRepository = boosterRoleRepository;
        this.boosterRoleMapper = new BoosterRoleMapper();
    }

    @Override
    public void handleCommand(SlashCommandInteractionEvent event) {
        if (event.isAcknowledged()) {
            return;
        }

        event.deferReply(true).queue();
        Role role = Objects.requireNonNull(event.getOption("role")).getAsRole();
        boolean autoAssignable = Objects.requireNonNull(event.getOption("auto_assignable")).getAsBoolean();

        Guild guild = event.getGuild();
        if (Objects.isNull(guild)) { // if the command was used inside a guild else use default guild id
            event.getHook().sendMessage("Failed to add role. Command must be used inside a guild").queue();
            return;
        }

        // Check if role already exists with the same id
        if (boosterRoleRepository.existsById(role.getIdLong())) {
            event.getHook().sendMessage("Failed to add role. Role already exists").queue();
            return;
        }

        BoosterRole boosterRole = boosterRoleMapper.map(role, guild.getIdLong(), autoAssignable);
        boosterRoleRepository.save(boosterRole);

        String autoAssignableText = autoAssignable ? "auto-assignable" : "not auto-assignable";
        String replyContent = String.format("Added role '%s' as a booster role and set it as '%s'", role.getName(), autoAssignableText);
        event.getHook().sendMessage(replyContent).setEphemeral(true).queue();
    }

    @Override
    public boolean canHandle(CommandName commandName, CommandName subCommandName) {
        return commandName == CommandName.BOOSTERROLE && subCommandName == CommandName.ADD;
    }
}
