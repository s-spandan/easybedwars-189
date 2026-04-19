package com.easybedwars.api.events;

import com.easybedwars.game.Arena;

public class GameStartEvent extends BedWarsEvent {

    public GameStartEvent(Arena arena) {
        super(arena);
    }
}