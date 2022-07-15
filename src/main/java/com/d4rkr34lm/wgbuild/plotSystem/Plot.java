package com.d4rkr34lm.wgbuild.plotSystem;

import com.d4rkr34lm.wgbuild.WGBuild;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.math.BlockVector3;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class Plot implements Listener {

	private WGBuild parent;

	private Clipboard plotSchem;
	private Location placementLocation;
	private Location corner;
	private BlockVector3 size;
	private int id;

	/*
	 * Settings
	 */
	private boolean tntEnabled = true;
	private boolean cannonProtectionEnabled = true;
	private boolean stopLag = false;
	
	public Plot(Location placementLocation, Clipboard plotSchem, WGBuild parent) {
		this.placementLocation = placementLocation;
		size = plotSchem.getDimensions();
		this.parent = parent;

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

	@EventHandler
	public void onTntExplosion(EntityExplodeEvent e) {
		if(e.getEntityType() == EntityType.PRIMED_TNT){
			if(isInsideArea(e.getLocation())){
				if(!tntEnabled){
					e.blockList().clear();
				}
				else if(isInProtectedArea(e.getLocation()) && cannonProtectionEnabled){
					e.blockList().clear();

					if(!e.getEntity().isInWater()){
						parent.getServer().broadcastMessage("Unwanted explosion has been blocked");
					}
				}
			}
		}
	}

	@EventHandler
	public void onBlockUpdate(BlockPhysicsEvent e){
		if(isInsideArea(e.getBlock().getLocation())){
			if(stopLag){
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onRedstoneUpdate(BlockRedstoneEvent e){
		if(isInsideArea(e.getBlock().getLocation())){
			if(stopLag){
				e.setNewCurrent(e.getOldCurrent());
			}
		}
	}

	public boolean isInsideArea(Location location) {
		return location.getBlockX() >= corner.getBlockX() && location.getBlockX() <= corner.getBlockX() + size.getBlockX()
				&& location.getBlockZ() >= corner.getBlockZ() && location.getBlockZ() <= corner.getBlockZ() + size.getBlockZ();
	}

	public boolean isInProtectedArea(Location location){
		return location.getBlockX() >= corner.getBlockX() && location.getBlockX() <= corner.getBlockX() + size.getBlockX()
				&& location.getBlockZ() >= corner.getBlockZ() && location.getBlockZ() <= placementLocation.getBlockZ() + 25;
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

	public void setTntEnabled(boolean tntEnabled){
		this.tntEnabled = tntEnabled;
	}

	public boolean isTntEnabled(){
		return  tntEnabled;
	}

	public void setCannonProtectionEnabled(boolean cannonProtrectionEnabled){
		this.cannonProtectionEnabled = cannonProtrectionEnabled;
	}

	public boolean isCannonProtectionEnabled(){
		return  cannonProtectionEnabled;
	}

	public void setStopLag(boolean stopLag){
		this.stopLag = stopLag;
	}

	public boolean isStopLag(){
		return  stopLag;
	}
}
