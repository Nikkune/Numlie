package fr.leguiodan.numlie;

import fr.leguiodan.numlie.managers.*;
import fr.leguiodan.numlie.utilities.Logger;
import fr.leguiodan.numlie.utilities.database.DatabaseManager;
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
    public PartyManager partyManager;

    @Override
    public void onEnable() {
        INSTANCE = this;
        Logger.init();
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
        Logger.log(LoggerType.NORMAL, "");
    }

    private void init() {
        YamlConfiguration configYaml = filesManager.getConfigYaml();
        PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        databaseManager = new DatabaseManager(INSTANCE);
        pluginManager.registerEvents(new EventsManager(INSTANCE), this);
        if (configYaml.getBoolean("Main.Reload")) {
            databaseManager.updateStats();
            configYaml.set("Main.Reload", false);
            filesManager.saveYaml(configYaml, true);
        }
        databaseManager.setOnline("server");
        playersManager = new PlayersManager(INSTANCE);
        inventoryManager = new InventoryManager(INSTANCE);
        partyManager = new PartyManager(INSTANCE);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> databaseManager.restartConnections(), 20L * 60 * 20, 20L * 60 * 20);
    }
}
