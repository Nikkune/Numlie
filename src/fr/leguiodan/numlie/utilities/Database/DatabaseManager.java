package fr.leguiodan.numlie.utilities.Database;

import fr.leguiodan.numlie.Main;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.UUID;

public class DatabaseManager {
	private final DbConnection dbConnection;

	public DatabaseManager()
	{
		this.dbConnection = new DbConnection(new DbCredentials("w1.webstrator.fr", "weball", "Xra24?k3", "server_database", 3306));
	}

	public void close()
	{
		try
		{
			this.dbConnection.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public DbConnection getDbConnection()
	{
		return dbConnection;
	}

	public boolean hasAccount(Connection connection, UUID uuid)
	{
		final PreparedStatement preparedStatement;
		try
		{
			preparedStatement = connection.prepareStatement("SELECT uuid FROM players WHERE uuid = ?");
			preparedStatement.setString(1, uuid.toString());
			final ResultSet resultSet = preparedStatement.executeQuery();
			return resultSet.next();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public void createAccount(Connection connection, Player player)
	{
		final UUID uuid = player.getUniqueId();
		StringBuilder link_key = new StringBuilder();
		while (link_key.length() < 12)
		{
			final Random random = new Random();
			link_key.append(random.nextInt(10));
		}
		if (!hasAccount(connection, uuid))
		{
			final PreparedStatement preparedStatement;
			try
			{
				preparedStatement = connection.prepareStatement("INSERT INTO players(uuid,pseudo,link_key) VALUES (?,?,?)");
				preparedStatement.setString(1, uuid.toString());
				preparedStatement.setString(2, player.getDisplayName());
				preparedStatement.setString(3, link_key.toString());
				preparedStatement.execute();
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
	}

	public void updateStats(Connection connection, Main main)
	{
		final PreparedStatement preparedStatement;
		try
		{
			preparedStatement = connection.prepareStatement("SELECT * FROM stats");
			final ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next())
			{
				int level = resultSet.getInt("level");
				int max_pv = resultSet.getInt("max_pv");
				int xp_need = resultSet.getInt("xp_need");
				int xp_win = resultSet.getInt("xp_win");
				main.filesManagers.statsUpdate(level, max_pv, xp_need, xp_win);
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
}
