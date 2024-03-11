package de.aimless.aimless_java_bot.handlers.boosterrole;

import de.aimless.aimless_java_bot.command.CommandName;
import de.aimless.aimless_java_bot.handlers.AbstractCommandHandler;
import de.aimless.aimless_java_bot.repository.BoosterRoleRepository;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class SetBoosterRoleCommandHandler extends AbstractCommandHandler {

    private final BoosterRoleRepository boosterRoleRepository;

    public SetBoosterRoleCommandHandler(BoosterRoleRepository boosterRoleRepository) {
        this.boosterRoleRepository = boosterRoleRepository;
    }

    public void handleCommand(SlashCommandInteractionEvent event) {
        if (event.isAcknowledged()) {
            return;
        }

        event.deferReply(true).queue();

        Role role = Objects.requireNonNull(event.getOption("role")).getAsRole();
        boolean autoAssignable = Objects.requireNonNull(event.getOption("auto_assignable")).getAsBoolean();

        InteractionHook hook = event.getHook();
        boosterRoleRepository.findById(role.getIdLong()).ifPresentOrElse(
                boosterRoleEntity -> {
                    boosterRoleEntity.setAutoAssignable(autoAssignable);
                    boosterRoleRepository.save(boosterRoleEntity);

                    String replyMessage = String.format("🚀 The auto-assignable status for role '%s' has been set to '%b!'", role.getName(), autoAssignable);
                    hook.sendMessage(replyMessage).queue();
                },
                () -> {
                    String replyMessage = "❌ Failed to update the role. The role does not exist.";
                    hook.sendMessage(replyMessage).queue();
                }
        );
    }

    @Override
    public boolean canHandle(CommandName commandName, CommandName subCommandName) {
        return commandName == CommandName.BOOSTERROLE && subCommandName == CommandName.SET;
    }

}
