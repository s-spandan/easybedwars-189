package com.easybedwars.managers;

import com.cryptomorin.xseries.XMaterial;
import com.easybedwars.EasyBedWars;
import com.easybedwars.game.Arena;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Location;
import org.bukkit.World;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

@Getter
public class TrackerCompass {

    private final EasyBedWars plugin;
    private final Map<UUID, Location> trackedLocations = new HashMap<>();

    public TrackerCompass(EasyBedWars plugin) {
        this.plugin = plugin;
    }

    public void giveCompass(Player player) {
        ItemStack compass = XMaterial.matchXMaterial("COMPASS").map(XMaterial::parseItem).orElse(null);
        if (compass != null) {
            player.getInventory().addItem(compass);
        }
    }

    public void updateCompass(Player player) {
        Player nearestEnemy = findNearestEnemy(player);
        if (nearestEnemy != null) {
            player.setCompassTarget(nearestEnemy.getLocation());
        }
    }

    public void startTracking(Player player, Arena arena) {
        // Start tracking enemies in the arena
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            Player nearestEnemy = findNearestEnemy(player);
            if (nearestEnemy != null) {
                player.setCompassTarget(nearestEnemy.getLocation());
            }
        }, 0L, 20L); // Update every second
    }

    private Player findNearestEnemy(Player player) {
        Arena arena = plugin.getArenaManager().getPlayerArena(player);
        if (arena == null) return null;

        Player nearestEnemy = null;
        double minDistance = Double.MAX_VALUE;

        for (Player otherPlayer : arena.getPlayers().stream().map(gp -> gp.getPlayer()).toArray(Player[]::new)) {
            if (otherPlayer.equals(player) || !otherPlayer.isOnline()) continue;

            // Check if players are on different teams
            if (arena.getPlayerTeam(player) != null && arena.getPlayerTeam(otherPlayer) != null) {
                if (arena.getPlayerTeam(player).equals(arena.getPlayerTeam(otherPlayer))) {
                    continue; // Same team
                }
            }

            double distance = player.getLocation().distance(otherPlayer.getLocation());
            if (distance < minDistance) {
                minDistance = distance;
                nearestEnemy = otherPlayer;
            }
        }

        return nearestEnemy;
    }

    public void setTrackedLocation(UUID playerId, Location location) {
        trackedLocations.put(playerId, location);
    }

    public Location getTrackedLocation(UUID playerId) {
        return trackedLocations.get(playerId);
    }
}