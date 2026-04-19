package com.easybedwars.listeners;

import com.easybedwars.EasyBedWars;
import com.easybedwars.game.Arena;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitListener implements Listener {

    private final EasyBedWars plugin;

    public JoinQuitListener(EasyBedWars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        plugin.getPlayerManager().loadPlayer(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        
        Arena arena = plugin.getArenaManager().getPlayerArena(player);
        if (arena != null) {
            arena.leavePlayer(player);
        }
        
        plugin.getPlayerManager().unloadPlayer(player);
    }
}