package com.easybedwars.managers;

import com.easybedwars.EasyBedWars;
import com.easybedwars.game.Arena;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ArenaManager {

    private final EasyBedWars plugin;
    private final Map<String, Arena> arenas;

    public ArenaManager(EasyBedWars plugin) {
        this.plugin = plugin;
        this.arenas = new HashMap<>();
    }

    public void loadArenas() {
        File arenasFolder = new File(plugin.getDataFolder(), "arenas");
        if (!arenasFolder.exists()) {
            arenasFolder.mkdirs();
            return;
        }

        File[] files = arenasFolder.listFiles((dir, name) -> name.endsWith(".yml"));
        if (files == null) return;

        for (File file : files) {
            String arenaName = file.getName().replace(".yml", "");
            Arena arena = loadArena(arenaName, file);
            if (arena != null) {
                arenas.put(arenaName, arena);
                plugin.getLogger().info("Loaded arena: " + arenaName);
            }
        }
    }

    private Arena loadArena(String name, File file) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        Arena arena = new Arena(plugin, name);
        return arena;
    }

    public void saveArena(Arena arena) {
        File file = new File(plugin.getDataFolder(), "arenas/" + arena.getName() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        try {
            config.save(file);
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to save arena: " + arena.getName());
        }
    }

    public Arena getArena(String name) {
        return arenas.get(name);
    }

    public Arena getPlayerArena(Player player) {
        return arenas.values().stream()
                .filter(arena -> arena.getGamePlayer(player) != null)
                .findFirst()
                .orElse(null);
    }

    public void createArena(String name) {
        Arena arena = new Arena(plugin, name);
        arenas.put(name, arena);
        saveArena(arena);
    }

    public void deleteArena(String name) {
        arenas.remove(name);
        File file = new File(plugin.getDataFolder(), "arenas/" + name + ".yml");
        file.delete();
    }

    public Map<String, Arena> getArenas() {
        return arenas;
    }

    public void stopAllGames() {
        arenas.values().forEach(arena -> {
            if (arena.isPlaying()) {
                arena.endGame(null);
            }
        });
    }
}