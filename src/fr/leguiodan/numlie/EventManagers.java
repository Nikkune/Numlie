package fr.leguiodan.numlie;

import fr.leguiodan.numlie.utilities.Database.DbConnection;
import fr.leguiodan.numlie.utilities.ScoreboardsHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.*;

import java.sql.Connection;
import java.sql.SQLException;

public class EventManagers implements Listener {

	private final Main main;

	public EventManagers(Main main)
	{
		this.main = main;
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
		ScoreboardsHandler.createScoreboard(player,main);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		final DbConnection dbConnection = main.getDatabaseManager().getDbConnection();

		try
		{
			final Connection connection = dbConnection.getConnection();
			main.databaseManager.updatesPlayers(connection, event.getPlayer());
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
}
