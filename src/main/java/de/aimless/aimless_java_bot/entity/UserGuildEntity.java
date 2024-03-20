package de.aimless.aimless_java_bot.entity;

import jakarta.persistence.*;

@Entity
public class UserGuildEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "guild_id")
    private GuildEntity guildEntity;

    @Column(columnDefinition = "boolean default true")
    private boolean rainbowRoleEnabled;

    @Column(name = "boost_count", nullable = false, columnDefinition = "integer default 0")
    private int boostCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public GuildEntity getGuildEntity() {
        return guildEntity;
    }

    public void setGuildEntity(GuildEntity guildEntity) {
        this.guildEntity = guildEntity;
    }

    public boolean isRainbowRoleEnabled() {
        return rainbowRoleEnabled;
    }

    public void setRainbowRoleEnabled(boolean rainbowRoleEnabled) {
        this.rainbowRoleEnabled = rainbowRoleEnabled;
    }

    public int getBoostCount() {
        return boostCount;
    }

    public void setBoostCount(int boostCount) {
        this.boostCount = boostCount;
    }
}
