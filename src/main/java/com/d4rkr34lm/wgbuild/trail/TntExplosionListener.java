package com.d4rkr34lm.wgbuild.trail;

import com.d4rkr34lm.wgbuild.WGBuild;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class TntExplosionListener implements Listener {

    @EventHandler
    public void onTntExplosion(EntityExplodeEvent e) {

        if(!e.getEntityType().equals(EntityType.PRIMED_TNT)){
            return;
        }

        if(!WGBuild.isRecordingTrail()){
            return;
        }

        WGBuild.addTrail(new TrailObject(e.getEntity(), WGBuild.getTickTime(), true));

    }
}
