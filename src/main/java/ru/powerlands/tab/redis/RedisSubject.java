package ru.powerlands.tab.redis;

import ru.powerlands.tab.methods.TabPlayers;
import java.util.ArrayList;

public class RedisSubject {

    public static ArrayList<String> players = new ArrayList<>();

    public synchronized void send(String player) {
        if(!players.contains(player)) {
            players.add(player);
            TabPlayers.updateJoin(player);
        }

    }
    public synchronized void remove(String player) {
        players.remove(player);
        TabPlayers.updateLeave(player);

    }
}
