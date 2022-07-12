package com.d4rkr34lm.wgbuild.Simulator;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class SimulationBlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){

        if(e.getBlock().getType().equals(Material.GREEN_SHULKER_BOX)){
            SimulationBlockManager.removeSimBlock(e.getBlock());
        }else if(e.getBlock().getType().equals(Material.RED_SHULKER_BOX)){
            SimulationBlockManager.removeSimBlock(e.getBlock());
        }

    }

}
