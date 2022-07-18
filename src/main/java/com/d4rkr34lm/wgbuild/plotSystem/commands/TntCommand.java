package com.d4rkr34lm.wgbuild.plotSystem.commands;

import com.d4rkr34lm.wgbuild.WGBuild;
import com.d4rkr34lm.wgbuild.plotSystem.Plot;
import com.d4rkr34lm.wgbuild.plotSystem.PlotConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TntCommand implements CommandExecutor {

    private WGBuild parent;

    public TntCommand(WGBuild parent){
        this.parent = parent;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;

            for(Plot plot : parent.getPlots()){
                if(plot.isInsideArea(player.getLocation())){
                    plot.setTntEnabled(!plot.isTntEnabled());
                    if(plot.isTntEnabled()){
                        parent.getServer().broadcastMessage("Tnt has been enabled");
                    }
                    else {
                        parent.getServer().broadcastMessage("Tnt has been disabled");
                    }
                    parent.getScoreboardManager().updateScoreboard(player);
                }
            }
        }
        return false;
    }
}
