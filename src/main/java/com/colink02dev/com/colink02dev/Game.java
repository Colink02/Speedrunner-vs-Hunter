package com.colink02dev;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Game {
    private String gameSessionID = null;
    private World gameWorld = null;
    private HashMap<Player, String> playersInGameSession = new HashMap<>();
    private ArrayList<ItemStack> runnerGameSessionItems = new ArrayList<>();
    private ArrayList<ItemStack> hunterGameSessionItems = new ArrayList<>();
    //Initiation Methods
    public Game() {
        generateGameSessionID();
    }
    public Game(World existingWorld) {

    }
    public Game(String manualGameSessionID) {

    }
    public Game(String manualGameSessionID, World existingWorld) {

    }
    //Instance Methods
    private void beginGame() {
        if(gameSessionID == null) generateGameSessionID();
        if(gameWorld == null) {
            WorldCreator wc = new WorldCreator(this.gameSessionID);
            wc.environment(World.Environment.NORMAL);
            wc.type(WorldType.NORMAL);
            if(seed)
            World generatedWorld = Bukkit.createWorld(wc);

        }
    }

    //Static Methods
    private void generateGameSessionID() {
        int n = 10;
        byte[] array = new byte[256];
        new Random().nextBytes(array);

        String randomString
                = new String(array, StandardCharsets.UTF_8);

        StringBuilder r = new StringBuilder();

        for (int k = 0; k < randomString.length(); k++) {

            char ch = randomString.charAt(k);

            if (((ch >= 'a' && ch <= 'z')
                    || (ch >= 'A' && ch <= 'Z')
                    || (ch >= '0' && ch <= '9'))
                    && (n > 0)) {

                r.append(ch);
                n--;
            }
        }

        this.gameSessionID = r.toString();
    }
}
