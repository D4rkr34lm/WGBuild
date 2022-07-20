package com.d4rkr34lm.wgbuild;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import com.d4rkr34lm.wgbuild.plotSystem.commands.*;
import com.d4rkr34lm.wgbuild.plotSystem.ScoreboardManager;
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

public class WGBuild extends JavaPlugin {

	/*
	 * PlotSystem
	 */
	private ArrayList<Plot> plots = new ArrayList<Plot>();
	private ScoreboardManager scoreboardManager = new ScoreboardManager(this);
    private int plotIdCounter = 1;

	/*
	 * Trail
	 */
	private static boolean recordingTrail = false;
	private static boolean waitingToStartRecording = false;
	private static ArrayList<TrailObject> trail = new ArrayList<TrailObject>();
	private static HashMap<FallingBlock, TrailObject> lookupTable = new HashMap<FallingBlock, TrailObject>();
	private static int tickTime;
	private static int tickStart = 80;


	@Override
	public void onEnable() {
		loadPlots();

		new StopLagCommand(this);
		new TntCommand(this);
		new CannonProtectionCommand(this);
		new PlotCommand(this);
		new PasteCommands(this);

		registerEventListeners();
		registerCommandListeners();
	}

	@Override
	public void onDisable(){
		TrailManager.removeTrail(false);
    	savePlots();
	}

	public void registerEventListeners(){

		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(scoreboardManager, this);

		pm.registerEvents(new TntPrimeListener(this), this);
		pm.registerEvents(new TntExplosionListener(), this);
		pm.registerEvents(new TrailClickListener(), this);
		pm.registerEvents(new GuiClickListener(this), this);
	}

	public void registerCommandListeners(){
		getCommand("trail").setExecutor(new TrailCommand());
	}

	/*
	 * Trail
	 */
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
	public void loadPlots(){
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

	public void savePlots(){
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

	public void addPlot(Plot newPlot) {
		plots.add(newPlot);
		newPlot.setId(plotIdCounter);
		plotIdCounter++;
	}

	public ArrayList<Plot> getPlots(){
		return  plots;
	}

	public ScoreboardManager getScoreboardManager(){
		return  scoreboardManager;
	}
}
