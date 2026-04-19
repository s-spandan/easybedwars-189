package com.easybedwars.listeners;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import com.easybedwars.EasyBedWars;
import com.easybedwars.data.GamePlayer;
import com.easybedwars.game.Arena;
import com.easybedwars.game.Team;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListener implements Listener {

    private final EasyBedWars plugin;

    public BlockListener(EasyBedWars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Arena arena = plugin.getArenaManager().getPlayerArena(player);

        if (arena == null || !arena.isPlaying()) {
            return;
        }

        XMaterial blockType = XMaterial.matchXMaterial(block.getType());

        if (blockType == XMaterial.RED_BED || blockType == XMaterial.BLUE_BED || 
            blockType == XMaterial.GREEN_BED || blockType == XMaterial.YELLOW_BED ||
            blockType == XMaterial.WHITE_BED || blockType == XMaterial.PINK_BED ||
            blockType == XMaterial.CYAN_BED || blockType == XMaterial.GRAY_BED) {

            for (Team team : arena.getTeams().values()) {
                if (team.getBedLocation() != null && 
                    team.getBedLocation().getBlock().equals(block)) {

                    Team playerTeam = arena.getPlayerTeam(player);
                    if (playerTeam == team) {
                        event.setCancelled(true);
                        player.sendMessage(plugin.getLanguageManager().getMessage("error.own-bed"));
                        return;
                    }

                    team.destroyBed();
                    
                    GamePlayer gamePlayer = arena.getGamePlayer(player);
                    if (gamePlayer != null) {
                        gamePlayer.addBedBroken();
                    }

                    String message = plugin.getLanguageManager().getMessage("team.bed-destroyed",
                            "team", team.getDisplayName(),
                            "player", player.getName());
                    arena.broadcast(message);
                    
                    XSound.ENTITY_ENDER_DRAGON_GROWL.play(player);
                    
                    team.getPlayers().forEach(p -> {
                        p.sendMessage(plugin.getLanguageManager().getMessage("player.bed-destroyed"));
                        XSound.ENTITY_WITHER_DEATH.play(p);
                    });

                    return;
                }
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Arena arena = plugin.getArenaManager().getPlayerArena(player);

        if (arena == null || !arena.isPlaying()) {
            return;
        }
    }
}