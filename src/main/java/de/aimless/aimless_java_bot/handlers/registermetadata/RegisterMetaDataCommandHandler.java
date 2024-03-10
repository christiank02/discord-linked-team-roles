package de.aimless.aimless_java_bot.handlers.registermetadata;

import de.aimless.aimless_java_bot.command.CommandName;
import de.aimless.aimless_java_bot.handlers.AbstractCommandHandler;
import net.dv8tion.jda.api.entities.RoleConnectionMetadata;
import net.dv8tion.jda.api.entities.RoleConnectionMetadata.MetadataType;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.requests.RestAction;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RegisterMetaDataCommandHandler extends AbstractCommandHandler {

    private static final String METADATA_ROLE_LEVEL_KEY = "rolelevel";
    private static final String METADATA_ROLE_LEVEL_NAME = "Role Level";
    private static final String METADATA_ROLE_LEVEL_DESCRIPTION = "Part of the team";

    public void handleCommand(SlashCommandInteractionEvent event) {
        if(event.isAcknowledged()) {
            return;
        }

        event.deferReply(true).queue();

        RoleConnectionMetadata metadata = new RoleConnectionMetadata(MetadataType.INTEGER_EQUALS, METADATA_ROLE_LEVEL_NAME, METADATA_ROLE_LEVEL_KEY, METADATA_ROLE_LEVEL_DESCRIPTION);
        List<RoleConnectionMetadata> metadataList = List.of(metadata);

        InteractionHook hook = event.getHook();
        RestAction<List<RoleConnectionMetadata>> restAction = event.getJDA().updateRoleConnectionMetadata(metadataList);
        restAction.queue(
                success -> {
                    String replyContent = "Registered metadata for the connection role";
                    hook.sendMessage(replyContent).queue();
                },
                failure -> {
                    String replyContent = String.format("Failed to register metadata for the connection role, error: %s", failure.getMessage());
                    hook.sendMessage(replyContent).queue();
                }
        );
    }

    @Override
    public boolean canHandle(CommandName commandName, CommandName subCommandName) {
        return commandName == CommandName.REGISTERMETADATACOMMAND;
    }
}
