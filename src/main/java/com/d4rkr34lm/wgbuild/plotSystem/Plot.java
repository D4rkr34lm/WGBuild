package com.d4rkr34lm.wgbuild.plotSystem;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.math.BlockVector3;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class Plot {

	private Clipboard plotSchem;
	private Location placementLocation;
	private Location corner;
	private BlockVector3 size;
	private int id;
	
	public Plot(Location placementLocation, Clipboard plotSchem) {
		this.placementLocation = placementLocation;
		size = plotSchem.getDimensions();

		int xOffset = Math.abs(plotSchem.getOrigin().getBlockX() - plotSchem.getMinimumPoint().getBlockX());
		int zOffset = Math.abs(plotSchem.getOrigin().getBlockZ() - plotSchem.getMinimumPoint().getBlockZ());
		corner = new Location(placementLocation.getWorld(), placementLocation.getBlockX() - xOffset, placementLocation.getBlockY(), placementLocation.getBlockZ() - zOffset);
	}

	public  boolean isColliding(Plot plot){
		return  plot.getCorner().getBlockX() + plot.getSize().getBlockX() > corner.getBlockX() &&
				plot.getCorner().getBlockZ() + plot.getSize().getBlockZ() > corner.getBlockZ() &&
				corner.getBlockX() + size.getBlockX() > plot.getCorner().getBlockX() &&
				corner.getBlockZ() + size.getBlockZ() > plot.getCorner().getBlockZ();
	}

	public boolean isInside(Location location) {
		return location.getBlockX() >= corner.getBlockX() && location.getBlockX() <= corner.getBlockX() + size.getBlockX()
				&& location.getBlockZ() >= corner.getBlockZ() && location.getBlockZ() <= corner.getBlockZ() + size.getBlockZ();
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(int id){
		return  id;
	}

	public Location getPlacementLocation() {
		return placementLocation;
	}

	public Location getCorner(){
		return  corner;
	}

	public BlockVector3 getSize(){
		return  size;
	}
}
