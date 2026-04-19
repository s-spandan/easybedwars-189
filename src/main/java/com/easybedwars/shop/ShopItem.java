package com.easybedwars.shop;

import com.cryptomorin.xseries.XMaterial;
import lombok.Getter;

@Getter
public class ShopItem {

    private final XMaterial material;
    private final String name;
    private final int amount;
    private final XMaterial currency;
    private final int price;

    public ShopItem(XMaterial material, String name, int amount, XMaterial currency, int price) {
        this.material = material;
        this.name = name;
        this.amount = amount;
        this.currency = currency;
        this.price = price;
    }
}