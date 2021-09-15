package fr.leguiodan.numlie.utilities.enumerations;

import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;

public enum Status {
    Vagabond(1, "Vagabond", "Vagabond", ChatColor.DARK_GRAY, 0, 10),
    Mercenary(2, "Mercenary", "Mercenaire", ChatColor.WHITE, 11, 20),
    Apprentice(3, "Apprentice", "Apprenti(e)", ChatColor.DARK_AQUA, 21, 30),
    Knight(4, "Knight", "Chevalier", ChatColor.AQUA, 31, 40),
    Great_Knight(5, "Great Knight", "Grand Chevalier", ChatColor.DARK_BLUE, 41, 50),
    High_Knight(6, "High Knight", "Haut Chevalier", ChatColor.BLUE, 51, 60),
    Paladin(7, "Paladin", "Paladin", ChatColor.DARK_GREEN, 61, 70),
    High_Paladin(8, "High Paladin", "Haut Paladin", ChatColor.GREEN, 71, 80),
    Sage(9, "Sage", "Sage", ChatColor.DARK_PURPLE, 81, 90),
    Master(10, "Master", "Maître(sse)", ChatColor.LIGHT_PURPLE, 91, 100),
    Prince(11, "Comte(ss)", "Comte(sse)", ChatColor.DARK_RED, 101, 101),
    Palatine(12, "Duke", "Duc", ChatColor.RED, 102, 102),
    Imperator(13, "Imperator", "Imperator", ChatColor.GOLD, 103, 103);

    private static final Map<Integer, Status> Id_Map;

    static {
        Id_Map = new HashMap<>();
        Status[] arrayOfStatus;
        int length = (arrayOfStatus = values()).length;
        for (int i = 0; i < length; i++) {
            Status status = arrayOfStatus[i];
            Id_Map.put(status.id, status);
        }
    }

    private final int id;
    private final String displayNameEN;
    private final String displayNameFR;
    private final ChatColor chatColor;
    private final int minLevel;
    private final int maxLevel;

    Status(int id, String displayNameEN, String displayNameFR, ChatColor chatColor, int minLevel, int maxLevel) {
        this.id = id;
        this.displayNameEN = displayNameEN;
        this.displayNameFR = displayNameFR;
        this.chatColor = chatColor;
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
    }

    public static Status idToStatus(int id) {
        return Id_Map.get(id);
    }

    public String getDisplayName(String language) {
        if (language.equalsIgnoreCase("FR")) {
            return displayNameFR;
        } else if (language.equalsIgnoreCase("EN")) {
            return displayNameEN;
        }
        return displayNameEN;
    }

    public ChatColor getChatColor() {
        return chatColor;
    }

    public int getMinLevel() {
        return minLevel;
    }

    public int getMaxLevel() {
        return maxLevel;
    }
}
