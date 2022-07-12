package com.d4rkr34lm.wgbuild.trail;

import com.d4rkr34lm.wgbuild.WGBuild;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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

        Location location = information.getLocation();

        double velX = velocity.getX();
        double velZ = velocity.getZ();
        double velY = velocity.getY();

        double posX = location.getX();
        double posZ = location.getZ();
        double posY = location.getY();

        Bukkit.broadcastMessage("\n"
                        + INFORMATION_ABOUT_ENTITY + "\n"
                        + "§aTick Time: §6" + information.getTickTime() + "\n"
                        + "§aVelocity: " + "\n"
                        + "     §6X: " + velX + " blocks/sec \n"
                        + "     §6Z: " + velZ + " blocks/sec \n"
                        + "     §6Y: " + velY + " blocks/sec \n"
                        + "§aPosition: " + "\n"
                        + "     §6X: " + posX + "\n"
                        + "     §6Z: " + posZ + "\n"
                        + "     §6Y: " + posY + "\n"

                        + "§aExploded: §6" + information.getExplosion());



    }

}
