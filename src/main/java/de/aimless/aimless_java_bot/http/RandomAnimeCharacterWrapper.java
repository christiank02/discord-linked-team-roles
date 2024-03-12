package de.aimless.aimless_java_bot.http;

import de.aimless.aimless_java_bot.http.model.AnimeCharacter;
import de.aimless.aimless_java_bot.http.model.AnimeCharacterResponse;
import de.aimless.aimless_java_bot.http.route.RandomAnimeCharacterRoute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class RandomAnimeCharacterWrapper {

    private static final String DEFAULT_IMAGE_URL = "https://cdn.myanimelist.net/img/sp/icon/apple-touch-icon-256.png";
    private static final Logger LOGGER = LoggerFactory.getLogger(RandomAnimeCharacterWrapper.class);

    private final RestTemplate restTemplate;

    public RandomAnimeCharacterWrapper(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Will get a random anime character from the API and return it.
     *
     * @return random anime character or null if no response from the API
     */
    public AnimeCharacter getRandomAnimeCharacter() {
        String url = RandomAnimeCharacterRoute.GET_RANDOM_ANIME_CHARACTER.getRoute();
        AnimeCharacterResponse response = fetchAnimeCharacterResponse(url);

        if (response == null) {
            return null;
        }

        AnimeCharacter character = response.getData();
        character = ensureCharacterHasImageUrl(character, url);

        return character;
    }

    private AnimeCharacterResponse fetchAnimeCharacterResponse(String url) {
        AnimeCharacterResponse response = restTemplate.getForObject(url, AnimeCharacterResponse.class);

        if (Objects.isNull(response)) {
            LOGGER.warn("No response from the API");
        }

        return response;
    }

    /**
     * Will get a specified number of random anime characters from the API and return them.
     *
     * @param size the number of random anime characters to generate
     * @return a list of random anime characters or an empty list if no response from the API
     */
    public List<AnimeCharacter> getRandomAnimeCharacters(int size) {
        List<AnimeCharacter> characters = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            AnimeCharacter character = getRandomAnimeCharacter();
            if (character != null) {
                characters.add(character);
            } else {
                LOGGER.warn("No response from the API for request number {}", i + 1);
            }
        }

        return characters;
    }

    private AnimeCharacter ensureCharacterHasImageUrl(AnimeCharacter character, String url) {
        String imageUrl = character.getImages().getJpg().getImage_url();
        int retryCount = 0;

        while ((imageUrl == null || imageUrl.isEmpty() || imageUrl.equals(DEFAULT_IMAGE_URL)) && retryCount < 5) {
            LOGGER.warn("Empty image URL, retrying...");
            AnimeCharacterResponse response = restTemplate.getForObject(url, AnimeCharacterResponse.class);

            if (Objects.isNull(response)) {
                LOGGER.warn("No response from the API");
                return null;
            }

            character = response.getData();
            imageUrl = character.getImages().getJpg().getImage_url();
            retryCount++;
        }

        return character;
    }
}
