package com.easybedwars.commands;

import com.easybedwars.EasyBedWars;
import com.easybedwars.game.Arena;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShoutCommand implements CommandExecutor {

    private final EasyBedWars plugin;

    public ShoutCommand(EasyBedWars plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;
        Arena arena = plugin.getArenaManager().getPlayerArena(player);

        if (arena == null || !arena.isPlaying()) {
            player.sendMessage(plugin.getLanguageManager().getMessage("game.not-in-game"));
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("&cUsage: /shout <message>");
            return true;
        }

        String message = String.join(" ", args);
        String shoutMessage = plugin.getConfig().getString("chat.global-chat-prefix") + player.getName() + ": " + message;
        
        arena.broadcast(shoutMessage);

        return true;
    }
}