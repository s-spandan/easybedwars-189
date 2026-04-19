package com.easybedwars.game;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import com.easybedwars.EasyBedWars;
import com.easybedwars.api.events.GameStartEvent;
import com.easybedwars.api.events.GameEndEvent;
import com.easybedwars.api.events.PlayerJoinArenaEvent;
import com.easybedwars.api.events.PlayerLeaveArenaEvent;
import com.easybedwars.data.GamePlayer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class Arena {

    private final EasyBedWars plugin;
    private final String name;
    private String displayName;
    private String worldName;
    private Location lobbySpawn;
    private Location spectatorSpawn;
    private int minPlayers;
    private int maxPlayers;
    private GameState state;
    private Map<String, Team> teams;
    private List<GamePlayer> players;
    private List<Generator> generators;
    private int countdown;
    private int gameTime;
    private BukkitTask gameTask;

    public Arena(EasyBedWars plugin, String name) {
        this.plugin = plugin;
        this.name = name;
        this.displayName = ChatColor.translateAlternateColorCodes('&', name);
        this.state = GameState.WAITING;
        this.teams = new LinkedHashMap<>();
        this.players = new ArrayList<>();
        this.generators = new ArrayList<>();
        this.minPlayers = plugin.getConfigManager().getMinPlayers();
        this.maxPlayers = 16;
        initializeTeams();
    }

    private void initializeTeams() {
        teams.put("RED", new Team("Red", ChatColor.RED, XMaterial.RED_WOOL, XMaterial.RED_STAINED_GLASS));
        teams.put("BLUE", new Team("Blue", ChatColor.BLUE, XMaterial.BLUE_WOOL, XMaterial.BLUE_STAINED_GLASS));
        teams.put("GREEN", new Team("Green", ChatColor.GREEN, XMaterial.GREEN_WOOL, XMaterial.GREEN_STAINED_GLASS));
        teams.put("YELLOW", new Team("Yellow", ChatColor.YELLOW, XMaterial.YELLOW_WOOL, XMaterial.YELLOW_STAINED_GLASS));
        teams.put("AQUA", new Team("Aqua", ChatColor.AQUA, XMaterial.CYAN_WOOL, XMaterial.CYAN_STAINED_GLASS));
        teams.put("WHITE", new Team("White", ChatColor.WHITE, XMaterial.WHITE_WOOL, XMaterial.WHITE_STAINED_GLASS));
        teams.put("PINK", new Team("Pink", ChatColor.LIGHT_PURPLE, XMaterial.PINK_WOOL, XMaterial.PINK_STAINED_GLASS));
        teams.put("GRAY", new Team("Gray", ChatColor.GRAY, XMaterial.GRAY_WOOL, XMaterial.GRAY_STAINED_GLASS));
    }

    public boolean joinPlayer(Player player) {
        if (state != GameState.WAITING && state != GameState.STARTING) {
            return false;
        }

        if (players.size() >= maxPlayers) {
            return false;
        }

        PlayerJoinArenaEvent event = new PlayerJoinArenaEvent(this, player);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return false;
        }

        GamePlayer gamePlayer = new GamePlayer(player, this);
        players.add(gamePlayer);

        Team smallestTeam = getSmallestTeam();
        if (smallestTeam != null) {
            smallestTeam.addPlayer(player);
            gamePlayer.setTeam(smallestTeam);
        }

        gamePlayer.saveInventory();
        player.getInventory().clear();
        player.teleport(lobbySpawn);

        broadcast(plugin.getLanguageManager().getMessage("game.join",
                "player", player.getName(),
                "current", String.valueOf(players.size()),
                "max", String.valueOf(maxPlayers)));

        if (players.size() >= minPlayers && state == GameState.WAITING) {
            startCountdown();
        }

        return true;
    }

    public void leavePlayer(Player player) {
        GamePlayer gamePlayer = getGamePlayer(player);
        if (gamePlayer == null) return;

        PlayerLeaveArenaEvent event = new PlayerLeaveArenaEvent(this, player);
        Bukkit.getPluginManager().callEvent(event);

        players.remove(gamePlayer);

        if (gamePlayer.getTeam() != null) {
            gamePlayer.getTeam().removePlayer(player);
        }

        gamePlayer.restoreInventory();

        broadcast(plugin.getLanguageManager().getMessage("game.leave", "player", player.getName()));

        if (state == GameState.STARTING && players.size() < minPlayers) {
            cancelCountdown();
        }

        if (state == GameState.PLAYING) {
            checkWinCondition();
        }
    }

    private void startCountdown() {
        state = GameState.STARTING;
        countdown = plugin.getConfigManager().getCountdownTime();

        gameTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (countdown <= 0) {
                startGame();
                return;
            }

            if (countdown <= 10 || countdown % 10 == 0) {
                broadcast(plugin.getLanguageManager().getMessage("game.countdown", "time", String.valueOf(countdown)));
                playSound(XSound.BLOCK_NOTE_BLOCK_PLING);
            }

            countdown--;
        }, 0L, 20L);
    }

    private void cancelCountdown() {
        if (gameTask != null) {
            gameTask.cancel();
            gameTask = null;
        }
        state = GameState.WAITING;
    }

    private void startGame() {
        if (gameTask != null) {
            gameTask.cancel();
        }

        state = GameState.PLAYING;
        gameTime = 0;

        GameStartEvent event = new GameStartEvent(this);
        Bukkit.getPluginManager().callEvent(event);

        for (GamePlayer gp : players) {
            Player player = gp.getPlayer();
            player.getInventory().clear();

            if (gp.getTeam() != null && gp.getTeam().getSpawn() != null) {
                player.teleport(gp.getTeam().getSpawn());
                giveStartingItems(player, gp.getTeam());
            }
        }

        broadcast(plugin.getLanguageManager().getMessage("game.start"));
        playSound(XSound.ENTITY_ENDER_DRAGON_GROWL);

        startGameLoop();
    }

    private void startGameLoop() {
        gameTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            gameTime++;

            for (Generator gen : generators) {
                gen.spawn();
            }

            if (gameTime == plugin.getConfigManager().getGeneratorUpgradeDiamond()) {
                upgradeGenerators(Generator.GeneratorType.DIAMOND);
            }

            if (gameTime == plugin.getConfigManager().getGeneratorUpgradeEmerald()) {
                upgradeGenerators(Generator.GeneratorType.EMERALD);
            }

            if (gameTime >= plugin.getConfigManager().getGameTimeLimit()) {
                endGame(null);
            }

        }, 0L, 20L);
    }

    private void upgradeGenerators(Generator.GeneratorType type) {
        generators.stream()
                .filter(gen -> gen.getType() == type)
                .forEach(Generator::upgradeTier);

        int tier = generators.stream()
                .filter(gen -> gen.getType() == type)
                .findFirst()
                .map(Generator::getTier)
                .orElse(1);

        broadcast(plugin.getLanguageManager().getMessage("generator.tier-upgrade",
                "resource", type.name(),
                "tier", String.valueOf(tier)));
    }

    public void endGame(Team winningTeam) {
        if (state != GameState.PLAYING) return;

        state = GameState.ENDING;

        GameEndEvent event = new GameEndEvent(this, winningTeam);
        Bukkit.getPluginManager().callEvent(event);

        if (gameTask != null) {
            gameTask.cancel();
            gameTask = null;
        }

        if (winningTeam != null) {
            broadcast(ChatColor.GOLD + "" + ChatColor.BOLD + winningTeam.getDisplayName() + " TEAM WON!");
        } else {
            broadcast(plugin.getLanguageManager().getMessage("game.end"));
        }

        for (GamePlayer gp : players) {
            Player player = gp.getPlayer();

            plugin.getStatsManager().updateStats(player, gp, winningTeam);

            gp.restoreInventory();
            player.teleport(lobbySpawn);
        }

        Bukkit.getScheduler().runTaskLater(plugin, this::restart, 100L);
    }

    private void restart() {
        players.clear();
        teams.values().forEach(team -> {
            team.getPlayers().clear();
            team.setBedAlive(true);
            team.setSharpnessLevel(0);
            team.setProtectionLevel(0);
            team.setHasteEnabled(false);
            team.setHealPoolEnabled(false);
            team.getTraps().clear();
        });
        generators.forEach(gen -> gen.setTier(1));
        gameTime = 0;
        state = GameState.WAITING;
    }

    public void checkWinCondition() {
        List<Team> aliveTeams = teams.values().stream()
                .filter(Team::isAlive)
                .collect(Collectors.toList());

        if (aliveTeams.size() == 1) {
            endGame(aliveTeams.get(0));
        } else if (aliveTeams.isEmpty()) {
            endGame(null);
        }
    }

    private Team getSmallestTeam() {
        return teams.values().stream()
                .filter(team -> team.getSpawn() != null)
                .min(Comparator.comparingInt(team -> team.getPlayers().size()))
                .orElse(null);
    }

    public GamePlayer getGamePlayer(Player player) {
        return players.stream()
                .filter(gp -> gp.getUuid().equals(player.getUniqueId()))
                .findFirst()
                .orElse(null);
    }

    public Team getPlayerTeam(Player player) {
        return teams.values().stream()
                .filter(team -> team.hasPlayer(player))
                .findFirst()
                .orElse(null);
    }

    public void broadcast(String message) {
        players.forEach(gp -> gp.getPlayer().sendMessage(message));
    }

    public void playSound(XSound sound) {
        players.forEach(gp -> {
            Player p = gp.getPlayer();
            sound.play(p.getLocation(), 1.0f, 1.0f);
        });
    }

    private void giveStartingItems(Player player, Team team) {
        ItemStack sword = XMaterial.matchXMaterial("WOODEN_SWORD").map(XMaterial::parseItem).orElse(null);
        if (sword != null) {
            player.getInventory().addItem(sword);
        }
    }

    public boolean isPlaying() {
        return state == GameState.PLAYING;
    }

    public boolean canJoin() {
        return (state == GameState.WAITING || state == GameState.STARTING) && players.size() < maxPlayers;
    }
}