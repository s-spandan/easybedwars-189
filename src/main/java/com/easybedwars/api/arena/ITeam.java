package com.easybedwars.api.arena;

import org.bukkit.entity.Player;

import java.util.List;

public interface ITeam {

    String getName();

    String getDisplayName();

    List<Player> getPlayers();

    boolean isBedAlive();

    boolean isAlive();
}