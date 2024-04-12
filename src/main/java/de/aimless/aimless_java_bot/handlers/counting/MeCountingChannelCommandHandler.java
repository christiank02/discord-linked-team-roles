package de.aimless.aimless_java_bot.handlers.counting;

import de.aimless.aimless_java_bot.command.CommandName;
import de.aimless.aimless_java_bot.entity.UserGuildEntity;
import de.aimless.aimless_java_bot.handlers.AbstractCommandHandler;
import de.aimless.aimless_java_bot.repository.UserGuildRepository;
import de.aimless.aimless_java_bot.utils.CountingAbility;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class MeCountingChannelCommandHandler extends AbstractCommandHandler {

    private final UserGuildRepository userGuildRepository;

    public MeCountingChannelCommandHandler(UserGuildRepository userGuildRepository) {
        this.userGuildRepository = userGuildRepository;
    }

    public void handleCommand(SlashCommandInteractionEvent event) {
        if (event.isAcknowledged()) {
            return;
        }

        event.deferReply(true).queue();

        Guild guild = event.getGuild();
        if (Objects.isNull(guild)) {
            event.getHook().sendMessage("Du musst diesen Befehl in einem Server verwenden.").queue();
            return;
        }

        long userId = event.getUser().getIdLong();
        long guildId = guild.getIdLong();
        Optional<UserGuildEntity> userGuildEntityOptional = userGuildRepository.findByUserEntityIdAndGuildEntityGuildId(userId, guildId);
        sendAbilityListEmbed(event, userGuildEntityOptional);
    }

    private void sendAbilityListEmbed(SlashCommandInteractionEvent event, Optional<UserGuildEntity> optionalUserGuildEntity) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.BLUE);
        embedBuilder.setTitle("Deine Fähigkeiten auf diesem Server");

        if (optionalUserGuildEntity.isEmpty()) {
            embedBuilder.setDescription(getNoAbilitiesDescription());
        } else {
            UserGuildEntity userGuildEntity = optionalUserGuildEntity.get();
            List<CountingAbility> countingGameAbilities = userGuildEntity.getCountingGameAbilities();
            if (countingGameAbilities.isEmpty()) {
                embedBuilder.setDescription(getNoAbilitiesDescription());
            } else {
                Map<String, Long> abilityCounts = getAbilityCounts(userGuildEntity);
                abilityCounts.forEach((abilityName, count) -> {
                    String description = getAbilityDescription(countingGameAbilities, abilityName);
                    embedBuilder.addField(abilityName + " - Anzahl: " + count, description, false);
                });
            }
        }

        event.getHook().sendMessageEmbeds(embedBuilder.build()).queue();
    }

    private static @NotNull String getNoAbilitiesDescription() {
        return "Du hast keine Fähigkeiten auf diesem Server.";
    }

    private Map<String, Long> getAbilityCounts(UserGuildEntity userGuildEntity) {
        return userGuildEntity.getCountingGameAbilities().stream()
                .map(CountingAbility::getDisplayName)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    private String getAbilityDescription(List<CountingAbility> countingGameAbilities, String abilityName) {
        return countingGameAbilities.stream()
                .filter(ability -> ability.getDisplayName().equals(abilityName))
                .findFirst()
                .map(CountingAbility::getDescription)
                .orElse("");
    }

    @Override
    public boolean canHandle(CommandName commandName, CommandName subCommandName) {
        return commandName == CommandName.COUNTING && subCommandName == CommandName.ME;
    }

}
