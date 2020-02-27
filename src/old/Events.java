package com.colink02dev;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class Events implements org.bukkit.event.Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        e.getPlayer().sendMessage(Game.prefix + "Welcome, do /join <hunter/runner> to join a created game or create a new game with /create");
        if(PlayerInfo.getPlayerInfo(e.getPlayer()) != null) {
            PlayerInfo pi = PlayerInfo.getPlayerInfo(e.getPlayer());
            if(pi.getState().equals(SessionState.INPAUSEDGAME)) {
                if(HunterVSRunner.gameInstances.get(pi.getCurrentSession()).getPlayers().getRunner().isOnline() && HunterVSRunner.gameInstances.get(pi.getCurrentSession()).getPlayers().getAttacker().isOnline()) {
                    HunterVSRunner.gameInstances.get(pi.getCurrentSession()).unpause();
                } else {
                    e.getPlayer().teleport(Game.mainWorld.getSpawnLocation());
                }
            }
        } else {
            new PlayerInfo(e.getPlayer(), SessionState.INLOBBY);
        }
    }
    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        if(PlayerInfo.getPlayerInfo(e.getPlayer()).getState().equals(SessionState.INGAME)) {
            Game instance = HunterVSRunner.gameInstances.get(PlayerInfo.getPlayerInfo(e.getPlayer()).getCurrentSession());
            instance.pauseGame(true);
        }
    }
    /*@EventHandler
    public void onGameModeUpdate(PlayerGameModeChangeEvent e) {
        if() {
            if(e.getNewGameMode() == GameMode.SPECTATOR) {
                e.setCancelled(false);
                return;
            }
            e.setCancelled(true);
            e.getPlayer().sendMessage(Game.prefix + "Sorry your not allowed to do that now!");
        } else {
            return;
        }
    }*/
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Game playerGame = HunterVSRunner.gameInstances.get(PlayerInfo.getPlayerInfo(e.getEntity()).getCurrentSession());
        PlayerHandler players = playerGame.getPlayers();

        if (players.getAttacker().equals(e.getEntity())) {
            e.setKeepInventory(false);
            for (ItemStack item : e.getDrops()) {
                if (item.getType() == Material.COMPASS) {
                    e.getDrops().remove(item);
                } else {
                    e.getEntity().getWorld().dropItemNaturally(e.getEntity().getLocation(), item);
                }
            }
        } else if (players.getRunner().equals(e.getEntity())) {
            if (playerGame.isStarted()) {
                for (Player p : playerGame.getPlayers().getAllPlayers()) {
                    p.sendMessage(Game.prefix + ChatColor.RED + "Game Over! " + e.getDeathMessage());
                    PlayerInfo.getPlayerInfo(p).setCurrentSession("");
                    if (Game.mainWorld != null) {
                        p.teleport(Game.mainWorld.getSpawnLocation());
                    } else {
                        System.out.println("ERROR: mainWorld is null");
                    }
                }
                playerGame.setStarted(false);
                players.getRunner().setBedSpawnLocation(Game.mainWorld.getSpawnLocation(), true);
                for (Player p : players.getSpectators()) {
                    p.teleport(Game.mainWorld.getSpawnLocation());
                }
                players.getAttacker().teleport(Game.mainWorld.getSpawnLocation());
                players.getAttacker().setBedSpawnLocation(Game.mainWorld.getSpawnLocation(), true);
                players.setAttacker(null);
                players.setRunner(null);
                playerGame.wipeWorld();
            }

        }
    }
    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Game playerGame = HunterVSRunner.gameInstances.get(PlayerInfo.getPlayerInfo(e.getPlayer()).getCurrentSession());
        if (playerGame.isStarted()) {
            if (!playerGame.getPlayers().isAttackerEmpty()) {
                if (playerGame.getPlayers().getAttacker().equals(e.getPlayer())) {
                    e.getPlayer().getInventory().addItem(new ItemStack(Material.COMPASS, 1));
                    e.getPlayer().teleport(playerGame.getSession().getWorld().getHighestBlockAt(playerGame.getSession().getWorld().getSpawnLocation()).getLocation());
                }
            }
        }
    }
    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        Game playerGame = HunterVSRunner.gameInstances.get(PlayerInfo.getPlayerInfo(e.getPlayer()).getCurrentSession());
        if(playerGame == null || e.getItem() == null) return;
        Action a = e.getAction();
        if (!playerGame.getPlayers().isAttackerEmpty()) return;
        if ((a.equals(Action.RIGHT_CLICK_AIR)|| a.equals(Action.RIGHT_CLICK_BLOCK)) && playerGame.getPlayers().getAttacker().equals(e.getPlayer()) && e.getItem().getType().equals(Material.COMPASS)) {
            e.getPlayer().sendMessage(Game.prefix + "Targetting Player: " + playerGame.getPlayers().getRunner().getName());
            e.getPlayer().setCompassTarget(playerGame.getPlayers().getRunner().getLocation());
        }
    }
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        for(Map.Entry<String, Game> set: HunterVSRunner.gameInstances.entrySet()) {
            if(set.getValue().getPlayers().getAllPlayers().contains(e.getPlayer())) {
                if(set.getValue().isPaused()) {//TODO check to make sure player isn't in another session actively
                    e.setCancelled(true);
                }
            }
        }
    }
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Game playerGame = HunterVSRunner.gameInstances.get(PlayerInfo.getPlayerInfo(e.getPlayer()).getCurrentSession());
        if(playerGame == null) return;
        if(playerGame.getPlayers().getAllPlayers().contains(e.getPlayer())) {
            if(playerGame.isPaused()) {
                e.setCancelled(true);
            }
        }
    }
}
