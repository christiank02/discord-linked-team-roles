package de.aimless.aimless_java_bot.http.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AnimeCharacter {

    private int malId;
    private String url;
    private Images images;
    private String name;
    private String name_kanji;
    private List<String> nicknames;
    private int favorites;
    private String about;

    @Override
    public String toString() {
        return "AnimeCharacter{" +
                "malId=" + malId +
                ", url='" + url + '\'' +
                ", images=" + images +
                ", name='" + name + '\'' +
                ", nameKanji='" + name_kanji + '\'' +
                ", nicknames=" + nicknames +
                ", favorites=" + favorites +
                ", about='" + about + '\'' +
                '}';
    }
}

@Getter
@Setter
class Images {
    private Jpg jpg;
    private Webp webp;
}

@Getter
@Setter
class Jpg {
    private String image_url;
}

@Getter
@Setter
class Webp {
    private String image_url;
    private String small_image_url;
}