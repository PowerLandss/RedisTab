package ru.powerlands.tab.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisLoader {

    private String address;
    private Integer port;
    private JedisPool pool;

    public RedisLoader(String address, Integer port) {
        this.address = address;
        this.port = port;
    }
    public void connect() {
        try {
            pool = new JedisPool(address, port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void disconnect() { pool.close(); }

    public Jedis getJedis() {
        return pool.getResource();
    }
}
