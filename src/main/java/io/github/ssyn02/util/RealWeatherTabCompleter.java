package io.github.ssyn02.util;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.List;

public class RealWeatherTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args){
        if (args.length == 1){
            return Arrays.asList("setapikey", "setcity", "reload");
        }
        return List.of();
    }
}
