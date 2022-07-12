package com.d4rkr34lm.wgbuild.Simulator;

import com.d4rkr34lm.wgbuild.WGBuild;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

public class SimulationBlockManager {

    private static ArrayList<SimulationBlock> simBlocks = new ArrayList<SimulationBlock>();
    private static HashMap<Block, SimulationBlock> lookupTable = new HashMap<>();

    public SimulationBlockManager(){

    }

    public static void addSimBlock(Block newSimulationBlock, boolean activated){
        simBlocks.add(new SimulationBlock(newSimulationBlock, activated));
        lookupTable.put(newSimulationBlock, simBlocks.get(simBlocks.size()-1));
        WGBuild.getPlugin().getLogger().log(Level.INFO, "New Block added");
    }

    public static void removeSimBlock(Block oldBlock){
        SimulationBlock oldSimBlock = lookupTable.get(oldBlock);
        simBlocks.remove(oldSimBlock);
        lookupTable.remove(oldBlock);
        WGBuild.getPlugin().getLogger().log(Level.INFO, "Old Block removed");
    }

    public static ArrayList<SimulationBlock> getSimulationBlocks(){
        return simBlocks;
    }

    public static HashMap<Block, SimulationBlock> getLookupTable(){
        return lookupTable;
    }

}
