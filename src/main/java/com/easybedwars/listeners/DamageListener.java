package com.easybedwars.listeners;

import com.easybedwars.EasyBedWars;
import com.easybedwars.game.Arena;
import com.easybedwars.game.Team;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener implements Listener {

    private final EasyBedWars plugin;

    public DamageListener(EasyBedWars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        Arena arena = plugin.getArenaManager().getPlayerArena(player);

        if (arena == null) return;

        if (!arena.isPlaying()) {
            event.setCancelled(true);
            return;
        }

        if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
            event.setDamage(1000);
        }
    }

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getDamager() instanceof Player)) return;

        Player victim = (Player) event.getEntity();
        Player attacker = (Player) event.getDamager();

        Arena arena = plugin.getArenaManager().getPlayerArena(victim);
        if (arena == null || !arena.isPlaying()) return;

        Team victimTeam = arena.getPlayerTeam(victim);
        Team attackerTeam = arena.getPlayerTeam(attacker);

        if (victimTeam != null && victimTeam == attackerTeam) {
            event.setCancelled(true);
        }
    }
}