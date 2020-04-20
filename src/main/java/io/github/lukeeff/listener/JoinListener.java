package io.github.lukeeff.listener;

import io.github.lukeeff.GlowingPlayers;
import io.github.lukeeff.version.VersionHandler;
import io.github.lukeeff.version.VersionPointer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    GlowingPlayers plugin;
    private VersionHandler versionHandler;

    public JoinListener(GlowingPlayers plugin) {
        this.plugin = plugin;
        this.versionHandler = VersionPointer.versionHandler;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        versionHandler.setGlowing(player);

    }




}
