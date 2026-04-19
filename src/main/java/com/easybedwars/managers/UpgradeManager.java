package com.easybedwars.managers;

import com.cryptomorin.xseries.XMaterial;
import com.easybedwars.EasyBedWars;
import com.easybedwars.shop.TeamUpgrade;

import java.util.ArrayList;
import java.util.List;

public class UpgradeManager {

    private final EasyBedWars plugin;
    private final List<TeamUpgrade> upgrades;

    public UpgradeManager(EasyBedWars plugin) {
        this.plugin = plugin;
        this.upgrades = new ArrayList<>();
        loadUpgrades();
    }

    private void loadUpgrades() {
        upgrades.add(new TeamUpgrade("SHARPNESS", "Sharpened Swords", XMaterial.IRON_SWORD, XMaterial.DIAMOND, 4));
        upgrades.add(new TeamUpgrade("PROTECTION", "Reinforced Armor I", XMaterial.IRON_CHESTPLATE, XMaterial.DIAMOND, 2));
        upgrades.add(new TeamUpgrade("PROTECTION", "Reinforced Armor II", XMaterial.IRON_CHESTPLATE, XMaterial.DIAMOND, 4));
        upgrades.add(new TeamUpgrade("PROTECTION", "Reinforced Armor III", XMaterial.IRON_CHESTPLATE, XMaterial.DIAMOND, 8));
        upgrades.add(new TeamUpgrade("PROTECTION", "Reinforced Armor IV", XMaterial.IRON_CHESTPLATE, XMaterial.DIAMOND, 16));
        upgrades.add(new TeamUpgrade("HASTE", "Maniac Miner I", XMaterial.GOLDEN_PICKAXE, XMaterial.DIAMOND, 2));
        upgrades.add(new TeamUpgrade("HASTE", "Maniac Miner II", XMaterial.GOLDEN_PICKAXE, XMaterial.DIAMOND, 4));
        upgrades.add(new TeamUpgrade("FORGE", "Iron Forge", XMaterial.FURNACE, XMaterial.DIAMOND, 4));
        upgrades.add(new TeamUpgrade("FORGE", "Gold Forge", XMaterial.FURNACE, XMaterial.DIAMOND, 8));
        upgrades.add(new TeamUpgrade("FORGE", "Emerald Forge", XMaterial.FURNACE, XMaterial.DIAMOND, 12));
        upgrades.add(new TeamUpgrade("HEAL_POOL", "Heal Pool", XMaterial.BEACON, XMaterial.DIAMOND, 3));
        upgrades.add(new TeamUpgrade("TRAP", "It's a trap!", XMaterial.TRIPWIRE_HOOK, XMaterial.DIAMOND, 1));
    }

    public List<TeamUpgrade> getUpgrades() {
        return upgrades;
    }
}