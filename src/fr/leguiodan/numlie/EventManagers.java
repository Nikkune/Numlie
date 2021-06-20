package fr.leguiodan.numlie;

import fr.leguiodan.numlie.utilities.Database.DbConnection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
		final DbConnection dbConnection = main.getDatabaseManager().getDbConnection();

		try
		{
			final Connection connection = dbConnection.getConnection();
			main.databaseManager.createAccount(connection, event.getPlayer());
			main.databaseManager.createPlayerCash(connection, event.getPlayer());
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
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
