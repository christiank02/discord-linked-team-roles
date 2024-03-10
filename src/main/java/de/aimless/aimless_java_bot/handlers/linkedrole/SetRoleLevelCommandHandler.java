package de.aimless.aimless_java_bot.handlers.linkedrole;

import de.aimless.aimless_java_bot.command.CommandName;
import de.aimless.aimless_java_bot.handlers.AbstractCommandHandler;
import de.aimless.aimless_java_bot.repository.LinkedRoleRepository;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class SetRoleLevelCommandHandler extends AbstractCommandHandler {

    private final LinkedRoleRepository linkedRoleRepository;

    public SetRoleLevelCommandHandler(LinkedRoleRepository linkedRoleRepository) {
        this.linkedRoleRepository = linkedRoleRepository;
    }

    public void handleCommand(SlashCommandInteractionEvent event) {
        if (event.isAcknowledged()) {
            return;
        }

        event.deferReply(true).queue();

        Role role = Objects.requireNonNull(event.getOption("role")).getAsRole();
        int level = Objects.requireNonNull(event.getOption("level")).getAsInt();

        InteractionHook hook = event.getHook();
        linkedRoleRepository.findById(role.getIdLong()).ifPresentOrElse(
                linkedRoleEntity -> {
                    linkedRoleEntity.setLevel(level);
                    linkedRoleRepository.save(linkedRoleEntity);

                    String replyMessage = String.format("Role level has been set to %d for role %s", level, role.getName());
                    hook.sendMessage(replyMessage).queue();
                },
                () -> {
                    String replyMessage = "Failed to set role level. Role does not exist";
                    hook.sendMessage(replyMessage).queue();
                }
        );
    }

    @Override
    public boolean canHandle(CommandName commandName, CommandName subCommandName) {
        return commandName == CommandName.LINKEDROLE && subCommandName == CommandName.SET;
    }

}
