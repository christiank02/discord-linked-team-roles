package de.aimless.linkedroles.handlers.linkedrole;

import de.aimless.linkedroles.command.CommandName;
import de.aimless.linkedroles.handlers.AbstractCommandHandler;
import de.aimless.linkedroles.repository.LinkedRoleRepository;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class RemoveRoleCommandHandler extends AbstractCommandHandler {

    private final LinkedRoleRepository linkedRoleRepository;

    public RemoveRoleCommandHandler(LinkedRoleRepository linkedRoleRepository) {
        this.linkedRoleRepository = linkedRoleRepository;
    }

    public void handleCommand(SlashCommandInteractionEvent event) {
        if(event.isAcknowledged()) {
            return;
        }

        event.deferReply(true).queue();

        Role role = Objects.requireNonNull(event.getOption("role")).getAsRole();

        // check if role exists
        if (!linkedRoleRepository.existsById(role.getIdLong())) {
            event.getHook().sendMessage("Failed to remove role. Role does not exist").queue();
            return;
        }

        linkedRoleRepository.deleteById(role.getIdLong());

        String replyContent = String.format("Removed role %s", role.getName());
        event.getHook().sendMessage(replyContent).queue();
    }

    @Override
    public boolean canHandle(CommandName commandName, CommandName subCommandName) {
        return commandName == CommandName.LINKEDROLE && subCommandName == CommandName.REMOVE;
    }
}
