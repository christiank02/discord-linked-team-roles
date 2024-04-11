package de.aimless.aimless_java_bot.entity;

import jakarta.persistence.*;

@Entity
public class CountingGameEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long channelId;

    @Column
    private Long lastUserId;

    @Column
    private int lastNumber;

    @Column
    private int lastNumberBeforeReset;

    @Column
    private boolean pendingDecision;

    @OneToOne
    @JoinColumn(name = "guild_id")
    private GuildEntity guild;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public Long getLastUserId() {
        return lastUserId;
    }

    public void setLastUserId(Long lastUserId) {
        this.lastUserId = lastUserId;
    }

    public int getLastNumber() {
        return lastNumber;
    }

    public void setLastNumber(int lastNumber) {
        this.lastNumber = lastNumber;
    }


    public int getLastNumberBeforeReset() {
        return lastNumberBeforeReset;
    }

    public void setLastNumberBeforeReset(int lastNumberBeforeReset) {
        this.lastNumberBeforeReset = lastNumberBeforeReset;
    }

    public Boolean getPendingDecision() {
        return pendingDecision;
    }

    public void setPendingDecision(Boolean pendingDecision) {
        this.pendingDecision = pendingDecision;
    }

    public GuildEntity getGuild() {
        return guild;
    }

    public void setGuild(GuildEntity guild) {
        this.guild = guild;
    }
}
