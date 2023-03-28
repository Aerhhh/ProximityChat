package net.aerh.proximitychat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Collection;

public class PlayerChatListener implements Listener {

    private final ProximityChatPlugin plugin;

    public PlayerChatListener(ProximityChatPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Collection<? extends Player> players;
        Player player = event.getPlayer();

        if (plugin.getConfigData().isPerWorld()) {
            players = player.getWorld().getPlayers();
        } else {
            players = Bukkit.getOnlinePlayers();
        }

        players.stream().filter(p -> !p.equals(player)).toList().forEach(event.getRecipients()::remove);

        for (Player otherPlayer : players.stream().filter(p -> !p.equals(player)).toList()) {
            int distance = (int) player.getLocation().distance(otherPlayer.getLocation());

            if (distance > plugin.getConfigData().getMaxDistance()) {
                continue;
            }

            ChatColor color = plugin.getConfigData().getDistanceColor(distance);
            if (color == null || (plugin.getConfigData().isPerWorld() && !player.getWorld().equals(otherPlayer.getWorld()))) {
                color = plugin.getConfigData().getDefaultColor();
            }

            otherPlayer.sendMessage(String.format(event.getFormat(), player.getDisplayName(), color + event.getMessage()));
        }
    }
}
