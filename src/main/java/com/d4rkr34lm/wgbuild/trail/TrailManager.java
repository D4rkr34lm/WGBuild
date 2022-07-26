package com.d4rkr34lm.wgbuild.trail;

import com.d4rkr34lm.wgbuild.WGBuild;
import com.d4rkr34lm.wgbuild.plotSystem.Plot;
import com.d4rkr34lm.wgbuild.plotSystem.PlotManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class TrailManager implements Listener {

    private WGBuild plugin;
    private static HashMap<Player, Trail> trails = new HashMap<>();
    private static HashMap<Plot, Trail> trailsWaitingToRecord = new HashMap<Plot, Trail>();
    private static HashMap<Plot, Trail> trailsRecording = new HashMap<Plot, Trail>();
    private static  HashMap<Plot, Integer> recordingTickTime = new HashMap<>();
    private int newTaskID;

    public TrailManager(WGBuild plugin){
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPrimedTntSpawn(EntitySpawnEvent event){
        if(event.getEntity().getType() == EntityType.PRIMED_TNT){
            for(Plot plot : PlotManager.getPlots()){
                if(plot.isInsideArea(event.getEntity().getLocation())){
                    if(trailsWaitingToRecord.containsKey(plot)){
                        Trail trail = trailsWaitingToRecord.get(plot);
                        trail.getPlayer().sendMessage("[" + ChatColor.BLUE  + "WGBuild" + ChatColor.DARK_PURPLE + "/" + ChatColor.BLUE + "Trail" + ChatColor.WHITE + "] Started recording tnt in plot " + ChatColor.BLUE + plot.getId());
                        trailsRecording.put(plot, trail);
                        trailsWaitingToRecord.remove(plot);
                        recordTnt(trail, plot);
                    }
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onTntExplosion(EntityExplodeEvent event){
        if(event.getEntity().getType() == EntityType.PRIMED_TNT){
            for(Plot plot : PlotManager.getPlots()){
                if(plot.isInsideArea(event.getEntity().getLocation()) && trailsRecording.containsKey(plot)){
                    Trail trail = trailsRecording.get(plot);
                    int tick = recordingTickTime.get(plot);
                    TrailObject trailObject = new TrailObject(event.getEntity(), tick + 1, true);
                    HashSet<TrailObject> trailObjects = new HashSet<>();
                    trailObjects.add(trailObject);
                    trail.addTrailObjects(trailObjects, tick);
                }
            }
        }
    }

    public void recordTnt(Trail trail, Plot plot){
        newTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {

            int taskID = 0;
            int currentTick = -1;

            @Override
            public void run() {
                if(taskID == 0){
                    taskID = newTaskID;
                }

                currentTick++;
                recordingTickTime.put(plot, currentTick + 1);
                if(currentTick < 80){
                    return;
                }

                boolean hasRecorded = false;

                ArrayList<TNTPrimed> primedTnt = new ArrayList<>();
                primedTnt.addAll(plugin.getServer().getWorld("world").getEntitiesByClass(TNTPrimed.class));

                HashSet<TrailObject> trailObjects = new HashSet<>();
                for(TNTPrimed tnt : primedTnt){
                    if(plot.isInsideArea(tnt.getLocation())){
                        trailObjects.add(new TrailObject(tnt, currentTick + 1, false));
                        hasRecorded = true;
                    }
                }

                if(!hasRecorded){
                    Bukkit.getScheduler().cancelTask(taskID);
                    trailsRecording.remove(plot);
                    trail.show(TrailViewingMode.normal);
                    trail.getPlayer().sendMessage("[" + ChatColor.BLUE  + "WGBuild" + ChatColor.DARK_PURPLE + "/" + ChatColor.BLUE + "Trail" + ChatColor.WHITE + "] Finished recording");
                }
                else {
                    trail.addTrailObjects(trailObjects, currentTick);
                }
            }
        },0, 1);
    }

    public static boolean isRecording(Plot plot){
        if(trailsRecording.containsKey(plot) || trailsWaitingToRecord.containsKey(plot)){
            return true;
        }
        else {
            return  false;
        }
    }

    public static HashMap<Player, Trail> getTrails() {
        return trails;
    }

    public static HashMap<Plot, Trail> getTrailsWaitingToRecord() {
        return trailsWaitingToRecord;
    }
}
