package com.easybedwars.api.events;

import com.easybedwars.game.Arena;
import com.easybedwars.game.Team;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

@Getter
@Setter
public class BedBreakEvent extends BedWarsEvent implements Cancellable {

    private final Player player;
    private final Team team;
    private boolean cancelled;

    public BedBreakEvent(Arena arena, Player player, Team team) {
        super(arena);
        this.player = player;
        this.team = team;
        this.cancelled = false;
    }
}