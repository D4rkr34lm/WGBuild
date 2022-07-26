package com.d4rkr34lm.wgbuild.plotSystem.commands;

import com.d4rkr34lm.wgbuild.WGBuild;
import com.d4rkr34lm.wgbuild.plotSystem.Plot;
import com.d4rkr34lm.wgbuild.plotSystem.PlotManager;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class PlotCommand implements CommandExecutor {

    private WGBuild plugin;
    private Clipboard baseplate;

    public PlotCommand(WGBuild plugin){
        this.plugin = plugin;

        File baseplateSchematic = new File("./plugins/WGBuild/ground.schem");

        ClipboardFormat format = ClipboardFormats.findByFile(baseplateSchematic);
        ClipboardReader reader;
        try {
            reader = format.getReader(new FileInputStream(baseplateSchematic));
            baseplate = reader.read();
            reader.close();
        }
        catch (IOException err) {
            err.printStackTrace();
        }

        plugin.getCommand("plot").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            createNewPlot(player.getLocation());
            plugin.getScoreboardManager().updateScoreboard(player);
        }
        return  false;
    }

    public void createNewPlot(Location placementLocation){
        int x = placementLocation.getBlockX();
        int y = placementLocation.getBlock().getLocation().getBlockY();
        int z = placementLocation.getBlock().getLocation().getBlockZ();

        Plot newPlot = new Plot(new Location(placementLocation.getWorld(), x, y, z), baseplate);

        for(Plot plot : PlotManager.getPlots()){
            if(plot.isColliding(newPlot)){
                return;
            }
        }

        PlotManager.addPlot(newPlot);
        newPlot.pasteClipboard(baseplate, true);
    }
}
