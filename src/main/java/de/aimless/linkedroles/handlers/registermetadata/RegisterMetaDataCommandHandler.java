package de.aimless.linkedroles.handlers.registermetadata;

import de.aimless.linkedroles.command.CommandName;
import de.aimless.linkedroles.handlers.AbstractCommandHandler;
import de.aimless.linkedroles.utils.CommandUtils;
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
        System.out.println("RegisterMetaDataCommandHandler.handleCommand");
        CommandUtils.acknowledgeAndDeferReply(event);

        RoleConnectionMetadata metadata = new RoleConnectionMetadata(MetadataType.INTEGER_EQUALS, METADATA_ROLE_LEVEL_NAME, METADATA_ROLE_LEVEL_KEY, METADATA_ROLE_LEVEL_DESCRIPTION);
        List<RoleConnectionMetadata> metadataList = List.of(metadata);

        InteractionHook hook = event.getHook();
        RestAction<List<RoleConnectionMetadata>> restAction = event.getJDA().updateRoleConnectionMetadata(metadataList);
        System.out.println(metadataList);
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
