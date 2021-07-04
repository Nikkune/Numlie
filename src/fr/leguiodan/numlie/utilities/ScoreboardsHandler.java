package fr.leguiodan.numlie.utilities;

import fr.leguiodan.numlie.Main;
import fr.leguiodan.numlie.utilities.enumerations.Messages;
import fr.leguiodan.numlie.utilities.enumerations.Status;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class ScoreboardsHandler {
	public static void createScoreboard(Player player, Main main)
	{
		final String uuid = player.getUniqueId().toString();
		final String player_lang = main.filesManager.getPlayerLang(player);
		final YamlConfiguration playersYaml = main.filesManager.getPlayersYaml();

		final int xp = playersYaml.getInt("Players." + uuid + ".xp");
		final int level = playersYaml.getInt("Players." + uuid + ".level");
		final int money = playersYaml.getInt("Players." + uuid + ".money");
		final int status_id = playersYaml.getInt("Players." + uuid + ".status");
		final int playtime = playersYaml.getInt("Players." + uuid + ".playtime");
		final int xp_need = main.filesManager.getXpNeed(level);
		final Status status = Status.idToStatus(status_id);

		final ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
		final Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
		final Objective objective = scoreboard.registerNewObjective("status", "dummy");
		objective.setDisplayName(ChatColor.BOLD + "" + ChatColor.LIGHT_PURPLE + "Stats");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);

		final Score scoreLevel = objective.getScore(ChatColor.AQUA + "Level : " + level);
		final Score scoreXp = objective.getScore(ChatColor.BLUE + "Xp : " + xp + " / " + xp_need);
		final Score scoreMoney = objective.getScore(ChatColor.GOLD + "You have " + money + " Golds");
		final Score scoreStatus = objective.getScore(status.getChatColor() + "You are a " + status.getDisplayName(player_lang));
		final Score scorePlaytime = objective.getScore(ChatColor.RED + "Playtime : " + playtime + " min");

		scoreLevel.setScore(6);
		scoreXp.setScore(5);
		scoreMoney.setScore(4);
		scoreStatus.setScore(3);
		scorePlaytime.setScore(2);

		player.setScoreboard(scoreboard);
	}

	public static void updateScoreboard(Player player, Main main)
	{
		final String uuid = player.getUniqueId().toString();
		final String player_lang = main.filesManager.getPlayerLang(player);
		final YamlConfiguration playersYaml = main.filesManager.getPlayersYaml();

		final int xp = playersYaml.getInt("Players." + uuid + ".xp");
		final int level = playersYaml.getInt("Players." + uuid + ".level");
		final int money = playersYaml.getInt("Players." + uuid + ".money");
		final int status_id = playersYaml.getInt("Players." + uuid + ".status");
		final int playtime = playersYaml.getInt("Players." + uuid + ".playtime");
		final int xp_need = main.filesManager.getXpNeed(level);
		final Status status = Status.idToStatus(status_id);

		final Scoreboard scoreboard = player.getScoreboard();
		final Objective objective = scoreboard.getObjective("status");
		Object[] score = scoreboard.getEntries().toArray();
		int i;
		for (i = 0; i < score.length; i++)
		{
			scoreboard.resetScores(score[i].toString());
		}

		final Score scoreLevel = objective.getScore(ChatColor.AQUA + main.filesManager.getMessage(Messages.UI_Level, player_lang) + " : " + level);
		final Score scoreXp = objective.getScore(ChatColor.BLUE + "Xp : " + xp + " / " + xp_need);
		final Score scoreMoney = objective.getScore(ChatColor.GOLD + main.filesManager.getMessage(Messages.UI_Money, player_lang) + " : " + money + " Golds");
		final Score scoreStatus = objective.getScore(status.getChatColor() + "Status : " + status.getDisplayName(player_lang));
		final Score scorePlaytime = objective.getScore(ChatColor.RED + main.filesManager.getMessage(Messages.UI_Playtime, player_lang) + " : " + playtime + " min");

		scoreLevel.setScore(6);
		scoreXp.setScore(5);
		scoreMoney.setScore(4);
		scoreStatus.setScore(3);
		scorePlaytime.setScore(2);
	}
}
