package com.d4rkr34lm.wgbuild;

import com.d4rkr34lm.wgbuild.plotSystem.Plot;
import com.d4rkr34lm.wgbuild.plotSystem.PlotManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardManager implements Listener {
    private WGBuild plugin;

    public ScoreboardManager(WGBuild plugin){
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){
        updateScoreboard(e.getPlayer());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        updateScoreboard(e.getPlayer());
    }


    public void updateScoreboard(Player player){
        for(Plot plot : PlotManager.getPlots()){
            if(plot.isInsideArea(player.getLocation())){
                org.bukkit.scoreboard.ScoreboardManager manager = Bukkit.getScoreboardManager();
                Scoreboard scoreboard = manager.getNewScoreboard();
                Objective objective = scoreboard.registerNewObjective("test", "dummy");

                objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                objective.setDisplayName(ChatColor.BLUE + "Plot: " + ChatColor.WHITE + plot.getId());

                Score tnt;
                if(plot.isTntEnabled()){
                    tnt = objective.getScore("TNT: " + ChatColor.GREEN + "on");
                }
                else {
                    tnt = objective.getScore("TNT: " + ChatColor.RED + "off");
                }
                tnt.setScore(3);

                Score cannonProtection;
                if(plot.isCannonProtectionEnabled()){
                    cannonProtection = objective.getScore("Cannon protection: " + ChatColor.GREEN + "on");
                }
                else {
                    cannonProtection = objective.getScore("Cannon protection: " + ChatColor.RED + "off");
                }
                cannonProtection.setScore(2);

                Score stopLag;
                if(plot.isStopLagEnabled()){
                    stopLag = objective.getScore("StopLag: " + ChatColor.GREEN + "on");
                }
                else {
                    stopLag = objective.getScore("StopLag: " + ChatColor.RED + "off");
                }
                stopLag.setScore(1);

                player.setScoreboard(scoreboard);

                return;
            }
        }

        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
    }
}
