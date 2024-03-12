package de.aimless.aimless_java_bot.http.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AnimeCharacterResponse {

    private Pagination pagination;
    private List<AnimeCharacter> data;
}
