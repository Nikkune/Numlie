package fr.leguiodan.numlie.utilities;

public class Logger {
	public static void logNormal(String message)
	{
		System.out.println(ColorsCodes.ANSI_PURPLE.getColorCode() + "[Numlie] " + ColorsCodes.ANSI_WHITE.getColorCode() + message + ColorsCodes.ANSI_RESET.getColorCode());
	}

	public static void logWarning(String message)
	{
		System.out.println(ColorsCodes.ANSI_PURPLE.getColorCode() + "[Numlie] " + ColorsCodes.ANSI_YELLOW.getColorCode() + message + ColorsCodes.ANSI_RESET.getColorCode());
	}

	public static void logError(String message)
	{
		System.out.println(ColorsCodes.ANSI_PURPLE.getColorCode() + "[Numlie] " + ColorsCodes.ANSI_RED.getColorCode() + message + ColorsCodes.ANSI_RESET.getColorCode());
	}

	public static void logSuccess(String message)
	{
		System.out.println(ColorsCodes.ANSI_PURPLE.getColorCode() + "[Numlie] " + ColorsCodes.ANSI_GREEN.getColorCode() + message + ColorsCodes.ANSI_RESET.getColorCode());
	}
}
