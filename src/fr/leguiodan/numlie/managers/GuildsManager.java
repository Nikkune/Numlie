package fr.leguiodan.numlie.managers;

import fr.leguiodan.numlie.Main;
import fr.leguiodan.numlie.utilities.Logger;
import fr.leguiodan.numlie.utilities.enumerations.Messages;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class GuildsManager {
	private final Main main;

	private final Map<String, Integer> guild_list;

	public GuildsManager(Main instance)
	{
		this.main = instance;
		this.guild_list = new HashMap<>();
	}

	public void loadGuilds()
	{
		try
		{
			main.databaseManager.loadGuilds(main.databaseManager.getDbConnection().getConnection());
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public void unloadGuilds()
	{
		guild_list.forEach((o, i) -> {
			try
			{
				main.databaseManager.updateGuild(main.databaseManager.getDbConnection().getConnection(), i);
				guild_list.remove(o);
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		});
	}

	public void setGuildCash(int guild_id, String guild_name, int guild_xp, int guild_level, int guild_money, int player_nbr, String owner_uuid)
	{
		String key = "Guilds." + guild_id + ".";
		main.filesManager.getGuildsYaml().set(key + "Name", guild_name);
		main.filesManager.getGuildsYaml().set(key + "Xp", guild_xp);
		main.filesManager.getGuildsYaml().set(key + "Level", guild_level);
		main.filesManager.getGuildsYaml().set(key + "Money", guild_money);
		main.filesManager.getGuildsYaml().set(key + "Player Number", player_nbr);
		main.filesManager.getGuildsYaml().set(key + "Owner UUID", owner_uuid);
		main.filesManager.saveFile(main.filesManager.getGuildsYaml());
		guild_list.put(owner_uuid, guild_id);
		Logger.logSuccess(main.filesManager.getMessage(Messages.Guild_Cash_Create, main.filesManager.getLanguage()) + guild_name + " !");
	}

	public int getGuildXp(int guild_id)
	{
		String key = "Guilds." + guild_id + ".";
		return main.filesManager.getGuildsYaml().getInt(key + "Xp");
	}

	public String getGuildName(int guild_id)
	{
		String key = "Guilds." + guild_id + ".";
		return main.filesManager.getGuildsYaml().getString(key + "Name");
	}

	public int getGuildLevel(int guild_id)
	{
		String key = "Guilds." + guild_id + ".";
		return main.filesManager.getGuildsYaml().getInt(key + "Level");
	}

	public int getGuildMoney(int guild_id)
	{
		String key = "Guilds." + guild_id + ".";
		return main.filesManager.getGuildsYaml().getInt(key + "Money");
	}

	public int getGuildNbr(int guild_id)
	{
		String key = "Guilds." + guild_id + ".";
		return main.filesManager.getGuildsYaml().getInt(key + "Player Number");
	}

	public String getGuildOwner(int guild_id)
	{
		String key = "Guilds." + guild_id + ".";
		return main.filesManager.getGuildsYaml().getString(key + "Owner UUID");
	}

	public void setGuildXp(int guild_id, int guild_xp)
	{
		String key = "Guilds." + guild_id + ".";
		main.filesManager.getGuildsYaml().set(key + "Xp", guild_xp);
		main.filesManager.saveFile(main.filesManager.getGuildsYaml());
	}

	public void setGuildLevel(int guild_id, int guild_Level)
	{
		String key = "Guilds." + guild_id + ".";
		main.filesManager.getGuildsYaml().set(key + "Level", guild_Level);
		main.filesManager.saveFile(main.filesManager.getGuildsYaml());
	}

	public void setGuildMoney(int guild_id, int guild_money)
	{
		String key = "Guilds." + guild_id + ".";
		main.filesManager.getGuildsYaml().set(key + "Money", guild_money);
		main.filesManager.saveFile(main.filesManager.getGuildsYaml());
	}

	public void setGuildNbr(int guild_id, int player_nbr)
	{
		String key = "Guilds." + guild_id + ".";
		main.filesManager.getGuildsYaml().set(key + "Money", player_nbr);
		main.filesManager.saveFile(main.filesManager.getGuildsYaml());
	}
}
