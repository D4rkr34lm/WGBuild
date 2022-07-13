package com.d4rkr34lm.wgbuild.Simulator;

import com.d4rkr34lm.wgbuild.WGBuild;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class TntSpawner {

    private JavaPlugin plugin;
    private Location spawnLocation;
    private HashMap<Integer, Integer> queue = new HashMap<Integer, Integer>();
    private int latestTick;

    int schedulerID;
    int currentTick = 1;

    public TntSpawner(Location spawnLocation, HashMap<Integer, Integer> queue){
        this.spawnLocation = spawnLocation;
        this.queue = queue;
        plugin = WGBuild.getPlugin();
    }

    public void spawnTnt(){

        spawnLocation.getWorld().getBlockAt(spawnLocation).setType(Material.AIR);

        schedulerID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {

                if(queue.containsKey(currentTick)){
                    int tntAmount = queue.get(currentTick);
                    latestTick = currentTick;

                    for(int i = 0; i < tntAmount; i++){
                        Entity tnt = spawnLocation.getWorld().spawnEntity(new Location(spawnLocation.getWorld(), spawnLocation.getX() + 0.5, spawnLocation.getY(), spawnLocation.getZ() + 0.5), EntityType.PRIMED_TNT);
                        tnt.setVelocity(new Vector(0.0, 0.0, 0.0));
                    }
                }

                currentTick++;

                if(currentTick >= (latestTick + 81)){
                    Bukkit.getScheduler().cancelTask(schedulerID);
                    spawnLocation.getWorld().getBlockAt(spawnLocation).setType(Material.GREEN_SHULKER_BOX);
                }

            }
        }, 0, 1);

    }

}
