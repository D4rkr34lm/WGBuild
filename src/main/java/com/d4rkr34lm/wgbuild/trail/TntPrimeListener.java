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
    private Logger logger;

    private int schedulerID;

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
        Bukkit.broadcastMessage("§aTNT found! Recording position and ticks");

        WGBuild.clearTrail();

        WGBuild.resetTickTime();

        schedulerID = Bukkit.getScheduler().scheduleSyncRepeatingTask(parent, new Runnable() {
            @Override
            public void run() {

                for(Entity entity : parent.getServer().getWorld("world").getEntitiesByClass(TNTPrimed.class)){

                    WGBuild.addTrail(new TrailObject(entity.getLocation(), WGBuild.getTickTime(), false));

                }

                WGBuild.addTickTime();

                if(parent.getServer().getWorld("world").getEntitiesByClass(TNTPrimed.class).size() == 0){
                    Bukkit.getScheduler().cancelTask(schedulerID);
                    logger.log(Level.INFO, "Scheduler has been stopped, No primed TNT was found. TickTime: " + WGBuild.getTickTime());
                    WGBuild.setRecordingTrail(false);
                    logger.log(Level.INFO, "The Recording has stopped");
                    TrailManager.showTrailNormal();
                }

            }
        }, WGBuild.getTickStart(), 1);

    }

}
