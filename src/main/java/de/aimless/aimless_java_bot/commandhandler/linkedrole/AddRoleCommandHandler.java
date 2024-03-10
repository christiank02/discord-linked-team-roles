package de.aimless.aimless_java_bot.commandhandler.linkedrole;

import de.aimless.aimless_java_bot.command.CommandName;
import de.aimless.aimless_java_bot.commandhandler.AbstractCommandHandler;
import de.aimless.aimless_java_bot.entity.LinkedRole;
import de.aimless.aimless_java_bot.mapper.LinkedRoleMapper;
import de.aimless.aimless_java_bot.repository.LinkedRoleRepository;
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
    private final long defaultGuildId;

    public AddRoleCommandHandler(@Value("${AIMLESS_BOT_GUILD_ID}") long defaultGuildId, LinkedRoleRepository linkedRoleRepository) {
        this.linkedRoleRepository = linkedRoleRepository;
        this.linkedRoleMapper = new LinkedRoleMapper();
        this.defaultGuildId = defaultGuildId;
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
        Guild guild = event.getGuild();
        long guildId = defaultGuildId;
        if(Objects.nonNull(guild)) { // if the command was used inside a guild else use default guild id
            guildId = guild.getIdLong();
        }
        LinkedRole linkedRoleEntity = linkedRoleMapper.map(role, level, guildId);
        linkedRoleRepository.save(linkedRoleEntity);
        String replyContent = String.format("Added role %s with permission level %d", role.getName(), level);
        event.getHook().sendMessage(replyContent).setEphemeral(true).queue();
    }

    @Override
    public boolean canHandle(CommandName commandName, CommandName subCommandName) {
        return commandName == CommandName.LINKEDROLE && subCommandName == CommandName.ADD;
    }
}
