package fr.leguiodan.numlie;

import fr.leguiodan.numlie.utilities.Logger;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class FilesManagers {

	private final Main main;
	private final File playersFile;
	private final File statsFile;
	private final File guildsFile;
	private final File configFile;
	private final YamlConfiguration playersYaml;
	private final YamlConfiguration statsYaml;
	private final YamlConfiguration guildsYaml;
	private final YamlConfiguration configYaml;

	public FilesManagers(Main main)
	{
		this.main = main;
		this.playersFile = new File(main.getDataFolder(), "/players.yml");
		this.statsFile = new File(main.getDataFolder(), "/stats.yml");
		this.guildsFile = new File(main.getDataFolder(), "/guilds.yml");
		this.configFile = new File(main.getDataFolder(), "/config.yml");

		this.playersYaml = YamlConfiguration.loadConfiguration(playersFile);
		this.statsYaml = YamlConfiguration.loadConfiguration(statsFile);
		this.guildsYaml = YamlConfiguration.loadConfiguration(guildsFile);
		this.configYaml = YamlConfiguration.loadConfiguration(configFile);
	}

	public YamlConfiguration getPlayersYaml()
	{
		return playersYaml;
	}

	public YamlConfiguration getStatsYaml()
	{
		return statsYaml;
	}

	public YamlConfiguration getGuildsYaml()
	{
		return guildsYaml;
	}

	public YamlConfiguration getConfigYaml()
	{
		return configYaml;
	}

	public void statsUpdate(int level, int max_pv, int xp_need, int xp_win)
	{
		final String key = "Stats.Level " + level;
		statsYaml.set(key + ".max_pv", max_pv);
		statsYaml.set(key + ".xp_need", xp_need);
		statsYaml.set(key + ".xp_win", xp_win);
		try
		{
			statsYaml.save(statsFile);
			Logger.logSuccess("Le fichier Stats.yml a bien enregistré le niveau " + level + " !");
		} catch (IOException e)
		{
			Logger.logError("Le fichier Stats.yml n'a pas enregistré le niveau " + level + " !");
			e.printStackTrace();
		}
	}

	public void init()
	{
		if (!configFile.exists())
		{
			try
			{
				main.databaseManager.updateStats(main.databaseManager.getDbConnection().getConnection(), main);
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			configYaml.set("Main.Reload", false);
			try
			{
				configYaml.save(configFile);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		} else
		{
			if (configYaml.getBoolean("Main.Reload"))
			{
				try
				{
					main.databaseManager.updateStats(main.databaseManager.getDbConnection().getConnection(), main);
				} catch (SQLException e)
				{
					e.printStackTrace();
				}
				configYaml.set("Main.Reload", false);
				try
				{
					configYaml.save(configFile);
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		Logger.logSuccess("inti Files ok !");
	}
}
