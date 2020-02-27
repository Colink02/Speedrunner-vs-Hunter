package com.colink02dev;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TabCompletions implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        //if(command.getName().toLowerCase().contains("join")) {
        List<String> finalArray = new ArrayList<>(Arrays.asList("hunter", "runner"));
        for(Map.Entry<String, Session> sessions: Session.getSessions().entrySet()) {
            finalArray.add(sessions.getKey());
        }
        return finalArray;
        //}
    }
}
