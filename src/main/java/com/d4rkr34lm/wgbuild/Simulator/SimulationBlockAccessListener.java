package com.d4rkr34lm.wgbuild.Simulator;

import com.d4rkr34lm.wgbuild.WGBuild;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.logging.Level;

public class SimulationBlockAccessListener implements Listener {

    @EventHandler
    public void onShulkerChestOpen(PlayerInteractEvent e){

        if(e.getAction().isLeftClick()){
            return;
        }

        Player player = e.getPlayer();

        if(e.getClickedBlock() == null){
            return;
        }

        Block clickedBlock = e.getClickedBlock();
        Material blockType = clickedBlock.getType();

        if(blockType.equals(Material.GREEN_SHULKER_BOX) || blockType.equals(Material.RED_SHULKER_BOX)){
            e.setCancelled(true);

            player.openInventory(SimulationBlockManager.getLookupTable().get(clickedBlock).getSimulationInventory());

        }

    }

}
