package com.colink02dev;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.w3c.dom.Text;

import java.util.Map;

public class Join implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(Game.prefix + ChatColor.RED + "You can only use this command as a player");
            return true;
        }
        if(args.length >= 2) {
            if(args[0].length() == 10) {
                if(HunterVSRunner.gameInstances.containsKey(args[0])) {
                    Game game = HunterVSRunner.gameInstances.get(args[0]);
                    if(args[1].toLowerCase().contains("runner")) {
                        if(!game.getPlayers().isRunnerEmpty()) {
                            if(!game.getPlayers().getRunner().equals(sender)) {
                                sender.sendMessage(game.prefix + ChatColor.RED + "Player '" + game.getPlayers().getRunner().getDisplayName() + ChatColor.RED + "' is already runner!");
                            } else {
                                sender.sendMessage(game.prefix + ChatColor.RED + "You're already the runner!");
                            }
                        } else if(game.getPlayers().isRunnerEmpty()) {
                            game.getPlayers().setRunner((Player)sender);
                            sender.sendMessage(game.prefix + ChatColor.GREEN + "You are now the Runner!");
                            PlayerInfo.getPlayerInfo((Player)sender).setCurrentSession(game.getSession().getSessionID());
                            PlayerInfo.getPlayerInfo((Player)sender).setState(SessionState.WAITING);
                            if(!game.getPlayers().isAttackerEmpty()) {
                                if(game.getPlayers().getAttacker().equals((Player)sender)) game.getPlayers().setAttacker(null);
                                PlayerInfo.getPlayerInfo((Player)sender).setCurrentSession(game.getSession().getSessionID());
                                PlayerInfo.getPlayerInfo((Player)sender).setState(SessionState.WAITING);
                            }
                        }
                    } else if(args[1].toLowerCase().contains("hunter")) {
                        if(!game.getPlayers().isAttackerEmpty()) {
                            if(!game.getPlayers().getAttacker().equals(sender)) {
                                sender.sendMessage(game.prefix + ChatColor.RED + "Player '" + game.getPlayers().getAttacker().getDisplayName() + ChatColor.RED + "' is already hunter!");
                            } else {
                                sender.sendMessage(game.prefix + ChatColor.RED + "You're already the hunter!");
                            }
                        } else if(game.getPlayers().isAttackerEmpty()) {
                            game.getPlayers().setAttacker((Player)sender);
                            sender.sendMessage(Game.prefix + ChatColor.GREEN + "You are now the Hunter!");
                            PlayerInfo.getPlayerInfo((Player)sender).setCurrentSession(game.getSession().getSessionID());
                            PlayerInfo.getPlayerInfo((Player)sender).setState(SessionState.WAITING);
                            if(!game.getPlayers().isRunnerEmpty()) {
                                if(game.getPlayers().getRunner().equals(((Player)sender))) game.getPlayers().setRunner(null);
                                PlayerInfo.getPlayerInfo((Player)sender).setCurrentSession(game.getSession().getSessionID());
                                PlayerInfo.getPlayerInfo((Player)sender).setState(SessionState.WAITING);
                            }
                        }
                    }
                }
            } else {
                for (Map.Entry<String, Game> set : HunterVSRunner.gameInstances.entrySet()) {
                    String runner;
                    if(set.getValue().getPlayers().getRunner() != null) {
                        runner = ChatColor.GREEN + set.getValue().getPlayers().getRunner().getName();
                    } else {
                        runner = ChatColor.GRAY + "None";
                    }
                    String hunter;
                    if(set.getValue().getPlayers().getAttacker() != null) {
                        hunter = ChatColor.GREEN + set.getValue().getPlayers().getAttacker().getName();
                    } else {
                        hunter = ChatColor.GRAY + "None";
                    }
                    sender.sendMessage("=====================================");
                    TextComponent mainComponent = new TextComponent(runner + ChatColor.GRAY + " Vs. " + hunter + " | Join as: ");
                    TextComponent runnerText = new TextComponent(ChatColor.GREEN + "[Runner] ");
                    runnerText.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/join " + set.getKey() + " runner"));
                    TextComponent hunterText = new TextComponent(ChatColor.GREEN + "[Hunter] ");
                    hunterText.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/join " + set.getKey() + " hunter"));
                    TextComponent spectatorText = new TextComponent(ChatColor.GREEN + "[Spectator]");
                    spectatorText.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/join " + set.getKey() + " spectator"));
                    mainComponent.addExtra(runnerText);
                    mainComponent.addExtra(hunterText);
                    mainComponent.addExtra(spectatorText);
                    sender.spigot().sendMessage(mainComponent);
                    sender.sendMessage("======================================");
                }
            }
        } else {
            for (Map.Entry<String, Game> set : HunterVSRunner.gameInstances.entrySet()) {
                String runner;
                if(set.getValue().getPlayers().getRunner() != null) {
                    runner = ChatColor.GREEN + set.getValue().getPlayers().getRunner().getName();
                } else {
                    runner = ChatColor.GRAY + "None";
                }
                String hunter;
                if(set.getValue().getPlayers().getAttacker() != null) {
                    hunter = ChatColor.GREEN + set.getValue().getPlayers().getAttacker().getName();
                } else {
                    hunter = ChatColor.GRAY + "None";
                }
                sender.sendMessage(ChatColor.YELLOW + "=====================================");
                TextComponent mainComponent = new TextComponent(runner + ChatColor.GRAY +" Vs. " + hunter + " | Join as: ");
                TextComponent runnerText = new TextComponent(ChatColor.GREEN + "[Runner] ");
                runnerText.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/join " + set.getKey() + " runner"));
                TextComponent hunterText = new TextComponent(ChatColor.GREEN + "[Hunter] ");
                hunterText.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/join " + set.getKey() + " hunter"));
                TextComponent spectatorText = new TextComponent(ChatColor.GREEN + "[Spectator]");
                spectatorText.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/join " + set.getKey() + " spectator"));
                mainComponent.addExtra(runnerText);
                mainComponent.addExtra(hunterText);
                mainComponent.addExtra(spectatorText);
                sender.spigot().sendMessage(mainComponent);
                sender.sendMessage(ChatColor.YELLOW + "=====================================");
            }
        }
        return true;
    }
}
