package ru.powerlands.tab.methods;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import ru.powerlands.tab.redis.RedisSubject;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class TabPlayers {

    private static HashMap<String, UUID> uuidPlayers = new HashMap<>();
    public static void updateJoin(String player) {
        Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
        if(onlinePlayers.isEmpty()) {
            return;
        }
        for (Player p : onlinePlayers) {
            if(!p.getName().equals(player)) {
                if(!uuidPlayers.containsKey(player)) {
                    if(Bukkit.getPlayer(player) == null) {
                        try {
                            MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
                            WorldServer world = ((CraftWorld) Bukkit.getWorlds().get(0)).getHandle();
                            UUID uuid = UUID.randomUUID();
                            uuidPlayers.put(player, uuid);
                            GameProfile profile = new GameProfile(uuid, player);
                            PlayerInteractManager manager = new PlayerInteractManager(world);
                            EntityPlayer npc = new EntityPlayer(server, world, profile, manager);
                            PlayerConnection connection = ((CraftPlayer) p).getHandle().playerConnection;
                            PacketPlayOutPlayerInfo info = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc);
                            connection.sendPacket(info);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public static void updateLeave(String player) {
        Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
        if(onlinePlayers.isEmpty()) {
            return;
        }
        for (Player p : onlinePlayers) {
            if(uuidPlayers.containsKey(player)) {
            try {
              MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
              WorldServer world = ((CraftWorld) Bukkit.getWorlds().get(0)).getHandle();
              UUID uuid = uuidPlayers.get(player);
              GameProfile profile = new GameProfile(uuid, player);
              PlayerInteractManager manager = new PlayerInteractManager(world);
              EntityPlayer npc = new EntityPlayer(server, world, profile, manager);
              PlayerConnection connection = ((CraftPlayer) p).getHandle().playerConnection;
              PacketPlayOutPlayerInfo info = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc);
              connection.sendPacket(info);
              uuidPlayers.remove(player);
            } catch (Exception e) {
               e.printStackTrace();
            }
            }
        }
    }
    public static void playerJoin(Player player) {
        for (String s : RedisSubject.players) {
            if (!s.equals(player.getName())) {
                    if (Bukkit.getPlayer(s) == null) {
                        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
                        WorldServer world = ((CraftWorld) Bukkit.getWorlds().get(0)).getHandle();
                        UUID uuid = UUID.randomUUID();
                        uuidPlayers.put(s, uuid);
                        GameProfile profile = new GameProfile(uuid, s);
                        PlayerInteractManager manager = new PlayerInteractManager(world);
                        EntityPlayer npc = new EntityPlayer(server, world, profile, manager);
                        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
                        PacketPlayOutPlayerInfo info = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc);
                        connection.sendPacket(info);
                    }

            }

        }
    }

}
