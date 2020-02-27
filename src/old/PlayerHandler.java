package com.colink02dev;

import org.bukkit.entity.Player;

import java.util.ArrayList;

public class PlayerHandler {
    private ArrayList<Player> allPlayers = new ArrayList<>();
    private Player runner = null;
    private Player attacker = null;
    private ArrayList<Player> spec = new ArrayList<>();

    public void setRunner(Player runner) {
        if(!this.isRunnerEmpty() && !(attacker.equals(this.runner) && !(spec.contains(this.runner)))) {
            allPlayers.remove(this.runner);
        }
        if(runner != null) {
            allPlayers.add(runner);
        }
        this.runner = runner;
    }
    public void setAttacker(Player attacker) {
        if(!this.isRunnerEmpty() && !(runner.equals(this.attacker) && !(spec.contains(this.attacker)))) {
            allPlayers.remove(this.attacker);
        }
        if(attacker != null) {
            allPlayers.add(attacker);
        }
        this.attacker = attacker;
    }
    public Player getRunner() {
        return runner;
    }

    public Player getAttacker() {
        return attacker;
    }
    public boolean isAttackerEmpty() {
        return attacker == null;
    }
    public boolean isRunnerEmpty() {
        return runner == null;
    }
    public boolean filledGame() {
        return runner != null && attacker != null;
    }
    public void addSpectator(Player p) {
        this.spec.add(p);
    }
    public void removeSpectator(Player p) {
        this.spec.remove(p);
    }
    public void removeAllSpectators() {
        this.spec.clear();
    }
    public ArrayList<Player> getSpectators() {
        return this.spec;
    }
    public ArrayList<Player> getAllPlayers() {
        return allPlayers;
    }
}
