package de.aimless.aimless_java_bot.events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.List;

@Component
public class MessageReceivedEventHandler extends ListenerAdapter {

    //TODO:
    // 1. Make code great again
    // 2. Watch out for the channel names and emojis
    // 3. Make it a Join Message not a MessageReceivedEvent

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }

        // create embed
        EmbedBuilder welcomeMessageEmbed = new EmbedBuilder();

        welcomeMessageEmbed.setThumbnail("https://i.pinimg.com/originals/21/69/21/21692199864e215d21a75775da2f8bd7.gif");
        welcomeMessageEmbed.setAuthor(event.getGuild().getName(), null, "https://images-ext-2.discordapp.net/external/tjO7yvXxJs8tAxKMWJELY3IyHLkqk42swbpPKh44teA/https/i.pinimg.com/564x/e4/f2/34/e4f2341cc887c4dce7f9c8a418598814.jpg");
        welcomeMessageEmbed.setTitle("ディスコード :heart_ghost:");

        String description = String.format("・୨୧・- -・♡・- -・୨୧・- -・♡・- -・୨୧・ %n឵឵ ឵឵ ឵឵ ឵឵ ឵឵**Wir hoffen du hast dir unsere %n%s angeschaut.** :pinkkitty:", event.getGuild().getTextChannelsByName("memes", true).get(0).getAsMention());
        welcomeMessageEmbed.setDescription(description);

        welcomeMessageEmbed.getFields().addAll(description(event.getGuild()));
        welcomeMessageEmbed.setImage("https://media.tenor.com/M_G9hoIJerUAAAAC/welcome.gif");

        welcomeMessageEmbed.setFooter(".gg/yogiri", "https://images-ext-2.discordapp.net/external/tjO7yvXxJs8tAxKMWJELY3IyHLkqk42swbpPKh44teA/https/i.pinimg.com/564x/e4/f2/34/e4f2341cc887c4dce7f9c8a418598814.jpg");

        welcomeMessageEmbed.setColor(Color.decode("#fbb1a4"));
        // send the embed to the channel
        event.getChannel().sendMessageEmbeds(welcomeMessageEmbed.build()).queue();
    }

    private List<MessageEmbed.Field> description(Guild guild) {

        TextChannel rolesChannel = guild.getTextChannelsByName("Allgemein", true).get(0);
        TextChannel aboutYouChannel = guild.getTextChannelsByName("bot-updates", true).get(0);

        String headerFooter = "・୨୧・- -・♡・- -・୨୧・- -・♡・- -・୨୧・";

        String roles = String.format("- Vergiss nicht dir deine Rollen zu holen in %s! %n", rolesChannel.getAsMention());

        String aboutYou = String.format("- Erzähl uns gerne was über dich in %s!", aboutYouChannel.getAsMention());

        String go = " ឵឵ ឵឵ ឵឵ ឵**Viel Spaß auf unserem Server! :kirbysleep:**";

        return List.of(
                new MessageEmbed.Field("", roles + aboutYou, true),
                new MessageEmbed.Field("", go, false),
                new MessageEmbed.Field("", headerFooter, false)
        );
    }
}
