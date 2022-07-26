package com.d4rkr34lm.wgbuild.plotSystem.commands;

import com.d4rkr34lm.wgbuild.WGBuild;
import com.d4rkr34lm.wgbuild.plotSystem.Plot;
import com.d4rkr34lm.wgbuild.plotSystem.PlotManager;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class PasteCommands implements CommandExecutor {
    private WGBuild plugin;
    private Clipboard[] clipboards = new Clipboard[10];
    private String[] pasteCommands = {"ground", "tb1", "tb2", "tb3", "tb3s", "tbm", "frm1", "frm2", "frm3", "frmm"};

    public PasteCommands(WGBuild plugin){
        this.plugin = plugin;

        ArrayList<File> schematics = new ArrayList<File>();

        for(String pasteCommand : pasteCommands){
            schematics.add(new File("./plugins/WGBuild/" + pasteCommand + ".schem"));
        }

        for(int i = 0; i < pasteCommands.length; i++){
            File file = schematics.get(i);
            ClipboardFormat format = ClipboardFormats.findByFile(file);
            ClipboardReader reader;
            try {
                reader = format.getReader(new FileInputStream(file));
                clipboards[i] = reader.read();
                reader.close();
            }
            catch (IOException err) {
                err.printStackTrace();
            }
        }

        for(String pasteCommand : pasteCommands){
            plugin.getCommand(pasteCommand).setExecutor(this);
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;

            for(Plot plot : PlotManager.getPlots()){
                if(plot.isInsideArea(player.getLocation())){
                    int index = Arrays.asList(pasteCommands).indexOf(command.getName());
                    if(index > 5){
                        plot.pasteClipboard(clipboards[index], true);
                    }
                    else {
                        plot.pasteClipboard(clipboards[index], false);
                    }
                }
            }
        }

        return false;
    }
}
