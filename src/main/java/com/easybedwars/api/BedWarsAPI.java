package com.easybedwars.api;

import com.easybedwars.EasyBedWars;
import com.easybedwars.api.arena.IArena;
import com.easybedwars.api.arena.ITeam;
import com.easybedwars.api.arena.IGamePlayer;
import com.easybedwars.game.Arena;
import com.easybedwars.game.Team;
import com.easybedwars.data.GamePlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class BedWarsAPI {

    private static EasyBedWars plugin;

    public static void setPlugin(EasyBedWars plugin) {
        BedWarsAPI.plugin = plugin;
    }

    public static EasyBedWars getPlugin() {
        return plugin;
    }

    public static IArena getArena(String name) {
        Arena arena = plugin.getArenaManager().getArena(name);
        return arena != null ? new ArenaWrapper(arena) : null;
    }

    public static IArena getPlayerArena(Player player) {
        Arena arena = plugin.getArenaManager().getPlayerArena(player);
        return arena != null ? new ArenaWrapper(arena) : null;
    }

    public static List<IArena> getArenas() {
        return plugin.getArenaManager().getArenas().values().stream()
                .map(ArenaWrapper::new)
                .collect(Collectors.toList());
    }

    public static IGamePlayer getGamePlayer(Player player) {
        Arena arena = plugin.getArenaManager().getPlayerArena(player);
        if (arena != null) {
            GamePlayer gp = arena.getGamePlayer(player);
            return gp != null ? new GamePlayerWrapper(gp) : null;
        }
        return null;
    }

    public static ITeam getPlayerTeam(Player player) {
        Arena arena = plugin.getArenaManager().getPlayerArena(player);
        if (arena != null) {
            Team team = arena.getPlayerTeam(player);
            return team != null ? new TeamWrapper(team) : null;
        }
        return null;
    }

    private static class ArenaWrapper implements IArena {
        private final Arena arena;

        ArenaWrapper(Arena arena) {
            this.arena = arena;
        }

        @Override
        public String getName() {
            return arena.getName();
        }

        @Override
        public String getDisplayName() {
            return arena.getDisplayName();
        }

        @Override
        public String getState() {
            return arena.getState().name();
        }

        @Override
        public List<Player> getPlayers() {
            return arena.getPlayers().stream()
                    .map(GamePlayer::getPlayer)
                    .collect(Collectors.toList());
        }

        @Override
        public int getMaxPlayers() {
            return arena.getMaxPlayers();
        }

        @Override
        public boolean isPlaying() {
            return arena.isPlaying();
        }
    }

    private static class GamePlayerWrapper implements IGamePlayer {
        private final GamePlayer gamePlayer;

        GamePlayerWrapper(GamePlayer gamePlayer) {
            this.gamePlayer = gamePlayer;
        }

        @Override
        public Player getPlayer() {
            return gamePlayer.getPlayer();
        }

        @Override
        public boolean isAlive() {
            return gamePlayer.isAlive();
        }

        @Override
        public boolean isSpectator() {
            return gamePlayer.isSpectator();
        }

        @Override
        public int getKills() {
            return gamePlayer.getKills();
        }

        @Override
        public int getFinalKills() {
            return gamePlayer.getFinalKills();
        }

        @Override
        public int getDeaths() {
            return gamePlayer.getDeaths();
        }

        @Override
        public int getBedsBroken() {
            return gamePlayer.getBedsBroken();
        }
    }

    private static class TeamWrapper implements ITeam {
        private final Team team;

        TeamWrapper(Team team) {
            this.team = team;
        }

        @Override
        public String getName() {
            return team.getName();
        }

        @Override
        public String getDisplayName() {
            return team.getDisplayName();
        }

        @Override
        public List<Player> getPlayers() {
            return team.getPlayers();
        }

        @Override
        public boolean isBedAlive() {
            return team.isBedAlive();
        }

        @Override
        public boolean isAlive() {
            return team.isAlive();
        }
    }
}