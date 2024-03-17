package de.aimless.aimless_java_bot.utils;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.Objects;

public class CommandUtils {

    private CommandUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Acknowledge and defer the reply if it is not already acknowledged
     * @param event The event
     */
    public static void acknowledgeAndDeferReply(SlashCommandInteractionEvent event) {
        if (event.isAcknowledged()) {
            return;
        }

        event.deferReply(true).queue();
    }

    /**
     * Get the guild from the event or throw an exception if the command was used outside a guild
     * @param event The event
     * @return The guild
     * @throws IllegalStateException If the command was used outside a guild
     */
    public static Guild getGuildOrThrow(SlashCommandInteractionEvent event) throws IllegalStateException {
        Guild guild = event.getGuild();
        if (Objects.isNull(guild)) {
            throw new IllegalStateException("Command must be used inside a guild");
        }
        return guild;
    }
}
