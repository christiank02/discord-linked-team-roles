package de.aimless.aimless_java_bot.events;

import de.aimless.aimless_java_bot.repository.GuildRepository;
import lombok.NonNull;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class GuildMemberJoinEventHandler extends ListenerAdapter {

    private static final String ROLES_CHANNEL_RAW = "<id:customize>";
    private static final String KIRBY_SLEEP_EMOJI = "<a:kirbysleep:1152718605725745182>";
    private static final String PINK_KITTY_EMOJI = "<a:pinkkitty:1018911085773987850>";
    private static final String HEART_GHOST_EMOJI = "<a:heart_ghost:1036224540335935488>";
    private static final String ABOUT_YOU_CHANNEL_RAW = "<#1031279058010525887>";

    private final GuildRepository guildRepository;

    public GuildMemberJoinEventHandler(GuildRepository guildRepository) {
        this.guildRepository = guildRepository;
    }

    // Handle guild member join event
    @Override
    public void onGuildMemberJoin(@NonNull GuildMemberJoinEvent event) {
        Guild eventGuild = event.getGuild();
        guildRepository.findById(eventGuild.getIdLong())
                .filter(guildEntity -> Objects.nonNull(guildEntity.getJoinMessageChannelId()))
                .map(guildEntity -> eventGuild.getTextChannelById(guildEntity.getJoinMessageChannelId()))
                .ifPresent(joinChannel -> sendWelcomeMessage(event, joinChannel));
    }

    // Send welcome message to the specified channel
    private void sendWelcomeMessage(@NotNull GuildMemberJoinEvent event, @NotNull TextChannel joinChannel) {
        Guild guild = event.getGuild();
        Optional.ofNullable(guild.getRulesChannel())
                .ifPresent(rulesChannel -> {
                    EmbedBuilder welcomeMessageEmbed = createWelcomeMessageEmbed(guild, rulesChannel);
                    joinChannel.sendMessage(String.format("willkommen %s!", event.getMember().getAsMention())).complete();
                    joinChannel.sendMessageEmbeds(welcomeMessageEmbed.build()).complete();
                });
    }

    // Create the welcome message embed
    private EmbedBuilder createWelcomeMessageEmbed(@NotNull Guild guild, @NotNull TextChannel rulesChannel) {
        EmbedBuilder welcomeMessageEmbed = new EmbedBuilder();
        welcomeMessageEmbed.setThumbnail("https://i.pinimg.com/originals/21/69/21/21692199864e215d21a75775da2f8bd7.gif");
        welcomeMessageEmbed.setAuthor(guild.getName(), null, "https://images-ext-2.discordapp.net/external/tjO7yvXxJs8tAxKMWJELY3IyHLkqk42swbpPKh44teA/https/i.pinimg.com/564x/e4/f2/34/e4f2341cc887c4dce7f9c8a418598814.jpg");
        welcomeMessageEmbed.setTitle("ディスコード " + HEART_GHOST_EMOJI);

        String description = String.format("・୨୧・- -・♡・- -・୨୧・- -・♡・- -・୨୧・ %n឵឵ ឵឵ ឵឵ ឵឵ ឵឵**Wir hoffen du hast dir unsere %n%s angeschaut.** %s", rulesChannel.getAsMention(), PINK_KITTY_EMOJI);
        welcomeMessageEmbed.setDescription(description);
        welcomeMessageEmbed.getFields().addAll(getFields());
        welcomeMessageEmbed.setImage("https://media.tenor.com/M_G9hoIJerUAAAAC/welcome.gif");
        welcomeMessageEmbed.setFooter(".gg/yogiri", "https://images-ext-2.discordapp.net/external/tjO7yvXxJs8tAxKMWJELY3IyHLkqk42swbpPKh44teA/https/i.pinimg.com/564x/e4/f2/34/e4f2341cc887c4dce7f9c8a418598814.jpg");
        welcomeMessageEmbed.setTimestamp(Instant.now());
        welcomeMessageEmbed.setColor(Color.decode("#fbb1a4"));
        return welcomeMessageEmbed;
    }

    // Get the list of message embed fields
    private List<MessageEmbed.Field> getFields() {
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
