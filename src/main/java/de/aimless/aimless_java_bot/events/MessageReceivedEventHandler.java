package de.aimless.aimless_java_bot.events;

import de.aimless.aimless_java_bot.entity.*;
import de.aimless.aimless_java_bot.repository.CountingGameRepository;
import de.aimless.aimless_java_bot.repository.GuildRepository;
import de.aimless.aimless_java_bot.repository.UserEntityRepository;
import de.aimless.aimless_java_bot.repository.UserGuildRepository;
import de.aimless.aimless_java_bot.utils.CountingAbility;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.awt.Color;
import java.util.*;

@Component
public class MessageReceivedEventHandler extends ListenerAdapter {

    private final CountingGameRepository countingGameRepository;
    private final UserGuildRepository userGuildEntityRepository;
    private final UserEntityRepository userEntityRepository;
    private final GuildRepository guildRepository;
    private final Random random;

    public MessageReceivedEventHandler(CountingGameRepository countingGameRepository, UserGuildRepository userGuildEntityRepository, UserEntityRepository userEntityRepository, GuildRepository guildRepository) {
        this.countingGameRepository = countingGameRepository;
        this.userGuildEntityRepository = userGuildEntityRepository;
        this.userEntityRepository = userEntityRepository;
        this.guildRepository = guildRepository;
        this.random = new Random();
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (isBotMessage(event)) {
            return;
        }

        Optional<CountingGameEntity> optionalGuildEntity = getCountingGameEntity(event);
        optionalGuildEntity.ifPresent(countingGameEntity -> handleCountingEvent(event, countingGameEntity));
    }

    private boolean isBotMessage(MessageReceivedEvent event) {
        return event.getAuthor().isBot();
    }

    private Optional<CountingGameEntity> getCountingGameEntity(MessageReceivedEvent event) {
        long guildId = event.getGuild().getIdLong();
        return countingGameRepository.findByGuildGuildId(guildId);
    }

    private void handleCountingEvent(MessageReceivedEvent event, CountingGameEntity countingGameEntity) {
        if (isCountingChannelMessage(event, countingGameEntity) && isNumericMessage(event)) {
            int number = getNumberFromMessage(event);
            handleNumberMessage(event, countingGameEntity, number);
        }

        tryAssignRandomCountingGameAbility(event, countingGameEntity);
    }

    private boolean isCountingChannelMessage(MessageReceivedEvent event, CountingGameEntity countingGameEntity) {
        return event.getChannel().getIdLong() == countingGameEntity.getChannelId();
    }

    private boolean isNumericMessage(MessageReceivedEvent event) {
        return event.getMessage().getContentRaw().matches("^-?\\d+$");
    }

    private int getNumberFromMessage(MessageReceivedEvent event) {
        return Integer.parseInt(event.getMessage().getContentRaw());
    }

    private void handleNumberMessage(MessageReceivedEvent event, CountingGameEntity countingGameEntity, int number) {
        if (isNextNumberAndDifferentUser(event, countingGameEntity, number)) {
            handleValidNumber(event, countingGameEntity, number);

            if (number == 1) {
                countingGameEntity.setPendingDecision(false);
                countingGameRepository.save(countingGameEntity);
            }
        } else {
            handleError(event, countingGameEntity);
        }
    }

    private boolean isNextNumberAndDifferentUser(MessageReceivedEvent event, CountingGameEntity countingGameEntity, int number) {
        return number == countingGameEntity.getLastNumber() + 1 && !isMessageFromSameUser(event, countingGameEntity);
    }

    private void handleValidNumber(MessageReceivedEvent event, CountingGameEntity countingGameEntity, int number) {
        updateCountingInfo(countingGameEntity, number, event);
        approveCountingNumber(event);
    }

    // update the messages
    private void handleError(MessageReceivedEvent event, CountingGameEntity countingGameEntity) {
        addErrorReaction(event);
        sendErrorMessage(event, countingGameEntity);
        resetCountingInfo(countingGameEntity);

        handleAbilities(event, countingGameEntity);
    }

    private boolean isMessageFromSameUser(MessageReceivedEvent event, CountingGameEntity countingGameEntity) {
        if (Objects.isNull(countingGameEntity.getLastUserId())) {
            return false;
        }
        return event.getAuthor().getIdLong() == countingGameEntity.getLastUserId();
    }

    private void updateCountingInfo(CountingGameEntity countingGameEntity, int number, MessageReceivedEvent event) {
        countingGameEntity.setLastNumber(number);
        countingGameEntity.setLastUserId(event.getAuthor().getIdLong());
        countingGameRepository.save(countingGameEntity);
    }

    private void approveCountingNumber(MessageReceivedEvent event) {
        event.getMessage()
                .addReaction(Emoji.fromUnicode("U+2705"))
                .queue();
    }

    private void handleAbilities(MessageReceivedEvent event, CountingGameEntity countingGameEntity) {
        countingGameEntity.setPendingDecision(true);
        sendMessageEmbed(event);
        countingGameRepository.save(countingGameEntity);
    }

    private void sendMessageEmbed(MessageReceivedEvent event) {
        MessageEmbed embed = createEmbedMessage();
        Button button = Button.primary(CountingAbility.StreakSaver.getButtonId(), "Fähigkeit verwenden");
        event.getChannel().sendMessageEmbeds(embed).setActionRow(button).queue();
    }

