package com.d4rkr34lm.wgbuild.tools;

import com.d4rkr34lm.wgbuild.WGBuild;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class CannonReloader implements Listener {

    private final String CHAT_TAG = "§f[§1WGBuild§f] §8";
    private WGBuild plugin;
    private HashMap<Player, HashSet<Block>> reloaderClipboards = new HashMap<>();
    private HashSet<Player> playersRecording = new HashSet<>();
    private HashSet<Player> playersAboutToDeleteClipboard = new HashSet<>();
    private Material reloaderMaterial = Material.TNT_MINECART;

    public CannonReloader(WGBuild plugin){
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        if(event.getItem() != null && event.getItem().getType() == reloaderMaterial){
            if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                if(reloaderClipboards.containsKey(event.getPlayer())){
                    if(playersAboutToDeleteClipboard.contains(event.getPlayer())){
                        playersAboutToDeleteClipboard.remove(event.getPlayer());
                        reloaderClipboards.remove(event.getPlayer());
                        event.getPlayer().sendMessage(CHAT_TAG + " Your reloader clipboard has been deleted");
                    }
                    else {
                        playersAboutToDeleteClipboard.add(event.getPlayer());
                        event.getPlayer().sendMessage(CHAT_TAG + " You are about to delete your reloader clipboard \n if you want to continue left click again \n if u want to abort right click");
                    }
                    event.setCancelled(true);
                }
            }
            else if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if(playersAboutToDeleteClipboard.contains(event.getPlayer())){
                    playersAboutToDeleteClipboard.remove(event.getPlayer());
                    event.getPlayer().sendMessage(CHAT_TAG + "Deletion has been aborted");
                    return;
                }

                if(!playersRecording.contains(event.getPlayer())){
                    if(reloaderClipboards.containsKey(event.getPlayer())){
                        for(Block block : reloaderClipboards.get(event.getPlayer())){
                            block.setType(Material.TNT);
                        }
                        event.getPlayer().sendMessage(CHAT_TAG + " Reloader clipboard has been pasted");
                    }
                    else {
                        playersRecording.add(event.getPlayer());
                        reloaderClipboards.put(event.getPlayer(), new HashSet<>());
                        event.getPlayer().sendMessage(CHAT_TAG + " Reloader is now recording placed Tnt");
                    }
                }
                else {
                    playersRecording.remove(event.getPlayer());
                    event.getPlayer().sendMessage(CHAT_TAG + " Reloader recording has been stopped");
                }
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        if(event.getBlock().getType() == Material.TNT && playersRecording.contains(event.getPlayer())){
            reloaderClipboards.get(event.getPlayer()).add(event.getBlock());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        if(reloaderClipboards.containsKey(event.getPlayer())){
            reloaderClipboards.remove(event.getPlayer());
        }

        if(playersRecording.contains(event.getPlayer())){
            playersRecording.remove(event.getPlayer());
        }

        if(playersAboutToDeleteClipboard.contains(event.getPlayer())){
            playersAboutToDeleteClipboard.remove(event.getPlayer());
        }
    }


}
