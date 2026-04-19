package com.easybedwars.managers;

import com.cryptomorin.xseries.XMaterial;
import com.easybedwars.EasyBedWars;
import com.easybedwars.shop.ShopCategory;
import com.easybedwars.shop.ShopItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopManager {

    private final EasyBedWars plugin;
    private final Map<String, ShopCategory> categories;

    public ShopManager(EasyBedWars plugin) {
        this.plugin = plugin;
        this.categories = new HashMap<>();
        loadShop();
    }

    private void loadShop() {
        ShopCategory blocks = new ShopCategory("Blocks", XMaterial.TERRACOTTA);
        blocks.addItem(new ShopItem(XMaterial.WHITE_WOOL, "Wool", 16, XMaterial.IRON_INGOT, 4));
        blocks.addItem(new ShopItem(XMaterial.TERRACOTTA, "Terracotta", 16, XMaterial.IRON_INGOT, 12));
        blocks.addItem(new ShopItem(XMaterial.GLASS, "Blast-Proof Glass", 4, XMaterial.IRON_INGOT, 12));
        blocks.addItem(new ShopItem(XMaterial.END_STONE, "End Stone", 12, XMaterial.IRON_INGOT, 24));
        blocks.addItem(new ShopItem(XMaterial.LADDER, "Ladder", 16, XMaterial.IRON_INGOT, 4));
        blocks.addItem(new ShopItem(XMaterial.OAK_PLANKS, "Wood", 16, XMaterial.GOLD_INGOT, 4));
        blocks.addItem(new ShopItem(XMaterial.OBSIDIAN, "Obsidian", 4, XMaterial.EMERALD, 4));
        categories.put("BLOCKS", blocks);

        ShopCategory weapons = new ShopCategory("Weapons", XMaterial.GOLDEN_SWORD);
        weapons.addItem(new ShopItem(XMaterial.STONE_SWORD, "Stone Sword", 1, XMaterial.IRON_INGOT, 10));
        weapons.addItem(new ShopItem(XMaterial.IRON_SWORD, "Iron Sword", 1, XMaterial.GOLD_INGOT, 7));
        weapons.addItem(new ShopItem(XMaterial.DIAMOND_SWORD, "Diamond Sword", 1, XMaterial.EMERALD, 4));
        weapons.addItem(new ShopItem(XMaterial.STICK, "Knockback Stick", 1, XMaterial.GOLD_INGOT, 5));
        categories.put("WEAPONS", weapons);

        ShopCategory armor = new ShopCategory("Armor", XMaterial.CHAINMAIL_BOOTS);
        armor.addItem(new ShopItem(XMaterial.CHAINMAIL_BOOTS, "Chainmail Armor", 1, XMaterial.IRON_INGOT, 40));
        armor.addItem(new ShopItem(XMaterial.IRON_BOOTS, "Iron Armor", 1, XMaterial.GOLD_INGOT, 12));
        armor.addItem(new ShopItem(XMaterial.DIAMOND_BOOTS, "Diamond Armor", 1, XMaterial.EMERALD, 6));
        categories.put("ARMOR", armor);

        ShopCategory tools = new ShopCategory("Tools", XMaterial.STONE_PICKAXE);
        tools.addItem(new ShopItem(XMaterial.WOODEN_PICKAXE, "Wooden Pickaxe", 1, XMaterial.IRON_INGOT, 10));
        tools.addItem(new ShopItem(XMaterial.IRON_PICKAXE, "Iron Pickaxe", 1, XMaterial.GOLD_INGOT, 10));
        tools.addItem(new ShopItem(XMaterial.DIAMOND_PICKAXE, "Diamond Pickaxe", 1, XMaterial.GOLD_INGOT, 6));
        tools.addItem(new ShopItem(XMaterial.WOODEN_AXE, "Wooden Axe", 1, XMaterial.IRON_INGOT, 10));
        tools.addItem(new ShopItem(XMaterial.STONE_AXE, "Stone Axe", 1, XMaterial.IRON_INGOT, 10));
        tools.addItem(new ShopItem(XMaterial.SHEARS, "Shears", 1, XMaterial.IRON_INGOT, 20));
        categories.put("TOOLS", tools);

        ShopCategory ranged = new ShopCategory("Ranged", XMaterial.BOW);
        ranged.addItem(new ShopItem(XMaterial.BOW, "Bow", 1, XMaterial.GOLD_INGOT, 12));
        ranged.addItem(new ShopItem(XMaterial.ARROW, "Arrow", 8, XMaterial.GOLD_INGOT, 2));
        categories.put("RANGED", ranged);

        ShopCategory potions = new ShopCategory("Potions", XMaterial.POTION);
        potions.addItem(new ShopItem(XMaterial.POTION, "Speed Potion", 1, XMaterial.EMERALD, 1));
        potions.addItem(new ShopItem(XMaterial.POTION, "Jump Potion", 1, XMaterial.EMERALD, 1));
        potions.addItem(new ShopItem(XMaterial.POTION, "Invisibility Potion", 1, XMaterial.EMERALD, 2));
        categories.put("POTIONS", potions);

        ShopCategory special = new ShopCategory("Special", XMaterial.TNT);
        special.addItem(new ShopItem(XMaterial.TNT, "TNT", 1, XMaterial.GOLD_INGOT, 4));
        special.addItem(new ShopItem(XMaterial.FIRE_CHARGE, "Fireball", 1, XMaterial.IRON_INGOT, 40));
        special.addItem(new ShopItem(XMaterial.ENDER_PEARL, "Ender Pearl", 1, XMaterial.EMERALD, 4));
        special.addItem(new ShopItem(XMaterial.WATER_BUCKET, "Water Bucket", 1, XMaterial.GOLD_INGOT, 3));
        special.addItem(new ShopItem(XMaterial.GOLDEN_APPLE, "Golden Apple", 1, XMaterial.GOLD_INGOT, 3));
        special.addItem(new ShopItem(XMaterial.COMPASS, "Tracker", 1, XMaterial.EMERALD, 2));
        special.addItem(new ShopItem(XMaterial.CHEST, "Popup Tower", 1, XMaterial.IRON_INGOT, 24));
        categories.put("SPECIAL", special);
    }

    public ShopCategory getCategory(String name) {
        return categories.get(name);
    }

    public Map<String, ShopCategory> getCategories() {
        return categories;
    }
}