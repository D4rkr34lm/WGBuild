package com.d4rkr34lm.wgbuild;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import com.d4rkr34lm.wgbuild.plotSystem.PlotManager;
import com.d4rkr34lm.wgbuild.plotSystem.commands.*;
import com.d4rkr34lm.wgbuild.simulator.SimulationManager;
import com.d4rkr34lm.wgbuild.simulator.SimulatorGuiManager;
import com.d4rkr34lm.wgbuild.tools.CannonReloader;
import com.d4rkr34lm.wgbuild.tools.RemoteDetonator;
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

	private ScoreboardManager scoreboardManager;

	@Override
	public void onEnable() {
		PlotManager.loadPlots();

		scoreboardManager = new ScoreboardManager(this);

		new StopLagCommand(this);
		new TntCommand(this);
		new CannonProtectionCommand(this);
		new PlotCommand(this);
		new PasteCommands(this);

		new SimulationManager(this);
		new SimulatorGuiManager(this);

		new TrailCommand(this);
		new TrailManager(this);

		new CannonReloader(this);
		new RemoteDetonator(this);
	}

	@Override
	public void onDisable(){
    	PlotManager.savePlots();

		for(Trail trail : TrailManager.getTrails().values()){
			trail.hide();
		}
	}

	public ScoreboardManager getScoreboardManager(){
		return  scoreboardManager;
	}
}
