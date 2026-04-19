package com.easybedwars.api.events;

import com.easybedwars.game.Arena;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

@Getter
@Setter
public class PlayerKillEvent extends BedWarsEvent implements Cancellable {

    private final Player killer;
    private final Player victim;
    private final boolean finalKill;
    private boolean cancelled;

    public PlayerKillEvent(Arena arena, Player killer, Player victim, boolean finalKill) {
        super(arena);
        this.killer = killer;
        this.victim = victim;
        this.finalKill = finalKill;
        this.cancelled = false;
    }
}