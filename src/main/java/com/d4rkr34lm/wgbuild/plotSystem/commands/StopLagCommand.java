package com.d4rkr34lm.wgbuild.plotSystem.commands;

import com.d4rkr34lm.wgbuild.WGBuild;
import com.d4rkr34lm.wgbuild.plotSystem.Plot;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StopLagCommand implements CommandExecutor {

    private WGBuild parent;

    public StopLagCommand(WGBuild parent){
        this.parent = parent;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;

            for(Plot plot : parent.getPlots()){
                if(plot.isInsideArea(player.getLocation())){
                    plot.setStopLagEnabled(!plot.isStopLagEnabled());
                    if(plot.isStopLagEnabled()){
                        parent.getServer().broadcastMessage("StopLag has been enabled");
                    }
                    else {
                        parent.getServer().broadcastMessage("StopLag has been disabled");
                    }
                    parent.getScoreboardManager().updateScoreboard(player);
                }
            }
        }
        return false;
    }
}
