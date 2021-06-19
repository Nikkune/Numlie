package fr.leguiodan.numlie.utilities;

public enum ColorsCodes {
	ANSI_RESET("\u001B[0m"),
	ANSI_BLACK("\u001B[30m"),
	ANSI_RED("\u001B[31m"),
	ANSI_GREEN("\u001B[32m"),
	ANSI_YELLOW("\u001B[33m"),
	ANSI_BLUE("\u001B[34m"),
	ANSI_PURPLE("\u001B[35m"),
	ANSI_CYAN("\u001B[36m"),
	ANSI_WHITE("\u001B[37m"),
	CHAT_RESET("§r"),
	CHAT_BLACK("§0"),
	CHAT_GRAY("§7"),
	CHAT_RED("§4"),
	CHAT_GREEN("§a"),
	CHAT_YELLOW("§e"),
	CHAT_BLUE("§9"),
	CHAT_PURPLE("§5"),
	CHAT_CYAN("§b"),
	CHAT_WHITE("§f");

	private final String colorCode;

	ColorsCodes(String colorCode)
	{
		this.colorCode = colorCode;
	}

	public String getColorCode()
	{
		return this.colorCode;
	}
}
