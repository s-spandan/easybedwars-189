package com.easybedwars;

import com.easybedwars.commands.BedWarsCommand;
import com.easybedwars.commands.ShoutCommand;
import com.easybedwars.listeners.*;
import com.easybedwars.managers.*;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class EasyBedWars extends JavaPlugin {

    private ConfigManager configManager;
    private LanguageManager languageManager;
    private DatabaseManager databaseManager;
    private ArenaManager arenaManager;
    private PlayerManager playerManager;
    private StatsManager statsManager;
    private ShopManager shopManager;
    private UpgradeManager upgradeManager;
    private GeneratorManager generatorManager;
    private CosmeticManager cosmeticManager;
    private ScoreboardManager scoreboardManager;
    private SetupManager setupManager;
    private TrackerCompass trackerCompass;

    @Getter
    private static EasyBedWars instance;

    @Override
    public void onEnable() {
        instance = this;
        long startTime = System.currentTimeMillis();
        
        getLogger().info("Initializing EasyBedWars...");
        
        com.easybedwars.api.BedWarsAPI.setPlugin(this);
        
        this.configManager = new ConfigManager(this);
        this.languageManager = new LanguageManager(this);
        this.databaseManager = new DatabaseManager(this);
        this.shopManager = new ShopManager(this);
        this.upgradeManager = new UpgradeManager(this);
        this.generatorManager = new GeneratorManager(this);
        this.cosmeticManager = new CosmeticManager(this);
        this.statsManager = new StatsManager(this);
        this.playerManager = new PlayerManager(this);
        this.arenaManager = new ArenaManager(this);
        this.scoreboardManager = new ScoreboardManager(this);
        this.setupManager = new SetupManager(this);
        this.trackerCompass = new TrackerCompass(this);
        
        databaseManager.connect();
        databaseManager.createTables();
        
        registerCommands();
        registerListeners();
        
        arenaManager.loadArenas();
        
        long loadTime = System.currentTimeMillis() - startTime;
        getLogger().info("EasyBedWars enabled in " + loadTime + "ms");
    }

    @Override
    public void onDisable() {
        if (arenaManager != null) {
            arenaManager.stopAllGames();
        }
        
        if (playerManager != null) {
            playerManager.saveAllPlayers();
        }
        
        if (databaseManager != null) {
            databaseManager.disconnect();
        }
        
        getLogger().info("EasyBedWars disabled");
    }

    private void registerCommands() {
        getCommand("bw").setExecutor(new BedWarsCommand(this));
        getCommand("shout").setExecutor(new ShoutCommand(this));
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new JoinQuitListener(this), this);
        getServer().getPluginManager().registerEvents(new GameListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockListener(this), this);
        getServer().getPluginManager().registerEvents(new InteractListener(this), this);
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(this), this);
        getServer().getPluginManager().registerEvents(new DamageListener(this), this);
    }
}
