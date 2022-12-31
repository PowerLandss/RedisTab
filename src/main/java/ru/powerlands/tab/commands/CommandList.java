package ru.powerlands.tab.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import ru.powerlands.tab.redis.RedisSubject;

public class CommandList implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        StringBuilder online = new StringBuilder();
        for(String s : RedisSubject.players) {
            if(!online.toString().contains(s)) {
                online.insert(0, "," + s);
            }
        }
        sender.sendMessage(online.toString().replaceFirst(",", "") + " (" + RedisSubject.players.size() + " players)");
        return false;
    }
}
