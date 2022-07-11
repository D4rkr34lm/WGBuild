package com.d4rkr34lm.wgbuild;

import java.io.*;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import com.d4rkr34lm.wgbuild.plotSystem.Plot;
import com.d4rkr34lm.wgbuild.plotSystem.PlotConstructor;

public class WGBuild extends JavaPlugin {

	private ArrayList<Plot> plots = new ArrayList<Plot>();
	private int idCounter = 1;
	
	@Override
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(new PlotConstructor(this), this);
		loadPlotData();
	}

	@Override
	public void onDisable(){
		savePlotData();
	}

	public void loadPlotData(){
		File file = new File("./plugins/WGBuild/plots.dat");

		try{
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			String line = bufferedReader.readLine();
			while (line != null){
				String[] lineParts = line.split(",");

				Location placementLocation = new Location(Bukkit.getWorld("world"), Integer.parseInt(lineParts[0]), Integer.parseInt(lineParts[1]), Integer.parseInt(lineParts[2]));
				Plot plot = new Plot(placementLocation);
				addPlot(plot);

				line = bufferedReader.readLine();
			}

			bufferedReader.close();
		}
		catch (IOException err){
			err.printStackTrace();
		}
	}

	public void savePlotData(){
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

	public boolean addPlot(Plot newPlot) {
		for(Plot plot : plots){
			if(newPlot.isColliding(plot)){
				return  false;
			}
		}

		plots.add(newPlot);
		newPlot.setId(idCounter);
		idCounter++;
		return true;
	}
}
