package com.d4rkr34lm.wgbuild.trail;

import com.d4rkr34lm.wgbuild.WGBuild;
import com.d4rkr34lm.wgbuild.plotSystem.Plot;
import com.d4rkr34lm.wgbuild.plotSystem.PlotManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
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
    private static HashMap<FallingBlock, TrailObject> currentlyVisibleTrailObjects = new HashMap<>();
    private static final String TRAIL_CHAT_TAG = "[" + ChatColor.BLUE  + "WGBuild" + ChatColor.WHITE + "]";
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

    @EventHandler
    public void onPlayerInteractWithTrailObject(PlayerInteractAtEntityEvent event){
        if(event.getRightClicked().getType() == EntityType.FALLING_BLOCK){
            FallingBlock fallingBlock = (FallingBlock) event.getRightClicked();
            TrailObject trailObject = currentlyVisibleTrailObjects.get(fallingBlock);
            String message = "";
            message += TRAIL_CHAT_TAG + "Displaying data about TrailObject : \n";
            message += "               " + "Tick Time: " + ChatColor.BLUE + trailObject.getTick() + ChatColor.WHITE + "\n";
            message += "               " + "Velocity: \n";
            message += "               " + "         X: " + ChatColor.BLUE + trailObject.getVelocity().getX() + ChatColor.WHITE + "\n";
            message += "               " + "         Y: " + ChatColor.BLUE + trailObject.getVelocity().getY() + ChatColor.WHITE + "\n";
            message += "               " + "         Z: " + ChatColor.BLUE + trailObject.getVelocity().getZ() + ChatColor.WHITE + "\n";
            message += "               " + "Position: \n";
            message += "               " + "         X: " + ChatColor.BLUE + trailObject.getLocation().getX() + ChatColor.WHITE + "\n";
            message += "               " + "         Y: " + ChatColor.BLUE + (trailObject.getLocation().getY() - 0.5) + ChatColor.WHITE + "\n";
            message += "               " + "         Z: " + ChatColor.BLUE + (trailObject.getLocation().getZ() - 0.5) + ChatColor.WHITE;
            event.getPlayer().sendMessage(message);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        if(trails.containsKey(event.getPlayer())){
            trails.get(event.getPlayer()).hide();
            trails.remove(event.getPlayer());
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

    public static void registerTrailObject(TrailObject trailObject){
        currentlyVisibleTrailObjects.put(trailObject.getVisualiser(), trailObject);
    }

    public static void checkoutTrailObject(TrailObject trailObject){
        currentlyVisibleTrailObjects.remove(trailObject.getVisualiser());
    }

    public static boolean issueRecording(Plot plot, Player player){
        if(!isRecording(plot)){
            if(trails.containsKey(player)){
                trails.get(player).hide();
            }
            Trail trail = new Trail(player);
            trailsWaitingToRecord.put(plot, trail);
            trails.put(player, trail);
            player.sendMessage("[" + ChatColor.BLUE  + "WGBuild" + ChatColor.DARK_PURPLE + "/" + ChatColor.BLUE + "Trail" + ChatColor.WHITE + "] Waiting for Tnt in Plot "+ ChatColor.BLUE + plot.getId());
            return true;
        }
        else {
            return false;
        }
    }
}
