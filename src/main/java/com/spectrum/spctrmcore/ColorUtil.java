package com.spectrum.spctrmcore;

import org.bukkit.ChatColor;

public final class ColorUtil {
    private ColorUtil() {}

    public static String colorize(String input) {
        if (input == null) return "";
        return ChatColor.translateAlternateColorCodes('&', input);
    }
}
