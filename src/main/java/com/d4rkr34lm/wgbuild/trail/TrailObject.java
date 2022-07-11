package com.d4rkr34lm.wgbuild.trail;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class TrailObject {

    private Entity entity;
    private Location location;
    private int tickTime;
    private boolean explosion;
    private Vector velocity;

    public TrailObject(Entity entity, int tickTime, boolean explosion){
        this.entity = entity;
        this.location = entity.getLocation();
        this.tickTime = tickTime;
        this.velocity = entity.getVelocity();
        this.explosion = explosion;
    }

    public Entity getEntity(){
        return entity;
    }

    public Location getLocation(){
        return location;
    }

    public int getTickTime(){
        return tickTime;
    }

    public boolean getExplosion(){
        return explosion;
    }

    public Vector getVelocity(){
        return velocity;
    }

}
