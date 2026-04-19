package com.easybedwars.api.events;

import com.easybedwars.game.Arena;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

@Getter
@Setter
public class ShopOpenEvent extends BedWarsEvent implements Cancellable {

    private final Player player;
    private final String shopType;
    private boolean cancelled;

    public ShopOpenEvent(Arena arena, Player player, String shopType) {
        super(arena);
        this.player = player;
        this.shopType = shopType;
        this.cancelled = false;
    }
}