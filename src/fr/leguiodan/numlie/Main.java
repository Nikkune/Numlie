package fr.leguiodan.numlie;

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
	public FilesManagers filesManagers;
	public PlayersManagers playersManagers;

	@Override
	public void onEnable()
	{
		INSTANCE = this;
		filesManagers = new FilesManagers(INSTANCE);
		filesManagers.init();
		init();
		logNormal("Hello !");
	}

	@Override
	public void onDisable()
	{
		this.databaseManager.close();
		logNormal("Bye !");
	}

	private void init()
	{
		YamlConfiguration configYaml = filesManagers.getConfigYaml();
		PluginManager pluginManager = Bukkit.getServer().getPluginManager();
		databaseManager = new DatabaseManager(INSTANCE);
		pluginManager.registerEvents(new EventManagers(INSTANCE), this);
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
			filesManagers.saveFile(configYaml);
		}
		playersManagers = new PlayersManagers(this);
	}

	public DatabaseManager getDatabaseManager()
	{
		return databaseManager;
	}

}
