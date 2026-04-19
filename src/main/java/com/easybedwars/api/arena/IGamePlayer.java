package com.easybedwars.api.arena;

import org.bukkit.entity.Player;

public interface IGamePlayer {

    Player getPlayer();

    boolean isAlive();

    boolean isSpectator();

    int getKills();

    int getFinalKills();

    int getDeaths();

    int getBedsBroken();
}