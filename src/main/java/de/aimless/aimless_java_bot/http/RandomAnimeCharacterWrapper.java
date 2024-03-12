package de.aimless.aimless_java_bot.http;

import de.aimless.aimless_java_bot.http.model.AnimeCharacter;
import de.aimless.aimless_java_bot.http.model.AnimeCharacterResponse;
import de.aimless.aimless_java_bot.http.route.RandomAnimeCharacterRoute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;

@Component
public class RandomAnimeCharacterWrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(RandomAnimeCharacterWrapper.class);
    private static final String DEFAULT_IMAGE_URL = "https://cdn.myanimelist.net/img/sp/icon/apple-touch-icon-256.png";
    private static final String NO_RESPONSE_FROM_API = "No response from the API";
    private static final int CACHED_TOTAL_PAGES = 1000;

    private final RestTemplate restTemplate;
    private final Random random = new Random();

    public RandomAnimeCharacterWrapper(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private Optional<AnimeCharacterResponse> getTopAnimeCharacters(int page) {
        String url = RandomAnimeCharacterRoute.GET_TOP_ANIME_CHARACTERS.getRoute();
        AnimeCharacterResponse response = fetchAnimeCharactersResponse(url, page);

        return Optional.ofNullable(response);
    }

    private AnimeCharacterResponse fetchAnimeCharactersResponse(String url, int limit) {
        String formattedUrl = String.format("%s?page=%d", url, limit);
        AnimeCharacterResponse response = restTemplate.getForObject(formattedUrl, AnimeCharacterResponse.class);

        if (Objects.isNull(response)) {
            LOGGER.warn(NO_RESPONSE_FROM_API);
        }

        return response;
    }

    private Optional<AnimeCharacter> getRandomAnimeCharacter() {
        int page = random.nextInt(CACHED_TOTAL_PAGES) + 1;
        Optional<AnimeCharacterResponse> randomCharacterFromPage = getTopAnimeCharacters(page);

        if (randomCharacterFromPage.isPresent()) {
            List<AnimeCharacter> randomCharacters = randomCharacterFromPage.get().getData();
            int randomCharacterIndex = random.nextInt(randomCharacterFromPage.get().getPagination().getItems().getOrDefault("count", 1));

            return Optional.of(randomCharacters.get(randomCharacterIndex));
        }

        return Optional.empty();
    }

    public List<AnimeCharacter> getRandomAnimeCharacters(int size) {
        return IntStream.range(0, size)
                .parallel()
                .mapToObj(i -> getRandomCharacterWithImage())
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    private boolean isValidImageUrl(String imageUrl) {
        return imageUrl != null && !imageUrl.isEmpty() && !imageUrl.equals(DEFAULT_IMAGE_URL);
    }

    private Optional<AnimeCharacter> getRandomCharacterWithImage() {
        Optional<AnimeCharacter> character;
        String imageUrl;

        do {
            character = getRandomAnimeCharacter();
            imageUrl = character.map(c -> c.getImages().getJpg().getImage_url()).orElse("");
        } while (!isValidImageUrl(imageUrl));

        return character;
    }
}