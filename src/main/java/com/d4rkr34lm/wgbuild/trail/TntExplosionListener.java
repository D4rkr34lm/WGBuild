package com.d4rkr34lm.wgbuild.trail;

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



    }

}
