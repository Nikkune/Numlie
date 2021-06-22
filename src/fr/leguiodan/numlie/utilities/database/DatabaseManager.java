package fr.leguiodan.numlie.utilities.database;

import fr.leguiodan.numlie.Main;
import fr.leguiodan.numlie.utilities.Logger;
import fr.leguiodan.numlie.utilities.enumerations.Messages;
import org.bukkit.configuration.ConfigurationSection;
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
		final String lang = main.filesManager.getLanguage();
		while (link_key.length() < 12)
		{
			final Random random = new Random();
			link_key.append(random.nextInt(10));
		}
		if (!hasAccount(connection, uuid))
		{
			final PreparedStatement preparedStatement;
			final PreparedStatement preparedStatement2;
			try
			{
				preparedStatement = connection.prepareStatement("INSERT INTO players(uuid,pseudo,link_key) VALUES (?,?,?)");
				preparedStatement.setString(1, uuid.toString());
				preparedStatement.setString(2, player.getDisplayName());
				preparedStatement.setString(3, link_key.toString());
				preparedStatement.execute();
				preparedStatement2 = connection.prepareStatement("INSERT INTO players(uuid,online) VALUES (?,?)");
				preparedStatement2.setString(1, uuid.toString());
				preparedStatement2.setBoolean(2, true);
				preparedStatement2.execute();
				Logger.logSuccess(main.filesManager.getMessage(Messages.Account_Created, lang) + " " + player.getDisplayName() + " !");
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
				main.filesManager.statsUpdate(level, max_pv, xp_need, xp_win);
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public void createPlayerCash(Connection connection, Player player)
	{
		final String lang = main.filesManager.getLanguage();
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
				String player_lang = resultSet.getString("lang");
				int[] playerStats = new int[6];
				playerStats[0] = xp;
				playerStats[1] = level;
				playerStats[2] = money;
				playerStats[3] = status;
				playerStats[4] = playtime;
				playerStats[5] = guild_id;
				main.filesManager.setPlayersStats(player, playerStats);
				main.filesManager.setLink_Key(player, link_key);
				main.filesManager.setPlayerLang(player, player_lang);
				Logger.logSuccess(main.filesManager.getMessage(Messages.Cash_Created, lang) + " " + player.getDisplayName() + " !");
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public void updatesPlayers(Connection connection, Player player)
	{
		final String lang = main.filesManager.getLanguage();
		final String key = "Players." + player.getUniqueId().toString();
		final ConfigurationSection playersSection = main.filesManager.getPlayersYaml().getConfigurationSection(key);
		if (playersSection != null)
		{
			final int xp = playersSection.getInt("xp");
			final int level = playersSection.getInt("level");
			final int money = playersSection.getInt("money");
			final int status = playersSection.getInt("status");
			final int playtime = playersSection.getInt("playtime");
			int guild_id = playersSection.getInt("guild_id");
			String player_lang = playersSection.getString("lang");
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
				preparedStatement = connection.prepareStatement("UPDATE players SET xp = ?, level = ?, money = ?, status = ?, playtime = ?, guild_id = ?, lang = ? WHERE uuid = ?");
				preparedStatement.setInt(1, xp);
				preparedStatement.setInt(2, level);
				preparedStatement.setInt(3, money);
				preparedStatement.setInt(4, status);
				preparedStatement.setInt(5, playtime);
				preparedStatement.setObject(6, real_id);
				preparedStatement.setString(7, player_lang);
				preparedStatement.setString(8, uuid);
				preparedStatement.execute();
				Logger.logSuccess(main.filesManager.getMessage(Messages.Account_Updated, lang) + " " + player.getDisplayName() + " !");
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
	}

	public void loadGuilds(Connection connection)
	{
		final PreparedStatement preparedStatement;
		try
		{
			preparedStatement = connection.prepareStatement("SELECT * FROM guilds");
			final ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next())
			{
				int guild_id = resultSet.getInt("id");
				createGuildCash(connection, guild_id);
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public void createGuildCash(Connection connection, int guild_id)
	{
		final PreparedStatement preparedStatement;
		try
		{
			preparedStatement = connection.prepareStatement("SELECT * FROM guilds WHERE id = ?");
			preparedStatement.setInt(1, guild_id);
			final ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next())
			{
				String name = resultSet.getString("name");
				int xp = resultSet.getInt("xp");
				int level = resultSet.getInt("level");
				int money = resultSet.getInt("money");
				int player_nbr = resultSet.getInt("player_nbr");
				String owner_uuid = resultSet.getString("owner_uuid");
				main.guildsManager.setGuildCash(guild_id, name, xp, level, money, player_nbr, owner_uuid);
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public void updateGuild(Connection connection, int guild_id)
	{
		final int xp = main.guildsManager.getGuildXp(guild_id);
		final int level = main.guildsManager.getGuildLevel(guild_id);
		final int money = main.guildsManager.getGuildMoney(guild_id);
		final int player_nbr = main.guildsManager.getGuildNbr(guild_id);
		final String name = main.guildsManager.getGuildName(guild_id);

		final PreparedStatement preparedStatement;
		try
		{
			preparedStatement = connection.prepareStatement("UPDATE guilds SET xp = ?, level = ?, money = ?, player_nbr = ? WHERE id = ?");
			preparedStatement.setInt(1, xp);
			preparedStatement.setInt(2, level);
			preparedStatement.setInt(3, money);
			preparedStatement.setInt(4, player_nbr);
			preparedStatement.setInt(5, guild_id);
			preparedStatement.execute();
			Logger.logSuccess(main.filesManager.getMessage(Messages.Guild_Updated, main.filesManager.getLanguage()) + " " + name + " !");
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public boolean hasGuild(Connection connection, UUID uuid)
	{
		final PreparedStatement preparedStatement;
		try
		{
			preparedStatement = connection.prepareStatement("SELECT player_uuid FROM guilds_members WHERE player_uuid = ?");
			preparedStatement.setString(1, uuid.toString());
			final ResultSet resultSet = preparedStatement.executeQuery();
			return resultSet.next();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public boolean guildExist(Connection connection, String guild_name)
	{
		final PreparedStatement preparedStatement;
		try
		{
			preparedStatement = connection.prepareStatement("SELECT name FROM guilds WHERE name = ?");
			preparedStatement.setString(1, guild_name);
			final ResultSet resultSet = preparedStatement.executeQuery();
			return resultSet.next();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public void createGuild(Connection connection, Player player, String guild_name)
	{
		final UUID uuid = player.getUniqueId();
		final String lang = main.filesManager.getLanguage();
		final String player_lang = main.filesManager.getPlayerLang(player);
		if (!hasGuild(connection, uuid))
		{
			if (!guildExist(connection, guild_name))
			{
				final PreparedStatement preparedStatement;
				final PreparedStatement preparedStatement2;
				final PreparedStatement preparedStatement3;
				try
				{
					preparedStatement = connection.prepareStatement("INSERT INTO guilds(name,owner_uuid) VALUES (?,?)");
					preparedStatement.setString(1, uuid.toString());
					preparedStatement.setString(2, uuid.toString());
					preparedStatement.execute();
					preparedStatement2 = connection.prepareStatement("SELECT * FROM guilds WHERE owner_uuid = ? ");
					preparedStatement2.setString(1, uuid.toString());
					ResultSet resultSet = preparedStatement2.executeQuery();
					if (resultSet.next())
					{
						int guild_id = resultSet.getInt("id");
						preparedStatement3 = connection.prepareStatement("INSERT INTO guilds_members(player_uuid,guild_id,guild_grad,accepted) VALUES (?,?,?,?)");
						preparedStatement3.setString(1, uuid.toString());
						preparedStatement3.setInt(2, guild_id);
						preparedStatement3.setInt(3, 10);
						preparedStatement3.setBoolean(4, true);
						preparedStatement3.execute();
						Logger.logSuccess(main.filesManager.getMessage(Messages.Guild_Created, lang) + " " + guild_name + " !");
					}

				} catch (SQLException e)
				{
					e.printStackTrace();
				}
			} else
			{
				player.sendMessage(main.filesManager.getMessage(Messages.Guild_Exist, player_lang));
			}
		} else
		{
			player.sendMessage(main.filesManager.getMessage(Messages.Guild_Have, player_lang));
		}
	}

	public void setOnline(Connection connection, String uuid)
	{
		final PreparedStatement preparedStatement;
		try
		{
			preparedStatement = connection.prepareStatement("UPDATE online_status SET(online) VALUES(?) WHERE uuid = ?");
			preparedStatement.setBoolean(1, true);
			preparedStatement.setString(2, uuid);
			preparedStatement.execute();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public void setOffline(Connection connection, String uuid)
	{
		final PreparedStatement preparedStatement;
		try
		{
			preparedStatement = connection.prepareStatement("UPDATE online_status SET(online) VALUES(?) WHERE uuid = ?");
			preparedStatement.setBoolean(1, false);
			preparedStatement.setString(2, uuid);
			preparedStatement.execute();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
}
