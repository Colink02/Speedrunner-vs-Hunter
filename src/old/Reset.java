package com.colink02dev;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Reset implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return true;
        if(PlayerInfo.getPlayerInfo((Player)sender).getCurrentSession() == "") return true;
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("force")) {
                    HunterVSRunner.gameInstances.get(PlayerInfo.getPlayerInfo((Player)sender).getCurrentSession()).reset();
                }
            }
            if (HunterVSRunner.gameInstances.get(PlayerInfo.getPlayerInfo((Player)sender).getCurrentSession()).isStarted()) {
                HunterVSRunner.gameInstances.get(PlayerInfo.getPlayerInfo((Player)sender).getCurrentSession()).reset();
            } else {
                sender.sendMessage(Game.prefix + ChatColor.RED + "You can't reset a game that isn't even started silly!");
            }
            return true;
        }
}
