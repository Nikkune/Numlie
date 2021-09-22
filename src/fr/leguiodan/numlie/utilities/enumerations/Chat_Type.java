package fr.leguiodan.numlie.utilities.enumerations;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;

public enum Chat_Type {
    GLOBAL("[G]", "!g", ChatColor.WHITE, null),
    PARTY("[P]", "!p", ChatColor.RED, null),
    INFO("[!]", "null", ChatColor.LIGHT_PURPLE, null),
    WARNING("[\u26a0]", "null", ChatColor.LIGHT_PURPLE, null),
    ERROR("[\u26a0]", "null", ChatColor.DARK_RED, null),
    SUCCESS("[!]", "null", ChatColor.DARK_GREEN, null),
    BROADCAST("[B]", "!b", ChatColor.BOLD, ChatColor.RED),
    WRONG("", "", null, null);
    private final String prefix;
    private final String selector;
    private final ChatColor color;
    private final ChatColor secondary;

    Chat_Type(String prefix, String selector, @Nullable ChatColor color, @Nullable ChatColor secondary) {
        this.prefix = prefix;
        this.selector = selector;
        this.color = color;
        this.secondary = secondary;
    }

    public String getSelector() {
        return selector;
    }

    public String getPrefix() {
        return ChatColor.DARK_GRAY + prefix + " ";
    }

    public ChatColor getColor() {
        return color;
    }

    public ChatColor getSecondary() {
        return secondary;
    }
}
