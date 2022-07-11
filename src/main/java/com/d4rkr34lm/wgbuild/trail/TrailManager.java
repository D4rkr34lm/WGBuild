package com.d4rkr34lm.wgbuild.trail;

import com.d4rkr34lm.wgbuild.WGBuild;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.FallingBlock;

public class TrailManager {

    private static String bdTnt = "minecraft:tnt";
    private static String bdGlass = "minecraft:red_stained_glass";

    private static final String MODE_NORMAL = "§aCurrent viewing mode: §6Normal";
    private static final String MODE_EXPLOSION = "§aCurrent viewing mode: §6Explosion";
    private static final String MODE_TRAVEL = "§aCurrent viewing mode: §6Travel";
    private static final String MODE_HIDDEN = "§aCurrent viewing mode: §6Hidden";

    public TrailManager(){

    }

    public static void showTrailNormal(){
        removeTrail(false);

        for(TrailObject t : WGBuild.getTrail()){

            BlockData bd = Bukkit.createBlockData(bdTnt);

            if(t.getExplosion()){
                bd = Bukkit.createBlockData(bdGlass);
            }

            setTrailBlock(t.getLocation(), t.getTickTime(), bd, t);

        }
        Bukkit.broadcastMessage(MODE_NORMAL);
    }

    public static void showTrailExplosion(){
        removeTrail(false);

        for(TrailObject t : WGBuild.getTrail()){

            if(!t.getExplosion()){
                continue;
            }

            BlockData bd = Bukkit.createBlockData(bdGlass);
            setTrailBlock(t.getLocation(), t.getTickTime(), bd, t);
        }
        Bukkit.broadcastMessage(MODE_EXPLOSION);
    }

    public static void showTrailTravel(){
        removeTrail(false);

        for(TrailObject t : WGBuild.getTrail()) {

            if (t.getExplosion()) {
                continue;
            }

            BlockData bd = Bukkit.createBlockData(bdTnt);
            setTrailBlock(t.getLocation(), t.getTickTime(), bd, t);
        }
        Bukkit.broadcastMessage(MODE_TRAVEL);
    }

    public static void removeTrail(boolean chatOutput){
        for(FallingBlock b : Bukkit.getWorld("world").getEntitiesByClass(FallingBlock.class)){
            b.remove();
        }
        WGBuild.clearLookupTable();
        if(chatOutput){
            Bukkit.broadcastMessage(MODE_HIDDEN);
        }
    }

    private static void setTrailBlock(Location loc, int tickTime, BlockData bd, TrailObject t){

        FallingBlock fb = Bukkit.getWorld("world").spawnFallingBlock(loc, bd);

        WGBuild.putEntry(fb, t);

        fb.setGravity(false);
        fb.setCustomName(Integer.toString(tickTime));
        fb.setCustomNameVisible(true);
        fb.shouldAutoExpire(false);
        fb.setDropItem(false);
    }

}
