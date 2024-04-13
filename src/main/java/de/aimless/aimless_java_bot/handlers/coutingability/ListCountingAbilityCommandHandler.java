package de.aimless.aimless_java_bot.handlers.coutingability;

import de.aimless.aimless_java_bot.command.CommandName;
import de.aimless.aimless_java_bot.handlers.AbstractCommandHandler;
import de.aimless.aimless_java_bot.utils.CountingAbility;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.Objects;

@Component
public class ListCountingAbilityCommandHandler extends AbstractCommandHandler {

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

        sendAbilityListEmbed(event);
    }

    private void sendAbilityListEmbed(SlashCommandInteractionEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.BLUE);
        embedBuilder.setTitle("Liste der Fähigkeiten");

        for (CountingAbility ability : CountingAbility.values()) {
            embedBuilder.addField(ability.getDisplayName(), ability.getDescription(), false);
        }

        event.getHook().sendMessageEmbeds(embedBuilder.build()).queue();
    }

    @Override
    public boolean canHandle(CommandName commandName, CommandName subCommandName) {
        return commandName == CommandName.ABILITY && subCommandName == CommandName.LIST;
    }

}
