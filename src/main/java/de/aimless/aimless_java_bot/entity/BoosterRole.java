package de.aimless.aimless_java_bot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class BoosterRole {

    @Id
    private long id;
    private String name;
    private long guildId;
    private boolean autoAssignable;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getGuildId() {
        return guildId;
    }

    public void setGuildId(long guildId) {
        this.guildId = guildId;
    }

    public boolean isAutoAssignable() {
        return autoAssignable;
    }

    public void setAutoAssignable(boolean autoAssignable) {
        this.autoAssignable = autoAssignable;
    }
}
