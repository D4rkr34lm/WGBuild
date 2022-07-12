package com.d4rkr34lm.wgbuild.Simulator;

import com.d4rkr34lm.wgbuild.WGBuild;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.logging.Level;

public class SimulationBlockPlaceListener implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){

        if(e.getBlock().getType().equals(Material.GREEN_SHULKER_BOX)){
            SimulationBlockManager.addSimBlock(e.getBlock(), true);
        }else if(e.getBlock().getType().equals(Material.RED_SHULKER_BOX)){
            SimulationBlockManager.addSimBlock(e.getBlock(), false);
        }

    }

}
