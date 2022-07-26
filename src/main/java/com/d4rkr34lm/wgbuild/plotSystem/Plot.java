package com.d4rkr34lm.wgbuild.plotSystem;

import com.d4rkr34lm.wgbuild.WGBuild;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class Plot implements Listener {

	/*
	 * Data
	 */
	private Location placementLocation;
	private Location corner;
	private BlockVector3 size;
	private int id;

	/*
	 * Settings
	 */
	private boolean tntEnabled = true;
	private boolean cannonProtectionEnabled = true;
	private boolean stopLagEnabled = false;

	public Plot(Location placementLocation, Clipboard baseplate) {
		this.placementLocation = placementLocation;
		size = baseplate.getDimensions();

		int xOffset = Math.abs(baseplate.getOrigin().getBlockX() - baseplate.getMinimumPoint().getBlockX());
		int zOffset = Math.abs(baseplate.getOrigin().getBlockZ() - baseplate.getMinimumPoint().getBlockZ());
		corner = new Location(placementLocation.getWorld(), placementLocation.getBlockX() - xOffset, placementLocation.getBlockY(), placementLocation.getBlockZ() - zOffset);
	}

	public  boolean isColliding(Plot plot){
		return  plot.getCorner().getBlockX() + plot.getSize().getBlockX() > corner.getBlockX() &&
				plot.getCorner().getBlockZ() + plot.getSize().getBlockZ() > corner.getBlockZ() &&
				corner.getBlockX() + size.getBlockX() > plot.getCorner().getBlockX() &&
				corner.getBlockZ() + size.getBlockZ() > plot.getCorner().getBlockZ();
	}

	public void pasteClipboard(Clipboard clipboard, boolean ignoreAir){
		try (EditSession editSession = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(placementLocation.getWorld()))) {
			Operation operation = new ClipboardHolder(clipboard)
					.createPaste(editSession)
					.to(BlockVector3.at(placementLocation.getBlockX(), placementLocation.getBlockY(), placementLocation.getBlockZ()))
					.ignoreAirBlocks(ignoreAir)
					.build();
			Operations.complete(operation);
		} catch (WorldEditException err) {
			err.printStackTrace();
		}
	}

	/*
	 * Logic Methods
	 */
	public boolean isInsideArea(Location location) {
		return location.getBlockX() >= corner.getBlockX() && location.getBlockX() <= corner.getBlockX() + size.getBlockX()
				&& location.getBlockZ() >= corner.getBlockZ() && location.getBlockZ() <= corner.getBlockZ() + size.getBlockZ();
	}

	public boolean isInProtectedArea(Location location){
		return location.getBlockX() >= corner.getBlockX() && location.getBlockX() <= corner.getBlockX() + size.getBlockX()
				&& location.getBlockZ() >= corner.getBlockZ() && location.getBlockZ() <= placementLocation.getBlockZ() + 25;
	}

	/*
	 * Getters / Setters
	 */
	public void setId(int id){
		this.id = id;
	}

	public int getId(){
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

	public void setStopLagEnabled(boolean stopLagEnabled){
		this.stopLagEnabled = stopLagEnabled;
	}

	public boolean isStopLagEnabled(){
		return stopLagEnabled;
	}
}

