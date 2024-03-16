package de.aimless.aimless_java_bot.events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Component
public class MessageReceivedEventHandler extends ListenerAdapter {

    //TODO:
    // 1. Make code great again
    // 2. Make it a Join Message not a MessageReceivedEvent

    private static final String ROLES_CHANNEL_RAW = "<id:customize>";
    private static final String KIRBY_SLEEP_EMOJI = "<a:kirbysleep:1152718605725745182>";
    private static final String PINK_KITTY_EMOJI = "<a:pinkkitty:1018911085773987850>";
    private static final String HEART_GHOST_EMOJI = "<a:heart_ghost:1036224540335935488>";
    private static final String ABOUT_YOU_CHANNEL_RAW = "<#1031279058010525887>";

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().getIdLong() != 244209930268049409L || !event.getMessage().getContentRaw().equals("!joinTest")) {
            return;
        }

        // create embed
        EmbedBuilder welcomeMessageEmbed = new EmbedBuilder();

        welcomeMessageEmbed.setThumbnail("https://i.pinimg.com/originals/21/69/21/21692199864e215d21a75775da2f8bd7.gif");
        welcomeMessageEmbed.setAuthor(event.getGuild().getName(), null, "https://images-ext-2.discordapp.net/external/tjO7yvXxJs8tAxKMWJELY3IyHLkqk42swbpPKh44teA/https/i.pinimg.com/564x/e4/f2/34/e4f2341cc887c4dce7f9c8a418598814.jpg");
        welcomeMessageEmbed.setTitle("ディスコード " + HEART_GHOST_EMOJI);

        TextChannel rulesChannel = event.getGuild().getRulesChannel();
        if(Objects.isNull(rulesChannel)) {
            return;
        }

        String description = String.format("・୨୧・- -・♡・- -・୨୧・- -・♡・- -・୨୧・ %n឵឵ ឵឵ ឵឵ ឵឵ ឵឵**Wir hoffen du hast dir unsere %n%s angeschaut.** %s", rulesChannel.getAsMention(), PINK_KITTY_EMOJI);
        welcomeMessageEmbed.setDescription(description);

        welcomeMessageEmbed.getFields().addAll(description());
        welcomeMessageEmbed.setImage("https://media.tenor.com/M_G9hoIJerUAAAAC/welcome.gif");

        welcomeMessageEmbed.setFooter(".gg/yogiri", "https://images-ext-2.discordapp.net/external/tjO7yvXxJs8tAxKMWJELY3IyHLkqk42swbpPKh44teA/https/i.pinimg.com/564x/e4/f2/34/e4f2341cc887c4dce7f9c8a418598814.jpg");
        welcomeMessageEmbed.setTimestamp(Instant.now());

        welcomeMessageEmbed.setColor(Color.decode("#fbb1a4"));
        // send the embed to the channel
        event.getChannel().sendMessageEmbeds(welcomeMessageEmbed.build()).queue();
    }

    private List<MessageEmbed.Field> description() {
        String headerFooter = "・୨୧・- -・♡・- -・୨୧・- -・♡・- -・୨୧・";

        String roles = String.format("- Vergiss nicht dir deine Rollen zu holen in %s! %n", ROLES_CHANNEL_RAW);
        String aboutYou = String.format("- Erzähl uns gerne was über dich in %s!", ABOUT_YOU_CHANNEL_RAW);

        String go = " ឵឵ ឵឵ ឵឵ ឵**Viel Spaß auf unserem Server~ " + KIRBY_SLEEP_EMOJI + "**";

        return List.of(
                new MessageEmbed.Field("", roles + aboutYou, true),
                new MessageEmbed.Field("", go, false),
                new MessageEmbed.Field("", headerFooter, false)
        );
    }
}
