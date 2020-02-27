package com.colink02dev;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Random;
import java.util.stream.Stream;

public class Game {
    private World sessionWorld;
    public static World mainWorld;
    boolean complete = true;
    private PlayerHandler players = new PlayerHandler();
    private Random ran = new Random();

    public static String prefix = ChatColor.GOLD + "Game" + ChatColor.AQUA + " >> " + ChatColor.RESET;
    private Session gameSession;
    private boolean isPaused = false;
    private boolean started = false;

    public void msgPlayers(String msg) {
        for(Player p: this.getPlayers().getAllPlayers()) {
            p.sendMessage(Game.prefix + msg);
        }
    }
    public Game() {
        gameSession = new Session(this);
    }
    public void createWorld(String seed ) {
        msgPlayers("One Moment.. Generating World");
        WorldCreator wc = new WorldCreator(gameSession.getSessionID());
        wc.environment(World.Environment.NORMAL);
        wc.type(WorldType.NORMAL);
        if(seed != null) {
            wc.seed(Long.parseLong(seed));
        }
        sessionWorld = wc.createWorld();
        sessionWorld.setAutoSave(true);
        sessionWorld.setTime(24000);
        sessionWorld.setDifficulty(Difficulty.NORMAL);
        gameSession.setWorld(sessionWorld);
        msgPlayers("World Complete!");
    }
    public void start(String seed, boolean keepItems) {
        createWorld(seed);
        Location properLoc = sessionWorld.getHighestBlockAt(sessionWorld.getSpawnLocation()).getLocation();
        properLoc.setY(properLoc.getBlockY()+5.0);
        players.getRunner().setGameMode(GameMode.SURVIVAL);
        players.getAttacker().setGameMode(GameMode.SURVIVAL);
        players.getRunner().setHealth(20d);
        players.getAttacker().setHealth(20d);
        for(Player p: players.getAllPlayers()) {
            PlayerInfo.getPlayerInfo(p).setCurrentSession(this.gameSession.getSessionID());
            PlayerInfo.getPlayerInfo(p).setState(SessionState.INGAME);
        }
        for(Player p: this.players.getAllPlayers()) {
            if(!keepItems) {
                p.getInventory().clear();
                p.setExp(0.0f);
            }
            p.setAllowFlight(false);
            if(p.getGameMode().equals(GameMode.SURVIVAL)) {
                double X = ran.nextInt(10);
                double Z = ran.nextInt(10);
                Location loc = sessionWorld.getHighestBlockAt((int)(X + properLoc.getX()), (int)(Z + properLoc.getZ())).getLocation();
                p.teleport(loc);
                p.setBedSpawnLocation(loc, true);
            } else {
                p.setGameMode(GameMode.SPECTATOR);
                p.teleport(sessionWorld.getSpawnLocation());
            }
        }
        started = true;
        msgPlayers(ChatColor.YELLOW + "GO!");
        if(!players.getAttacker().getInventory().contains(new ItemStack(Material.COMPASS, 1))) {
            players.getAttacker().getInventory().addItem(new ItemStack(Material.COMPASS, 1));
        }
    }
    public void reset() {
        started = false;
        for(Player p: this.players.getAllPlayers()) {
            p.teleport(mainWorld.getSpawnLocation());
        }
        msgPlayers(ChatColor.GREEN + "Game Reset!");
        players.setRunner(null);
        players.setAttacker(null);
        wipeWorld();
    }
    public void removeGame() {
        for(Player p: this.getPlayers().getAllPlayers()) {
            if(PlayerInfo.getPlayerInfo(p).getCurrentSession() == this.getSession().getSessionID()) {
                PlayerInfo.getPlayerInfo(p).setCurrentSession("");
                if(p.getWorld().equals(mainWorld)) {
                    PlayerInfo.getPlayerInfo(p).setState(SessionState.INLOBBY);
                } else {
                    PlayerInfo.getPlayerInfo(p).setState(SessionState.NOTPLAYING);
                }
            }
        }
        msgPlayers("Removing Game with id: " + this.gameSession.getSessionID());
        HunterVSRunner.gameInstances.remove(this.gameSession.getSessionID());
        this.gameSession = null;

    }
    public void wipeWorld() {

        if(Bukkit.getServer().getWorld(gameSession.getSessionID()).getWorldFolder().exists()) {
            msgPlayers("Removing World!");
            World toDelete = Bukkit.getServer().getWorld(gameSession.getSessionID());
            Bukkit.getServer().unloadWorld(toDelete, false);
            Bukkit.getWorlds().remove(toDelete);
            complete = false;
            while(!complete) {
                try (Stream<Path> files = Files.walk(toDelete.getWorldFolder().toPath())) {
                    files.sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
                    complete = true;
                    msgPlayers("Wiped World");
                } catch (IOException e) {
                    complete = false;
                }
                if(!complete) {
                    for(Player p: Bukkit.getOnlinePlayers()) {
                        p.teleport(mainWorld.getSpawnLocation());
                    }
                    Bukkit.unloadWorld(toDelete, false);
                }
            }
        }
    }
    public Session getSession() {
        return this.gameSession;
    }
    public PlayerHandler getPlayers() {
        return this.players;
    }
    public void pauseGame(boolean FreezeEntities) {
        this.players.getAllPlayers().stream().forEach(p -> {
            p.sendMessage(Game.prefix + "Game Paused!");
            PlayerInfo.getPlayerInfo(p).setState(SessionState.INPAUSEDGAME);
        });
        if(FreezeEntities) isPaused = true;
    }
    public void unpause() {
        if(this.players.getRunner().isOnline() && this.players.getAttacker().isOnline()) {
            isPaused = false;
            for(Player p: this.players.getAllPlayers()) {
                if(PlayerInfo.getPlayerInfo(p).getCurrentSession().equals(this.getSession().getSessionID())) {
                    if(players.getAttacker().equals(p) || players.getRunner().equals(p)) PlayerInfo.getPlayerInfo(p).setState(SessionState.INGAME);
                    if(players.getSpectators().contains(p)) PlayerInfo.getPlayerInfo(p).setState(SessionState.SPECTATING);
                }
            }
        }
    }
    public boolean isPaused() {
        return this.isPaused;
    }

    public boolean isStarted() {
        return started;
    }
    public void setStarted(boolean started) {
        this.started = started;
    }
}
