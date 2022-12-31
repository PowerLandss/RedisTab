package ru.powerlands.tab.redis;

import org.bukkit.Bukkit;
import ru.powerlands.tab.RedisTab;
import ru.powerlands.tab.methods.TabPlayers;

public class RedisTask {

    private String player;
    public RedisTask(String player) {
        this.player = player;
    }

    public void send() {
        RedisTab.getRedis().getJedis().publish("RedisTab", "send|" + player);
        RedisSubject.players.add(player);
        TabPlayers.playerJoin(Bukkit.getPlayer(player));
    }
    public void remove() {
        RedisTab.getRedis().getJedis().publish("RedisTab", "remove|" + player);
        RedisSubject.players.remove(player);
        TabPlayers.updateLeave(player);

    }
    public void setPlayer(String player) {
        this.player = player;
    }
}
