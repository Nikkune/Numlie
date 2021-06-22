package fr.leguiodan.numlie;

import fr.leguiodan.numlie.managers.EventManager;
import fr.leguiodan.numlie.managers.FilesManager;
import fr.leguiodan.numlie.managers.GuildsManager;
import fr.leguiodan.numlie.managers.PlayersManager;
import fr.leguiodan.numlie.utilities.database.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

import static fr.leguiodan.numlie.utilities.Logger.logNormal;

public class Main extends JavaPlugin {

	public static Main INSTANCE;

	public DatabaseManager databaseManager;
	public FilesManager filesManager;
	public PlayersManager playersManager;
	public GuildsManager guildsManager;

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
		guildsManager.unloadGuilds();
		this.databaseManager.close();
		logNormal("Bye !");
	}

	private void init()
	{
		YamlConfiguration configYaml = filesManager.getConfigYaml();
		PluginManager pluginManager = Bukkit.getServer().getPluginManager();
		databaseManager = new DatabaseManager(INSTANCE);
		pluginManager.registerEvents(new EventManager(INSTANCE), this);
		if (configYaml.getBoolean("Main.Reload"))
		{
			try
			{
				databaseManager.updateStats(databaseManager.getDbConnection().getConnection());
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			configYaml.set("Main.Reload", false);
			filesManager.saveFile(configYaml);
		}
		guildsManager = new GuildsManager(INSTANCE);
		guildsManager.loadGuilds();
		playersManager = new PlayersManager(INSTANCE);
	}

	public DatabaseManager getDatabaseManager()
	{
		return databaseManager;
	}

}
