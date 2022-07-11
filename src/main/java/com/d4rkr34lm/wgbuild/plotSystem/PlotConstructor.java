package com.d4rkr34lm.wgbuild.plotSystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Logger;

import com.d4rkr34lm.wgbuild.WGBuild;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;

public class PlotConstructor implements Listener{
	
	private WGBuild parent;
	Clipboard plotSchem = null;
	
	public PlotConstructor(WGBuild parent) {
		this.parent = parent;

		File file = new File("./plugins/WGBuild/baseplate.schem");
		ClipboardFormat format = ClipboardFormats.findByFile(file);
		ClipboardReader reader;
		try {
			reader = format.getReader(new FileInputStream(file));
			plotSchem = reader.read();
		}
		catch (IOException err) {
			err.printStackTrace();
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		if(e.getBlock().getType() == Material.BLACK_SHULKER_BOX) {
			Logger logger = parent.getLogger();
			
			int x = e.getBlock().getLocation().getBlockX();
			int y = e.getBlock().getLocation().getBlockY();
			int z = e.getBlock().getLocation().getBlockZ();

			parent.getServer().broadcastMessage("Plot construction started at " + x + " " + y + " " + z);

			Plot plot = new Plot(new Location(e.getBlock().getWorld(), x, y, z), plotSchem);

			if(parent.addPlot(plot)){


				try (EditSession editSession = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(e.getBlock().getWorld()))) {
					Operation operation = new ClipboardHolder(plotSchem)
							.createPaste(editSession)
							.to(BlockVector3.at(x, y, z))
							.build();
					Operations.complete(operation);
				} catch (WorldEditException err) {
					err.printStackTrace();
				}

				parent.getServer().broadcastMessage("Plot construction finished");
			}
			else{
				parent.getServer().broadcastMessage("Plot can not be constructed here!");
			}

			e.setCancelled(true);
		}
	}
}
