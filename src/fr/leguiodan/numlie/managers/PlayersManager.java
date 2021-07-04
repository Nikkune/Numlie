package fr.leguiodan.numlie.managers;

import fr.leguiodan.numlie.Main;
import fr.leguiodan.numlie.utilities.ScoreboardsHandler;
import fr.leguiodan.numlie.utilities.enumerations.Messages;
import fr.leguiodan.numlie.utilities.enumerations.Status;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PlayersManager {
	private final Main main;

	public PlayersManager(Main main)
	{
		this.main = main;
	}

	public void updatePlayerMin(Player player)
	{
		int[] playerStats = main.filesManager.getPlayersStats(player);
		playerStats[4] = playerStats[4] + 1;
		main.filesManager.setPlayersStats(player, playerStats);
		updatePlayer(player);
	}

	public void updatePlayer(Player player)
	{
		int[] playerStats = main.filesManager.getPlayersStats(player);
		int xp = playerStats[0];
		int level = playerStats[1];
		int status_id = playerStats[3];
		final Status status = Status.idToStatus(status_id);
		final int xp_need = main.filesManager.getXpNeed(level);
		final double max_pv = main.filesManager.getMaxPv(level);

		if (xp > xp_need)
		{
			level++;
			xp = xp - xp_need;
			levelPassed(player, level);
		}
		if (xp == xp_need)
		{
			xp = 0;
			level++;
			levelPassed(player, level);
		}

		if (player.getHealthScale() < max_pv)
		{
			player.setHealthScale(max_pv);
		}

		if (level < status.getMinLevel())
		{
			status_id--;
		} else if (level > status.getMaxLevel())
		{
			status_id++;
			statusPassed(player, status_id);
		}

		playerStats[0] = xp;
		playerStats[1] = level;
		playerStats[3] = status_id;
		main.filesManager.setPlayersStats(player, playerStats);
		ScoreboardsHandler.updateScoreboard(player, main);
	}

	public void entityKilled(Player player, int entity_level)
	{
		String player_lang = main.filesManager.getPlayerLang(player);
		final int xp_win = main.filesManager.getXpWin(entity_level);
		int[] playerStats = main.filesManager.getPlayersStats(player);
		if (playerStats[1] < 100)
		{
			int xp = playerStats[0];
			xp = xp + xp_win;
			playerStats[0] = xp;
			main.filesManager.setPlayersStats(player, playerStats);
			player.sendMessage(main.filesManager.getMessage(Messages.Xp_Up, player_lang) + xp_win + " xp");
			updatePlayer(player);
		} else
		{
			playerStats[0] = 0;
			main.filesManager.setPlayersStats(player, playerStats);
		}
	}

	public void levelPassed(Player player, int level)
	{
		String player_lang = main.filesManager.getPlayerLang(player);
		player.sendTitle(ChatColor.GREEN + main.filesManager.getMessage(Messages.Congratulations, player_lang), ChatColor.GREEN + main.filesManager.getMessage(Messages.Level_Up, player_lang) + level, 10, 20 * 2, 10);
	}

	public void statusPassed(Player player, int status_id)
	{
		String player_lang = main.filesManager.getPlayerLang(player);
		Status status = Status.idToStatus(status_id);
		player.sendTitle(ChatColor.GREEN + main.filesManager.getMessage(Messages.Congratulations, player_lang), ChatColor.GREEN + main.filesManager.getMessage(Messages.Status_Up, player_lang) + status.getChatColor() + status.getDisplayName(player_lang), 10, 20 * 2, 10);
	}

	public void respawn(Player player)
	{
		String player_lang = main.filesManager.getPlayerLang(player);
		int[] playerStats = main.filesManager.getPlayersStats(player);
		int xp = playerStats[0];
		xp = (int) Math.round(xp * 0.75);
		playerStats[0] = xp;
		main.filesManager.setPlayersStats(player, playerStats);
		player.sendTitle(ChatColor.RED + main.filesManager.getMessage(Messages.Shame, player_lang), ChatColor.RED + main.filesManager.getMessage(Messages.Xp_Down, player_lang), 10, 20 * 2, 10);
		updatePlayer(player);
	}
}
