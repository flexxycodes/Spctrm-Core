package com.spectrum.spctrmcore;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class SpctrmCore extends JavaPlugin implements Listener {

    private static SpctrmCore instance;

    public static SpctrmCore getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        getCommand("spctrm").setExecutor(new SpctrmCommand(this));
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("SpctrmCore enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("SpctrmCore disabled.");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        FileConfiguration cfg = getConfig();
        if (cfg.getBoolean("settings.joinMessage", true)) {
            String msg = cfg.getString("messages.join", "&eWelcome, &b%player%&e!");
            msg = msg.replace("%player%", event.getPlayer().getName());
            event.getPlayer().sendMessage(ColorUtil.colorize(cfg.getString("messages.prefix", "&7[&dSpctrm&7]&r ") + msg));
        }
    }
}
