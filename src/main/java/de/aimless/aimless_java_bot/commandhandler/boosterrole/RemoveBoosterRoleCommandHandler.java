package de.aimless.aimless_java_bot.commandhandler.boosterrole;

import de.aimless.aimless_java_bot.command.CommandName;
import de.aimless.aimless_java_bot.commandhandler.AbstractCommandHandler;
import de.aimless.aimless_java_bot.repository.BoosterRoleRepository;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class RemoveBoosterRoleCommandHandler extends AbstractCommandHandler {

    private final BoosterRoleRepository boosterRolesRepository;

    public RemoveBoosterRoleCommandHandler(BoosterRoleRepository boosterRolesRepository) {
        this.boosterRolesRepository = boosterRolesRepository;
    }

    public void handleCommand(SlashCommandInteractionEvent event) {
        if(event.isAcknowledged()) {
            return;
        }

        event.deferReply(true).queue();

        Role role = Objects.requireNonNull(event.getOption("role")).getAsRole();

        // check if role exists
        if (!boosterRolesRepository.existsById(role.getIdLong())) {
            event.getHook().sendMessage("Failed to remove role. Role does not exist").queue();
            return;
        }

        boosterRolesRepository.deleteById(role.getIdLong());

        String replyContent = String.format("Removed role %s", role.getName());
        event.getHook().sendMessage(replyContent).queue();
    }

    @Override
    public boolean canHandle(CommandName commandName, CommandName subCommandName) {
        return commandName == CommandName.BOOSTERROLE && subCommandName == CommandName.REMOVE;
    }
}
