package com.d4rkr34lm.wgbuild.Simulator;

import com.d4rkr34lm.wgbuild.WGBuild;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.logging.Level;

public class SimulationGuiClickListener implements Listener {

    @EventHandler
    public void onGuiClick(InventoryClickEvent e){

        Player player = (Player) e.getWhoClicked();

        if(!e.getView().getTitle().equals("§gTnt Prime Simulator")){
            return;
        }

        int slot = e.getSlot();

        if(slot == -999){
            return;
        }

        e.setCancelled(true);

        int column = slot % 9;
        int row = (int) Math.floor(slot/9);
        int currentPage = SimulationBlockManager.getPlayerSimulationPageMap().get(player);

        SimulationBlock sb = SimulationBlockManager.getLookupTable().get(player.getTargetBlock(10));

        if(e.getCurrentItem().getType().equals(Material.CAMPFIRE)){
            int newPage = currentPage+1;

            SimulationBlockManager.removePlayerPage(player);
            SimulationBlockManager.putPlayerPage(player, newPage);
            player.closeInventory();
            player.openInventory(sb.getSimulationInventory(newPage));
        }else if(e.getCurrentItem().getType().equals(Material.SOUL_CAMPFIRE)){
            int newPage = currentPage-1;

            if(newPage >= 0){
                SimulationBlockManager.removePlayerPage(player);
                SimulationBlockManager.putPlayerPage(player, newPage);
                player.closeInventory();
                player.openInventory(sb.getSimulationInventory(newPage));
            }

        }else if(e.getCurrentItem().getType().equals(Material.LIME_DYE)){

            int tickSlot = column + (4 * 9);
            int tntSlot = column + 9;
            int playerPage = SimulationBlockManager.getPlayerSimulationPageMap().get((Player) e.getWhoClicked());

            ItemStack tntSlotStack = e.getInventory().getItem(tntSlot);
            ItemStack tickSlotStack = e.getInventory().getItem(tickSlot);

            int currentTick;
            int currentTnt;

            if(tntSlotStack == null){
                currentTnt = 0;
            }else{
                currentTnt = e.getInventory().getItem(tntSlot).getAmount();
            }

            if(tickSlotStack == null){
                currentTick = 0;
            }else{
                currentTick = e.getInventory().getItem(tickSlot).getAmount();
            }

            if(row == 0){

                if(currentTnt > 0){
                    sb.removeTick(currentTick, playerPage);
                    int newTnt = currentTnt + 1;
                    sb.putTick(currentTick, newTnt, playerPage);
                }else{
                    int newTick = 1;
                    while(sb.getSortedQueue().contains(newTick)){
                        newTick += 1;
                    }
                    sb.putTick(newTick, 1, playerPage);
                }


            }else if(row == 3){

                if(currentTick > 0){
                    int newTick = currentTick + 1;
                    while(sb.getSortedQueue().contains(newTick)){
                        newTick += 1;
                    }

                    sb.removeTick(currentTick, playerPage);
                    sb.putTick(newTick, currentTnt, playerPage);
                }else{
                    int newTick = 1;
                    while(sb.getSortedQueue().contains(newTick)){
                        newTick += 1;
                    }
                    sb.putTick(newTick, 1, playerPage);
                }



            }

        }else if(e.getCurrentItem().getType().equals(Material.RED_DYE)){

            int tickSlot = column + (4 * 9);
            int tntSlot = column + 9;
            int playerPage = SimulationBlockManager.getPlayerSimulationPageMap().get((Player) e.getWhoClicked());

            ItemStack tntSlotStack = e.getInventory().getItem(tntSlot);
            ItemStack tickSlotStack = e.getInventory().getItem(tickSlot);

            int currentTick;
            int currentTnt;

            if(tntSlotStack == null){
                return;
            }else{
                currentTnt = e.getInventory().getItem(tntSlot).getAmount();
            }

            if(tickSlotStack == null){
                return;
            }else{
                currentTick = e.getInventory().getItem(tickSlot).getAmount();
            }

            if(row == 2){

                sb.removeTick(currentTick, playerPage);
                int newTnt = currentTnt - 1;

                if(newTnt > 0){
                    sb.putTick(currentTick, newTnt, playerPage);
                }

            }else if(row == 5){

                int newTick = currentTick - 1;
                while(sb.getSortedQueue().contains(newTick)){
                    newTick -= 1;
                }

                sb.removeTick(currentTick, playerPage);

                if(newTick > 1){
                    sb.putTick(newTick, currentTnt, playerPage);
                }

            }

        }else if(e.getCurrentItem().getType().equals(Material.TNT) || e.getCurrentItem().getType().equals(Material.PAPER)){
            int tickSlot = column + (4 * 9);
            int currentTick = e.getInventory().getItem(tickSlot).getAmount();
            int playerPage = SimulationBlockManager.getPlayerSimulationPageMap().get((Player) e.getWhoClicked());

            sb.removeTick(currentTick, playerPage);
        }

    }

}
