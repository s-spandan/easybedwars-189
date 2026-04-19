package com.easybedwars.managers;

import com.easybedwars.EasyBedWars;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class LanguageManager {

    private final EasyBedWars plugin;
    private final Map<String, String> messages;

    public LanguageManager(EasyBedWars plugin) {
        this.plugin = plugin;
        this.messages = new HashMap<>();
        loadLanguage();
    }

    private void loadLanguage() {
        File langFile = new File(plugin.getDataFolder(), "messages.yml");
        if (!langFile.exists()) {
            plugin.saveResource("messages.yml", false);
        }

        FileConfiguration lang = YamlConfiguration.loadConfiguration(langFile);
        for (String key : lang.getKeys(false)) {
            messages.put(key, lang.getString(key));
        }
    }

    public String getMessage(String key) {
        return ChatColor.translateAlternateColorCodes('&', messages.getOrDefault(key, key));
    }

    public String getMessage(String key, String... replacements) {
        String message = getMessage(key);
        for (int i = 0; i < replacements.length; i += 2) {
            if (i + 1 < replacements.length) {
                message = message.replace("{" + replacements[i] + "}", replacements[i + 1]);
            }
        }
        return message;
    }
}