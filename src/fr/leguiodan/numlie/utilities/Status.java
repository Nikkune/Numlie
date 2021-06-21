package fr.leguiodan.numlie.utilities;

import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;

public enum Status {
	Vagabond(1, "Vagabond", ChatColor.DARK_GRAY),
	Mercenary(2, "Mercenary", ChatColor.WHITE),
	Apprentice(3, "Apprentice", ChatColor.DARK_AQUA),
	Knight(4, "Knight", ChatColor.AQUA),
	Great_Knight(5, "Great Knight", ChatColor.DARK_BLUE),
	High_Knight(6, "High_Knight", ChatColor.BLUE),
	Paladin(7, "Paladin", ChatColor.DARK_GREEN),
	High_Paladin(8, "High_Paladin", ChatColor.GREEN),
	Sage(9, "Sage", ChatColor.DARK_PURPLE),
	Master(10, "Master", ChatColor.LIGHT_PURPLE),
	Prince(11, "Prince", ChatColor.DARK_RED),
	Palatine(12, "Palatine", ChatColor.RED),
	Imperator(13, "Imperator", ChatColor.GOLD);

	private final int id;
	private final String displayName;
	private final ChatColor chatColor;
	private static final Map<Integer, Status> Id_Map;

	Status(int id, String displayName, ChatColor chatColor)
	{
		this.id = id;
		this.displayName = displayName;
		this.chatColor = chatColor;
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

	public int getId()
	{
		return id;
	}

	public String getDisplayName()
	{
		return displayName;
	}

	public ChatColor getChatColor()
	{
		return chatColor;
	}
}