    private MessageEmbed createEmbedMessage() {
        return new EmbedBuilder()
                .setTitle(String.format("%s Fähigkeit", CountingAbility.StreakSaver.getDisplayName()))
                .setDescription(String.format("Wenn du die Fähigkeit '%s' hast, kannst du sie verwenden, um die Serie zu retten!", CountingAbility.StreakSaver.getDisplayName()))
                .setColor(Color.RED)
                .build();
    }

    private void addErrorReaction(MessageReceivedEvent event) {
        event.getMessage()
                .addReaction(Emoji.fromUnicode("U+274C"))
                .queue();
    }

    private void sendErrorMessage(MessageReceivedEvent event, CountingGameEntity countingGameEntity) {
        String replyMessage;
        if (isMessageFromSameUser(event, countingGameEntity)) {
            replyMessage = String.format("%s HAT DIE REIHE BEI %d ZERSTÖRT!! Die nächste Zahl ist **1. Du darfst nicht zweimal hintereinander zählen.**", event.getAuthor().getAsMention(), countingGameEntity.getLastNumber());
        } else {
            replyMessage = String.format("%s HAT DIE REIHE ZERSTÖRT! DIE NÄCHSTE ZAHL IST **1. Falsche Zahl.**", event.getAuthor().getAsMention());
        }
        event.getChannel().sendMessage(replyMessage).queue();
    }

    private void resetCountingInfo(CountingGameEntity countingGameEntity) {
        countingGameEntity.setLastNumberBeforeReset(countingGameEntity.getLastNumber());
        countingGameEntity.setLastNumber(0);
        countingGameEntity.setLastUserId(null);
        countingGameRepository.save(countingGameEntity);
    }

    private void tryAssignRandomCountingGameAbility(MessageReceivedEvent event, CountingGameEntity countingGameEntity) {
        int randomNumber = generateRandomNumber(500);

        // Assign a random counting ability if the random number is less than 10 (2% chance, when the range is 0 - 499)
        if (randomNumber < 10) {
            assignRandomCountingGameAbility(event, countingGameEntity);
        }
    }

    private int generateRandomNumber(int bound) {
        return random.nextInt(bound);
    }

    private void assignRandomCountingGameAbility(MessageReceivedEvent event, CountingGameEntity countingGameEntity) {
        // Generate a random number within the range of the CountingAbility enum values
        int randomIndex = new Random().nextInt(CountingAbility.values().length);

        // Select a CountingAbility using the random number
        CountingAbility randomAbility = CountingAbility.values()[randomIndex];

        // get the UserGuildEntity and add the new CountingGameAbility to it
        long userId = event.getAuthor().getIdLong();
        long guildId = event.getGuild().getIdLong();

        Optional<UserGuildEntity> optionalUserGuildEntity = userGuildEntityRepository.findByUserEntityIdAndGuildEntityGuildId(userId, guildId);
        UserGuildEntity userGuildEntity = optionalUserGuildEntity.orElseGet(() -> createUserGuildEntity(userId, guildId));

        userGuildEntity.getCountingGameAbilities().add(randomAbility);
        userGuildEntityRepository.save(userGuildEntity);

        sendAbilityAssignedMessage(event, randomAbility, countingGameEntity);
    }

    private void sendAbilityAssignedMessage(MessageReceivedEvent event, CountingAbility ability, CountingGameEntity countingGameEntity) {
        TextChannel countingChannel = event.getGuild().getTextChannelById(countingGameEntity.getChannelId());

        String embedMessage = String.format("Glückwunsch, **%s**! Du hast die **%s** Fähigkeit erhalten. \nDu kannst die Fähigkeit in %s verwenden.", event.getAuthor().getEffectiveName(), ability.getDisplayName(), countingChannel);
        MessageEmbed embed = new EmbedBuilder()
                .setTitle("Neue Counting Fähigkeit!")
                .setDescription(embedMessage)
                .addField(ability.getDisplayName(), ability.getDescription(), false)
                .setColor(Color.GREEN)
                .build();

        event.getChannel().sendMessageEmbeds(embed).queue();
    }

    private @NotNull UserGuildEntity createUserGuildEntity(long userId, long guildId) {
        // Retrieve or create UserEntity
        UserEntity userEntity = userEntityRepository.findById(userId)
                .orElseGet(() -> {
                    UserEntity newUserEntity = new UserEntity();
                    newUserEntity.setId(userId);
                    return userEntityRepository.save(newUserEntity);
                });

        // Retrieve or create GuildEntity
        GuildEntity guildEntity = guildRepository.findById(guildId)
                .orElseGet(() -> {
                    GuildEntity newGuildEntity = new GuildEntity();
                    newGuildEntity.setGuildId(guildId);
                    return guildRepository.save(newGuildEntity);
                });

        // Create new UserGuildEntity
        UserGuildEntity newUserGuildEntity = new UserGuildEntity();
        newUserGuildEntity.setUserEntity(userEntity);
        newUserGuildEntity.setGuildEntity(guildEntity);

        return newUserGuildEntity;
    }
}
