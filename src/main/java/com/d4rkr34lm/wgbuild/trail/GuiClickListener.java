package com.d4rkr34lm.wgbuild.trail;

import com.d4rkr34lm.wgbuild.WGBuild;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import java.util.logging.Logger;

public class GuiClickListener implements Listener {

    JavaPlugin parent;
    Logger logger;

    Player player;

    private final String NO_TRAIL = "§cThere is currently no trail saved. Generate a new one by using §6/trail new";
    private final String ALREADY_RECORDING = "§aThe recording has already started. U silly little bastard";
    private final String RECORDING_STARTED = "§aStarted recording. Waiting for TNT...";

    public GuiClickListener(JavaPlugin parent){
        this.parent = parent;
        logger = parent.getLogger();
    }

    @EventHandler
    public void onGuiClick(InventoryClickEvent e){

        logger.log(Level.INFO, "Inv Click Event");

        player = (Player) e.getWhoClicked();

        String invName = e.getView().getTitle();
        ItemStack clickedItem = e.getCurrentItem();

        if (clickedItem == null){
            return;
        }

        logger.log(Level.INFO, "item: " + clickedItem.getType() + ", " + clickedItem.getItemMeta().getDisplayName());

        if(!invName.equals(TrailGui.getInventoryName())){
            return;
        }

        Material itemType = clickedItem.getType();
        ItemMeta itemMeta = clickedItem.getItemMeta();

        if(itemType.equals(Material.RED_STAINED_GLASS_PANE) && itemMeta.getDisplayName().equals(" ")){
            e.setCancelled(true);
        }else if(itemType.equals(Material.MAP) && itemMeta.getDisplayName().equals("§aNew Trail")){
            newTrail();
            e.setCancelled(true);
        }else if (itemType.equals(Material.OAK_LEAVES) && itemMeta.getDisplayName().equals("§dHide Trail")){
            hideTrail();
            e.setCancelled(true);
        }else if (itemType.equals(Material.REDSTONE_BLOCK) && itemMeta.getDisplayName().equals("§cExplosion Only")){
            showExplosionOnly();
            e.setCancelled(true);
        }else if (itemType.equals(Material.BLUE_ICE) && itemMeta.getDisplayName().equals("§9Travel Only")){
            showTravelOnly();
            e.setCancelled(true);
        }else if (itemType.equals(Material.SPONGE) && itemMeta.getDisplayName().equals("§eNormal Mode")){
            showNormalTrail();
            e.setCancelled(true);
        }

        player.closeInventory();

    }

    private void showNormalTrail() {
        if(isTrailEmpty()){
            player.sendMessage(NO_TRAIL);
            return;
        }

        TrailManager.showTrailNormal();
    }

    private void showTravelOnly() {
        if(isTrailEmpty()){
            player.sendMessage(NO_TRAIL);
            return;
        }

        TrailManager.showTrailTravel();
    }

    private void showExplosionOnly() {
        if(isTrailEmpty()){
            player.sendMessage(NO_TRAIL);
            return;
        }

        TrailManager.showTrailExplosion();
    }

    private void hideTrail() {
        TrailManager.removeTrail(true);
    }

    private void newTrail() {

        if(WGBuild.isWaitingToStartRecording()){
            player.sendMessage(ALREADY_RECORDING);
            return;
        }

        TrailManager.removeTrail(false);
        WGBuild.clearTrail();

        player.sendMessage(RECORDING_STARTED);
        WGBuild.setWaitingToStartRecording(true);
    }

    private boolean isTrailEmpty(){
        return WGBuild.getTrail().size() <= 0;
    }

}
