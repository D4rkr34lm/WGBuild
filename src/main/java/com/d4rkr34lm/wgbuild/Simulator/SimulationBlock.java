package com.d4rkr34lm.wgbuild.Simulator;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;

public class SimulationBlock {

    private Block simBlock;
    private Inventory simInv;
    private boolean activated;

    public SimulationBlock(Block block, boolean activated){
        simBlock = block;
        simInv = Bukkit.createInventory(null, 9 * 6, "§gTnt Prime Simulator");
        this.activated = activated;
    }

    public Block getBlock(){
        return simBlock;
    }

    public Inventory getSimulationInventory(){
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

}
