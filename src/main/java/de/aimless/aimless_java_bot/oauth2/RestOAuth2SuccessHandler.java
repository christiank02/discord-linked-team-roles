package de.aimless.aimless_java_bot.oauth2;

import de.aimless.aimless_java_bot.http.RoleConnectionWrapper;
import de.aimless.aimless_java_bot.entity.LinkedRole;
import de.aimless.aimless_java_bot.repository.LinkedRoleRepository;
import de.aimless.aimless_java_bot.utils.RoleUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class RestOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JDA jda;
    private final LinkedRoleRepository linkedRoleRepository;
    private final OAuth2AuthorizedClientService authorizedClientService;
    private final long guildId;

    public RestOAuth2SuccessHandler(@Value("${AIMLESS_BOT_GUILD_ID}") long guildId, JDA jda, LinkedRoleRepository linkedRoleRepository, OAuth2AuthorizedClientService authorizedClientService) {
        this.jda = jda;
        this.linkedRoleRepository = linkedRoleRepository;
        this.authorizedClientService = authorizedClientService;
        this.guildId = guildId;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        DefaultOAuth2User user = (DefaultOAuth2User) token.getPrincipal();

        OAuth2AuthorizedClient oAuth2AuthorizedClient = authorizedClientService.loadAuthorizedClient(token.getAuthorizedClientRegistrationId(), token.getName());
        String accessToken = oAuth2AuthorizedClient.getAccessToken().getTokenValue();

        Map<String, Object> userAttributes = (Map<String, Object>) user.getAttributes().get("user");

        String userId = (String) userAttributes.get("id");

        Guild guild = jda.getGuildById(guildId);
        if (Objects.isNull(guild)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Something went wrong. Please try again.");
            return;
        }

        Member member = guild.findMembers(predicateMember ->
                predicateMember.getId().equals(userId)).get().get(0);
        if (Objects.isNull(member)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Something went wrong. Please try again.");
            return;
        }

        List<LinkedRole> linkedRoles = RoleUtils.getLinkedRoles(member.getRoles(), linkedRoleRepository);
        if (linkedRoles.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "There is no Role associated. Please contact an admin.");
            return;
        }

        int highestRoleLevel = RoleUtils.getLinkedRoles(member.getRoles(), linkedRoleRepository).get(0).getLevel();
        String applicationId = jda.getSelfUser().getApplicationId();
        RoleConnectionWrapper roleConnectionWrapper = new RoleConnectionWrapper();
        RoleUtils.updateRoleConnection(applicationId, accessToken, roleConnectionWrapper, highestRoleLevel);

        response.getWriter().write("You are all set. You can go back to Discord now.");
    }
}
