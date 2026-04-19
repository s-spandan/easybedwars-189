package com.easybedwars.managers;

import com.easybedwars.EasyBedWars;
import com.easybedwars.game.Arena;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class ScoreboardManager {

    private final EasyBedWars plugin;

    public ScoreboardManager(EasyBedWars plugin) {
        this.plugin = plugin;
    }

    public void createGameScoreboard(Player player, Arena arena) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("bedwars", "dummy", ChatColor.YELLOW + "" + ChatColor.BOLD + "BED WARS");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        player.setScoreboard(scoreboard);
    }

    public void removeScoreboard(Player player) {
        player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
    }
}