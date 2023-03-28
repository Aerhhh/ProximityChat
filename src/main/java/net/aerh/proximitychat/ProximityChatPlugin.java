package net.aerh.proximitychat;

import org.bukkit.plugin.java.JavaPlugin;

public final class ProximityChatPlugin extends JavaPlugin {

    private ConfigData configData;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        configData = new ConfigData(this);
        configData.load();

        getServer().getPluginManager().registerEvents(new PlayerChatListener(this), this);
    }

    public ConfigData getConfigData() {
        return configData;
    }
}
