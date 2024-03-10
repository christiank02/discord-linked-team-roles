package de.aimless.aimless_java_bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class AimlessJavaBotApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(AimlessJavaBotApplication.class);


    private final DiscordBot bot;

    @Autowired
    public AimlessJavaBotApplication(DiscordBot bot) {
        this.bot = bot;
    }

    public static void main(String[] args) {
        SpringApplication.run(AimlessJavaBotApplication.class, args);
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
