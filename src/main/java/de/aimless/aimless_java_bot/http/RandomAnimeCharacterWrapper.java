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
        AnimeCharacterResponse response = restTemplate.getForObject(url, AnimeCharacterResponse.class);

        if (Objects.isNull(response)) {
            LOGGER.warn("No response from the API");
            return null;
        }

        return response.getData();
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
}
