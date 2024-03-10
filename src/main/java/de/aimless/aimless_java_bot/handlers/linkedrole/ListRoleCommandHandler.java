package de.aimless.aimless_java_bot.handlers.linkedrole;

import de.aimless.aimless_java_bot.command.CommandName;
import de.aimless.aimless_java_bot.handlers.AbstractCommandHandler;
import de.aimless.aimless_java_bot.entity.LinkedRole;
import de.aimless.aimless_java_bot.repository.LinkedRoleRepository;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.List;
import java.util.Objects;

@Component
public class ListRoleCommandHandler extends AbstractCommandHandler {

    private final LinkedRoleRepository linkedRoleRepository;
    private final long defaultGuildId;

    public ListRoleCommandHandler(@Value("${AIMLESS_BOT_GUILD_ID}") long defaultGuildId, LinkedRoleRepository linkedRoleRepository) {
        this.linkedRoleRepository = linkedRoleRepository;
        this.defaultGuildId = defaultGuildId;
    }

    public void handleCommand(SlashCommandInteractionEvent event) {
        if (event.isAcknowledged()) {
            return;
        }

        event.deferReply(true).queue();

        Guild guild = event.getGuild();
        long guildId = defaultGuildId;
        if (Objects.nonNull(guild)) { // if the command was used inside a guild else use default guild id
            guildId = guild.getIdLong();
        }
        List<LinkedRole> linkedRoles = linkedRoleRepository.findAllByGuildId(guildId);

        StringBuilder replyContent = new StringBuilder("```");
        replyContent.append("Role Name     Level\n");
        linkedRoles.forEach(linkedRole ->
                replyContent.append(String.format("%-12s  %s%n", linkedRole.getName(), linkedRole.getLevel())));
        replyContent.append("```");

        event.getHook().sendMessageEmbeds(new EmbedBuilder()
                        .setTitle("Linked roles and their role levels")
                        .setDescription(replyContent.toString())
                        .setColor(Color.CYAN)
                        .build())
                .setEphemeral(true)
                .queue();
    }

    @Override
    public boolean canHandle(CommandName commandName, CommandName subCommandName) {
        return commandName == CommandName.LINKEDROLE && subCommandName == CommandName.LIST;
    }
}
