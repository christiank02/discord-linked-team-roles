package de.aimless.aimless_java_bot.http.route;

import net.dv8tion.jda.api.requests.Route;

import static net.dv8tion.jda.api.requests.Method.GET;

public class RandomAnimeCharacterRoute {

    private RandomAnimeCharacterRoute() {}

    public static final Route GET_RANDOM_ANIME_CHARACTER = Route.custom(GET, "https://api.jikan.moe/v4/random/characters");

    public static final Route GET_TOP_ANIME_CHARACTERS = Route.custom(GET, "https://api.jikan.moe/v4/top/characters");
}
