package com.colink02dev;

import org.bukkit.World;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Random;

public class Session {
    private String sessionID;
    private String seed;
    private World world;
    private Game game;
    private static HashMap<String, Session> sessions = new HashMap<>();

    public Session(String sessionID) {
        this.sessionID = sessionID;
    }
    public Session(Game game) {
        this.sessionID = generateID();
        this.game = game;
    }
    public Game getGame() {
        return this.game;
    }
    public World getWorld() {
        return this.world;
    }
    public void setWorld(World world) {
        this.world = world;
    }
    public String getSessionID() {
        return this.sessionID;
    }
    private static String generateID() {
        {
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

            return r.toString();
        }
    }
    public static HashMap<String, Session> getSessions() {
        return sessions;
    }
}
