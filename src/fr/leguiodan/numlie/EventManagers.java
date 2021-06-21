package fr.leguiodan.numlie;

import fr.leguiodan.numlie.utilities.Database.DbConnection;
import fr.leguiodan.numlie.utilities.ScoreboardsHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class EventManagers implements Listener {

	private final Main main;
	private final Map<Player, Integer> task_map;

	public EventManagers(Main main)
	{
		this.main = main;
		this.task_map = new HashMap<>();
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		final Player player = event.getPlayer();
		final DbConnection dbConnection = main.getDatabaseManager().getDbConnection();
		try
		{
			final Connection connection = dbConnection.getConnection();
			main.databaseManager.createAccount(connection, player);
			main.databaseManager.createPlayerCash(connection, player);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		ScoreboardsHandler.createScoreboard(player, main);
		final int task = Bukkit.getScheduler().scheduleSyncRepeatingTask(main, () -> main.playersManagers.updatePlayerMin(player), 20L * 60, 20L * 60);
		task_map.put(player, task);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		final Player player = event.getPlayer();
		final DbConnection dbConnection = main.getDatabaseManager().getDbConnection();
		Bukkit.getScheduler().cancelTask(task_map.get(player));
		try
		{
			final Connection connection = dbConnection.getConnection();
			main.databaseManager.updatesPlayers(connection, player);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
}
