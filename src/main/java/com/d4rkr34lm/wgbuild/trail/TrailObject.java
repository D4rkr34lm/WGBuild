package com.d4rkr34lm.wgbuild.trail;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.util.Vector;

public class TrailObject {
    private Location location;
    private Vector velocity;
    private boolean explosion;
    private int tick;
    private FallingBlock visualiser = null;

    private boolean showing = false;

    public TrailObject(Entity tnt, int tick, boolean explosion){
        location = tnt.getLocation();
        velocity = tnt.getVelocity();

        this.explosion = explosion;

        this.tick = tick;
    }

    public void show(){
        if(!showing){
            BlockData blockData;
            if(explosion){
                blockData = Bukkit.createBlockData(Material.RED_STAINED_GLASS);
            }
            else {
                blockData = Bukkit.createBlockData(Material.TNT);
            }

            FallingBlock fallingBlock = Bukkit.getWorld("world").spawnFallingBlock(location, blockData);
            fallingBlock.setGravity(false);
            fallingBlock.setCustomName(Integer.toString(tick));
            fallingBlock.setCustomNameVisible(true);
            fallingBlock.shouldAutoExpire(false);
            fallingBlock.setDropItem(false);

            visualiser = fallingBlock;
            showing = true;
        }
    }

    public void hide(){
        if(showing){
            visualiser.remove();
            showing = false;
        }
    }

    public boolean isExplosion() {
        return explosion;
    }
}
