package com.d4rkr34lm.wgbuild.plotSystem;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class Plot {
	private Location placementLocation;
	private Location corner;
	private Vector size;
	private int id;
	
	public Plot(Location placementLocation) {
		this.placementLocation = placementLocation;
		corner = new Location(placementLocation.getWorld(), placementLocation.getBlockX() - 33, placementLocation.getBlockY(), placementLocation.getBlockZ() - 47);
		size = new Vector(67.0, 1.0, 144.0);
	}

	public  boolean isColliding(Plot plot){
		if(plot.getCorner().getWorld() == corner.getWorld()){
			return  plot.getCorner().getBlockX() + plot.getSize().getBlockX() > corner.getBlockX() &&
					plot.getCorner().getBlockZ() + plot.getSize().getBlockZ() > corner.getBlockZ() &&
					corner.getBlockX() + size.getBlockX() > plot.getCorner().getBlockX() &&
					corner.getBlockZ() + size.getBlockZ() > plot.getCorner().getBlockZ();
		}
		else {
			return false;
		}
	}

	public boolean isInside(Location location) {
		return location.getWorld() == this.corner.getWorld()
				&& location.getBlockX() >= corner.getBlockX() && location.getBlockX() <= corner.getBlockX() + size.getBlockX()
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

	public Vector getSize(){
		return  size;
	}
}
