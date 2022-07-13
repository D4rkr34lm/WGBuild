package com.d4rkr34lm.wgbuild.Simulator;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class SimulationActivationListener implements Listener {

    @EventHandler
    public void onSimulationStart(PlayerInteractEvent e){

        if(e.getAction().isLeftClick()){
           return;
        }

        if(e.getItem() == null || !e.getItem().getType().equals(Material.CLOCK)){
            return;
        }

        for(SimulationBlock simBlock : SimulationBlockManager.getSimulationBlocks()){
            if(simBlock.isActivated()){
                new TntSpawner(simBlock.getBlock().getLocation(), simBlock.getQueue()).spawnTnt();
            }
        }
    }

}
