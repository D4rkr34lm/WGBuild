package com.d4rkr34lm.wgbuild;

import java.util.ArrayList;

import org.bukkit.plugin.java.JavaPlugin;

import com.d4rkr34lm.wgbuild.plotSystem.Plot;
import com.d4rkr34lm.wgbuild.plotSystem.PlotConstructor;

public class WGBuild extends JavaPlugin {

	private ArrayList<Plot> plots = new ArrayList<Plot>();
	
	@Override
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(new PlotConstructor(this), this);
	}
	
}
