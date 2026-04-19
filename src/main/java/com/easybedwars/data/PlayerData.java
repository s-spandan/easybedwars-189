package com.easybedwars.data;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PlayerData {

    private UUID uuid;
    private String username;
    private int kills;
    private int deaths;
    private int finalKills;
    private int finalDeaths;
    private int wins;
    private int losses;
    private int bedsBroken;
    private int gamesPlayed;
    private int level;
    private int experience;
    private int coins;

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
        this.username = "";
        this.kills = 0;
        this.deaths = 0;
        this.finalKills = 0;
        this.finalDeaths = 0;
        this.wins = 0;
        this.losses = 0;
        this.bedsBroken = 0;
        this.gamesPlayed = 0;
        this.level = 1;
        this.experience = 0;
        this.coins = 0;
    }

    public PlayerData(UUID uuid, String username, int kills, int deaths, int finalKills, int finalDeaths,
                     int wins, int losses, int bedsBroken, int gamesPlayed, int level, int experience, int coins) {
        this.uuid = uuid;
        this.username = username;
        this.kills = kills;
        this.deaths = deaths;
        this.finalKills = finalKills;
        this.finalDeaths = finalDeaths;
        this.wins = wins;
        this.losses = losses;
        this.bedsBroken = bedsBroken;
        this.gamesPlayed = gamesPlayed;
        this.level = level;
        this.experience = experience;
        this.coins = coins;
    }

    public void addKill() {
        kills++;
    }

    public void addDeath() {
        deaths++;
    }

    public void addFinalKill() {
        finalKills++;
    }

    public void addFinalDeath() {
        finalDeaths++;
    }

    public void addWin() {
        wins++;
    }

    public void addLoss() {
        losses++;
    }

    public void addBedBroken() {
        bedsBroken++;
    }

    public void addExperience(int amount) {
        experience += amount;
        checkLevelUp();
    }

    public void addCoins(int amount) {
        coins += amount;
    }

    private void checkLevelUp() {
        int requiredExp = getRequiredExpForLevel(level + 1);
        while (experience >= requiredExp) {
            experience -= requiredExp;
            level++;
            requiredExp = getRequiredExpForLevel(level + 1);
        }
    }

    private int getRequiredExpForLevel(int level) {
        return 500 + (level * 250);
    }

    public double getKDRatio() {
        return deaths == 0 ? kills : (double) kills / deaths;
    }

    public double getFinalKDRatio() {
        return finalDeaths == 0 ? finalKills : (double) finalKills / finalDeaths;
    }

    public double getWinRate() {
        int totalGames = wins + losses;
        return totalGames == 0 ? 0 : (double) wins / totalGames * 100;
    }
}