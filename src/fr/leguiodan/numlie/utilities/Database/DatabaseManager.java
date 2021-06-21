package fr.leguiodan.numlie.utilities.Database;

import fr.leguiodan.numlie.Main;
import fr.leguiodan.numlie.utilities.Logger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.UUID;

public class DatabaseManager {
	private final Main main;
	private final DbConnection dbConnection;

	public DatabaseManager(Main main)
	{
		this.main = main;
		this.dbConnection = new DbConnection(new DbCredentials("w1.webstrator.fr", "weball", "Xra24?k3", "server_database", 3306), main);
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
		final String lang = main.filesManagers.getLanguage();
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
				Logger.logSuccess(main.filesManagers.getMessageYaml().getString("Messages.accountCreate." + lang) + " " + player.getDisplayName() + " !");
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
	}

	public void updateStats(Connection connection)
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

	public void createPlayerCash(Connection connection, Player player)
	{
		final String lang = main.filesManagers.getLanguage();
		final PreparedStatement preparedStatement;
		try
		{
			preparedStatement = connection.prepareStatement("SELECT * FROM players WHERE uuid = ?");
			preparedStatement.setString(1, player.getUniqueId().toString());
			final ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next())
			{
				int xp = resultSet.getInt("xp");
				int level = resultSet.getInt("level");
				int money = resultSet.getInt("money");
				int status = resultSet.getInt("status");
				String link_key = resultSet.getString("link_key");
				int playtime = resultSet.getInt("playtime");
				int guild_id = resultSet.getInt("guild_id");
				int[] playerStats = new int[6];
				playerStats[0] = xp;
				playerStats[1] = level;
				playerStats[2] = money;
				playerStats[3] = status;
				playerStats[4] = playtime;
				playerStats[5] = guild_id;
				main.filesManagers.setPlayersStats(player, playerStats);
				main.filesManagers.setLink_Key(link_key,player);
				Logger.logSuccess(main.filesManagers.getMessageYaml().getString("Messages.cashCreate." + lang) + " " + player.getDisplayName() + " !");
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public void updatesPlayers(Connection connection, Player player)
	{
		final String lang = main.filesManagers.getLanguage();
		final String key = "Players." + player.getUniqueId().toString();
		final ConfigurationSection playersSection = main.filesManagers.getPlayersYaml().getConfigurationSection(key);
		if (playersSection != null)
		{
			final int xp = playersSection.getInt("xp");
			final int level = playersSection.getInt("level");
			final int money = playersSection.getInt("money");
			final int status = playersSection.getInt("status");
			final int playtime = playersSection.getInt("playtime");
			int guild_id = playersSection.getInt("guild_id");
			Object real_id;
			if (guild_id == 0)
			{
				real_id = null;
			} else
			{
				real_id = guild_id;
			}
			String uuid = player.getUniqueId().toString();
			final PreparedStatement preparedStatement;
			try
			{
				preparedStatement = connection.prepareStatement("UPDATE players SET xp = ?, level = ?, money = ?, status = ?, playtime = ?, guild_id = ? WHERE uuid = ?");
				preparedStatement.setInt(1, xp);
				preparedStatement.setInt(2, level);
				preparedStatement.setInt(3, money);
				preparedStatement.setInt(4, status);
				preparedStatement.setInt(5, playtime);
				preparedStatement.setObject(6, real_id);
				preparedStatement.setString(7, uuid);
				preparedStatement.execute();
				Logger.logSuccess(main.filesManagers.getMessageYaml().getString("Messages.accountUpdate." + lang) + " " + player.getDisplayName() + " !");
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
	}
}
