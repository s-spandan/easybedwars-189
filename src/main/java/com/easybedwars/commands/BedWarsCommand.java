package com.easybedwars.commands;

import com.easybedwars.EasyBedWars;
import com.easybedwars.data.PlayerData;
import com.easybedwars.game.Arena;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BedWarsCommand implements CommandExecutor {

    private final EasyBedWars plugin;

    public BedWarsCommand(EasyBedWars plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            sendHelp(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "join":
                if (args.length < 2) {
                    player.sendMessage(plugin.getLanguageManager().getMessage("error.usage-join"));
                    return true;
                }
                handleJoin(player, args[1]);
                break;

            case "leave":
                handleLeave(player);
                break;

            case "stats":
                if (args.length == 1) {
                    handleStats(player, player);
                } else {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target == null) {
                        player.sendMessage(plugin.getLanguageManager().getMessage("error.player-not-found"));
                        return true;
                    }
                    handleStats(player, target);
                }
                break;

            case "setup":
                if (!player.hasPermission("bedwars.setup")) {
                    player.sendMessage(plugin.getLanguageManager().getMessage("error.no-permission"));
                    return true;
                }
                handleSetup(player, args);
                break;

            case "reload":
                if (!player.hasPermission("bedwars.admin")) {
                    player.sendMessage(plugin.getLanguageManager().getMessage("error.no-permission"));
                    return true;
                }
                plugin.getConfigManager().reload();
                player.sendMessage("&aConfiguration reloaded!");
                break;

            default:
                sendHelp(player);
                break;
        }

        return true;
    }

    private void handleJoin(Player player, String arenaName) {
        Arena arena = plugin.getArenaManager().getArena(arenaName);
        
        if (arena == null) {
            player.sendMessage(plugin.getLanguageManager().getMessage("error.arena-not-found"));
            return;
        }

        if (plugin.getArenaManager().getPlayerArena(player) != null) {
            player.sendMessage(plugin.getLanguageManager().getMessage("game.already-in-game"));
            return;
        }

        if (!arena.canJoin()) {
            player.sendMessage(plugin.getLanguageManager().getMessage("game.full"));
            return;
        }

        arena.joinPlayer(player);
    }

    private void handleLeave(Player player) {
        Arena arena = plugin.getArenaManager().getPlayerArena(player);
        
        if (arena == null) {
            player.sendMessage(plugin.getLanguageManager().getMessage("game.not-in-game"));
            return;
        }

        arena.leavePlayer(player);
    }

    private void handleStats(Player player, Player target) {
        PlayerData data = plugin.getPlayerManager().getPlayerData(target);
        
        if (data == null) {
            player.sendMessage(plugin.getLanguageManager().getMessage("error.player-not-found"));
            return;
        }

        String stats = plugin.getStatsManager().formatStats(data);
        player.sendMessage(stats);
    }

    private void handleSetup(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("&c/bw setup <create|setspawn|generator|save> [args]");
            return;
        }

        if (args[1].equalsIgnoreCase("create") && args.length >= 3) {
            plugin.getArenaManager().createArena(args[2]);
            player.sendMessage(plugin.getLanguageManager().getMessage("setup.arena-created", "arena", args[2]));
        }
    }

    private void sendHelp(Player player) {
        player.sendMessage("&8&m--------------&8[ &c&lBedWars &8]&m--------------");
        player.sendMessage("&e/bw join <arena> &7- Join a game");
        player.sendMessage("&e/bw leave &7- Leave your current game");
        player.sendMessage("&e/bw stats [player] &7- View statistics");
        
        if (player.hasPermission("bedwars.setup")) {
            player.sendMessage("&e/bw setup &7- Arena setup commands");
        }
        
        if (player.hasPermission("bedwars.admin")) {
            player.sendMessage("&e/bw reload &7- Reload configuration");
        }
        
        player.sendMessage("&8&m------------------------------------");
    }
}