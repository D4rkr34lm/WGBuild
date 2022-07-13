package com.d4rkr34lm.wgbuild.Simulator;

import com.d4rkr34lm.wgbuild.WGBuild;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

public class SimulationBlockManager {

    private static ArrayList<SimulationBlock> simBlocks = new ArrayList<SimulationBlock>();
    private static HashMap<Block, SimulationBlock> lookupTable = new HashMap<>();
    private static HashMap<Player, Integer> playerSimulationPage = new HashMap<Player, Integer>();

    public SimulationBlockManager(){

    }

    public static void addSimBlock(Block newBlock, boolean activated){
        simBlocks.add(new SimulationBlock(newBlock, activated));
        lookupTable.put(newBlock, simBlocks.get(simBlocks.size()-1));
        WGBuild.getPlugin().getLogger().log(Level.INFO, "New Block added");
    }

    public static void changeSimBlock(Block changedBlock, boolean activated){

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

    public static HashMap<Player, Integer> getPlayerSimulationPageMap(){
        return playerSimulationPage;
    }

    public static void putPlayerPage(Player player, int page){
        playerSimulationPage.put(player, page);
    }

    public static void removePlayerPage(Player player){
        playerSimulationPage.remove(player);
    }

}
