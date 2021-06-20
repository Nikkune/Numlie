package fr.leguiodan.numlie.utilities;

import fr.leguiodan.numlie.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class ScoreboardsHandler {
	public static void createScoreboard(Player player, Main main)
	{
		final String uuid = player.getUniqueId().toString();
		final YamlConfiguration playersYaml = main.filesManagers.getPlayersYaml();

		final int xp = playersYaml.getInt("Players." + uuid + ".xp");
		final int level = playersYaml.getInt("Players." + uuid + ".level");
		final int money = playersYaml.getInt("Players." + uuid + ".money");
		final int status = playersYaml.getInt("Players." + uuid + ".status");
		final int playtime = playersYaml.getInt("Players." + uuid + ".playtime");
		final int guild_id = playersYaml.getInt("Players." + uuid + ".guild_id");

		final ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
		final Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
		final Objective objective = scoreboard.registerNewObjective("status", "dummy");
		objective.setDisplayName(ChatColor.BOLD + "" + ChatColor.LIGHT_PURPLE + "Stats");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);

		final Score scoreLevel = objective.getScore(ChatColor.AQUA + "Level : " + level);
		final Score scoreXp = objective.getScore(ChatColor.BLUE + "Xp : " + xp + " / ");
		final Score scoreMoney = objective.getScore(ChatColor.GOLD + "You have " + money + " Golds");
		final Score scoreStatus = objective.getScore("You are a " + status);
		final Score scorePlaytime = objective.getScore(ChatColor.RED + "Playtime : " + playtime + " min");
		final Score scoreGuild = objective.getScore("Guild : " + guild_id);

		scoreLevel.setScore(6);
		scoreXp.setScore(5);
		scoreMoney.setScore(4);
		scoreStatus.setScore(3);
		scorePlaytime.setScore(2);
		scoreGuild.setScore(1);

		player.setScoreboard(scoreboard);
	}

	public static void updateScoreboard(Player player, Main main)
	{
		final Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		final Objective objective = scoreboard.getObjective("status");

	}
}
