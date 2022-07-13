package com.d4rkr34lm.wgbuild.Simulator;

import com.d4rkr34lm.wgbuild.WGBuild;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.logging.Level;

public class SimulationGuiClickListener implements Listener {

    @EventHandler
    public void onGuiClick(InventoryClickEvent e){

        e.setCancelled(true);

        Player player = (Player) e.getWhoClicked();

        if(!e.getView().getTitle().equals("§gTnt Prime Simulator")){
            return;
        }

        int slot = e.getSlot();

        if(slot == -999){
            return;
        }

        int column = slot % 9;
        int row = (int) Math.floor(slot/9);
        int currentPage = SimulationBlockManager.getPlayerSimulationPage().get(player);

        if(e.getCurrentItem().getType().equals(Material.CAMPFIRE)){
            int newPage = currentPage+1;
            SimulationBlockManager.removePlayerPage(player);
            SimulationBlockManager.putPlayerPage(player, newPage);
            player.closeInventory();
            player.openInventory(SimulationBlockManager.getLookupTable().get(player.getTargetBlock(10)).getSimulationInventory(newPage));
        }else if(e.getCurrentItem().getType().equals(Material.SOUL_CAMPFIRE)){
            int newPage = currentPage-1;

            if(newPage < 0){
                return;
            }

            SimulationBlockManager.removePlayerPage(player);
            SimulationBlockManager.putPlayerPage(player, newPage);
            player.closeInventory();
            player.openInventory(SimulationBlockManager.getLookupTable().get(player.getTargetBlock(10)).getSimulationInventory(newPage));
        }

    }

}
