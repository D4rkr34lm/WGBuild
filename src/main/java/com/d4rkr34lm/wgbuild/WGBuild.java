package com.d4rkr34lm.wgbuild;

import java.util.ArrayList;

import com.d4rkr34lm.wgbuild.trail.TntExplosionListener;
import com.d4rkr34lm.wgbuild.trail.TntPrimeListener;
import com.d4rkr34lm.wgbuild.trail.TrailCommand;
import com.d4rkr34lm.wgbuild.trail.TrailObject;
import org.bukkit.plugin.java.JavaPlugin;

import com.d4rkr34lm.wgbuild.plotSystem.Plot;
import com.d4rkr34lm.wgbuild.plotSystem.PlotConstructor;

public class WGBuild extends JavaPlugin {

	private ArrayList<Plot> plots = new ArrayList<Plot>();

	private static boolean recordingTrail = false;
	private static boolean waitingToStartRecording = false;
	private static ArrayList<TrailObject> trail = new ArrayList<TrailObject>();
	private static int tickTime;
	private static int tickStart = 80;

	
	@Override
	public void onEnable() {

		registerPlugins();
		registerCommands();

	}

	public void registerPlugins(){
		this.getServer().getPluginManager().registerEvents(new PlotConstructor(this), this);

		this.getServer().getPluginManager().registerEvents(new TntPrimeListener(this), this);
		this.getServer().getPluginManager().registerEvents(new TntExplosionListener(), this);
	}

	public void registerCommands(){
		getCommand("trail").setExecutor(new TrailCommand());
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
}
