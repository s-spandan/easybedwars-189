package com.easybedwars.managers;

import com.easybedwars.EasyBedWars;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SetupManager {

    private final EasyBedWars plugin;
    private final Map<UUID, String> setupMode;

    public SetupManager(EasyBedWars plugin) {
        this.plugin = plugin;
        this.setupMode = new HashMap<>();
    }

    public void enterSetupMode(Player player, String arenaName) {
        setupMode.put(player.getUniqueId(), arenaName);
    }

    public void exitSetupMode(Player player) {
        setupMode.remove(player.getUniqueId());
    }

    public boolean isInSetupMode(Player player) {
        return setupMode.containsKey(player.getUniqueId());
    }

    public String getSetupArena(Player player) {
        return setupMode.get(player.getUniqueId());
    }
}