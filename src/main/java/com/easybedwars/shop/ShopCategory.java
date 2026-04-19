package com.easybedwars.shop;

import com.cryptomorin.xseries.XMaterial;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ShopCategory {

    private final String name;
    private final XMaterial icon;
    private final List<ShopItem> items;

    public ShopCategory(String name, XMaterial icon) {
        this.name = name;
        this.icon = icon;
        this.items = new ArrayList<>();
    }

    public void addItem(ShopItem item) {
        items.add(item);
    }
}