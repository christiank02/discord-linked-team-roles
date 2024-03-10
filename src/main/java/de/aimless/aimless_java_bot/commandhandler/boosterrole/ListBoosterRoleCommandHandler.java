package de.aimless.aimless_java_bot.commandhandler.boosterrole;

import de.aimless.aimless_java_bot.command.CommandName;
import de.aimless.aimless_java_bot.commandhandler.AbstractCommandHandler;
import de.aimless.aimless_java_bot.entity.BoosterRole;
import de.aimless.aimless_java_bot.repository.BoosterRoleRepository;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.List;
import java.util.Objects;

@Component
public class ListBoosterRoleCommandHandler extends AbstractCommandHandler {

    private final BoosterRoleRepository boosterRoleRepository;

    public ListBoosterRoleCommandHandler(BoosterRoleRepository boosterRoleRepository) {
        this.boosterRoleRepository = boosterRoleRepository;
    }

    @Override
    public void handleCommand(SlashCommandInteractionEvent event) {
        if (event.isAcknowledged()) {
            return;
        }

        event.deferReply(true).queue();

        Guild guild = event.getGuild();
        if (Objects.isNull(guild)) { // if the command was used inside a guild else use default guild id
            event.getHook().sendMessage("Failed to list roles. Command must be used inside a guild").queue();
            return;
        }

        List<BoosterRole> boosterRoles = boosterRoleRepository.findAllByGuildId(guild.getIdLong());

        StringBuilder replyContent = new StringBuilder("```");
        replyContent.append("Role Name \n");
        boosterRoles.forEach(linkedRole ->
                replyContent.append(linkedRole.getName()).append("\n"));
        replyContent.append("```");

        event.getHook().sendMessageEmbeds(new EmbedBuilder()
                        .setTitle("Booster roles")
                        .setDescription(replyContent.toString())
                        .setColor(Color.CYAN)
                        .build())
                .setEphemeral(true)
                .queue();
    }

    @Override
    public boolean canHandle(CommandName commandName, CommandName subCommandName) {
        return commandName == CommandName.BOOSTERROLE && subCommandName == CommandName.LIST;
    }
}
