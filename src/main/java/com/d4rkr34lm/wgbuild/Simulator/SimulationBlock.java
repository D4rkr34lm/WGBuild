package com.d4rkr34lm.wgbuild.Simulator;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

public class SimulationBlock {

    private Block simBlock;
    private Inventory simInv;
    private HashMap<Integer, Integer> queue = new HashMap<>();
    private boolean activated;

    public SimulationBlock(Block block, boolean activated){
        simBlock = block;
        simInv = Bukkit.createInventory(null, 9 * 6, "§gTnt Prime Simulator");
        this.activated = activated;

        queue.put(1, 1);
        updateInventory(1);

    }

    public Block getBlock(){
        return simBlock;
    }

    public Inventory getSimulationInventory(int page){
        updateInventory(page);
        return simInv;
    }

    public void activate(){
        activated = true;
    }

    public void deactivate(){
        activated = false;
    }

    public boolean isActivated(){
        return activated;
    }

    public void updateInventory(int page){

        ItemMeta im;

        ItemStack increase = new ItemStack(Material.LIME_DYE);
        im = increase.getItemMeta();
        im.setDisplayName("§aIncrease");
        increase.setItemMeta(im);

        ItemStack decrease = new ItemStack(Material.RED_DYE);
        im = decrease.getItemMeta();
        im.setDisplayName("§cDecrease");
        decrease.setItemMeta(im);

        ItemStack tnt = new ItemStack(Material.TNT);
        im = tnt.getItemMeta();
        im.setDisplayName("Tnt");
        tnt.setItemMeta(im);

        ItemStack paper = new ItemStack(Material.PAPER);
        im = paper.getItemMeta();
        im.setDisplayName("Tick");
        paper.setItemMeta(im);



    }

}
