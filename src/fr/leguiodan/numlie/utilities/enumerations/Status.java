package fr.leguiodan.numlie.utilities.enumerations;

import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;

public enum Status {
	Vagabond(1, "Vagabond", ChatColor.DARK_GRAY,0,10),
	Mercenary(2, "Mercenary", ChatColor.WHITE,11,20),
	Apprentice(3, "Apprentice", ChatColor.DARK_AQUA,21,30),
	Knight(4, "Knight", ChatColor.AQUA,31,40),
	Great_Knight(5, "Great Knight", ChatColor.DARK_BLUE,41,50),
	High_Knight(6, "High_Knight", ChatColor.BLUE,51,60),
	Paladin(7, "Paladin", ChatColor.DARK_GREEN,61,70),
	High_Paladin(8, "High_Paladin", ChatColor.GREEN,71,80),
	Sage(9, "Sage", ChatColor.DARK_PURPLE,81,90),
	Master(10, "Master", ChatColor.LIGHT_PURPLE,91,100),
	Prince(11, "Prince", ChatColor.DARK_RED,101,101),
	Palatine(12, "Palatine", ChatColor.RED,102,102),
	Imperator(13, "Imperator", ChatColor.GOLD,103,103);

	private final int id;
	private final String displayName;
	private final ChatColor chatColor;
	private final int minLevel;
	private final int maxLevel;
	private static final Map<Integer, Status> Id_Map;

	Status(int id, String displayName, ChatColor chatColor, int minLevel, int maxLevel)
	{
		this.id = id;
		this.displayName = displayName;
		this.chatColor = chatColor;
		this.minLevel = minLevel;
		this.maxLevel = maxLevel;
	}

	static
	{
		Id_Map = new HashMap<>();
		Status[] arrayOfStatus;
		int length = (arrayOfStatus = values()).length;
		for (int i = 0; i < length; i++)
		{
			Status status = arrayOfStatus[i];
			Id_Map.put(status.id, status);
		}
	}

	public static Status idToStatus(int id)
	{
		return Id_Map.get(id);
	}

	public String getDisplayName()
	{
		return displayName;
	}

	public ChatColor getChatColor()
	{
		return chatColor;
	}

	public int getMinLevel()
	{
		return minLevel;
	}

	public int getMaxLevel()
	{
		return maxLevel;
	}
}
