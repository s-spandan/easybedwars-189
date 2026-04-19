package com.easybedwars.listeners;

import com.cryptomorin.xseries.XMaterial;
import com.easybedwars.EasyBedWars;
import com.easybedwars.game.Arena;
import com.easybedwars.game.PopupTower;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class InteractListener implements Listener {

    private final EasyBedWars plugin;

    public InteractListener(EasyBedWars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Arena arena = plugin.getArenaManager().getPlayerArena(player);

        if (arena == null || !arena.isPlaying()) {
            return;
        }

        ItemStack item = event.getItem();
        if (item == null) return;

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            XMaterial material = XMaterial.matchXMaterial(item.getType());

            if (material == XMaterial.COMPASS) {
                event.setCancelled(true);
                plugin.getTrackerCompass().startTracking(player, arena);
            }

            if (material == XMaterial.CHEST) {
                event.setCancelled(true);
                spawnPopupTower(player, arena);
            }
        }
    }

    private void spawnPopupTower(Player player, Arena arena) {
        Block targetBlock = player.getTargetBlock(null, 5);
        
        if (targetBlock == null || targetBlock.getType() == Material.AIR) {
            player.sendMessage(ChatColor.RED + "No valid block in range!");
            return;
        }

        Location towerLoc = targetBlock.getLocation().add(0, 1, 0);
        
        XMaterial towerMaterial = XMaterial.WHITE_WOOL;
        if (arena.getPlayerTeam(player) != null) {
            towerMaterial = arena.getPlayerTeam(player).getWoolMaterial();
        }

        PopupTower tower = PopupTower.createTower(towerLoc, towerMaterial, PopupTower.TowerType.MEDIUM);
        
        if (tower.build()) {
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item.getAmount() > 1) {
                item.setAmount(item.getAmount() - 1);
            } else {
                player.getInventory().setItemInMainHand(null);
            }
            
            player.sendMessage(ChatColor.GREEN + "Popup tower deployed!");
        } else {
            player.sendMessage(ChatColor.RED + "Cannot place tower here!");
        }
    }
}