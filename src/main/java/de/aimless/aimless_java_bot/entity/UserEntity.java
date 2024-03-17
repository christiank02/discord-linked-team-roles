package de.aimless.aimless_java_bot.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class UserEntity {

    @Id
    private Long id;

    @OneToMany(mappedBy = "userEntity")
    private Set<UserGuildEntity> userGuilds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<UserGuildEntity> getUserGuilds() {
        return userGuilds;
    }

    public void setUserGuilds(Set<UserGuildEntity> userGuilds) {
        this.userGuilds = userGuilds;
    }
}
