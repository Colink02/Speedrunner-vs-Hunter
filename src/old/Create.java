package com.colink02dev;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Create implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Game newGameSession = new Game();
        HunterVSRunner.gameInstances.put(newGameSession.getSession().getSessionID(), newGameSession);
        PlayerInfo.getPlayerInfo((Player)commandSender).setOwnSession(newGameSession.getSession());
        commandSender.sendMessage(Game.prefix + ChatColor.GREEN + " New Game Session Created! To use this session use this id: " + newGameSession.getSession().getSessionID());
        return false;
    }
}
