package fr.leguiodan.numlie.utilities.handlers;

import com.sun.istack.internal.NotNull;
import fr.leguiodan.numlie.Main;
import fr.leguiodan.numlie.utilities.enumerations.Status;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class ChatHandler {

	public static String setJoinMessage(@NotNull Main main, @NotNull Player player)
	{
		final String uuid = player.getUniqueId().toString();
		final YamlConfiguration playersYaml = main.filesManager.getPlayersYaml();
		final int status_id = playersYaml.getInt("Players." + uuid + ".status");
		final Status status = Status.idToStatus(status_id);
		return ChatColor.DARK_GRAY + "[+] " + status.getChatColor() + player.getDisplayName() + ChatColor.RESET;
	}

	public static String setLeaveMessage(@NotNull Main main, @NotNull Player player)
	{
		final String uuid = player.getUniqueId().toString();
		final YamlConfiguration playersYaml = main.filesManager.getPlayersYaml();
		final int status_id = playersYaml.getInt("Players." + uuid + ".status");
		final Status status = Status.idToStatus(status_id);
		return ChatColor.DARK_GRAY + "[-] " + status.getChatColor() + player.getDisplayName() + ChatColor.RESET;
	}

	public static String setChatMessage(@NotNull Main main, String message, @NotNull Player sender)
	{
		final String uuid = sender.getUniqueId().toString();
		final YamlConfiguration playersYaml = main.filesManager.getPlayersYaml();
		final int status_id = playersYaml.getInt("Players." + uuid + ".status");
		final Status status = Status.idToStatus(status_id);
		return status.getChatColor() + sender.getDisplayName() + ChatColor.DARK_GRAY + " >> " + ChatColor.RESET + message;
	}

	public static String setInfoMessage(String message)
	{
		return ChatColor.DARK_GRAY + "[!] " + ChatColor.LIGHT_PURPLE + message + ChatColor.RESET;
	}

	public static String setWarningMessage(String message)
	{
		return ChatColor.DARK_GRAY + "[\u26a0] " + ChatColor.DARK_PURPLE + message + ChatColor.RESET;
	}

	public static String setErrorMessage(String message)
	{
		return ChatColor.DARK_GRAY + "[\u26a0] " + ChatColor.DARK_RED + message + ChatColor.RESET;
	}

	public static String setSuccessMessage(String message)
	{
		return ChatColor.DARK_GRAY + "[!] " + ChatColor.DARK_GREEN + message + ChatColor.RESET;
	}

	public static String setGlobalMessage(String message)
	{
		return ChatColor.GOLD + "[Global] " + message + ChatColor.RESET;
	}

	public static String setBroadcastMessage(String message)
	{
		return ChatColor.RED + "" + ChatColor.BOLD + "[\u26a0] " + message + ChatColor.RESET;
	}
}
