package de.aimless.linkedroles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@EnableScheduling
@SpringBootApplication
public class BotApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(BotApplication.class);


    private final DiscordBot bot;

    @Autowired
    public BotApplication(DiscordBot bot) {
        this.bot = bot;
    }

    public static void main(String[] args) {
        SpringApplication.run(BotApplication.class, args);
    }

    /**
     * Will create a {@link RestTemplate} and make it usable in the entire application through spring as a {@link Bean}.
     *
     * @param builder {@link RestTemplateBuilder} for creating the {@link RestTemplate}
     * @return created {@link RestTemplate}
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            try {
                bot.start();
            } catch (InterruptedException e) {
                LOGGER.error("Error starting the bot", e);
                Thread.currentThread().interrupt();
            }
        };
    }

}
