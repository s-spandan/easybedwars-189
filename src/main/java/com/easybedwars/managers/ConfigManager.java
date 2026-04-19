package com.easybedwars.managers;

import com.easybedwars.EasyBedWars;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

@Getter
public class ConfigManager {

    private final EasyBedWars plugin;
    private FileConfiguration config;

    public ConfigManager(EasyBedWars plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
        this.config = plugin.getConfig();
    }

    public void reload() {
        plugin.reloadConfig();
        this.config = plugin.getConfig();
    }

    public boolean debug() {
        return config.getBoolean("debug", false);
    }

    public int getMinPlayers() {
        return config.getInt("game.min-players", 2);
    }

    public int getCountdownTime() {
        return config.getInt("game.countdown-time", 10);
    }

    public int getRespawnTime() {
        return config.getInt("game.respawn-time", 5);
    }

    public int getGeneratorUpgradeDiamond() {
        return config.getInt("game.generator-upgrade-diamond", 360);
    }

    public int getGeneratorUpgradeEmerald() {
        return config.getInt("game.generator-upgrade-emerald", 720);
    }

    public int getGameTimeLimit() {
        return config.getInt("game.game-time-limit", 3600);
    }

    public String getDatabaseType() {
        return config.getString("database.type", "SQLITE");
    }

    public boolean isStatsEnabled() {
        return config.getBoolean("stats.enabled", true);
    }

    public int getStatsSaveInterval() {
        return config.getInt("stats.save-interval", 300);
    }

    public boolean isScoreboardEnabled() {
        return config.getBoolean("scoreboard.enabled", true);
    }

    public int getScoreboardRefresh() {
        return config.getInt("scoreboard.refresh-interval", 20);
    }
}