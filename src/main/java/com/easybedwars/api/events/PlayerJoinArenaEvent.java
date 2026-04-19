package com.easybedwars.api.events;

import com.easybedwars.game.Arena;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

@Getter
@Setter
public class PlayerJoinArenaEvent extends BedWarsEvent implements Cancellable {

    private final Player player;
    private boolean cancelled;

    public PlayerJoinArenaEvent(Arena arena, Player player) {
        super(arena);
        this.player = player;
        this.cancelled = false;
    }
}