package com.easybedwars.listeners;

import com.easybedwars.EasyBedWars;
import com.easybedwars.data.GamePlayer;
import com.easybedwars.game.Arena;
import com.easybedwars.game.Team;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    private final EasyBedWars plugin;

    public ChatListener(EasyBedWars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Arena arena = plugin.getArenaManager().getPlayerArena(player);

        if (arena == null || !arena.isPlaying()) {
            return;
        }

        event.setCancelled(true);

        GamePlayer gamePlayer = arena.getGamePlayer(player);
        if (gamePlayer == null) return;

        Team team = gamePlayer.getTeam();
        String message = event.getMessage();
        
        if (team != null && !gamePlayer.isSpectator()) {
            String teamMessage = team.getPrefix() + player.getName() + ": " + message;
            team.getPlayers().forEach(p -> p.sendMessage(teamMessage));
        } else if (gamePlayer.isSpectator()) {
            String spectatorMessage = plugin.getConfig().getString("chat.spectator-prefix") + player.getName() + ": " + message;
            arena.getPlayers().stream()
                    .filter(gp -> gp.isSpectator())
                    .forEach(gp -> gp.getPlayer().sendMessage(spectatorMessage));
        }
    }
}