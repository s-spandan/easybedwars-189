package com.easybedwars.api.events;

import com.easybedwars.game.Arena;
import com.easybedwars.game.Generator;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
@Setter
public class ResourceSpawnEvent extends BedWarsEvent implements Cancellable {

    private final Generator generator;
    private final ItemStack item;
    private final List<Player> receivers;
    private boolean cancelled;

    public ResourceSpawnEvent(Arena arena, Generator generator, ItemStack item, List<Player> receivers) {
        super(arena);
        this.generator = generator;
        this.item = item;
        this.receivers = receivers;
        this.cancelled = false;
    }
}