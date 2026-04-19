package com.easybedwars.game;

import com.cryptomorin.xseries.XMaterial;
import com.easybedwars.EasyBedWars;
import com.easybedwars.api.events.ResourceSpawnEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Generator {

    private Location location;
    private GeneratorType type;
    private int tier;
    private int delay;
    private int amount;
    private long lastSpawn;
    private Team team;
    private boolean splitEnabled;
    private double splitRadius;

    public Generator(Location location, GeneratorType type) {
        this.location = location;
        this.type = type;
        this.tier = 1;
        this.lastSpawn = System.currentTimeMillis();
        this.splitEnabled = true;
        this.splitRadius = 5.0;
        updateStats();
    }

    public Generator(Location location, GeneratorType type, Team team) {
        this(location, type);
        this.team = team;
    }

    public void spawn() {
        long now = System.currentTimeMillis();
        if (now - lastSpawn >= delay) {
            XMaterial material = type.getMaterial();
            ItemStack item = material.parseItem();
            if (item != null) {
                item.setAmount(amount);
                
                if (splitEnabled && team != null) {
                    spawnWithSplit(item);
                } else {
                    Item dropped = location.getWorld().dropItem(location.clone().add(0.5, 0.5, 0.5), item);
                    dropped.setVelocity(new Vector(0, 0, 0));
                    dropped.setPickupDelay(0);
                }
            }
            lastSpawn = now;
        }
    }

    private void spawnWithSplit(ItemStack item) {
        List<Player> nearbyPlayers = findNearbyTeamPlayers();
        
        Arena arena = findArena();
        if (arena != null) {
            ResourceSpawnEvent event = new ResourceSpawnEvent(arena, this, item, nearbyPlayers);
            Bukkit.getPluginManager().callEvent(event);
            
            if (event.isCancelled()) {
                return;
            }
            
            nearbyPlayers = event.getReceivers();
        }
        
        if (nearbyPlayers.isEmpty()) {
            Item dropped = location.getWorld().dropItem(location.clone().add(0.5, 0.5, 0.5), item);
            dropped.setVelocity(new Vector(0, 0, 0));
            dropped.setPickupDelay(0);
        } else {
            int amountPerPlayer = Math.max(1, amount / nearbyPlayers.size());
            int remainder = amount % nearbyPlayers.size();
            
            for (int i = 0; i < nearbyPlayers.size(); i++) {
                Player player = nearbyPlayers.get(i);
                ItemStack playerItem = item.clone();
                playerItem.setAmount(amountPerPlayer + (i < remainder ? 1 : 0));
                
                player.getInventory().addItem(playerItem).values().forEach(leftover -> {
                    player.getWorld().dropItem(player.getLocation(), leftover);
                });
            }
        }
    }

    private List<Player> findNearbyTeamPlayers() {
        List<Player> nearby = new ArrayList<>();
        
        if (team == null) {
            return nearby;
        }
        
        for (Player player : team.getPlayers()) {
            if (player.isOnline() && !player.isDead()) {
                if (player.getLocation().distance(location) <= splitRadius) {
                    nearby.add(player);
                }
            }
        }
        
        return nearby;
    }

    private Arena findArena() {
        EasyBedWars plugin = EasyBedWars.getInstance();
        if (plugin == null) return null;
        
        for (Arena arena : plugin.getArenaManager().getArenas().values()) {
            if (arena.getGenerators().contains(this)) {
                return arena;
            }
        }
        return null;
    }

    public void upgradeTier() {
        if (tier < 3) {
            tier++;
            updateStats();
        }
    }

    private void updateStats() {
        switch (type) {
            case IRON:
                delay = tier == 1 ? 1000 : tier == 2 ? 800 : 600;
                amount = 1;
                break;
            case GOLD:
                delay = tier == 1 ? 5000 : tier == 2 ? 4000 : 3000;
                amount = 1;
                break;
            case DIAMOND:
                delay = tier == 1 ? 30000 : tier == 2 ? 20000 : 15000;
                amount = 1;
                break;
            case EMERALD:
                delay = tier == 1 ? 60000 : tier == 2 ? 45000 : 30000;
                amount = 1;
                break;
        }
    }

    public enum GeneratorType {
        IRON(XMaterial.IRON_INGOT),
        GOLD(XMaterial.GOLD_INGOT),
        DIAMOND(XMaterial.DIAMOND),
        EMERALD(XMaterial.EMERALD);

        private final XMaterial material;

        GeneratorType(XMaterial material) {
            this.material = material;
        }

        public XMaterial getMaterial() {
            return material;
        }
    }
}