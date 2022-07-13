package com.d4rkr34lm.wgbuild;

import java.util.ArrayList;

import com.d4rkr34lm.wgbuild.Simulator.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.d4rkr34lm.wgbuild.plotSystem.Plot;
import com.d4rkr34lm.wgbuild.plotSystem.PlotConstructor;

public class WGBuild extends JavaPlugin {

	private ArrayList<Plot> plots = new ArrayList<Plot>();

	private static JavaPlugin plugin;

	@Override
	public void onEnable() {

		plugin = this;

		registerListener();
	}

	public static JavaPlugin getPlugin(){
		return plugin;
	}

	public void registerListener(){
		PluginManager pm = Bukkit.getPluginManager();

		pm.registerEvents(new PlotConstructor(this), this);

		pm.registerEvents(new SimulationBlockPlaceListener(), this);
		pm.registerEvents(new SimulationBlockBreakListener(), this);
		pm.registerEvents(new SimulationBlockAccessListener(), this);
		pm.registerEvents(new SimulationGuiClickListener(), this);
	}

}
