package com.colink02dev;

import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Note;

import java.util.concurrent.atomic.AtomicBoolean;


public class HeartBeat implements Runnable {
    String id;
    AtomicBoolean stopHeartbeat = new AtomicBoolean(false);
    public HeartBeat(String id, boolean heartbeat) {
        this.id = id;
        this.stopHeartbeat.set(heartbeat);
    } //Game Session ID
    @Override
    public void run() {
        if (HunterVSRunner.gameInstances.containsKey(this.id)) { //Check it exists in instances
            Game gameInstance = HunterVSRunner.gameInstances.get(this.id); //pull that instance
            if (!gameInstance.getPlayers().isAttackerEmpty() && !gameInstance.getPlayers().isRunnerEmpty()) {//check to make sure players are in the game
                if (gameInstance.getPlayers().getAttacker().getLocation().distance(gameInstance.getPlayers().getRunner().getLocation()) <= 15.00f) {//Check Location of two players and measure the distance
                    gameInstance.getPlayers().getRunner().playNote(gameInstance.getPlayers().getRunner().getLocation(), Instrument.BASS_DRUM, Note.natural(1, Note.Tone.G)); //Play first beat of heart
                    Bukkit.getScheduler().runTaskLater(this, new Runnable(true) {
                        private boolean heart = false;
                        public void Runnable(boolean heartbeat) {
                            this.heart = heartbeat;
                        }
                        @Override
                        public void run() {
                            gameInstance.getPlayers().getRunner().playNote(gameInstance.getPlayers().getRunner().getLocation(), Instrument.BASS_DRUM, Note.sharp(1, Note.Tone.G));//Play second beat of heart
                            if(!HunterVSRunner.stopHeartBeat) {
                                gameInstance.setHeartbeatSpeed((int) (gameInstance.getPlayers().getAttacker().getLocation().distance(gameInstance.getPlayers().getRunner().getLocation()) / 100000));//rerun with a speed base off of the distance divided by 1000
                                gameInstance.addHeartbeatTimer();
                            }
                        }
                    });
                }
                if(!HunterVSRunner.stopHeartBeat && !this.stopHeartbeat.get()) {
                    gameInstance.setHeartbeatSpeed(15000);//rerun with a speed base off of the distance divided by 1000
                    gameInstance.addHeartbeatTimer();
                }
            }
        }
    }
}
