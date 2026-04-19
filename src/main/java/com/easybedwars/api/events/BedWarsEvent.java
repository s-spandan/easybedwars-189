package com.easybedwars.api.events;

import com.easybedwars.game.Arena;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public abstract class BedWarsEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Arena arena;

    public BedWarsEvent(Arena arena) {
        this.arena = arena;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}