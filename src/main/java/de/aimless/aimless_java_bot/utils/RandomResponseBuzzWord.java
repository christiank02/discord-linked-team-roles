package de.aimless.aimless_java_bot.utils;

import lombok.Getter;

import java.util.Optional;

@Getter
public enum RandomResponseBuzzWord {
    PIZZA("pizza", "Hat wer Pizza gesagt? Ich will auch Pizza!", "https://64.media.tumblr.com/2a166a275e4a8132ea5f4f7f8a66b5e5/4916119f6066ed41-28/s500x750/475fd54afe0ca5394b5c783e760fdb9bf8e5290b.gif" , "Aimless wants pizza"),
    AIMLESS("aimless", "", "https://cdn.discordapp.com/avatars/244209930268049409/e8c5beee3db2c89771b252c0a0ae07ee.webp?size=2048", "Aimless loves you"),
    AIMLESS_SELINA("custom_651051055119073288", "Selinaaaa. Hiii.", "https://media0.giphy.com/media/MW0UdQdIaXXDq/giphy.gif", "Aimless misses you"),
    AIMLESS_BLUEMCHEN("custom_526102422523871255", "Hallo.", "https://i.pinimg.com/originals/8c/d8/10/8cd810da13a38b8acf700b6ce75a6250.gif", "Aimless hates you");

    private final String buzzWord;
    private final String response;
    private final String responseImage;
    private final String footer;

    RandomResponseBuzzWord(String buzzWord, String response, String responseImageUrl, String footer) {
        this.buzzWord = buzzWord;
        this.response = response;
        this.responseImage = responseImageUrl;
        this.footer = footer;
    }

    public static Optional<RandomResponseBuzzWord> findBuzzWord(String message) {
        for (RandomResponseBuzzWord buzzWord : RandomResponseBuzzWord.values()) {
            if (message.toLowerCase().contains(buzzWord.buzzWord)) {
                return Optional.of(buzzWord);
            }
        }
        return Optional.empty();
    }
}
