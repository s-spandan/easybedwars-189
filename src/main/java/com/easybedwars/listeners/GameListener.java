package com.easybedwars.listeners;

import com.cryptomorin.xseries.XSound;
import com.easybedwars.EasyBedWars;
import com.easybedwars.data.GamePlayer;
import com.easybedwars.game.Arena;
import com.easybedwars.game.Team;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class GameListener implements Listener {

    private final EasyBedWars plugin;

    public GameListener(EasyBedWars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Arena arena = plugin.getArenaManager().getPlayerArena(player);
        
        if (arena == null || !arena.isPlaying()) return;

        event.setDeathMessage(null);
        event.getDrops().clear();
        event.setDroppedExp(0);

        GamePlayer gamePlayer = arena.getGamePlayer(player);
        if (gamePlayer == null) return;

        gamePlayer.addDeath();

        Player killer = player.getKiller();
        if (killer != null) {
            GamePlayer killerGP = arena.getGamePlayer(killer);
            if (killerGP != null) {
                killerGP.addKill();
            }

            String message = plugin.getLanguageManager().getMessage("player.kill",
                    "killer", killer.getName(),
                    "player", player.getName());
            arena.broadcast(message);
        }

        Team team = gamePlayer.getTeam();
        if (team != null && !team.isBedAlive()) {
            gamePlayer.addFinalKill();
            team.removePlayer(player);
            gamePlayer.makeSpectator();

            String message = plugin.getLanguageManager().getMessage("player.final-kill",
                    "killer", killer != null ? killer.getName() : "Unknown",
                    "player", player.getName());
            arena.broadcast(message);

            if (team.isEliminated()) {
                String eliminatedMsg = plugin.getLanguageManager().getMessage("team.eliminated",
                        "team", team.getDisplayName());
                arena.broadcast(eliminatedMsg);
                
                arena.checkWinCondition();
            }
        } else {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (team != null && team.getSpawn() != null) {
                    player.spigot().respawn();
                    player.teleport(team.getSpawn());
                }
            }, plugin.getConfigManager().getRespawnTime() * 20L);
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        Arena arena = plugin.getArenaManager().getPlayerArena(player);
        
        if (arena == null || !arena.isPlaying()) return;

        GamePlayer gamePlayer = arena.getGamePlayer(player);
        if (gamePlayer == null) return;

        Team team = gamePlayer.getTeam();
        if (team != null && team.getSpawn() != null) {
            event.setRespawnLocation(team.getSpawn());
            
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                player.getInventory().clear();
                XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player);
            }, 1L);
        }
    }
}