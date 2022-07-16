package com.d4rkr34lm.wgbuild.plotSystem.commands;

import com.d4rkr34lm.wgbuild.WGBuild;
import com.d4rkr34lm.wgbuild.plotSystem.Plot;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PasteCommands implements CommandExecutor {
    private WGBuild parent;

    public PasteCommands(WGBuild parent){
        this.parent = parent;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;

            for(Plot plot : parent.getPlots()){
                if(plot.isInsideArea(player.getLocation())){
                    switch (command.getName()){
                        case "ground":
                            plot.pasteSchematic(0);
                            break;
                        case "tb1":
                            plot.pasteSchematic(1);
                            break;
                        case "tb2":
                            plot.pasteSchematic(2);
                            break;
                        case "tb3":
                            plot.pasteSchematic(3);
                            break;
                        case "frm1":
                            plot.pasteSchematic(4);
                            break;
                        case "frm2":
                            plot.pasteSchematic(5);
                            break;
                        case "frm3":
                            plot.pasteSchematic(6);
                            break;
                    }
                }
            }
        }

        return false;
    }
}
