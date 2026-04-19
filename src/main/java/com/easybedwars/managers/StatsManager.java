package com.easybedwars.managers;

import com.easybedwars.EasyBedWars;
import com.easybedwars.data.GamePlayer;
import com.easybedwars.data.PlayerData;
import com.easybedwars.game.Team;
import org.bukkit.entity.Player;

public class StatsManager {

    private final EasyBedWars plugin;

    public StatsManager(EasyBedWars plugin) {
        this.plugin = plugin;
    }

    public void updateStats(Player player, GamePlayer gamePlayer, Team winningTeam) {
        PlayerData data = plugin.getPlayerManager().getPlayerData(player);
        if (data == null) return;

        data.setKills(data.getKills() + gamePlayer.getKills());
        data.setFinalKills(data.getFinalKills() + gamePlayer.getFinalKills());
        data.setDeaths(data.getDeaths() + gamePlayer.getDeaths());
        data.setBedsBroken(data.getBedsBroken() + gamePlayer.getBedsBroken());
        data.setGamesPlayed(data.getGamesPlayed() + 1);

        if (winningTeam != null && gamePlayer.getTeam() == winningTeam) {
            data.addWin();
            data.addExperience(50);
            data.addCoins(100);
        } else {
            data.addLoss();
            data.addExperience(10);
            data.addCoins(25);
        }

        data.addExperience(gamePlayer.getKills() * 5);
        data.addExperience(gamePlayer.getFinalKills() * 10);
        data.addExperience(gamePlayer.getBedsBroken() * 15);

        plugin.getDatabaseManager().savePlayerData(data);
    }

    public String formatStats(PlayerData data) {
        return plugin.getLanguageManager().getMessage("stats.header") + "\n" +
               plugin.getLanguageManager().getMessage("stats.kills", "kills", String.valueOf(data.getKills())) + "\n" +
               plugin.getLanguageManager().getMessage("stats.deaths", "deaths", String.valueOf(data.getDeaths())) + "\n" +
               plugin.getLanguageManager().getMessage("stats.final-kills", "finalKills", String.valueOf(data.getFinalKills())) + "\n" +
               plugin.getLanguageManager().getMessage("stats.wins", "wins", String.valueOf(data.getWins())) + "\n" +
               plugin.getLanguageManager().getMessage("stats.losses", "losses", String.valueOf(data.getLosses())) + "\n" +
               plugin.getLanguageManager().getMessage("stats.beds-broken", "bedsBroken", String.valueOf(data.getBedsBroken())) + "\n" +
               plugin.getLanguageManager().getMessage("stats.level", "level", String.valueOf(data.getLevel())) + "\n" +
               plugin.getLanguageManager().getMessage("stats.footer");
    }
}