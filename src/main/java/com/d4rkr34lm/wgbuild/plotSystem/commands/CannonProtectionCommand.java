package com.d4rkr34lm.wgbuild.plotSystem.commands;

import com.d4rkr34lm.wgbuild.WGBuild;
import com.d4rkr34lm.wgbuild.plotSystem.Plot;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CannonProtectionCommand implements CommandExecutor {

    private WGBuild parent;

    public CannonProtectionCommand(WGBuild parent){
        this.parent = parent;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;

            for(Plot plot : parent.getPlots()){
                if(plot.isInsideArea(player.getLocation())){
                    plot.setCannonProtectionEnabled(plot.isCannonProtectionEnabled());
                    if(plot.isTntEnabled()){
                        parent.getServer().broadcastMessage("Cannon protection has been enabled");
                    }
                    else {
                        parent.getServer().broadcastMessage("Cannon protection has been disabled");
                    }
                    parent.getScoreboardManager().updateScoreboard(player);
                }
            }
        }
        return false;
    }
}
