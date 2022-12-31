package ru.powerlands.tab;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.JedisPubSub;
import ru.powerlands.tab.commands.CommandList;
import ru.powerlands.tab.events.Events;
import ru.powerlands.tab.redis.RedisLoader;
import ru.powerlands.tab.redis.RedisSubject;
import ru.powerlands.tab.redis.RedisTask;

public final class RedisTab extends JavaPlugin {

    static RedisTab Main;
    static RedisLoader redis;
    final RedisSubject sub = new RedisSubject();

    @Override
    public void onEnable() {
        Main = this;
        saveDefaultConfig();

        redis = new RedisLoader(
                getConfig().getString("redis.address"),
                getConfig().getInt("redis.port")
        );
        redis.connect();

        jedisPubSub();
        bukkit();
        load();
    }
    void bukkit() {
        getServer().getPluginCommand("list").setExecutor(new CommandList());
        getServer().getPluginManager().registerEvents(new Events(), this);
    }
    void load() {
        RedisTask redisTask = new RedisTask("");
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(p != null) {
                redisTask.setPlayer(p.getName());
                redisTask.send();
            }
        }
    }


    void jedisPubSub(){
        Runnable runnable = () -> {
            JedisPubSub jedisPubSub = new JedisPubSub() {
                @Override
                public void onMessage(String channel, String message) {
                    if (channel.equals("RedisTab")) {
                        String[] parts = message.split("\\|");
                        switch (parts[0]){
                            case "send": sub.send(parts[1]);
                                break;
                            case "remove": sub.remove(parts[1]);
                                break;
                        }
                    }
                }
            };
            redis.getJedis().subscribe(jedisPubSub, "RedisTab");
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public static RedisTab getMain() {
        return Main;
    }
    public static RedisLoader getRedis() {
        return redis;
    }


    @Override
    public void onDisable() {
    }
}
