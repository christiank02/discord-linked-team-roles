package de.aimless.aimless_java_bot.http.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class Pagination {
    private int last_visible_page;
    private int current_page;
    private Map<String, Integer> items;
}
