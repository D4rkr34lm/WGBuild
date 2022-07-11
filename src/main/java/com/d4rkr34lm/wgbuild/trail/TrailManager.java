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

    public TrailManager(){

    }

    public static void showTrailNormal(){

        removeTrail();

        for(TrailObject t : WGBuild.getTrail()){

            BlockData bd = Bukkit.createBlockData(bdTnt);

            if(t.getExplosion()){
                bd = Bukkit.createBlockData(bdGlass);
            }

            setTrailBlock(t.getLocation(), t.getTickTime(), bd);
        }
    }

    public static void showTrailExplosion(){

        removeTrail();

        for(TrailObject t : WGBuild.getTrail()){

            if(!t.getExplosion()){
                continue;
            }

            BlockData bd = Bukkit.createBlockData(bdGlass);
            setTrailBlock(t.getLocation(), t.getTickTime(), bd);
        }
    }

    public static void showTrailTravel(){

        removeTrail();

        for(TrailObject t : WGBuild.getTrail()) {

            if (t.getExplosion()) {
                continue;
            }

            BlockData bd = Bukkit.createBlockData(bdTnt);
            setTrailBlock(t.getLocation(), t.getTickTime(), bd);
        }
    }


    public static void removeTrail(){
        for(FallingBlock b : Bukkit.getWorld("world").getEntitiesByClass(FallingBlock.class)){
            b.remove();
        }
    }

    private static void setTrailBlock(Location loc, int tickTime, BlockData bd){

        FallingBlock fb = Bukkit.getWorld("world").spawnFallingBlock(loc, bd);

        fb.setGravity(false);
        fb.setCustomName(Integer.toString(tickTime));
        fb.setCustomNameVisible(true);
        fb.shouldAutoExpire(false);
        fb.setDropItem(false);
    }

}
