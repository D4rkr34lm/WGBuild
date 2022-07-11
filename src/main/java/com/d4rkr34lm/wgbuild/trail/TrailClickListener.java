package com.d4rkr34lm.wgbuild.trail;

import com.d4rkr34lm.wgbuild.WGBuild;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.util.Vector;

public class TrailClickListener implements Listener {

    private final String INFORMATION_ABOUT_ENTITY = "§aInformation about the TNT you have clicked on:";

    @EventHandler
    public void onPlayerHit(PlayerInteractAtEntityEvent e){

        if(!e.getRightClicked().getType().equals(EntityType.FALLING_BLOCK)){
            return;
        }

        Entity clicked = e.getRightClicked();
        TrailObject information = WGBuild.getLookupTable().get(clicked);
        Vector velocity = information.getVelocity();
        double velX = Math.round((velocity.getX()*1000.0))/1000.0;
        double velZ = Math.round((velocity.getZ()*1000.0))/1000.0;
        double velY = Math.round((velocity.getY()*1000.0))/1000.0;

        Bukkit.broadcastMessage("\n"
                        + INFORMATION_ABOUT_ENTITY + "\n"
                        + "§aTick Time: §6" + information.getTickTime() + "\n"
                        + "§aVelocity: " + "\n"
                        + "     §6X: " + velX + " blocks/sec \n"
                        + "     §6Z: " + velZ + " blocks/sec \n"
                        + "     §6Y: " + velY + " blocks/sec \n"
                        + "§aExploded: §6" + information.getExplosion());



    }

}
