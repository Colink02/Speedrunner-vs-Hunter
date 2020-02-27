package com.colink02dev;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerInfo {
    private static HashMap<String, PlayerInfo> playerInfoHashMap = new HashMap<>();
    private Player player;
    private String currentSession;
    private SessionState state;
    private Session ownSession;
    private HashMap<String, ArrayList<ItemStack>> PlayerSessionItems = new HashMap<String, ArrayList<ItemStack>>();
    public PlayerInfo(Player p, SessionState currentState) {
        playerInfoHashMap.put(p.getName(), this);
        this.player = p;
        this.state = currentState;
    }

    public static PlayerInfo getPlayerInfo(Player p) {
        if(playerInfoHashMap.containsKey(p.getName())) {
            return playerInfoHashMap.get(p.getName());
        } else {
            return null;
        }
    }
    public void setCurrentSession(String currentSession) {
        this.currentSession = currentSession;
    }
    public String getCurrentSession() {
        return currentSession;
    }
    public void setState(SessionState state) {
        this.state = state;
    }
    public SessionState getState() {
        return state;
    }
    public boolean hasOwnSession() {
        if(ownSession != null) return true;
        return false;
    }
    public Session getOwnSession() {
        return this.ownSession;
    }
    public void setOwnSession(Session newSession) {
        if(ownSession != null) {
            player.sendMessage(Game.prefix + "Already have your own session! Removing old Session");
            this.ownSession.getGame().removeGame();
        }
        this.ownSession = newSession;
    }
    public void addSessionItems(String sessionID, ArrayList<ItemStack> items) {
        this.PlayerSessionItems.put(sessionID, items);
    }
    public ArrayList<ItemStack> getSessionItems(String sessionID) {
        return this.PlayerSessionItems.get(sessionID);
    }
}
