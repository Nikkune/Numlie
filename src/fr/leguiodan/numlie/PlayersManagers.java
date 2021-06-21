package fr.leguiodan.numlie;

import fr.leguiodan.numlie.utilities.ScoreboardsHandler;
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
		ScoreboardsHandler.updateScoreboard(player, main);
	}
}
