package com.d4rkr34lm.wgbuild.trail;

import com.d4rkr34lm.wgbuild.WGBuild;
import com.destroystokyo.paper.event.block.TNTPrimeEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TntPrimeListener implements Listener {

    private JavaPlugin parent;
    private Logger logger;

    private int schedulerID;
    private int tickTime;

    ArrayList<TrailObject> trail = new ArrayList<TrailObject>();

    public TntPrimeListener(JavaPlugin parent){
        this.parent = parent;
        this.logger = parent.getLogger();
    }

    @EventHandler
    public void onTntPrime(TNTPrimeEvent e){

        logger.log(Level.INFO,"A tnt has been primed");

        if(!WGBuild.isWaitingToStartRecording() || WGBuild.isRecordingTrail()){
            return;
        }


        WGBuild.setRecordingTrail(true);
        WGBuild.setWaitingToStartRecording(false);
        logger.log(Level.INFO, "The recording has started");

        trail.clear();

        tickTime = 1;

        schedulerID = Bukkit.getScheduler().scheduleSyncRepeatingTask(parent, new Runnable() {
            @Override
            public void run() {

                for(Entity entity : parent.getServer().getWorld("world").getEntitiesByClass(TNTPrimed.class)){

                    trail.add(new TrailObject(entity.getLocation(), tickTime, false));

                }

                tickTime++;

                if(parent.getServer().getWorld("world").getEntitiesByClass(TNTPrimed.class).size() == 0){
                    Bukkit.getScheduler().cancelTask(schedulerID);
                    logger.log(Level.INFO, "Scheduler has been stopped, No primed TNT was found. TickTime: " + tickTime);
                    showTrail();
                }

            }
        }, 0, 1);

    }

    public void showTrail(){
        WGBuild.setRecordingTrail(false);
        logger.log(Level.INFO, "The Recording has stopped");

        for(TrailObject t : trail){
            String bdString = "minecraft:tnt";
            BlockData bd = Bukkit.createBlockData(bdString);
            Bukkit.getWorld("world").spawnFallingBlock(t.getLocation(), bd);
        }
    }

}
