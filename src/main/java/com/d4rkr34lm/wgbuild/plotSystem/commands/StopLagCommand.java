package com.d4rkr34lm.wgbuild.plotSystem.commands;

import com.d4rkr34lm.wgbuild.WGBuild;
import com.d4rkr34lm.wgbuild.plotSystem.Plot;
import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import com.destroystokyo.paper.event.block.TNTPrimeEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.jetbrains.annotations.NotNull;

public class StopLagCommand implements CommandExecutor, Listener {

    private WGBuild plugin;

    public StopLagCommand(WGBuild plugin){
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        plugin.getCommand("sl").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;

            for(Plot plot : plugin.getPlots()){
                if(plot.isInsideArea(player.getLocation())){
                    plot.setStopLagEnabled(!plot.isStopLagEnabled());
                    if(plot.isStopLagEnabled()){
                        plugin.getServer().broadcastMessage("StopLag has been enabled");
                    }
                    else {
                        plugin.getServer().broadcastMessage("StopLag has been disabled");
                    }
                    plugin.getScoreboardManager().updateScoreboard(player);
                }
            }
        }
        return false;
    }

    @EventHandler
    public void onBlockDestroy(BlockDestroyEvent event){
        for(Plot plot : plugin.getPlots()){
            if(plot.isInsideArea(event.getBlock().getLocation())){
                if(plot.isStopLagEnabled()){
                    event.setCancelled(true);
                }
                return;
            }
        }
    }

    @EventHandler
    public void onRedstoneUpdate(BlockRedstoneEvent event){
        for(Plot plot : plugin.getPlots()){
            if(plot.isInsideArea(event.getBlock().getLocation())){
                if(plot.isStopLagEnabled()){
                    event.setNewCurrent(event.getOldCurrent());
                }
                return;
            }
        }
    }

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event){
        for(Plot plot : plugin.getPlots()){
            if(plot.isInsideArea(event.getBlock().getLocation())){
                if(plot.isStopLagEnabled()){
                    event.setCancelled(true);
                }
                return;
            }
        }
    }

    @EventHandler
    public void onTntPrime(TNTPrimeEvent event){
        for(Plot plot : plugin.getPlots()){
            if(plot.isInsideArea(event.getBlock().getLocation())){
                if(plot.isStopLagEnabled()){
                    event.setCancelled(true);
                }
                return;
            }
        }
    }

    @EventHandler
    public void onPistonExtend(BlockPistonExtendEvent event){
        for(Plot plot : plugin.getPlots()){
            if(plot.isInsideArea(event.getBlock().getLocation())){
                if(plot.isStopLagEnabled()){
                    event.setCancelled(true);
                }
                return;
            }
        }
    }

    @EventHandler
    public void onPistonRetract(BlockPistonRetractEvent event){
        for(Plot plot : plugin.getPlots()){
            if(plot.isInsideArea(event.getBlock().getLocation())){
                if(plot.isStopLagEnabled()){
                    event.setCancelled(true);
                }
                return;
            }
        }
    }
}
