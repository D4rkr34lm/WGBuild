package com.d4rkr34lm.wgbuild.trail;

import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.Material;
import org.bukkit.block.data.type.TNT;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.plugin.java.JavaPlugin;

import javax.sound.sampled.LineListener;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EntityMoveListener implements Listener {

    JavaPlugin parent;
    Logger logger;

    public EntityMoveListener(JavaPlugin parent){
        this.parent = parent;
        logger = parent.getLogger();
    }

    @EventHandler
    public void onEntityMoveEvent(EntityMoveEvent e){

        if(e.getEntityType().equals(EntityType.FALLING_BLOCK)){
            logger.log(Level.INFO, e.getEntityType().toString());
            e.setCancelled(true);
        }

    }

}
