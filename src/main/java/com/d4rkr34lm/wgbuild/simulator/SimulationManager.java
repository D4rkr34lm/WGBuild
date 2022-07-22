package com.d4rkr34lm.wgbuild.simulator;

import com.d4rkr34lm.wgbuild.WGBuild;
import com.d4rkr34lm.wgbuild.plotSystem.Plot;
import com.d4rkr34lm.wgbuild.plotSystem.PlotManager;
import com.sk89q.worldedit.util.formatting.text.Component;
import com.sk89q.worldedit.util.formatting.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;

public class SimulationManager implements Listener {
    private WGBuild plugin;

    private final Material ENABLED_SIMULATOR_MATERIAL = Material.LIGHT_BLUE_SHULKER_BOX;
    private final Material DISABLE_SIMULATOR_MATERIAL = Material.PURPLE_SHULKER_BOX;

    private int currentTick = 1;
    private int lastSimulatedTick = 0;
    private boolean phaseSimulated = false;
    private int schedulerID;

    public SimulationManager(WGBuild plugin){
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        if(event.getBlock().getType() == ENABLED_SIMULATOR_MATERIAL|| event.getBlock().getType() == DISABLE_SIMULATOR_MATERIAL){
            createSimulator(event.getBlock());
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        if(event.getBlock().getType() == ENABLED_SIMULATOR_MATERIAL || event.getBlock().getType() == DISABLE_SIMULATOR_MATERIAL){
            if(event.getPlayer().getInventory().getItemInMainHand().getType() == Material.CLOCK){
                ShulkerBox simulator = (ShulkerBox) event.getBlock().getState();
                ItemStack dataContainer = simulator.getInventory().getItem(0);

                if(event.getBlock().getType() == ENABLED_SIMULATOR_MATERIAL){
                    event.getBlock().setType(DISABLE_SIMULATOR_MATERIAL);
                }
                else {
                    event.getBlock().setType(ENABLED_SIMULATOR_MATERIAL);
                }
                simulator = (ShulkerBox) event.getBlock().getState();
                simulator.getInventory().addItem(dataContainer);
                event.setCancelled(true);
            }
            else {
                event.getBlock().setType(Material.AIR);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        if(event.getAction().isRightClick() && event.getItem() != null && event.getAction().isRightClick() && event.getItem().getType() == Material.CLOCK){
            for(Plot plot : PlotManager.getPlots()){
                if(plot.isInsideArea(event.getPlayer().getLocation())){
                    startSimulation(gatherSimulators(plot));
                    event.setCancelled(true);
                }
            }
        }
    }

    public ArrayList<Simulator> gatherSimulators(Plot plot){
        ArrayList<Simulator> simulators = new ArrayList<Simulator>();

        for(int x = plot.getCorner().getBlockX(); x < plot.getCorner().getBlockX() + plot.getSize().getBlockX(); x++){
            for(int y = plot.getCorner().getBlockY(); y < plot.getCorner().getBlockY() + 80; y++){
                for(int z = plot.getCorner().getBlockZ(); z < plot.getCorner().getBlockZ() + plot.getSize().getBlockZ(); z++){
                    Block block = Bukkit.getServer().getWorld("world").getBlockAt(x, y, z);
                    if(block.getType() == ENABLED_SIMULATOR_MATERIAL || block.getType() == DISABLE_SIMULATOR_MATERIAL){


                        if(block.getType() == ENABLED_SIMULATOR_MATERIAL){
                            simulators.add(new Simulator(block, true));
                        }
                        else if (block.getType() == DISABLE_SIMULATOR_MATERIAL) {
                            simulators.add(new Simulator(block, false));
                        }
                    }
                }
            }
        }

        return  simulators;
    }

    public void startSimulation(ArrayList<Simulator> simulators){
        simulators.sort(new Comparator<Simulator>() {
            @Override
            public int compare(Simulator o1, Simulator o2) {
                if(o1.getPriority() == o2.getPriority()){
                    return 0;
                }
                if(o1.getPriority() > o2.getPriority()){
                    return 1;
                }
                else {
                    return -1;
                }
            }
        });

        for(Simulator simulator : simulators){
            simulator.startSimulation();
        }

        schedulerID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                phaseSimulated = false;
                for(Simulator simulator : simulators){
                    if(simulator.hasPhase(currentTick)){
                        simulator.simulatePhase(currentTick);
                        phaseSimulated = true;
                        lastSimulatedTick = currentTick;
                    }
                }

                if(!phaseSimulated && currentTick - lastSimulatedTick > 120){
                    for(Simulator simulator : simulators){
                        simulator.stopSimulation();
                    }
                    Bukkit.getScheduler().cancelTask(schedulerID);
                }

                currentTick++;
            }
        }, 0, 1);

        currentTick = 1;
    }

    public void createSimulator(Block block){
        ShulkerBox simulator = (ShulkerBox) block.getState();

        ItemStack dataContainer = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = dataContainer.getItemMeta();
        itemMeta.setDisplayName("1,0,0,0;1,1,0,0,0;");
        dataContainer.setItemMeta(itemMeta);

        simulator.getInventory().setItem(0, dataContainer);

     }
}
