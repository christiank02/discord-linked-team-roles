package de.aimless.linkedroles.configuration;

import de.aimless.linkedroles.listener.ReadyListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JDAConfiguration {

    @Value("${AIMLESS_BOT_TOKEN}")
    private String token;

    private static final String ACTIVITY = "I wish you a great day!";

    @Bean
    public JDA jda(ReadyListener readyListener) throws InterruptedException {

        JDA jda = JDABuilder.createDefault(token)
                .setStatus(OnlineStatus.IDLE)
                .setActivity(Activity.playing(ACTIVITY))
                .addEventListeners(readyListener)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .build();

        jda.awaitReady();

        return jda;
    }
}
