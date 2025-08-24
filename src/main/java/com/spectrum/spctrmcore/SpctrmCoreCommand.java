package com.spectrum.spctrmcore;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class SpctrmCoreCommand implements CommandExecutor {

    private final SpctrmCorePlugin plugin;

    public SpctrmCoreCommand(SpctrmCorePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FileConfiguration cfg = plugin.getConfig();
        String prefix = cfg.getString("messages.prefix", "&8[&dSpctrm&8]&r ");

        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(ColorUtil.colorize(prefix + "&f/spctrmcore help &7- Show this help"));
            sender.sendMessage(ColorUtil.colorize(prefix + "&f/spctrmcore ping &7- Show your ping"));
            if (sender.hasPermission("spctrm.admin")) {
                sender.sendMessage(ColorUtil.colorize(prefix + "&f/spctrmcore heal &7- Heal yourself"));
                sender.sendMessage(ColorUtil.colorize(prefix + "&f/spctrmcore reload &7- Reload the config"));
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("ping")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ColorUtil.colorize(prefix + "&cOnly players can use this."));
                return true;
            }
            Player p = (Player) sender;
            int ping = getPingCompat(p);
            String msg = cfg.getString("messages.pong", "&aPong! &7Your ping is &f%ping%ms&7.")
                    .replace("%ping%", String.valueOf(ping));
            p.sendMessage(ColorUtil.colorize(prefix + msg));
            return true;
        }

        if (args[0].equalsIgnoreCase("heal")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ColorUtil.colorize(prefix + "&cOnly players can use this."));
                return true;
            }
            if (!sender.hasPermission("spctrm.admin")) {
                sender.sendMessage(ColorUtil.colorize(prefix + cfg.getString("messages.no_permission", "&cYou don't have permission to do that.")));
                return true;
            }
            Player p = (Player) sender;
            p.setHealth(p.getMaxHealth());
            p.setFoodLevel(20);
            p.setFireTicks(0);
            p.sendMessage(ColorUtil.colorize(prefix + cfg.getString("messages.healed", "&aYou have been healed.")));
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("spctrm.admin")) {
                sender.sendMessage(ColorUtil.colorize(prefix + cfg.getString("messages.no_permission", "&cYou don't have permission to do that.")));
                return true;
            }
            plugin.reloadConfig();
            sender.sendMessage(ColorUtil.colorize(prefix + cfg.getString("messages.reloaded", "&aConfiguration reloaded.")));
            return true;
        }

        sender.sendMessage(ColorUtil.colorize(prefix + "&cUnknown subcommand. Use &f/spctrmcore help&c."));
        return true;
    }

    // Best-effort ping across versions: Paper exposes Player#getPing; otherwise try CraftBukkit handle; else -1.
    private int getPingCompat(Player p) {
        try {
            return (int) Player.class.getMethod("getPing").invoke(p);
        } catch (Throwable ignored) {
            try {
                Object handle = p.getClass().getMethod("getHandle").invoke(p);
                return (int) handle.getClass().getField("ping").get(handle);
            } catch (Throwable t) {
                return -1;
            }
        }
    }
}
