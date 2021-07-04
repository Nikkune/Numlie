package fr.leguiodan.numlie.managers;

import fr.leguiodan.numlie.Main;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class InstancesManager {
	final Main main;
	final FilesManager filesManager;
	List<String> instancesList = new ArrayList<>();
	Map<Player, String> instancesMap = new HashMap<>();

	InstancesManager(Main main, FilesManager filesManager)
	{
		this.main = main;
		this.filesManager = filesManager;
	}

	public void createAnInstance(Player playerCreator, Player[] players)
	{

	}

	public void deleteAnInstance(Player playerCreator)
	{

	}

	public boolean isInAInstance(Player player)
	{
		return true;
	}
}
