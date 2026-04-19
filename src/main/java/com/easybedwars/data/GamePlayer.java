package com.easybedwars.data;

import com.easybedwars.game.Arena;
import com.easybedwars.game.Team;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@Getter
@Setter
public class GamePlayer {

    private UUID uuid;
    private Player player;
    private Arena arena;
    private Team team;
    private boolean alive;
    private boolean spectator;
    private int kills;
    private int finalKills;
    private int deaths;
    private int bedsBroken;
    private ItemStack[] savedInventory;
    private ItemStack[] savedArmor;

    public GamePlayer(Player player, Arena arena) {
        this.uuid = player.getUniqueId();
        this.player = player;
        this.arena = arena;
        this.alive = true;
        this.spectator = false;
        this.kills = 0;
        this.finalKills = 0;
        this.deaths = 0;
        this.bedsBroken = 0;
    }

    public void addKill() {
        kills++;
    }

    public void addFinalKill() {
        finalKills++;
    }

    public void addDeath() {
        deaths++;
    }

    public void addBedBroken() {
        bedsBroken++;
    }

    public void saveInventory() {
        this.savedInventory = player.getInventory().getContents();
        this.savedArmor = player.getInventory().getArmorContents();
    }

    public void restoreInventory() {
        if (savedInventory != null) {
            player.getInventory().setContents(savedInventory);
        }
        if (savedArmor != null) {
            player.getInventory().setArmorContents(savedArmor);
        }
    }

    public void makeSpectator() {
        this.spectator = true;
        this.alive = false;
        player.setAllowFlight(true);
        player.setFlying(true);
        arena.getPlayers().forEach(gp -> gp.getPlayer().hidePlayer(player));
    }
}