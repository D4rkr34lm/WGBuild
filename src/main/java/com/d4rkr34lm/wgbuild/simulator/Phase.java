package com.d4rkr34lm.wgbuild.simulator;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;

public class Phase {

    private int tick;
    private int tnt;
    private double offsetX;
    private double offsetY;
    private double offsetZ;


    public Phase(String data){
        String[] phaseDataParts = data.split(",");

        tick = Integer.parseInt(phaseDataParts[0]);
        tnt = Integer.parseInt(phaseDataParts[1]);
        offsetX = Double.parseDouble(phaseDataParts[2]);
        offsetY = Double.parseDouble(phaseDataParts[3]);
        offsetZ = Double.parseDouble(phaseDataParts[4]);
    }

    public Phase(int tick, int tnt, double offsetX, double offsetY, double offsetZ){
        this.tick = tick;
        this.tnt = tnt;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
    }

    public void simulate(Location location){
        Location spawnLocation = new Location(location.getWorld(), location.getX() + 0.5 + offsetX, location.getY() + offsetY, location.getZ() + 0.5 + offsetZ);
       for(int n = 0; n < tnt; n++){
           Entity tnt = location.getWorld().spawnEntity(spawnLocation, EntityType.PRIMED_TNT);
           tnt.setVelocity(new Vector(0.0, 0.0, 0.0));
       }
    }

    @Override
    public String toString() {
        String data = "";

        data += Integer.toString(tick);
        data += ",";
        data += Integer.toString(tnt);
        data += ",";
        data += Double.toString(offsetX);
        data += ",";
        data += Double.toString(offsetY);
        data += ",";
        data += Double.toString(offsetZ);
        data += ";";

        return data;
    }

    public int getTnt() {
        return tnt;
    }

    public void setTnt(int tnt) {
        this.tnt = tnt;
    }

    public int getTick() {
        return tick;
    }

    public void setTick(int tick) {
        this.tick = tick;
    }
}
