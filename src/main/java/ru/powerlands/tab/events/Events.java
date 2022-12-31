package ru.powerlands.tab.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.powerlands.tab.redis.RedisTask;

public class Events implements Listener {

    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if(player != null) {
            RedisTask task = new RedisTask(player.getName());
            task.send();
        }
    }
    @EventHandler
    public void playerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if(player != null) {
            RedisTask task = new RedisTask(player.getName());
            task.remove();
        }
    }
}

