package com.easybedwars.game;

import com.cryptomorin.xseries.XMaterial;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Team {

    private String name;
    private ChatColor color;
    private XMaterial woolMaterial;
    private XMaterial glassMaterial;
    private Location spawn;
    private Location bedLocation;
    private Location shopLocation;
    private Location upgradesLocation;
    private boolean bedAlive;
    private List<Player> players;
    private int sharpnessLevel;
    private int protectionLevel;
    private boolean hasteEnabled;
    private boolean healPoolEnabled;
    private List<String> traps;

    public Team(String name, ChatColor color, XMaterial woolMaterial, XMaterial glassMaterial) {
        this.name = name;
        this.color = color;
        this.woolMaterial = woolMaterial;
        this.glassMaterial = glassMaterial;
        this.bedAlive = true;
        this.players = new ArrayList<>();
        this.sharpnessLevel = 0;
        this.protectionLevel = 0;
        this.hasteEnabled = false;
        this.healPoolEnabled = false;
        this.traps = new ArrayList<>();
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public boolean hasPlayer(Player player) {
        return players.contains(player);
    }

    public boolean isAlive() {
        return bedAlive || !players.isEmpty();
    }

    public boolean isEliminated() {
        return !bedAlive && players.isEmpty();
    }

    public String getDisplayName() {
        return color + name;
    }

    public String getPrefix() {
        return color + "[" + name + "] ";
    }

    public void destroyBed() {
        this.bedAlive = false;
    }

    public void upgradeSharpness() {
        if (sharpnessLevel < 1) {
            sharpnessLevel = 1;
        }
    }

    public void upgradeProtection() {
        if (protectionLevel < 4) {
            protectionLevel++;
        }
    }

    public void enableHaste() {
        this.hasteEnabled = true;
    }

    public void enableHealPool() {
        this.healPoolEnabled = true;
    }

    public void addTrap(String trapType) {
        if (traps.size() < 3) {
            traps.add(trapType);
        }
    }

    public String activateFirstTrap() {
        if (!traps.isEmpty()) {
            return traps.remove(0);
        }
        return null;
    }
}