package io.github.ssyn02.command;

import io.github.ssyn02.RealWeather;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Arrays;

public class RealWeatherRootCommand implements CommandExecutor {
    private final RealWeather plugin;

    public RealWeatherRootCommand(RealWeather plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Component.text("[RealWeather]\n" +
                    "    /realweather - See this message\n" +
                    "    /realweather setapikey - Set the API key\n" +
                    "    /realweather setcity <city> - Set the city to fetch the weather from\n" +
                    "    /realweather reload - Reload the plugin\"").color(NamedTextColor.YELLOW));
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "setapikey":
                if (!sender.hasPermission("realweather.use")) {
                    sender.sendMessage(Component.text("You don't have permission.").color(NamedTextColor.RED));
                    return true;
                }
                if (args.length != 2) {
                    sender.sendMessage(Component.text("Usage: /realweather setapikey <key>").color(NamedTextColor.YELLOW));
                    return true;
                }
                plugin.setApiKey(args[1]);
                sender.sendMessage(Component.text("API key set.").color(NamedTextColor.YELLOW));
                return true;

            case "setcity":
                if (!sender.hasPermission("realweather.use")) {
                    sender.sendMessage(Component.text("You don't have permission.").color(NamedTextColor.RED));
                    return true;
                }
                if (args.length < 2) {
                    sender.sendMessage(Component.text("Usage: /realweather setcity <city>").color(NamedTextColor.YELLOW));
                    return true;
                }
                String city = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                plugin.setCity(city);
                sender.sendMessage(Component.text("City set to " + city + ".").color(NamedTextColor.YELLOW));
                return true;

            case "reload":
                if (!sender.hasPermission("realweather.use")) {
                    sender.sendMessage(Component.text("You don't have permission.").color(NamedTextColor.RED));
                    return true;
                }
                if (args.length != 1) {
                    sender.sendMessage(Component.text("Usage: /realweather reload").color(NamedTextColor.YELLOW));
                    return true;
                }
                plugin.reload();
                sender.sendMessage(Component.text("Plugin config reloaded.").color(NamedTextColor.YELLOW));
                return true;

            case "rain":
                if (!sender.hasPermission("realweather.use")) {
                    sender.sendMessage(Component.text("You don't have permission.").color(NamedTextColor.RED));
                    return true;
                }
                if (args.length != 1) {
                    sender.sendMessage(Component.text("Usage: /realweather reload").color(NamedTextColor.YELLOW));
                    return true;
                }
                plugin.rain();
                sender.sendMessage(Component.text("rain").color(NamedTextColor.YELLOW));
                return true;

            case "clear":
                if (!sender.hasPermission("realweather.use")) {
                    sender.sendMessage(Component.text("You don't have permission.").color(NamedTextColor.RED));
                    return true;
                }
                if (args.length != 1) {
                    sender.sendMessage(Component.text("Usage: /realweather reload").color(NamedTextColor.YELLOW));
                    return true;
                }
                plugin.clear();
                sender.sendMessage(Component.text("clear").color(NamedTextColor.YELLOW));
                return true;

            default:
                return true;
        }
    }
}
