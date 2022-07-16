package com.d4rkr34lm.wgbuild.plotSystem;

import com.d4rkr34lm.wgbuild.WGBuild;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlotCommand implements CommandExecutor {

    private WGBuild parent;
    private PlotConstructor constructor;

    public PlotCommand(WGBuild parent){
        this.parent = parent;
        constructor = new PlotConstructor(parent);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;

            switch(args[0]){
                case "new":
                    constructor.createNewPlot(player.getLocation());
                    break;
                case "tnt":
                    for(Plot plot : parent.getPlots()){
                        if(plot.isInsideArea(player.getLocation())){
                            plot.setTntEnabled(!plot.isTntEnabled());
                            if(plot.isTntEnabled()){
                                parent.getServer().broadcastMessage("Tnt has been enabled");
                            }
                            else {
                                parent.getServer().broadcastMessage("Tnt has been disabled");
                            }

                        }
                    }
                    break;
                case "protection":
                    for(Plot plot : parent.getPlots()){
                        if(plot.isInsideArea(player.getLocation())){
                            plot.setCannonProtectionEnabled(plot.isCannonProtectionEnabled());
                            if(plot.isTntEnabled()){
                                parent.getServer().broadcastMessage("Cannon protection has been enabled");
                            }
                            else {
                                parent.getServer().broadcastMessage("Cannon protection has been disabled");
                            }

                        }
                    }
                    break;
                case "sl":
                    for(Plot plot : parent.getPlots()){
                        if(plot.isInsideArea(player.getLocation())){
                            plot.setStopLagEnabled(!plot.isStopLagEnabled());
                            if(plot.isStopLagEnabled()){
                                parent.getServer().broadcastMessage("StopLag has been enabled");
                            }
                            else {
                                parent.getServer().broadcastMessage("StopLag has been disabled");
                            }
                        }
                    }
                    break;
            }
            parent.getScoreboardManager().updateScoreboard(player);
        }
        return  false;
    }
}
