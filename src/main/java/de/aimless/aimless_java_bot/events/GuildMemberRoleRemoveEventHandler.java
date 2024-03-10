package de.aimless.aimless_java_bot.events;

import de.aimless.aimless_java_bot.http.RoleConnectionWrapper;
import de.aimless.aimless_java_bot.entity.LinkedRole;
import de.aimless.aimless_java_bot.repository.LinkedRoleRepository;
import de.aimless.aimless_java_bot.utils.RoleUtils;
import jakarta.annotation.Nonnull;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class GuildMemberRoleRemoveEventHandler extends ListenerAdapter {

    private final LinkedRoleRepository linkedRoleRepository;
    private final OAuth2AuthorizedClientService authorizedClientService;

    @Autowired
    public GuildMemberRoleRemoveEventHandler(LinkedRoleRepository linkedRoleRepository, OAuth2AuthorizedClientService authorizedClientService) {
        this.linkedRoleRepository = linkedRoleRepository;
        this.authorizedClientService = authorizedClientService;
    }

    @Override
    public void onGuildMemberRoleRemove(@Nonnull GuildMemberRoleRemoveEvent event) {
        String applicationId = event.getJDA().getSelfUser().getApplicationId();
        RoleConnectionWrapper roleConnectionWrapper = new RoleConnectionWrapper();

        event.getRoles().forEach(role -> {
            if (role.getTags().isLinkedRole() || role.getTags().isBoost()) {
                return;
            }

            // Get all roles of the user
            List<Role> oldUserRoles = new ArrayList<>(event.getMember().getRoles());
            oldUserRoles.add(role);
            List<LinkedRole> linkedRoles = RoleUtils.getLinkedRoles(oldUserRoles, linkedRoleRepository);

            // If the highest role is removed, set it to the next highest
            long removedRoleId = role.getIdLong();
            if (linkedRoles.removeIf(linkedRole -> linkedRole.getId() == removedRoleId)) {
                String userName = event.getMember().getUser().getName();
                OAuth2AuthorizedClient oAuth2AuthorizedClient = authorizedClientService.loadAuthorizedClient("discord", userName);

                String accessToken = oAuth2AuthorizedClient.getAccessToken().getTokenValue();
                if (!linkedRoles.isEmpty()) {
                    LinkedRole nextHighestRole = linkedRoles.stream()
                            .max(Comparator.comparingInt(LinkedRole::getLevel))
                            .orElseThrow();
                    RoleUtils.updateRoleConnection(applicationId, accessToken, roleConnectionWrapper, nextHighestRole.getLevel());
                } else {
                    RoleUtils.updateRoleConnection(applicationId, accessToken, roleConnectionWrapper, 0);
                }
            }
        });
    }
}
