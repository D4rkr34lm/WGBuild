package com.d4rkr34lm.wgbuild.plotSystem;

import com.d4rkr34lm.wgbuild.ScoreboardManager;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.*;
import java.util.ArrayList;

public class PlotManager {
    private static ArrayList<Plot> plots = new ArrayList<Plot>();
    private static int plotIdCounter = 1;


    public static void loadPlots(){
        Clipboard baseplate = null;

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

        File plotsFile = new File("./plugins/WGBuild/plots.dat");
        try{
            FileReader fileReader = new FileReader(plotsFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line = bufferedReader.readLine();
            while (line != null && !line.equals("")){
                String[] lineParts = line.split(",");

                Location placementLocation = new Location(Bukkit.getWorld("world"), Integer.parseInt(lineParts[0]), Integer.parseInt(lineParts[1]), Integer.parseInt(lineParts[2]));
                Plot plot = new Plot(placementLocation, baseplate);
                addPlot(plot);

                line = bufferedReader.readLine();
            }

            bufferedReader.close();
        }
        catch (IOException err){
            err.printStackTrace();
        }
    }

    public static void savePlots(){
        File file = new File("./plugins/WGBuild/plots.dat");

        try{
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            for(Plot plot : plots){
                String line = plot.getPlacementLocation().getBlockX() + "," + plot.getPlacementLocation().getBlockY() + "," + plot.getPlacementLocation().getBlockZ() + "\n";

                bufferedWriter.write(line);
            }

            bufferedWriter.close();
        }
        catch (IOException err){
            err.printStackTrace();
        }
    }

    public static void addPlot(Plot newPlot) {
        plots.add(newPlot);
        newPlot.setId(plotIdCounter);
        plotIdCounter++;
    }

    public static ArrayList<Plot> getPlots(){
        return  plots;
    }

}
