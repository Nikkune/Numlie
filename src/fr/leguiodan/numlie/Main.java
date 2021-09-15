package fr.leguiodan.numlie;

import fr.leguiodan.numlie.managers.EventsManager;
import fr.leguiodan.numlie.managers.FilesManager;
import fr.leguiodan.numlie.managers.InventoryManager;
import fr.leguiodan.numlie.managers.PlayersManager;
import fr.leguiodan.numlie.utilities.Logger;
import fr.leguiodan.numlie.utilities.database.DatabaseManager;
import fr.leguiodan.numlie.utilities.enumerations.Files;
import fr.leguiodan.numlie.utilities.enumerations.LoggerType;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public static Main INSTANCE;

    public DatabaseManager databaseManager;
    public FilesManager filesManager;
    public PlayersManager playersManager;
    public InventoryManager inventoryManager;

    @Override
    public void onEnable() {
        INSTANCE = this;
        filesManager = new FilesManager(INSTANCE);
        filesManager.init();
        init();
        Logger.log(LoggerType.NORMAL, "Hello !");
    }

    @Override
    public void onDisable() {
        databaseManager.setOffline("server");
        this.databaseManager.close();
        Logger.log(LoggerType.NORMAL, "Bye !");
    }

    private void init() {
        YamlConfiguration configYaml = filesManager.loadYaml(Files.CONFIG);
        PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        databaseManager = new DatabaseManager(INSTANCE);
        pluginManager.registerEvents(new EventsManager(INSTANCE), this);
        if (configYaml.getBoolean("Main.Reload")) {
            databaseManager.updateStats();
            configYaml.set("Main.Reload", false);
            filesManager.saveYaml(Files.CONFIG, true);
        }
        databaseManager.setOnline("server");
        playersManager = new PlayersManager(INSTANCE);
        inventoryManager = new InventoryManager(INSTANCE);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> databaseManager.restartConnections(), 20L * 60 * 20, 20L * 60 * 20);
    }
}
