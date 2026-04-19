package com.easybedwars.game;

import com.cryptomorin.xseries.XMaterial;
import com.easybedwars.EasyBedWars;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TrackerCompass {

    private final EasyBedWars plugin;
    private final Map<UUID, Player> tracking;
    private final Map<UUID, BukkitRunnable> tasks;

    public TrackerCompass(EasyBedWars plugin) {
        this.plugin = plugin;
        this.tracking = new HashMap<>();
        this.tasks = new HashMap<>();
    }

    public void startTracking(Player player, Arena arena) {
        Player target = findNearestEnemy(player, arena);
        
        if (target == null) {
            player.sendMessage(ChatColor.RED + "No enemies found!");
            return;
        }

        tracking.put(player.getUniqueId(), target);
        
        stopTracking(player);

        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                Player currentTarget = tracking.get(player.getUniqueId());
                
                if (currentTarget == null || !currentTarget.isOnline() || currentTarget.isDead()) {
                    Player newTarget = findNearestEnemy(player, arena);
                    if (newTarget == null) {
                        player.sendMessage(ChatColor.RED + "Target lost!");
                        cancel();
                        return;
                    }
                    tracking.put(player.getUniqueId(), newTarget);
                    currentTarget = newTarget;
                }

                player.setCompassTarget(currentTarget.getLocation());
                
                Team targetTeam = arena.getPlayerTeam(currentTarget);
                String teamName = targetTeam != null ? targetTeam.getDisplayName() : "Unknown";
                int distance = (int) player.getLocation().distance(currentTarget.getLocation());
                
                player.sendMessage(ChatColor.GREEN + "Tracking: " + teamName + ChatColor.GRAY + 
                        " (" + distance + "m away)");
            }
        };

        task.runTaskTimer(plugin, 0L, 40L);
        tasks.put(player.getUniqueId(), task);
        
        player.sendMessage(ChatColor.GREEN + "Tracker activated!");
    }

    public void stopTracking(Player player) {
        BukkitRunnable task = tasks.remove(player.getUniqueId());
        if (task != null) {
            task.cancel();
        }
        tracking.remove(player.getUniqueId());
    }

    private Player findNearestEnemy(Player player, Arena arena) {
        Team playerTeam = arena.getPlayerTeam(player);
        if (playerTeam == null) return null;

        return arena.getPlayers().stream()
                .map(gp -> gp.getPlayer())
                .filter(p -> !p.equals(player))
                .filter(p -> arena.getPlayerTeam(p) != playerTeam)
                .filter(p -> p.isOnline() && !p.isDead())
                .min((p1, p2) -> {
                    double d1 = player.getLocation().distance(p1.getLocation());
                    double d2 = player.getLocation().distance(p2.getLocation());
                    return Double.compare(d1, d2);
                })
                .orElse(null);
    }

    public static ItemStack createCompass() {
        ItemStack compass = XMaterial.COMPASS.parseItem();
        if (compass != null) {
            ItemMeta meta = compass.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Tracker");
                meta.setLore(Arrays.asList(
                        ChatColor.GRAY + "Track the nearest enemy player",
                        "",
                        ChatColor.YELLOW + "Right-click to use!"
                ));
                compass.setItemMeta(meta);
            }
        }
        return compass;
    }

    public boolean isTracking(Player player) {
        return tracking.containsKey(player.getUniqueId());
    }
}