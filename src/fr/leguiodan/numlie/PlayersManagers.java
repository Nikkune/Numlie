package fr.leguiodan.numlie;

import fr.leguiodan.numlie.utilities.ScoreboardsHandler;
import fr.leguiodan.numlie.utilities.enumerations.Status;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PlayersManagers {
	private final Main main;

	public PlayersManagers(Main main)
	{
		this.main = main;
	}

	public void updatePlayerMin(Player player)
	{
		int[] playerStats = main.filesManagers.getPlayersStats(player);
		playerStats[4] = playerStats[4] + 1;
		main.filesManagers.setPlayersStats(player, playerStats);
		updatePlayer(player);
	}

	public void updatePlayer(Player player)
	{
		int[] playerStats = main.filesManagers.getPlayersStats(player);
		int xp = playerStats[0];
		int level = playerStats[1];
		int status_id = playerStats[3];
		final Status status = Status.idToStatus(status_id);
		final int xp_need = main.filesManagers.getXpNeed(level);
		final double max_pv = main.filesManagers.getMaxPv(level);

		if (xp > xp_need)
		{
			level++;
			xp = xp - xp_need;
			levelPassed(player,level);
		}
		if (xp == xp_need)
		{
			xp = 0;
			level++;
			levelPassed(player,level);
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
			statusPassed(player,status_id);
		}

		playerStats[0] = xp;
		playerStats[1] = level;
		playerStats[3] = status_id;
		main.filesManagers.setPlayersStats(player, playerStats);
		ScoreboardsHandler.updateScoreboard(player, main);
	}

	public void entityKilled(Player player, int entity_level)
	{
		final int xp_win = main.filesManagers.getXpWin(entity_level);
		int[] playerStats = main.filesManagers.getPlayersStats(player);
		int xp = playerStats[0];
		xp = xp + xp_win;
		playerStats[0] = xp;
		main.filesManagers.setPlayersStats(player, playerStats);
		player.sendMessage("You have gained " + xp_win + " xp");
		updatePlayer(player);
	}

	public void levelPassed(Player player, int level)
	{
		String player_lang = main.filesManagers.getPlayerLang(player);
		player.sendTitle(ChatColor.GREEN + "Congratulations", ChatColor.GREEN + "You have gained a level ! You are now level : " + level,10,20*2,10);
	}

	public void statusPassed(Player player, int status_id)
	{
		Status status = Status.idToStatus(status_id);
		player.sendTitle(ChatColor.GREEN + "Congratulations", ChatColor.GREEN + "You have a new status ! You are now a : " + status.getChatColor() + status.getDisplayName(),10,20*2,10);
	}
}
