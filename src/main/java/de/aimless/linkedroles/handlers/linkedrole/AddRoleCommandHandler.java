package de.aimless.linkedroles.handlers.linkedrole;

import de.aimless.linkedroles.command.CommandName;
import de.aimless.linkedroles.handlers.AbstractCommandHandler;
import de.aimless.linkedroles.entity.LinkedRole;
import de.aimless.linkedroles.mapper.LinkedRoleMapper;
import de.aimless.linkedroles.repository.LinkedRoleRepository;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class AddRoleCommandHandler extends AbstractCommandHandler {

    private final LinkedRoleRepository linkedRoleRepository;
    private final LinkedRoleMapper linkedRoleMapper;

    public AddRoleCommandHandler(LinkedRoleRepository linkedRoleRepository) {
        this.linkedRoleRepository = linkedRoleRepository;
        this.linkedRoleMapper = new LinkedRoleMapper();
    }

    public void handleCommand(SlashCommandInteractionEvent event) {
        if(event.isAcknowledged()) {
            return;
        }

        event.deferReply(true).queue();

        Role role = Objects.requireNonNull(event.getOption("role")).getAsRole();
        int level = Objects.requireNonNull(event.getOption("level")).getAsInt();

        // check if role already exists with same id
        if (linkedRoleRepository.existsById(role.getIdLong())) {
            event.getHook().sendMessage("Failed to add role. Role already exists").queue();
            return;
        }
        LinkedRole linkedRoleEntity = linkedRoleMapper.map(role, level);
        linkedRoleRepository.save(linkedRoleEntity);
        String replyContent = String.format("Added role %s with permission level %d", role.getName(), level);
        event.getHook().sendMessage(replyContent).setEphemeral(true).queue();
    }

    @Override
    public boolean canHandle(CommandName commandName, CommandName subCommandName) {
        return commandName == CommandName.LINKEDROLE && subCommandName == CommandName.ADD;
    }
}
