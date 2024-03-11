package de.aimless.aimless_java_bot.handlers.boosterrole;

import de.aimless.aimless_java_bot.command.CommandName;
import de.aimless.aimless_java_bot.handlers.AbstractCommandHandler;
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

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Booster Roles");
        embedBuilder.setColor(Color.CYAN); // You can change the color to your preference
        embedBuilder.setDescription("List of booster roles and their auto-assignable status:");

        StringBuilder tableContent = new StringBuilder();
        tableContent.append("```\n");
        tableContent.append(String.format("%-20s | Auto Assignable%n", "Role Name"));

        List<BoosterRole> boosterRoles = boosterRoleRepository.findAllByGuildId(guild.getIdLong());
        for (BoosterRole linkedRole : boosterRoles) {
            tableContent.append(String.format("%-20s | %s%n", linkedRole.getName(), linkedRole.isAutoAssignable() ? "Yes" : "No"));
        }

        tableContent.append("```");

        embedBuilder.addField("Roles", tableContent.toString(), false);

        event.getHook()
                .sendMessageEmbeds(embedBuilder.build())
                .setEphemeral(true)
                .queue();
    }

    @Override
    public boolean canHandle(CommandName commandName, CommandName subCommandName) {
        return commandName == CommandName.BOOSTERROLE && subCommandName == CommandName.LIST;
    }
}
