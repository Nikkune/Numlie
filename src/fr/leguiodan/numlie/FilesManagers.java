package fr.leguiodan.numlie;

import fr.leguiodan.numlie.utilities.Logger;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FilesManagers {

	private final File playersFile;
	private final File statsFile;
	private final File guildsFile;
	private final File configFile;
	private final File messageFile;
	private final YamlConfiguration playersYaml;
	private final YamlConfiguration statsYaml;
	private final YamlConfiguration guildsYaml;
	private final YamlConfiguration configYaml;
	private final YamlConfiguration messageYaml;

	public FilesManagers(Main main)
	{
		this.playersFile = new File(main.getDataFolder(), "/players.yml");
		this.statsFile = new File(main.getDataFolder(), "/stats.yml");
		this.guildsFile = new File(main.getDataFolder(), "/guilds.yml");
		this.configFile = new File(main.getDataFolder(), "/config.yml");
		this.messageFile = new File(main.getDataFolder(), "/message.yml");

		this.playersYaml = YamlConfiguration.loadConfiguration(playersFile);
		this.statsYaml = YamlConfiguration.loadConfiguration(statsFile);
		this.guildsYaml = YamlConfiguration.loadConfiguration(guildsFile);
		this.configYaml = YamlConfiguration.loadConfiguration(configFile);
		this.messageYaml = YamlConfiguration.loadConfiguration(messageFile);
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

	public YamlConfiguration getMessageYaml()
	{
		return messageYaml;
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

	public void messageUpdate()
	{
		final String key = "Messages.";
		final String lang_en = ".EN";
		final String lang_fr = ".FR";

		messageYaml.set(key + "databaseOk" + lang_en, "Successful connection to the database !");
		messageYaml.set(key + "databaseOk" + lang_fr, "Connexion réussie à la base de données !");

		messageYaml.set(key + "accountCreate" + lang_en, "Account created for the player :");
		messageYaml.set(key + "accountCreate" + lang_fr, "Compte créé pour le joueur :");

		messageYaml.set(key + "cashCreate" + lang_en, "Cash created for the player :");
		messageYaml.set(key + "cashCreate" + lang_fr, "Cash créé pour le joueur :");

		messageYaml.set(key + "accountUpdate" + lang_en, "Account updated for the player :");
		messageYaml.set(key + "accountUpdate" + lang_fr, "Compte mis à jour pour le joueur :");


		saveFile(messageYaml);
		Logger.logSuccess("Message OK !");
	}

	public String getLanguage()
	{
		return configYaml.getString("Main.Language");
	}

	public void init()
	{
		if (!configFile.exists())
		{
			configYaml.set("Main.Reload", false);
			configYaml.set("Main.Language", "EN");
			saveFile(configYaml);
		}
		messageUpdate();
		Logger.logSuccess("inti Files ok !");
	}

	public void saveFile(YamlConfiguration yamlConfiguration)
	{
		if (yamlConfiguration == configYaml)
		{
			try
			{
				configYaml.save(configFile);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		} else if (yamlConfiguration == messageYaml)
		{
			try
			{
				messageYaml.save(messageFile);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		} else if (yamlConfiguration == guildsYaml)
		{
			try
			{
				guildsYaml.save(guildsFile);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		} else if (yamlConfiguration == playersYaml)
		{
			try
			{
				playersYaml.save(playersFile);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		} else if (yamlConfiguration == statsYaml)
		{
			try
			{
				statsYaml.save(statsFile);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
