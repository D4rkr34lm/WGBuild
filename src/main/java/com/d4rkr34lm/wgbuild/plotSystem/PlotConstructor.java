package com.d4rkr34lm.wgbuild.plotSystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Logger;

import com.d4rkr34lm.wgbuild.WGBuild;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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
import org.jetbrains.annotations.NotNull;

public class PlotConstructor implements Listener, CommandExecutor {
	
	private WGBuild parent;
	Clipboard plotSchem = null;
	
	public PlotConstructor(WGBuild parent) {
		this.parent = parent;

		File file = new File("./plugins/WGBuild/plot.schem");
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
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if(sender instanceof Player){
			Player player = (Player) sender;

			switch(args[0]){
				case "new":
					createNewPlot(player.getLocation());
					break;
			}
		}
		return  false;
	}

	public void createNewPlot(Location placementLocation){
		Logger logger = parent.getLogger();

		int x = placementLocation.getBlockX();
		int y = placementLocation.getBlock().getLocation().getBlockY();
		int z = placementLocation.getBlock().getLocation().getBlockZ();

		parent.getServer().broadcastMessage("Plot construction started at " + x + " " + y + " " + z);

		Plot plot = new Plot(new Location(placementLocation.getWorld(), x, y, z), plotSchem);

		if(parent.addPlot(plot)){


			try (EditSession editSession = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(placementLocation.getWorld()))) {
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


	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		if(e.getBlock().getType() == Material.BLACK_SHULKER_BOX) {
			createNewPlot(e.getBlock().getLocation());
			e.setCancelled(true);
		}
	}
}
