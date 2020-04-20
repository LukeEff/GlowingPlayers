package io.github.lukeeff;

import io.github.lukeeff.listener.JoinListener;
import io.github.lukeeff.version.VersionHandler;
import io.github.lukeeff.version.VersionPointer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class GlowingPlayers extends JavaPlugin {

    VersionPointer versionPointer;

    @Override
    public void onEnable() {

        versionPointer = new VersionPointer(this);
        Bukkit.getPluginManager().registerEvents(new JoinListener(this), this );
    }

    public void onDisable() {

    }

}
