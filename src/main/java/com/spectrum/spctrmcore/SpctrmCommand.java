package com.spectrum.spctrmcore;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class SpctrmCommand implements CommandExecutor {

    private final SpctrmCore plugin;

    public SpctrmCommand(SpctrmCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FileConfiguration cfg = plugin.getConfig();
        String prefix = cfg.getString("messages.prefix", "&7[&dSpctrm&7]&r ");

        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(ColorUtil.colorize(prefix + "&f/spctrm help &7- Show this help"));
            sender.sendMessage(ColorUtil.colorize(prefix + "&f/spctrm ping &7- Show your ping"));
            if (sender.hasPermission("spctrm.admin")) {
                sender.sendMessage(ColorUtil.colorize(prefix + "&f/spctrm heal &7- Heal yourself"));
                sender.sendMessage(ColorUtil.colorize(prefix + "&f/spctrm reload &7- Reload config"));
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("ping")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ColorUtil.colorize(prefix + "&cOnly players can use this."));
                return true;
            }
            Player p = (Player) sender;
            int ping = -1;
            try {
                ping = (int) p.getClass().getMethod("getPing").invoke(p);
            } catch (Exception ignored) {}
            String msg = cfg.getString("messages.pong").replace("%ping%", String.valueOf(ping));
            p.sendMessage(ColorUtil.colorize(prefix + msg));
            return true;
        }

        if (args[0].equalsIgnoreCase("heal")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ColorUtil.colorize(prefix + "&cOnly players can use this."));
                return true;
            }
            if (!sender.hasPermission("spctrm.admin")) {
                sender.sendMessage(ColorUtil.colorize(prefix + cfg.getString("messages.no_permission")));
                return true;
            }
            Player p = (Player) sender;
            p.setHealth(p.getMaxHealth());
            p.setFoodLevel(20);
            p.setFireTicks(0);
            p.sendMessage(ColorUtil.colorize(prefix + cfg.getString("messages.healed")));
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("spctrm.admin")) {
                sender.sendMessage(ColorUtil.colorize(prefix + cfg.getString("messages.no_permission")));
                return true;
            }
            plugin.reloadConfig();
            sender.sendMessage(ColorUtil.colorize(prefix + cfg.getString("messages.reloaded")));
            return true;
        }

        sender.sendMessage(ColorUtil.colorize(prefix + "&cUnknown subcommand. Use /spctrm help."));
        return true;
    }
}
