package com.easybedwars.api.events;

import com.easybedwars.game.Arena;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class PlayerLeaveArenaEvent extends BedWarsEvent {

    private final Player player;

    public PlayerLeaveArenaEvent(Arena arena, Player player) {
        super(arena);
        this.player = player;
    }
}