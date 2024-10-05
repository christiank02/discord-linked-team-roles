package de.aimless.linkedroles.utils;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

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
}
