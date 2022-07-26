package com.d4rkr34lm.wgbuild.trail;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;

public class Trail {
    private Player player;
    private ArrayList<HashSet<TrailObject>> trailObjectsByTick= new ArrayList<>();
    private TrailViewingMode viewingMode = null;
    public Trail(Player player){
        this.player = player;
    }

    public ArrayList<HashSet<TrailObject>> getTrailObjectsByTick() {
        return trailObjectsByTick;
    }

    public void addTrailObjects(HashSet<TrailObject> trailObjects, int tick){
        if(tick - 79 > trailObjectsByTick.size() - 1){
            trailObjectsByTick.add(trailObjects);
        }
        else {
            trailObjectsByTick.get(tick - 79).addAll(trailObjects);
        }
    }

    public void show(TrailViewingMode viewingMode){
        if(this.viewingMode == viewingMode){
            return;
        }
        else {
            this.viewingMode = viewingMode;
        }

        for(HashSet<TrailObject> trailObjectsAtTick : trailObjectsByTick){
            for(TrailObject trailObject : trailObjectsAtTick){
                if(viewingMode == TrailViewingMode.travel){
                    if( !trailObject.isExplosion()){
                        trailObject.show();
                    }
                    else {
                        trailObject.hide();
                    }
                }
                else if (viewingMode == TrailViewingMode.explosion) {
                    if(trailObject.isExplosion()){
                        trailObject.show();
                    }
                    else{
                        trailObject.hide();
                    }
                }
                else {
                    trailObject.show();
                }
            }
        }
    }

    public void hide(){
        if(viewingMode != null){
            for(HashSet<TrailObject> trailObjectsAtTick : trailObjectsByTick) {
                for (TrailObject trailObject : trailObjectsAtTick) {
                    trailObject.hide();
                }
            }
            viewingMode = null;
        }
    }

    public Player getPlayer() {
        return player;
    }
}
