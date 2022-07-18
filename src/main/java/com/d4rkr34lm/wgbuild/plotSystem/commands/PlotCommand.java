package com.d4rkr34lm.wgbuild.plotSystem.commands;

import com.d4rkr34lm.wgbuild.WGBuild;
import com.d4rkr34lm.wgbuild.plotSystem.Plot;
import com.d4rkr34lm.wgbuild.plotSystem.PlotConstructor;
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
            constructor.createNewPlot(player.getLocation());
            parent.getScoreboardManager().updateScoreboard(player);
        }
        return  false;
    }
}
