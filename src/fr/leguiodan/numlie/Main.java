package fr.leguiodan.numlie;

import fr.leguiodan.numlie.utilities.ColorsCodes;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	@Override
	public void onDisable()
	{
		logNormal("Bye !");
	}

	@Override
	public void onEnable()
	{
		logNormal("Hello !");
	}

	public void logNormal(String message)
	{
		System.out.println(ColorsCodes.ANSI_PURPLE.getColorCode() + "[Numlie] " + ColorsCodes.ANSI_WHITE.getColorCode() + message + ColorsCodes.ANSI_RESET.getColorCode());
	}

	public void logWarning(String message)
	{
		System.out.println(ColorsCodes.ANSI_PURPLE.getColorCode() + "[Numlie] " + ColorsCodes.ANSI_YELLOW.getColorCode() + message + ColorsCodes.ANSI_RESET.getColorCode());
	}

	public void logError(String message)
	{
		System.out.println(ColorsCodes.ANSI_PURPLE.getColorCode() + "[Numlie] " + ColorsCodes.ANSI_RED.getColorCode() + message + ColorsCodes.ANSI_RESET.getColorCode());
	}

	public void logSuccess(String message)
	{
		System.out.println(ColorsCodes.ANSI_PURPLE.getColorCode() + "[Numlie] " + ColorsCodes.ANSI_GREEN.getColorCode() + message + ColorsCodes.ANSI_RESET.getColorCode());
	}
}
