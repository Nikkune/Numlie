package fr.leguiodan.numlie.managers;

import fr.leguiodan.numlie.Main;
import fr.leguiodan.numlie.utilities.enumerations.Messages;
import fr.leguiodan.numlie.utilities.handlers.ChatHandler;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class InstancesManager {
	final Main main;
	final FilesManager filesManager;

	public InstancesManager(Main main, FilesManager filesManager)
	{
		this.main = main;
		this.filesManager = filesManager;
	}

	public void createAnInstance(Player playerCreator)
	{
		if (!isInAInstance(playerCreator))
		{
			UUID rand = UUID.randomUUID();
			int id = filesManager.getInstancesNumbers();
			File instanceFile = new File(filesManager.getInstancesFile(), "/Instance_" + id + ".yml");
			YamlConfiguration instanceYaml = YamlConfiguration.loadConfiguration(instanceFile);
			instanceYaml.set("Players Numbers", 1);
			instanceYaml.set("Invite Code", rand.toString());
			instanceYaml.set("Players.0", playerCreator.getUniqueId().toString());
			filesManager.saveFile(instanceYaml, instanceFile, true);
			filesManager.setInstancesNumbers(id + 1);
		}
	}

	public void deleteAnInstance(Player player)
	{
		if (isInAInstance(player))
		{
			File instanceFile = filesManager.getInstanceOfPlayer(player);
			Player instanceCreator = filesManager.getPlayerWhoCreateInstance(instanceFile);
			if (instanceCreator == player)
			{
				filesManager.deleteFile(instanceFile);
			}
		}
	}

	public void joinAnInstance(Player player, String code)
	{
		if (!isInAInstance(player))
		{
			File instance = filesManager.getInstanceViaCode(code);
			if (instance != null)
			{
				filesManager.addPlayerToInstance(instance, player);
			} else
			{
				player.sendMessage(ChatHandler.setErrorMessage(filesManager.getMessage(Messages.Wrong_Instance_Code, filesManager.getPlayerLang(player))));
			}
		} else
		{
			player.sendMessage(ChatHandler.setWarningMessage(filesManager.getMessage(Messages.Already_In_Instance, filesManager.getPlayerLang(player))));
		}
	}

	public void leaveAnInstance(Player player)
	{
		if (isInAInstance(player))
		{
			File file = filesManager.getInstanceOfPlayer(player);
			int playerId = filesManager.getPlayersIdInInstance(file, player);
			YamlConfiguration instanceYaml = YamlConfiguration.loadConfiguration(file);
			instanceYaml.set("Players." + playerId, "leave");
			filesManager.saveFile(instanceYaml, file, true);
		}
	}

	public String getInstanceCode(Player player)
	{
		if (isInAInstance(player))
		{
			YamlConfiguration instanceYaml = YamlConfiguration.loadConfiguration(filesManager.getInstanceOfPlayer(player));
			return instanceYaml.getString("Invite Code");
		}
		return "No Instance";
	}

	public boolean isInAInstance(Player player)
	{
		for (File file : filesManager.getAllInstances())
		{
			List<Player> players = filesManager.getPlayersOfInstance(file);
			return players.contains(player);
		}
		return false;
	}
}
