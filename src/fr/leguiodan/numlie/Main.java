package fr.leguiodan.numlie;

import fr.leguiodan.numlie.managers.*;
import fr.leguiodan.numlie.utilities.database.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import static fr.leguiodan.numlie.utilities.Logger.logNormal;

public class Main extends JavaPlugin {

	public static Main INSTANCE;

	public DatabaseManager databaseManager;
	public FilesManager filesManager;
	public PlayersManager playersManager;
	public InstancesManager instancesManager;
	public InventoryManager inventoryManager;

	@Override
	public void onEnable()
	{
		INSTANCE = this;
		filesManager = new FilesManager(INSTANCE);
		filesManager.init();
		init();
		logNormal("Hello !");
	}

	@Override
	public void onDisable()
	{
		databaseManager.setOffline("server");
		this.databaseManager.close();
		logNormal("Bye !");
	}

	private void init()
	{
		YamlConfiguration configYaml = filesManager.getConfigYaml();
		PluginManager pluginManager = Bukkit.getServer().getPluginManager();
		databaseManager = new DatabaseManager(INSTANCE);
		pluginManager.registerEvents(new EventsManager(INSTANCE), this);
		if (configYaml.getBoolean("Main.Reload"))
		{
			databaseManager.updateStats();
			configYaml.set("Main.Reload", false);
			filesManager.saveFile(configYaml, true);
		}
		databaseManager.setOnline("server");
		playersManager = new PlayersManager(INSTANCE);
		instancesManager = new InstancesManager(INSTANCE, filesManager);
		inventoryManager = new InventoryManager(INSTANCE);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> databaseManager.restartConnections(), 20L * 60 * 20, 20L * 60 * 20);
	}

	public DatabaseManager getDatabaseManager()
	{
		return databaseManager;
	}

}
