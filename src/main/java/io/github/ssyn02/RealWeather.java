package io.github.ssyn02;

import io.github.ssyn02.api.WeatherFetch;
import io.github.ssyn02.command.RealWeatherRootCommand;
import io.github.ssyn02.util.RealWeatherTabCompleter;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import net.kyori.adventure.text.Component;

public final class RealWeather extends JavaPlugin {
    private WeatherFetch weatherFetch;
    private String apiKey;
    private String city;

    final Component onEnableText = Component.text("[RealWeather]" +
            " RealWeather v1.0 enabled").color(NamedTextColor.DARK_GREEN);

    final Component onDisableText = Component.text("[RealWeather]" +
            " RealWeather v1.0 disabled").color(NamedTextColor.DARK_GREEN);
    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        loadSettings();

        weatherFetch = new WeatherFetch(this);
        weatherFetch.fetchWeather(city, apiKey);

        getServer().getConsoleSender().sendMessage(onEnableText);
        getCommand("realweather").setExecutor(new RealWeatherRootCommand(this));
        getCommand("realweather").setTabCompleter(new RealWeatherTabCompleter());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getServer().getConsoleSender().sendMessage(onDisableText);
    }

    public void reload(){
        reloadConfig();
        loadSettings();
        if (weatherFetch != null) {
            weatherFetch.fetchWeather(city, apiKey);
        }
    }

    public void loadSettings() {
        this.apiKey = getConfig().getString("apikey", "");
        this.city = getConfig().getString("city", "");
        getLogger().info("Loaded API key and city from saved config.");
    }

    public void clear(){
        Bukkit.getWorlds().forEach(world -> {
            world.setStorm(false);
            world.setThundering(false);
        });
    }

    public void rain(){
        Bukkit.getWorlds().forEach(world -> {
            world.setStorm(true);
            world.setThundering(true);
        });
    }

    public void setApiKey(String key) {
        this.apiKey = key;
        getConfig().set("apikey", key);
        saveConfig();
    }

    public void setCity(String city) {
        this.city = city;
        getConfig().set("city", city);
        saveConfig();
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getCity() {
        return city;
    }

    public WeatherFetch getWeatherFetcher() {
        return weatherFetch;
    }
}
