package net.aerh.proximitychat;

import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ConfigData {

    private final ProximityChatPlugin plugin;
    private final Map<Integer, ChatColor> distanceColors;
    private boolean perWorld;
    private int maxDistance;
    private ChatColor defaultColor;

    public ConfigData(ProximityChatPlugin plugin) {
        this.plugin = plugin;
        distanceColors = new HashMap<>();
    }

    public void load() {
        perWorld = plugin.getConfig().getBoolean("chat.per-world", false);
        maxDistance = plugin.getConfig().getInt("chat.max-distance", Integer.MAX_VALUE);
        defaultColor = ChatColor.valueOf(plugin.getConfig().getString("chat.default-color", "RESET"));
        distanceColors.clear();

        if (plugin.getConfig().getConfigurationSection("chat.distance-colors") == null) {
            plugin.getLogger().warning("No chat.distance-colors section found in config.yml!");
            return;
        }

        for (String key : plugin.getConfig().getConfigurationSection("chat.distance-colors").getKeys(false)) {
            distanceColors.put(Integer.parseInt(key), ChatColor.valueOf(plugin.getConfig().getString("chat.distance-colors." + key)));
            plugin.getLogger().info("Loaded distance color for " + key + " blocks: " + plugin.getConfig().getString("chat.distance-colors." + key));
        }
    }

    public ChatColor getDistanceColor(int distance) {
        TreeMap<Integer, ChatColor> sorted = new TreeMap<>(distanceColors);
        int highest = sorted.firstKey();

        for (Integer key : sorted.keySet()) {
            if (key <= distance && key > highest) {
                highest = key;
            }
        }

        return sorted.get(highest);
    }

    public boolean isPerWorld() {
        return perWorld;
    }

    public ProximityChatPlugin getPlugin() {
        return plugin;
    }

    public Map<Integer, ChatColor> getDistanceColors() {
        return distanceColors;
    }

    public int getMaxDistance() {
        return maxDistance;
    }

    public ChatColor getDefaultColor() {
        return defaultColor;
    }
}
