package com.colink02dev;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Start implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(args.length >= 1)) sender.sendMessage(Game.prefix + ChatColor.RED + "Please provide a session ID");
        if(HunterVSRunner.gameInstances.containsKey(args[0])) {
            Game sessionedGame = HunterVSRunner.gameInstances.get(args[0]);
            if (!sessionedGame.complete) {
                sender.sendMessage("Please Wait until Deleting is complete!");
                return true;
            }
            if (!sessionedGame.getPlayers().isRunnerEmpty()) {
                if (!sessionedGame.getPlayers().isAttackerEmpty()) {
                    Bukkit.broadcastMessage(Game.prefix + ChatColor.GREEN + "Starting");
                    if (args.length > 1) {
                       if (args.length == 2) {
                            sessionedGame.start(args[1], false);
                        } else if(args.length == 3) {
                            sessionedGame.start(args[1], Boolean.valueOf(args[2]));
                        }
                    } else {
                        sessionedGame.start(null, false);
                    }

                } else {
                    sender.sendMessage(Game.prefix + ChatColor.RED + " You can't start without a hunter!");
                }
            } else {
                sender.sendMessage(Game.prefix + ChatColor.RED + " You can't start without a runner!");
            }
        }
        return true;
    }
}
