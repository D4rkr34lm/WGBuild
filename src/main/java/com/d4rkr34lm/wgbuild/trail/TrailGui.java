package com.d4rkr34lm.wgbuild.trail;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TrailGui {

    private Inventory gui;
    private static String inventoryName = "§6Trail Settings";

    public TrailGui(){

        gui = Bukkit.createInventory(null, 9*3, inventoryName);

        ItemStack none = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta imNone = none.getItemMeta();
        imNone.setDisplayName(" ");
        none.setItemMeta(imNone);

        ItemStack newTrail = new ItemStack(Material.MAP);
        ItemMeta imNewTrail = newTrail.getItemMeta();
        imNewTrail.setDisplayName("§aNew Trail");
        newTrail.setItemMeta(imNewTrail);

        ItemStack hideTrail = new ItemStack(Material.OAK_LEAVES);
        ItemMeta imHideTrail = hideTrail.getItemMeta();
        imHideTrail.setDisplayName("§dHide Trail");
        hideTrail.setItemMeta(imHideTrail);

        ItemStack explosion = new ItemStack(Material.REDSTONE_BLOCK);
        ItemMeta imExplosion = explosion.getItemMeta();
        imExplosion.setDisplayName("§cExplosion Only");
        explosion.setItemMeta(imExplosion);

        ItemStack travel = new ItemStack(Material.BLUE_ICE);
        ItemMeta imTravel = travel.getItemMeta();
        imTravel.setDisplayName("§9Travel Only");
        travel.setItemMeta(imTravel);

        ItemStack normal = new ItemStack(Material.SPONGE);
        ItemMeta imNormal = normal.getItemMeta();
        imNormal.setDisplayName("§eNormal Mode");
        normal.setItemMeta(imNormal);

        for(int i = 0; i < gui.getSize(); i++){
            gui.setItem(i, none);
        }

        gui.setItem(9 + 2, newTrail);
        gui.setItem(9 + 3, hideTrail);
        gui.setItem(9 + 4, explosion);
        gui.setItem(9 + 5, travel);
        gui.setItem(9 + 6, normal);

    }

    public Inventory getGui(){
        return gui;
    }

    public static String getInventoryName(){
        return inventoryName;
    }

}
