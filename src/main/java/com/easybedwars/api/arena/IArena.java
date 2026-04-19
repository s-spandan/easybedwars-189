package com.easybedwars.api.arena;

import org.bukkit.entity.Player;

import java.util.List;

public interface IArena {

    String getName();

    String getDisplayName();

    String getState();

    List<Player> getPlayers();

    int getMaxPlayers();

    boolean isPlaying();
}