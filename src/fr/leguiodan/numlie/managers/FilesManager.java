package fr.leguiodan.numlie.managers;

import com.sun.istack.internal.NotNull;
import fr.leguiodan.numlie.Main;
import fr.leguiodan.numlie.utilities.Logger;
import fr.leguiodan.numlie.utilities.enumerations.Messages;
import fr.leguiodan.numlie.utilities.handlers.ChatHandler;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FilesManager {

	private final Main main;

	private final File playersFile;
	private final File statsFile;
	private final File configFile;
	private final File messageFile;
	private final File instancesFile;
	private final YamlConfiguration playersYaml;
	private final YamlConfiguration statsYaml;
	private final YamlConfiguration configYaml;
	private final YamlConfiguration messageYaml;

	public FilesManager(Main main)
	{
		this.main = main;

		this.playersFile = new File(main.getDataFolder(), "/players.yml");
		this.statsFile = new File(main.getDataFolder(), "/stats.yml");
		this.configFile = new File(main.getDataFolder(), "/config.yml");
		this.messageFile = new File(main.getDataFolder(), "/message.yml");
		this.instancesFile = new File(main.getDataFolder(), "/Instances");

		this.playersYaml = YamlConfiguration.loadConfiguration(playersFile);
		this.statsYaml = YamlConfiguration.loadConfiguration(statsFile);
		this.configYaml = YamlConfiguration.loadConfiguration(configFile);
		this.messageYaml = YamlConfiguration.loadConfiguration(messageFile);
	}

	public void init()
	{
		if (!configFile.exists())
		{
			configYaml.set("Main.Reload", false);
			configYaml.set("Main.Language", "EN");
			configYaml.set("Instances.Numbers", 0);
			saveFile(configYaml, true);
		}
		messageUpdate();
		Logger.logSuccess("inti Files ok !");
	}

	public void saveFile(YamlConfiguration yamlConfiguration, boolean log)
	{
		if (yamlConfiguration == configYaml)
		{
			try
			{
				configYaml.save(configFile);
				if (log)
				{
					Logger.logSuccess(getMessage(Messages.Backup_Success, getLanguage()) + "Config.yml");
				}
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
				if (log)
				{
					Logger.logSuccess(getMessage(Messages.Backup_Success, getLanguage()) + "Messages.yml");
				}
			} catch (IOException e)
			{
				Logger.logError(getMessage(Messages.Backup_Error, getLanguage()) + "Messages.yml");
				e.printStackTrace();
			}
		} else if (yamlConfiguration == playersYaml)
		{
			try
			{
				playersYaml.save(playersFile);
				if (log)
				{
					Logger.logSuccess(getMessage(Messages.Backup_Success, getLanguage()) + "Players.yml");
				}
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
				if (log)
				{
					Logger.logSuccess(getMessage(Messages.Backup_Success, getLanguage()) + "Stats.yml");
				}
			} catch (IOException e)
			{
				Logger.logError(getMessage(Messages.Backup_Error, getLanguage()) + "Stats.yml");
				e.printStackTrace();
			}
		}
	}

	public void saveFile(YamlConfiguration yamlConfiguration, File file, boolean log)
	{
		try
		{
			yamlConfiguration.save(file);
			if (log)
			{
				Logger.logSuccess(getMessage(Messages.Backup_Success, getLanguage()) + file.getName());
			}
		} catch (IOException e)
		{
			Logger.logError(getMessage(Messages.Backup_Error, getLanguage()) + file.getName());
			e.printStackTrace();
		}
	}

	public void deleteFile(File file)
	{
		String fileName = file.getName();
		if (file.delete())
		{
			Logger.logSuccess(getMessage(Messages.Delete_Success, getLanguage()) + fileName);
		} else
		{
			Logger.logError(getMessage(Messages.Delete_Error, getLanguage()) + fileName);
		}
	}

	//region ===== Players =====
	public YamlConfiguration getPlayersYaml()
	{
		return playersYaml;
	}

	public void setLink_Key(Player player, String link_key)
	{
		final String uuid = player.getUniqueId().toString();
		playersYaml.set("Players." + uuid + ".link_key", link_key);
		saveFile(playersYaml, false);
	}

	public String getLink_Key(Player player)
	{
		final String uuid = player.getUniqueId().toString();
		return playersYaml.getString("Players." + uuid + ".link_key");
	}

	public void setPlayerLang(Player player, String lang)
	{
		final String uuid = player.getUniqueId().toString();
		playersYaml.set("Players." + uuid + ".lang", lang);
		saveFile(playersYaml, false);
	}

	public String getPlayerLang(Player player)
	{
		final String uuid = player.getUniqueId().toString();
		return playersYaml.getString("Players." + uuid + ".lang");
	}

	public void setPlayerTitle(Player player, int title_type)
	{
		final String uuid = player.getUniqueId().toString();
		playersYaml.set("Players." + uuid + ".title", title_type);
		saveFile(playersYaml, true);
	}

	public int getPlayerTitle(Player player)
	{
		final String uuid = player.getUniqueId().toString();
		return playersYaml.getInt("Players." + uuid + ".title");
	}

	public int[] getPlayersStats(Player player)
	{
		final String uuid = player.getUniqueId().toString();
		int[] playerStats = new int[5];
		playerStats[0] = playersYaml.getInt("Players." + uuid + ".xp");
		playerStats[1] = playersYaml.getInt("Players." + uuid + ".level");
		playerStats[2] = playersYaml.getInt("Players." + uuid + ".money");
		playerStats[3] = playersYaml.getInt("Players." + uuid + ".status");
		playerStats[4] = playersYaml.getInt("Players." + uuid + ".playtime");
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
		saveFile(playersYaml, false);
	}
	//endregion

	//region ===== Stats =====
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
	//endregion

	//region ===== Messages =====
	public void messageUpdate()
	{
		final String key = "Messages.";
		final String lang_en = ".EN";
		final String lang_fr = ".FR";

		messageYaml.set(key + "databaseOk" + lang_en, "Successful connection to the database !");
		messageYaml.set(key + "databaseOk" + lang_fr, "Connexion réussie à la base de données !");

		messageYaml.set(key + "accountCreate" + lang_en, "Account created for the player : ");
		messageYaml.set(key + "accountCreate" + lang_fr, "Compte créé pour le joueur : ");

		messageYaml.set(key + "cashCreate" + lang_en, "Cash created for the player : ");
		messageYaml.set(key + "cashCreate" + lang_fr, "Cash créé pour le joueur : ");

		messageYaml.set(key + "accountUpdate" + lang_en, "Account updated for the player : ");
		messageYaml.set(key + "accountUpdate" + lang_fr, "Compte mis à jour pour le joueur : ");

		messageYaml.set(key + "statsYamlOk" + lang_en, "The Stats.yml file has saved the level : ");
		messageYaml.set(key + "statsYamlOk" + lang_fr, "Le fichier Stats.yml a enregistré le niveau : ");

		messageYaml.set(key + "statsYmlNo" + lang_en, "The Stats.yml file did not save level : ");
		messageYaml.set(key + "statsYmlNo" + lang_fr, "Le fichier Stats.yml n'a pas enregistré le niveau : ");

		messageYaml.set(key + "congratulations" + lang_en, "Congratulations !");
		messageYaml.set(key + "congratulations" + lang_fr, "Félicitations !");

		messageYaml.set(key + "shame" + lang_en, "Oh no !");
		messageYaml.set(key + "shame" + lang_fr, "Oh non !");

		messageYaml.set(key + "levelPlus" + lang_en, "You have gained a level! You are now at the level : ");
		messageYaml.set(key + "levelPlus" + lang_fr, "Vous avez gagné un niveau ! Vous êtes maintenant au niveau : ");

		messageYaml.set(key + "statusPlus" + lang_en, "You have a new status! You are now a : ");
		messageYaml.set(key + "statusPlus" + lang_fr, "Vous avez un nouveau statut ! Vous êtes maintenant un : ");

		messageYaml.set(key + "xpPlus" + lang_en, "You have gained : ");
		messageYaml.set(key + "xpPlus" + lang_fr, "Vous avez gagné : ");

		messageYaml.set(key + "xpMinus" + lang_en, "You lost 25% of your xp !");
		messageYaml.set(key + "xpMinus" + lang_fr, "Vous avez perdu 25% de votre xp !");

		messageYaml.set(key + "backupErr" + lang_en, "Backup error on the file : ");
		messageYaml.set(key + "backupErr" + lang_fr, "Erreur de sauvegarde sur le fichier : ");

		messageYaml.set(key + "backupOk" + lang_en, "Successful backup for file : ");
		messageYaml.set(key + "backupOk" + lang_fr, "Sauvegarde réussie pour le fichier : ");

		messageYaml.set(key + "deleteErr" + lang_en, "Delete error for the file : ");
		messageYaml.set(key + "deleteErr" + lang_fr, "Erreur de suppression pour le fichier : ");

		messageYaml.set(key + "deleteOk" + lang_en, "Successful deletion for the file : ");
		messageYaml.set(key + "deleteOk" + lang_fr, "Suppression réussie du fichier : ");

		messageYaml.set(key + "UILevel" + lang_en, "Level");
		messageYaml.set(key + "UILevel" + lang_fr, "Niveau");

		messageYaml.set(key + "UIMoney" + lang_en, "Money");
		messageYaml.set(key + "UIMoney" + lang_fr, "Argent");

		messageYaml.set(key + "UIPlaytime" + lang_en, "Playtime");
		messageYaml.set(key + "UIPlaytime" + lang_fr, "Temps De Jeu");

		messageYaml.set(key + "UIChangeLang" + lang_en, "Change language to : ");
		messageYaml.set(key + "UIChangeLang" + lang_fr, "Changer la langue pour : ");

		messageYaml.set(key + "UIChangeTitle" + lang_en, "Change announce in : ");
		messageYaml.set(key + "UIChangeTitle" + lang_fr, "Modifier l'annonce en : ");

		messageYaml.set(key + "UIGetLinkKey" + lang_en, "Get the link_key !");
		messageYaml.set(key + "UIGetLinkKey" + lang_fr, "Récupérer la link_key !");

		messageYaml.set(key + "UIChat" + lang_en, "Chat message");
		messageYaml.set(key + "UIChat" + lang_fr, "Message du chat");

		messageYaml.set(key + "UITitle" + lang_en, "Title");
		messageYaml.set(key + "UITitle" + lang_fr, "Titre");

		messageYaml.set(key + "instanceJoin" + lang_en, "You are in the instance : ");
		messageYaml.set(key + "instanceJoin" + lang_fr, "Vous êtes dans l'instance : ");

		messageYaml.set(key + "instanceHas" + lang_en, "You are already in an Instance / Group !");
		messageYaml.set(key + "instanceHas" + lang_fr, "Vous êtes déjà dans une Instance/Groupe !");

		messageYaml.set(key + "instanceCodeErr" + lang_en, "Incorrect instance code !!");
		messageYaml.set(key + "instanceCodeErr" + lang_fr, "Code d'instance incorrect !!");

		saveFile(messageYaml, true);
	}

	public String getMessage(Messages message, String lang)
	{
		return messageYaml.getString("Messages." + message.getKey() + "." + lang);
	}
	//endregion

	//region ===== Config =====
	public String getLanguage()
	{
		return configYaml.getString("Main.Language");
	}

	public YamlConfiguration getConfigYaml()
	{
		return configYaml;
	}
	//endregion

	//region ===== Instances =====
	public File[] getAllInstances()
	{
		FilenameFilter filenameFilter = (dir, name) -> {
			String nameLower = name.toLowerCase();
			return nameLower.endsWith(".yml");
		};
		return instancesFile.listFiles(filenameFilter);
	}

	public int getInstancesNumbers()
	{
		return configYaml.getInt("Instances.Numbers");
	}

	public void setInstancesNumbers(int nbr)
	{
		configYaml.set("Instances.Numbers", nbr);
		saveFile(configYaml, true);
	}

	public File getInstancesFile()
	{
		return instancesFile;
	}

	public List<Player> getPlayersOfInstance(@NotNull File instanceFile)
	{
		YamlConfiguration instanceYaml = YamlConfiguration.loadConfiguration(instanceFile);
		int number = instanceYaml.getInt("Players Numbers");
		int x = 0;
		List<Player> playerList = new ArrayList<>();
		while (x < number)
		{
			String playerUUID = instanceYaml.getString("Players." + x);
			Player player = main.getServer().getPlayer(playerUUID);
			playerList.add(player);
			x++;
		}
		return playerList;
	}

	public int getPlayersIdInInstance(@NotNull File instanceFile, Player player)
	{
		YamlConfiguration instanceYaml = YamlConfiguration.loadConfiguration(instanceFile);
		int number = instanceYaml.getInt("Players Numbers");
		int x = 0;
		while (x < number)
		{
			if (player.getUniqueId().toString().equalsIgnoreCase(instanceYaml.getString("Players." + x)))
			{
				return x;
			}
			x++;
		}
		return 99;
	}

	public File getInstanceViaCode(String code)
	{
		File[] files = getAllInstances();
		for (File file : files)
		{
			YamlConfiguration intance = YamlConfiguration.loadConfiguration(file);
			String instanceCode = intance.getString("Invite Code");
			if (instanceCode.equalsIgnoreCase(code))
			{
				return file;
			}
		}
		return null;
	}

	public File getInstanceOfPlayer(Player player)
	{
		File[] files = getAllInstances();
		for (File file : files)
		{
			List<Player> players = getPlayersOfInstance(file);
			if (players.contains(player))
			{
				Logger.logSuccess("This File is the One" + file.getName());
				return file;
			}
		}
		return null;
	}

	public Player getPlayerWhoCreateInstance(@NotNull File instanceFile)
	{
		YamlConfiguration instanceYaml = YamlConfiguration.loadConfiguration(instanceFile);
		String playerUUID = instanceYaml.getString("Players.0");
		return main.getServer().getPlayer(playerUUID);
	}

	public void addPlayerToInstance(@NotNull File instanceFile, Player player)
	{
		YamlConfiguration instanceYaml = YamlConfiguration.loadConfiguration(instanceFile);
		int nbr = instanceYaml.getInt("Players Numbers");
		instanceYaml.set("Players." + nbr, player.getUniqueId().toString());
		instanceYaml.set("Players Numbers", nbr + 1);
		saveFile(instanceYaml, instanceFile, true);
		player.sendMessage(ChatHandler.setInfoMessage(getMessage(Messages.Join_Instance, getPlayerLang(player)) + instanceFile.getName().substring(0, instanceFile.getName().length() - 4)));
	}
	//endregion
}
