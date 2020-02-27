package com.colink02dev;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;

public class HunterVSRunner extends JavaPlugin {
    public static HashMap<String, Game> gameInstances = new HashMap<>();// Session id, game
    public static ArrayList<PlayerInfo> playerCache = new ArrayList<>();// PlayerInfo
    private static Plugin instance;
    public static boolean stopHeartBeat = false;
    @Override
    public void onEnable() {
        instance = this;
        Game.mainWorld = Bukkit.getWorlds().get(0);
        PluginCommand start = this.getCommand("start");
        start.setExecutor(new Start());
        start.setTabCompleter(new TabCompletions());
        this.getCommand("reset").setExecutor(new Reset());
        PluginCommand join = this.getCommand("join");
        join.setExecutor(new Join());
        join.setTabCompleter(new TabCompletions());
        this.getCommand("swap").setExecutor(new Swap());
        this.getCommand("create").setExecutor(new Create());
        this.getServer().getPluginManager().registerEvents(new Events(), this);
        Bukkit.broadcastMessage(ChatColor.BLUE +"Speedrunner vs. Hunter plugin enabled!");
        for(Player p: Bukkit.getOnlinePlayers()) {
            p.sendMessage(Game.prefix + "Welcome, do /join <hunter/runner> to join a created game or create a new game with /create");
            if(PlayerInfo.getPlayerInfo(p) != null) {
                PlayerInfo pi = PlayerInfo.getPlayerInfo(p);
                if(pi.getState().equals(SessionState.INPAUSEDGAME)) {
                    if(HunterVSRunner.gameInstances.get(pi.getCurrentSession()).getPlayers().getRunner().isOnline() && HunterVSRunner.gameInstances.get(pi.getCurrentSession()).getPlayers().getAttacker().isOnline()) {
                        HunterVSRunner.gameInstances.get(pi.getCurrentSession()).unpause();
                    }
                } else {
                    p.teleport(Game.mainWorld.getSpawnLocation());
                }
            } else {
                new PlayerInfo(p, SessionState.INLOBBY);
            }

        }
    }
    @Override
    public void onDisable() {
        stopHeartBeat = true;
    }
    public static Plugin getInstance() {
        return instance;
    }
}
