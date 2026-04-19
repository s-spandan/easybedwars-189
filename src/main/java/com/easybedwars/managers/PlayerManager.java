package com.easybedwars.managers;

import com.easybedwars.EasyBedWars;
import com.easybedwars.data.PlayerData;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager {

    private final EasyBedWars plugin;
    private final Map<UUID, PlayerData> playerData;

    public PlayerManager(EasyBedWars plugin) {
        this.plugin = plugin;
        this.playerData = new HashMap<>();
    }

    public void loadPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        PlayerData data = plugin.getDatabaseManager().loadPlayerData(uuid);
        data.setUsername(player.getName());
        playerData.put(uuid, data);
    }

    public void unloadPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        PlayerData data = playerData.remove(uuid);
        if (data != null) {
            plugin.getDatabaseManager().savePlayerData(data);
        }
    }

    public PlayerData getPlayerData(Player player) {
        return playerData.get(player.getUniqueId());
    }

    public PlayerData getPlayerData(UUID uuid) {
        return playerData.get(uuid);
    }

    public void saveAllPlayers() {
        playerData.values().forEach(data -> plugin.getDatabaseManager().savePlayerData(data));
    }
}