package io.github.ssyn02.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Set;
import java.util.logging.Level;

public class WeatherFetch {

    private static final Set<Integer> rain_codes = Set.of(1150, 1153, 1168, 1171, 1180, 1183, 1186, 1189, 1192, 1195,
            1198, 1201, 1240, 1243, 1246, 1273, 1276);
    private static final Set<Integer> thunder_codes = Set.of(1273, 1276, 1279, 1282);
    private final JavaPlugin plugin;

    public WeatherFetch(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void fetchWeather(String city, String apiKey) {
        if (city == null || city.isEmpty() || apiKey == null || apiKey.isEmpty()) {
            plugin.getLogger().warning("City or API key is missing — cannot fetch weather.");
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                String urlStr = "https://api.weatherapi.com/v1/current.json?key=" + apiKey +
                        "&q=" + city.replace(" ", "%20") + "&aqi=no";

                URI uri = URI.create(urlStr);
                HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
                conn.setRequestMethod("GET");

                if (conn.getResponseCode() != 200) {
                    plugin.getLogger().warning("Failed to fetch weather: HTTP " + conn.getResponseCode());
                    return;
                }

                InputStreamReader reader = new InputStreamReader(conn.getInputStream());
                JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();

                JsonObject current = json.getAsJsonObject("current");
                Integer condition = current.getAsJsonObject("condition").get("code").getAsInt();
                String description = current.getAsJsonObject("condition").get("text").getAsString();
                double tempC = current.get("temp_c").getAsDouble();
                boolean isDay = current.get("is_day").getAsInt() == 1;

                plugin.getLogger().info("Weather in " + city + ": " + description + " (" + tempC + "°C)");

                // Optional: store this info in fields or apply to the world (e.g., rain, thunder, etc.)
                applyWeatherToWorld(condition);

            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Error fetching weather data", e);
            }
        });
    }

    private void applyWeatherToWorld(Integer condition) {
        Bukkit.getScheduler().runTask(plugin, () -> {
            boolean rain = rain_codes.contains(condition);
            boolean thunder = thunder_codes.contains(condition);

            Bukkit.getWorlds().forEach(world -> {
                world.setStorm(rain);
                world.setThundering(thunder);
                world.setWeatherDuration(20 * 60 * 5); // 5 minutes
            });
        });
    }
}