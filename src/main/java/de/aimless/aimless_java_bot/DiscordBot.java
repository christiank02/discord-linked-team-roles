package de.aimless.aimless_java_bot;

import de.aimless.aimless_java_bot.command.RegisterMetaDataCommand;
import de.aimless.aimless_java_bot.command.boosterrole.BoosterRoleCommand;
import de.aimless.aimless_java_bot.command.joinmessage.JoinMessageCommand;
import de.aimless.aimless_java_bot.command.linkedrole.RoleCommand;
import de.aimless.aimless_java_bot.command.randomcharacter.RandomCharacterCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Collection;

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
        CommandData roleCommandData = new RoleCommand().roleCommandData();
        CommandData boosterRoleCommandData = new BoosterRoleCommand().roleCommandData();
        CommandData registerMetaDataCommandData = new RegisterMetaDataCommand().registerMetaDataCommandData();
        CommandData randomCharacterCommandData = new RandomCharacterCommand().roleCommandData();
        CommandData joinMessageCommandData = new JoinMessageCommand().roleCommandData();

        jda.updateCommands()
                .addCommands(roleCommandData, registerMetaDataCommandData, boosterRoleCommandData, randomCharacterCommandData, joinMessageCommandData)
                .queue();
    }

    public Collection<ListenerAdapter> getListeners() {
        return applicationContext.getBeansOfType(ListenerAdapter.class).values();
    }
}
