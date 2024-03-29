package com.d4rkr34lm.wgbuild.trail;

import com.d4rkr34lm.wgbuild.WGBuild;
import com.d4rkr34lm.wgbuild.plotSystem.Plot;
import com.d4rkr34lm.wgbuild.plotSystem.PlotManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class TrailCommand implements CommandExecutor {

    private WGBuild plugin;

    public TrailCommand(WGBuild plugin){
        this.plugin = plugin;
        plugin.getCommand("trail").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;

        switch (args[0]){
            case "new":
                for(Plot plot : PlotManager.getPlots()){
                    if(plot.isInsideArea(player.getLocation())){
                        TrailManager.issueRecording(plot, player);
                    }
                }
                break;
            case "hide":
                if(TrailManager.getTrails().containsKey(player)){
                    TrailManager.getTrails().get(player).hide();
                }
                break;

            case "show":
                if(TrailManager.getTrails().containsKey(player)){
                    TrailManager.getTrails().get(player).show(TrailViewingMode.normal);
                }
                break;
            case "explosion":
                if(TrailManager.getTrails().containsKey(player)){
                    TrailManager.getTrails().get(player).show(TrailViewingMode.explosion);
                }
                break;
            case "travel":
                if(TrailManager.getTrails().containsKey(player)){
                    TrailManager.getTrails().get(player).show(TrailViewingMode.travel);
                }
                break;

        }

        return false;
    }
}
