package com.easybedwars.managers;

import com.easybedwars.EasyBedWars;
import com.easybedwars.data.PlayerData;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class DatabaseManager {

    private final EasyBedWars plugin;
    private HikariDataSource dataSource;

    public DatabaseManager(EasyBedWars plugin) {
        this.plugin = plugin;
    }

    public void connect() {
        HikariConfig config = new HikariConfig();
        String dbType = plugin.getConfigManager().getDatabaseType();

        if (dbType.equalsIgnoreCase("MYSQL")) {
            config.setJdbcUrl("jdbc:mysql://" + 
                plugin.getConfig().getString("database.mysql.host") + ":" +
                plugin.getConfig().getInt("database.mysql.port") + "/" +
                plugin.getConfig().getString("database.mysql.database"));
            config.setUsername(plugin.getConfig().getString("database.mysql.username"));
            config.setPassword(plugin.getConfig().getString("database.mysql.password"));
            config.setMaximumPoolSize(plugin.getConfig().getInt("database.mysql.pool-size", 10));
        } else {
            File dbFile = new File(plugin.getDataFolder(), "bedwars.db");
            config.setJdbcUrl("jdbc:sqlite:" + dbFile.getAbsolutePath());
            config.setMaximumPoolSize(1);
        }

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        this.dataSource = new HikariDataSource(config);
    }

    public void disconnect() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void createTables() {
        String playerDataTable = "CREATE TABLE IF NOT EXISTS player_data (" +
                "uuid VARCHAR(36) PRIMARY KEY," +
                "username VARCHAR(16) NOT NULL," +
                "kills INT DEFAULT 0," +
                "deaths INT DEFAULT 0," +
                "final_kills INT DEFAULT 0," +
                "final_deaths INT DEFAULT 0," +
                "wins INT DEFAULT 0," +
                "losses INT DEFAULT 0," +
                "beds_broken INT DEFAULT 0," +
                "games_played INT DEFAULT 0," +
                "level INT DEFAULT 1," +
                "experience INT DEFAULT 0," +
                "coins INT DEFAULT 0," +
                "selected_kill_message VARCHAR(50)," +
                "selected_death_cry VARCHAR(50)," +
                "selected_victory_dance VARCHAR(50)," +
                "quick_buy TEXT," +
                "first_join BIGINT," +
                "last_join BIGINT" +
                ")";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(playerDataTable)) {
            stmt.execute();
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to create tables: " + e.getMessage());
        }
    }

    public PlayerData loadPlayerData(UUID uuid) {
        String query = "SELECT * FROM player_data WHERE uuid = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, uuid.toString());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new PlayerData(
                        uuid,
                        rs.getString("username"),
                        rs.getInt("kills"),
                        rs.getInt("deaths"),
                        rs.getInt("final_kills"),
                        rs.getInt("final_deaths"),
                        rs.getInt("wins"),
                        rs.getInt("losses"),
                        rs.getInt("beds_broken"),
                        rs.getInt("games_played"),
                        rs.getInt("level"),
                        rs.getInt("experience"),
                        rs.getInt("coins")
                );
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to load player data: " + e.getMessage());
        }

        return new PlayerData(uuid);
    }

    public void savePlayerData(PlayerData data) {
        String query = "INSERT INTO player_data (uuid, username, kills, deaths, final_kills, final_deaths, " +
                "wins, losses, beds_broken, games_played, level, experience, coins, last_join) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "username = VALUES(username), kills = VALUES(kills), deaths = VALUES(deaths), " +
                "final_kills = VALUES(final_kills), final_deaths = VALUES(final_deaths), " +
                "wins = VALUES(wins), losses = VALUES(losses), beds_broken = VALUES(beds_broken), " +
                "games_played = VALUES(games_played), level = VALUES(level), experience = VALUES(experience), " +
                "coins = VALUES(coins), last_join = VALUES(last_join)";

        String sqliteQuery = "INSERT OR REPLACE INTO player_data (uuid, username, kills, deaths, final_kills, " +
                "final_deaths, wins, losses, beds_broken, games_played, level, experience, coins, last_join) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        boolean isMySQL = plugin.getConfigManager().getDatabaseType().equalsIgnoreCase("MYSQL");

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(isMySQL ? query : sqliteQuery)) {
            stmt.setString(1, data.getUuid().toString());
            stmt.setString(2, data.getUsername());
            stmt.setInt(3, data.getKills());
            stmt.setInt(4, data.getDeaths());
            stmt.setInt(5, data.getFinalKills());
            stmt.setInt(6, data.getFinalDeaths());
            stmt.setInt(7, data.getWins());
            stmt.setInt(8, data.getLosses());
            stmt.setInt(9, data.getBedsBroken());
            stmt.setInt(10, data.getGamesPlayed());
            stmt.setInt(11, data.getLevel());
            stmt.setInt(12, data.getExperience());
            stmt.setInt(13, data.getCoins());
            stmt.setLong(14, System.currentTimeMillis());
            stmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to save player data: " + e.getMessage());
        }
    }
}