package com.d4rkr34lm.wgbuild.Simulator;

import com.d4rkr34lm.wgbuild.WGBuild;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.units.qual.A;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class SimulationBlock {

    private Block simBlock;
    private Inventory simInv;
    private boolean activated;

    private HashMap<Integer, Integer> queue = new HashMap<>();
    private ArrayList<Integer> sortedQueue = new ArrayList<>();

    public SimulationBlock(Block block, boolean activated){
        simBlock = block;
        simInv = Bukkit.createInventory(null, 9 * 6, "§gTnt Prime Simulator");
        this.activated = activated;

        queue.put(4, 1);
        queue.put(1, 4);
        queue.put(3, 2);
        queue.put(2, 3);
        updateInventory(0);

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

        for(int i = 0; i < 7; i++){
            simInv.setItem(i, increase);
            simInv.setItem(i + (2 * 9), decrease);
            simInv.setItem(i + (3 * 9), increase);
            simInv.setItem(i + (5 * 9), decrease);
        }

        sortedQueue = getSortedHashMap(queue);

        for(int i = 0; i < 7; i++){

            int pageOffset = page * 7;

            if((i+pageOffset) >= sortedQueue.size()){
                simInv.setItem(i + (1 * 9), null);
                simInv.setItem(i + (4 * 9), null);
                continue;
            }

            int currKey = sortedQueue.get(i)+pageOffset;
            paper.setAmount(currKey);
            tnt.setAmount(queue.get(currKey));

            simInv.setItem(i + (1 * 9), tnt);
            simInv.setItem(i + (4 * 9), paper);

        }

    }

    public ArrayList<Integer> getSortedHashMap(HashMap<Integer, Integer> map){
        ArrayList<Integer> sorted = new ArrayList<>();
        map.keySet().stream().sorted().forEach(sorted::add);
        return sorted;
    }

}
