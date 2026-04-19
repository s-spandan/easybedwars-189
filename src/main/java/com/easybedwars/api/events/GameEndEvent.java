package com.easybedwars.api.events;

import com.easybedwars.game.Arena;
import com.easybedwars.game.Team;
import lombok.Getter;

@Getter
public class GameEndEvent extends BedWarsEvent {

    private final Team winningTeam;

    public GameEndEvent(Arena arena, Team winningTeam) {
        super(arena);
        this.winningTeam = winningTeam;
    }
}