package com.d4rkr34lm.wgbuild.trail;

import org.bukkit.Location;

public class TrailObject {

    private Location location;
    private int tickTime;
    private boolean explosion;

    public TrailObject(Location location, int tickTime, boolean explosion){
        this.location = location;
        this.tickTime = tickTime;
        this.explosion = explosion;
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

}
