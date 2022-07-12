package com.d4rkr34lm.wgbuild.Simulator;

import com.d4rkr34lm.wgbuild.WGBuild;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class TntSpawner {

    JavaPlugin plugin;
    Location spawnLocation;
    HashMap<Integer, Integer> queue = new HashMap<Integer, Integer>();

    int schedulerID;
    int currentTick = 1;

    public TntSpawner(Location spawnLocation, HashMap<Integer, Integer> queue){
        this.spawnLocation = spawnLocation;
        this.queue = queue;
        plugin = WGBuild.getPlugin();
    }

    public void spawnTnt(){

        schedulerID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {

                if(queue.containsKey(currentTick)){
                    int tntAmount = queue.get(currentTick);

                    for(int i = 0; i < tntAmount; i++){
                        spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.PRIMED_TNT);
                    }
                }

                currentTick++;

                if(currentTick >= 80){
                    Bukkit.getScheduler().cancelTask(schedulerID);
                }

            }
        }, 0, 1);

    }

}
