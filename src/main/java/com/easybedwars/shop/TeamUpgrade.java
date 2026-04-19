package com.easybedwars.shop;

import com.cryptomorin.xseries.XMaterial;
import lombok.Getter;

@Getter
public class TeamUpgrade {

    private final String type;
    private final String name;
    private final XMaterial icon;
    private final XMaterial currency;
    private final int price;

    public TeamUpgrade(String type, String name, XMaterial icon, XMaterial currency, int price) {
        this.type = type;
        this.name = name;
        this.icon = icon;
        this.currency = currency;
        this.price = price;
    }
}