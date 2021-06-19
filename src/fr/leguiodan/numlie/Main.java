package fr.leguiodan.numlie;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Override
    public void onDisable() {
        System.out.println("Bye");
    }

    @Override
    public void onEnable() {
        System.out.println("Hello!");
    }
}
