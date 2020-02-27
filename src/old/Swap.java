package com.colink02dev;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.swing.*;

public class Swap implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (!HunterVSRunner.gameInstances.get(PlayerInfo.getPlayerInfo((Player)sender).getCurrentSession()).isStarted()) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(Game.prefix + ChatColor.RED + "You must be a player to do this!");
                    return true;
                }
                if (!HunterVSRunner.gameInstances.get(PlayerInfo.getPlayerInfo((Player)sender).getCurrentSession()).getPlayers().isRunnerEmpty()) {
                    if (HunterVSRunner.gameInstances.get(PlayerInfo.getPlayerInfo((Player)sender).getCurrentSession()).getPlayers().getRunner().equals(((Player) sender))) {
                        Player oldAttacker = HunterVSRunner.gameInstances.get(PlayerInfo.getPlayerInfo((Player)sender).getCurrentSession()).getPlayers().getAttacker();
                        HunterVSRunner.gameInstances.get(PlayerInfo.getPlayerInfo((Player)sender).getCurrentSession()).getPlayers().setAttacker((Player) sender);
                        if (oldAttacker == null) {
                            HunterVSRunner.gameInstances.get(PlayerInfo.getPlayerInfo((Player)sender).getCurrentSession()).getPlayers().setRunner(null);
                        } else {
                            HunterVSRunner.gameInstances.get(PlayerInfo.getPlayerInfo((Player)sender).getCurrentSession()).getPlayers().setRunner(oldAttacker);
                        }
                        sender.sendMessage(Game.prefix + ChatColor.GREEN + "You are now the attacker!");
                    }
                } else {
                    sender.sendMessage(Game.prefix + ChatColor.RED + "Only the runner is allowed to do this!");
                }
            } else {
                sender.sendMessage(Game.prefix + ChatColor.RED + "You can't do this while a game is going! Do /reset if you would like to do so.");
            }
        return true;
    }
}
