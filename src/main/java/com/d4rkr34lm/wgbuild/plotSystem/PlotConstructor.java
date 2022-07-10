package com.d4rkr34lm.wgbuild.plotSystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;

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
	
	private JavaPlugin parent;
	
	public PlotConstructor(JavaPlugin parent) {
		this.parent = parent;
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		if(e.getBlock().getType() == Material.BLACK_SHULKER_BOX) {
			Logger logger = parent.getLogger();
			
			int x = e.getBlock().getLocation().getBlockX();
			int y = e.getBlock().getLocation().getBlockY();
			int z = e.getBlock().getLocation().getBlockZ();
			
			logger.log(Level.INFO, "Plot construction started at " + x + " " + y + " " + z);
			
			File file = new File("./plugins/WGBuild/baseplate.schem");
			Clipboard baseplate = null;

			ClipboardFormat format = ClipboardFormats.findByFile(file);
			ClipboardReader reader;
			try {
				reader = format.getReader(new FileInputStream(file));
				baseplate = reader.read();
			} 
			catch (IOException err) {
				err.printStackTrace();
			}

			try (EditSession editSession = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(e.getBlock().getWorld()))) {
			    Operation operation = new ClipboardHolder(baseplate)
			            .createPaste(editSession)
			            .to(BlockVector3.at(x, y, z))
			            .build();
			    Operations.complete(operation);
			} catch (WorldEditException err) {
				err.printStackTrace();
			}
			
			logger.log(Level.INFO, "Plot construction finished");
			
		}
	}
}
