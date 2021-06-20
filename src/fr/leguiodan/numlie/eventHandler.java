package fr.leguiodan.numlie;

import fr.leguiodan.numlie.utilities.Database.DbConnection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.Connection;
import java.sql.SQLException;

public class eventHandler implements Listener {

	private Main main;

	public eventHandler(Main main)
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
			main.getDatabaseManager().createAccount(connection, event.getPlayer());
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
}
