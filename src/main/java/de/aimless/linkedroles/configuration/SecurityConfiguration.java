package de.aimless.linkedroles.configuration;

import de.aimless.linkedroles.security.RestOAuth2AccessTokenResponseClient;
import de.aimless.linkedroles.security.RestOAuth2SuccessHandler;
import de.aimless.linkedroles.security.RestOAuth2UserService;
import de.aimless.linkedroles.repository.LinkedRoleRepository;
import net.dv8tion.jda.api.JDA;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final OAuth2AuthorizedClientService authorizedClientService;
    private final JDA jda;
    private final LinkedRoleRepository linkedRoleRepository;
    private final String userAgent;
    private final long guildId;
    private final String botName;

    public SecurityConfiguration(@Value("AIMLESS_BOT_NAME") String botName, @Value("${AIMLESS_BOT_OAUTH2_USER_AGENT}") String userAgent, @Value("${AIMLESS_BOT_GUILD_ID}") long guildId,
                                 OAuth2AuthorizedClientService authorizedClientService, JDA jda, LinkedRoleRepository linkedRoleRepository) {
        this.botName = botName;
        this.userAgent = userAgent;
        this.authorizedClientService = authorizedClientService;
        this.jda = jda;
        this.linkedRoleRepository = linkedRoleRepository;
        this.guildId = guildId;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/login/oauth2/code/discord", "/oauth2/authorization/discord", "invite", "error").permitAll()
                        .anyRequest().permitAll()
                )
                .oauth2Login(oauth2 ->
                        oauth2.tokenEndpoint(token ->
                                        token.accessTokenResponseClient(new RestOAuth2AccessTokenResponseClient(restOperations(), userAgent))
                                )
                                .userInfoEndpoint(userInfo ->
                                        userInfo.userService(new RestOAuth2UserService(restOperations(), userAgent))
                                )
                                .loginPage("/oauth2/authorization/discord")
                                .successHandler(new RestOAuth2SuccessHandler(botName, guildId, jda, linkedRoleRepository, authorizedClientService))
                )
                .csrf(csrf ->
                        csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()));
        return http.build();
    }

    @Bean
    public RestOperations restOperations() {
        return new RestTemplate();
    }
}
