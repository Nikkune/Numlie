package fr.leguiodan.numlie;

import fr.leguiodan.numlie.utilities.Logger;
import fr.leguiodan.numlie.utilities.enumerations.Messages;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class FilesManager {

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

	public FilesManager(Main main)
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
			Logger.logSuccess(getMessage(Messages.Level_Saved, getLanguage()) + level + " !");
		} catch (IOException e)
		{
			Logger.logWarning(getMessage(Messages.Level_Not_Saved, getLanguage()) + level + " !");
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

		messageYaml.set(key + "statsYamlOk" + lang_en, "The Stats.yml file has saved the level : ");
		messageYaml.set(key + "statsYamlOk" + lang_fr, "Le fichier Stats.yml a enregistré le niveau : ");

		messageYaml.set(key + "statsYmlNo" + lang_en, "The Stats.yml file did not save level : ");
		messageYaml.set(key + "statsYmlNo" + lang_fr, "Le fichier Stats.yml n'a pas enregistré le niveau : ");

		messageYaml.set(key + "congratulations" + lang_en, "Congratulations !");
		messageYaml.set(key + "congratulations" + lang_fr, "Félicitations !");

		messageYaml.set(key + "shame" + lang_en, "Oh no !");
		messageYaml.set(key + "shame" + lang_fr, "Oh non !");

		messageYaml.set(key + "levelPlus" + lang_en, "You have gained a level! You are now at the level :");
		messageYaml.set(key + "levelPlus" + lang_fr, "Vous avez gagné un niveau ! Vous êtes maintenant au niveau :");

		messageYaml.set(key + "statusPlus" + lang_en, "You have a new status! You are now a:");
		messageYaml.set(key + "statusPlus" + lang_fr, "Vous avez un nouveau statut ! Vous êtes maintenant un :");

		messageYaml.set(key + "xpPlus" + lang_en, "You have gained : ");
		messageYaml.set(key + "xpPlus" + lang_fr, "Vous avez gagné : ");

		messageYaml.set(key + "xpMinus" + lang_en, "You lost 25% of your xp !");
		messageYaml.set(key + "xpMinus" + lang_fr, "Vous avez perdu 25% de votre xp !");

		messageYaml.set(key + "backupErr" + lang_en, "Backup error on the file :");
		messageYaml.set(key + "backupErr" + lang_fr, "Erreur de sauvegarde sur le fichier :");

		messageYaml.set(key + "backupOk" + lang_en, "Successful backup for file :");
		messageYaml.set(key + "backupOk" + lang_fr, "Sauvegarde réussie pour le fichier :");


		saveFile(messageYaml);
	}

	public String getMessage(Messages message, String lang)
	{
		return messageYaml.getString("Messages." + message.getKey() + "." + lang);
	}

	public String getLanguage()
	{
		return configYaml.getString("Main.Language");
	}

	public int[] getPlayersStats(Player player)
	{
		final String uuid = player.getUniqueId().toString();
		int[] playerStats = new int[6];
		playerStats[0] = playersYaml.getInt("Players." + uuid + ".xp");
		playerStats[1] = playersYaml.getInt("Players." + uuid + ".level");
		playerStats[2] = playersYaml.getInt("Players." + uuid + ".money");
		playerStats[3] = playersYaml.getInt("Players." + uuid + ".status");
		playerStats[4] = playersYaml.getInt("Players." + uuid + ".playtime");
		playerStats[5] = playersYaml.getInt("Players." + uuid + ".guild_id");
		return playerStats;
	}

	public void setPlayersStats(Player player, int[] playerStats)
	{
		final String uuid = player.getUniqueId().toString();
		playersYaml.set("Players." + uuid + ".xp", playerStats[0]);
		playersYaml.set("Players." + uuid + ".level", playerStats[1]);
		playersYaml.set("Players." + uuid + ".money", playerStats[2]);
		playersYaml.set("Players." + uuid + ".status", playerStats[3]);
		playersYaml.set("Players." + uuid + ".playtime", playerStats[4]);
		playersYaml.set("Players." + uuid + ".guild_id", playerStats[5]);
		saveFile(playersYaml);
	}

	public int getXpNeed(int level)
	{
		final String key = "Stats.Level " + level;
		return statsYaml.getInt(key + ".xp_need");
	}

	public int getXpWin(int level)
	{
		final String key = "Stats.Level " + level;
		return statsYaml.getInt(key + ".xp_win");
	}

	public int getMaxPv(int level)
	{
		final String key = "Stats.Level " + level;
		return statsYaml.getInt(key + ".max_pv");
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
				Logger.logSuccess(getMessage(Messages.Backup_Success, getLanguage()) + "Config.yml");
			} catch (IOException e)
			{
				Logger.logError(getMessage(Messages.Backup_Error, getLanguage()) + "Config.yml");
				e.printStackTrace();
			}
		} else if (yamlConfiguration == messageYaml)
		{
			try
			{
				messageYaml.save(messageFile);
				Logger.logSuccess(getMessage(Messages.Backup_Success, getLanguage()) + "Messages.yml");
			} catch (IOException e)
			{
				Logger.logError(getMessage(Messages.Backup_Error, getLanguage()) + "Messages.yml");
				e.printStackTrace();
			}
		} else if (yamlConfiguration == guildsYaml)
		{
			try
			{
				guildsYaml.save(guildsFile);
				Logger.logSuccess(getMessage(Messages.Backup_Success, getLanguage()) + "Guilds.yml");
			} catch (IOException e)
			{
				Logger.logError(getMessage(Messages.Backup_Error, getLanguage()) + "Guilds.yml");
				e.printStackTrace();
			}
		} else if (yamlConfiguration == playersYaml)
		{
			try
			{
				playersYaml.save(playersFile);
				Logger.logSuccess(getMessage(Messages.Backup_Success, getLanguage()) + "Players.yml");
			} catch (IOException e)
			{
				Logger.logError(getMessage(Messages.Backup_Error, getLanguage()) + "Players.yml");
				e.printStackTrace();
			}
		} else if (yamlConfiguration == statsYaml)
		{
			try
			{
				statsYaml.save(statsFile);
				Logger.logSuccess(getMessage(Messages.Backup_Success, getLanguage()) + "Stats.yml");
			} catch (IOException e)
			{
				Logger.logError(getMessage(Messages.Backup_Error, getLanguage()) + "Stats.yml");
				e.printStackTrace();
			}
		}
	}

	public void setLink_Key(Player player, String link_key)
	{
		final String uuid = player.getUniqueId().toString();
		playersYaml.set("Players." + uuid + ".link_key", link_key);
		saveFile(playersYaml);
	}

	public void setPlayerLang(Player player, String lang)
	{
		final String uuid = player.getUniqueId().toString();
		playersYaml.set("Players." + uuid + ".lang", lang);
		saveFile(playersYaml);
	}

	public String getPlayerLang(Player player)
	{
		final String uuid = player.getUniqueId().toString();
		return playersYaml.getString("Players." + uuid + ".lang");
	}
}
