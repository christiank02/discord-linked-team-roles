package de.aimless.aimless_java_bot.utils;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;

// TODO: Implement LoggingUtils
// This class should provide static methods to log messages to the dms of the bot owner
public class LoggingUtils {

    public static void logToOwner(JDA jda, String message) {
        long ownerId = jda.retrieveApplicationInfo().complete().getOwner().getIdLong();
        User owner = jda.getUserById(ownerId);
        if (owner != null) {
            owner.openPrivateChannel().queue(privateChannel -> sendPrivateMessage(privateChannel, message));
        }
    }

    private static void sendPrivateMessage(PrivateChannel channel, String message) {
        channel.sendMessage(message).queue();
    }
}
