package com.d4rkr34lm.wgbuild.trail;

import com.d4rkr34lm.wgbuild.WGBuild;
import com.destroystokyo.paper.event.block.TNTPrimeEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TntPrimeListener implements Listener {

    private JavaPlugin parent;

    private int schedulerID;

    private final String WORLD_NAME = "world";

    private final String TNT_FOUND = "§aA TNT has been primed! Recording positions and ticks...";
    private final String RECORDING_STOPPED = "§aThe last TNT has exploded. The recording has been stopped";
    private final String MODE_NORMAL = "§aCurrent viewing mode: §6Normal";

    public TntPrimeListener(JavaPlugin parent){
        this.parent = parent;
    }

    @EventHandler
    public void onTntPrime(TNTPrimeEvent e){

        if(!WGBuild.isWaitingToStartRecording() || WGBuild.isRecordingTrail()){
            return;
        }

        WGBuild.setRecordingTrail(true);
        WGBuild.setWaitingToStartRecording(false);
        Bukkit.broadcastMessage("\n"
                    + TNT_FOUND);

        WGBuild.clearTrail();
        WGBuild.resetTickTime();

        schedulerID = Bukkit.getScheduler().scheduleSyncRepeatingTask(parent, new Runnable() {
            @Override
            public void run() {

                for(Entity entity : parent.getServer().getWorld(WORLD_NAME).getEntitiesByClass(TNTPrimed.class)){
                    TrailObject trailObject = new TrailObject(entity, WGBuild.getTickTime(), false);
                    WGBuild.addTrail(trailObject);
                }

                WGBuild.addTickTime();

                if(Bukkit.getWorld(WORLD_NAME).getEntitiesByClass(TNTPrimed.class).size() == 0){
                    Bukkit.getScheduler().cancelTask(schedulerID);
                    WGBuild.setRecordingTrail(false);
                    Bukkit.broadcastMessage(RECORDING_STOPPED + "\n" + "");
                    TrailManager.showTrailNormal();
                }

            }
        }, WGBuild.getTickStart(), 1);

    }

}
