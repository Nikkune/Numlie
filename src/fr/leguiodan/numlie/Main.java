package fr.leguiodan.numlie;

import fr.leguiodan.numlie.utilities.Database.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import static fr.leguiodan.numlie.utilities.Logger.*;

public class Main extends JavaPlugin {

	public static Main INSTANCE;

	private DatabaseManager databaseManager;
	private PluginManager pluginManager;

	@Override
	public void onEnable()
	{
		INSTANCE = this;
		pluginManager = Bukkit.getServer().getPluginManager();
		logNormal("Hello !");
		databaseManager = new DatabaseManager();
		pluginManager.registerEvents(new eventHandler(INSTANCE), this);
	}

	@Override
	public void onDisable()
	{
		this.databaseManager.close();
		logNormal("Bye !");
	}

	public DatabaseManager getDatabaseManager()
	{
		return databaseManager;
	}
}
