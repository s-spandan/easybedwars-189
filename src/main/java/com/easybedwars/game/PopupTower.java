package com.easybedwars.game;

import com.cryptomorin.xseries.XMaterial;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PopupTower {

    private final Location baseLocation;
    private final XMaterial material;
    private final int height;
    private final List<Block> placedBlocks;

    public PopupTower(Location baseLocation, XMaterial material, int height) {
        this.baseLocation = baseLocation;
        this.material = material;
        this.height = height;
        this.placedBlocks = new ArrayList<>();
    }

    public boolean build() {
        Location loc = baseLocation.clone();
        Material mat = material.parseMaterial();
        
        if (mat == null) {
            return false;
        }

        if (!isValidLocation()) {
            return false;
        }

        for (int y = 0; y < height; y++) {
            Block base = loc.clone().add(0, y, 0).getBlock();
            if (base.getType() != Material.AIR && base.getType() != XMaterial.WATER.parseMaterial() 
                    && base.getType() != XMaterial.LAVA.parseMaterial()) {
                continue;
            }

            base.setType(mat);
            placedBlocks.add(base);

            if (y > 0) {
                for (int x = -1; x <= 1; x++) {
                    for (int z = -1; z <= 1; z++) {
                        if (x == 0 && z == 0) continue;
                        
                        Block side = loc.clone().add(x, y, z).getBlock();
                        if (side.getType() == Material.AIR || side.getType() == XMaterial.WATER.parseMaterial() 
                                || side.getType() == XMaterial.LAVA.parseMaterial()) {
                            Material ladderMat = XMaterial.LADDER.parseMaterial();
                            if (ladderMat != null) {
                                side.setType(ladderMat);
                                placedBlocks.add(side);
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    private boolean isValidLocation() {
        Location loc = baseLocation.clone();
        
        for (int y = 0; y < height; y++) {
            Block block = loc.clone().add(0, y, 0).getBlock();
            if (block.getType() != Material.AIR && block.getType() != XMaterial.WATER.parseMaterial()
                    && block.getType() != XMaterial.LAVA.parseMaterial()) {
                if (y == 0) {
                    return true;
                }
            }
        }
        
        return true;
    }

    public void remove() {
        for (Block block : placedBlocks) {
            block.setType(Material.AIR);
        }
        placedBlocks.clear();
    }

    public static PopupTower createTower(Location location, XMaterial material, TowerType type) {
        return new PopupTower(location, material, type.getHeight());
    }

    public enum TowerType {
        SMALL(5),
        MEDIUM(10),
        LARGE(15);

        private final int height;

        TowerType(int height) {
            this.height = height;
        }

        public int getHeight() {
            return height;
        }
    }
}