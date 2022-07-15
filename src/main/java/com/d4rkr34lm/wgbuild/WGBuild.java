package com.d4rkr34lm.wgbuild;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;


import com.d4rkr34lm.wgbuild.plotSystem.PlotCommand;
import com.d4rkr34lm.wgbuild.trail.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.FallingBlock;
import org.bukkit.plugin.PluginManager;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import com.d4rkr34lm.wgbuild.plotSystem.Plot;
import com.d4rkr34lm.wgbuild.plotSystem.PlotConstructor;

public class WGBuild extends JavaPlugin {

	private ArrayList<Plot> plots = new ArrayList<Plot>();
  private int idCounter = 1;

	private static boolean recordingTrail = false;
	private static boolean waitingToStartRecording = false;
	private static ArrayList<TrailObject> trail = new ArrayList<TrailObject>();
	private static HashMap<FallingBlock, TrailObject> lookupTable = new HashMap<FallingBlock, TrailObject>();
	private static int tickTime;
	private static int tickStart = 80;


	@Override
	public void onEnable() {
		loadPlotData();
		registerEventListeners();
		registerCommandListeners();
	}

	@Override
	public void onDisable(){
		TrailManager.removeTrail(false);
    	savePlotData();
	}

	public void registerEventListeners(){

		PluginManager pm = Bukkit.getPluginManager();

		pm.registerEvents(new PlotConstructor(this), this);

		for(Plot plot : plots){
			pm.registerEvents(plot, this);
		}

		pm.registerEvents(new TntPrimeListener(this), this);
		pm.registerEvents(new TntExplosionListener(), this);
		pm.registerEvents(new TrailClickListener(), this);
		pm.registerEvents(new GuiClickListener(this), this);
	}

	public void registerCommandListeners(){
		getCommand("trail").setExecutor(new TrailCommand());
    	getCommand("plot").setExecutor(new PlotCommand(this));
	}

	public static void setRecordingTrail(boolean newState){
		recordingTrail = newState;
	}

	public static boolean isRecordingTrail(){
		return recordingTrail;
	}

	public static void setWaitingToStartRecording(boolean newState){
		waitingToStartRecording = newState;
	}

	public static boolean isWaitingToStartRecording(){
		return waitingToStartRecording;
	}

	public static ArrayList<TrailObject> getTrail(){
		return trail;
	}

	public static void addTrail(TrailObject to){
		trail.add(to);
	}

	public static void clearTrail(){
		trail.clear();
	}

	public static void resetTickTime(){
		tickTime = tickStart;
	}

	public static void addTickTime(){
		tickTime++;
	}

	public static int getTickTime(){
		return tickTime;
	}

	public static int getTickStart(){
		return tickStart;
	}

	public static void putEntry(FallingBlock fb, TrailObject t){
		lookupTable.put(fb, t);
	}

	public static HashMap<FallingBlock, TrailObject> getLookupTable(){
		return lookupTable;
	}

	public static void clearLookupTable() {
		lookupTable.clear();	
	}


	/*
	 * Plot System
	 */
	public void loadPlotData(){
		Clipboard plotSchem = null;
		File schemFile = new File("./plugins/WGBuild/plot.schem");
		ClipboardFormat format = ClipboardFormats.findByFile(schemFile);
		ClipboardReader reader;
		try {
			reader = format.getReader(new FileInputStream(schemFile));
			plotSchem = reader.read();
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
				Plot plot = new Plot(placementLocation, plotSchem, this);
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
		Bukkit.getPluginManager().registerEvents(newPlot, this);
		newPlot.setId(idCounter);
		idCounter++;
		return true;
	}

	public ArrayList<Plot> getPlots(){
		return  plots;
	}
}
