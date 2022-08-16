package com.d4rkr34lm.wgbuild.tools;

import com.d4rkr34lm.wgbuild.WGBuild;
import com.d4rkr34lm.wgbuild.plotSystem.Plot;
import com.d4rkr34lm.wgbuild.plotSystem.PlotManager;
import com.d4rkr34lm.wgbuild.trail.TrailManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.Powerable;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.block.data.type.Switch;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;

public class RemoteDetonator implements Listener {
    private WGBuild plugin;
    private final String CHAT_TAG = "§f[§1WGBuild§f] §8";
    private HashMap<Player, Block> detonatorBlocks = new HashMap<>();

    private Material remoteDetonatorMaterial = Material.WOODEN_HOE;

    private Material[] woodenButtons = {Material.BIRCH_BUTTON, Material.ACACIA_BUTTON, Material.CRIMSON_BUTTON,
            Material.JUNGLE_BUTTON,  Material.MANGROVE_BUTTON,  Material.DARK_OAK_BUTTON,  Material.OAK_BUTTON,
            Material.SPRUCE_BUTTON,  Material.WARPED_BUTTON};
    private Material[] stoneButtons = { Material.POLISHED_BLACKSTONE_BUTTON,  Material.STONE_BUTTON};

    private int newTaskId = 0;

    public RemoteDetonator(WGBuild plugin){
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        if(event.getPlayer().getInventory().getItemInMainHand().getType() == remoteDetonatorMaterial){
            if(event.getBlock().getBlockData() instanceof Switch || event.getBlock().getBlockData() instanceof Openable || event.getBlock().getBlockData() instanceof NoteBlock){
                detonatorBlocks.put(event.getPlayer(), event.getBlock());
                event.getPlayer().sendMessage(CHAT_TAG + " Block saved as detonator");
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayRightClick(PlayerInteractEvent event){
        if(event.getItem() != null && event.getItem().getType() == remoteDetonatorMaterial && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && detonatorBlocks.containsKey(event.getPlayer())){
            Block block = detonatorBlocks.get(event.getPlayer());

            if(block.getType() == Material.AIR || block == null){
                detonatorBlocks.remove(event.getPlayer());
                return;
            };

            for(Plot plot : PlotManager.getPlots()){
                if(plot.isInsideArea(block.getLocation())){
                    if(block.getBlockData() instanceof Switch){
                        Powerable powerable = (Powerable) block.getBlockData();
                        powerable.setPowered(!powerable.isPowered());
                        block.setBlockData(powerable, true);
                        doAfterActivation(block);
                    }
                    else if (block.getBlockData() instanceof Openable) {
                        Openable openable = (Openable) block.getBlockData();
                        openable.setOpen(!openable.isOpen());
                        block.setBlockData(openable, true);
                    }
                    else if (block.getBlockData() instanceof NoteBlock) {
                        NoteBlock noteBlock = (NoteBlock) block.getBlockData();
                        noteBlock.setPowered(!noteBlock.isPowered());
                        block.setBlockData(noteBlock, true);
                    }
                    TrailManager.issueRecording(plot, event.getPlayer());
                    event.getPlayer().sendMessage(CHAT_TAG + " Saved block has been triggered");
                    return;
                }
            }
        }
    }

    public void doAfterActivation(Block block){
        for(Material woodenButton : woodenButtons){
            if(block.getType() == woodenButton){
                newTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                    private int taskID = 0;
                    private int passedTick = 1;
                    @Override
                    public void run() {
                        if(taskID == 0){
                            taskID = newTaskId;
                        }

                        if(passedTick < 30){
                            passedTick++;
                        }
                        else {
                            Powerable powerable = (Powerable) block.getBlockData();
                            powerable.setPowered(false);
                            block.setBlockData(powerable);
                            Bukkit.getScheduler().cancelTask(taskID);
                        }
                    }
                }, 0, 1);
                return;
            }
        }

        for(Material stoneButton : stoneButtons){
            if(block.getType() == stoneButton){
                newTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                    private int taskID = 0;
                    private int passedTick = 1;
                    @Override
                    public void run() {
                        if(taskID == 0){
                            taskID = newTaskId;
                        }

                        if(passedTick < 20){
                            passedTick++;
                        }
                        else {
                            Powerable powerable = (Powerable) block.getBlockData();
                            powerable.setPowered(false);
                            block.setBlockData(powerable);
                            Bukkit.getScheduler().cancelTask(taskID);
                        }
                    }
                }, 0, 1);
                return;
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        if(detonatorBlocks.containsKey(event.getPlayer())){
            detonatorBlocks.remove(event.getPlayer());
        }
    }
}
