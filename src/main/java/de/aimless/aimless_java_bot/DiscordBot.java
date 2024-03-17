package de.aimless.aimless_java_bot;

import de.aimless.aimless_java_bot.command.SlashCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class DiscordBot extends ListenerAdapter {

    private final ApplicationContext applicationContext;
    private final JDA jda;

    public DiscordBot(ApplicationContext applicationContext, JDA jda) {
        this.applicationContext = applicationContext;
        this.jda = jda;
    }

    public void start() throws InterruptedException {
        registerListeners(jda);
        registerSlashCommands(jda);

        jda.awaitReady();
    }

    private void registerListeners(JDA jda) {
        getListeners().forEach(jda::addEventListener);
    }

    private void registerSlashCommands(JDA jda) {
        List<CommandData> commands = getSlashCommands().stream()
                .map(SlashCommand::getCommandData)
                .toList();

        jda.updateCommands()
                .addCommands(commands)
                .queue();
    }

    /**
     * Returns all beans of type {@link ListenerAdapter} from the application context
     * @return all beans of type {@link ListenerAdapter}
     */
    public Collection<ListenerAdapter> getListeners() {
        return applicationContext.getBeansOfType(ListenerAdapter.class).values();
    }

    /**
     * Returns all beans of type {@link SlashCommand} from the application context
     * @return all beans of type {@link SlashCommand}
     */
    public Collection<SlashCommand> getSlashCommands() {
        return applicationContext.getBeansOfType(SlashCommand.class).values();
    }
}
