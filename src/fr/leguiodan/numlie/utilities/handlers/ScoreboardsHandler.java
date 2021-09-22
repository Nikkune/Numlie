package fr.leguiodan.numlie.utilities.handlers;

import fr.leguiodan.numlie.Main;
import fr.leguiodan.numlie.utilities.enumerations.Messages;
import fr.leguiodan.numlie.utilities.enumerations.Status;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class ScoreboardsHandler {
    public static void manageScoreboard(Player player, Main main, ManageType type) {
        final String uuid = player.getUniqueId().toString();
        final String player_lang = main.filesManager.getPlayerLang(player);
        final YamlConfiguration playersYaml = main.filesManager.getPlayersYaml();

        final int xp = playersYaml.getInt("Players." + uuid + ".xp");
        final int level = playersYaml.getInt("Players." + uuid + ".level");
        final int status_id = playersYaml.getInt("Players." + uuid + ".status");
        final int playtime = playersYaml.getInt("Players." + uuid + ".playtime");
        final int xp_need = main.filesManager.getXpNeed(level);
        final Status status = Status.idToStatus(status_id);

        if (type == ManageType.Make) {
            final ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
            final Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
            final Objective objective = scoreboard.registerNewObjective("status", "dummy");
            objective.setDisplayName(ChatColor.BOLD + "" + ChatColor.LIGHT_PURPLE + "Stats");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            setScore(player, main, player_lang, xp, level, playtime, xp_need, status, scoreboard, objective);
        } else if (type == ManageType.Update) {
            final Scoreboard scoreboard = player.getScoreboard();
            final Objective objective = scoreboard.getObjective("status");
            Object[] score = scoreboard.getEntries().toArray();
            int i;
            for (i = 0; i < score.length; i++) {
                scoreboard.resetScores(score[i].toString());
            }
            setScore(player, main, player_lang, xp, level, playtime, xp_need, status, scoreboard, objective);
        }
    }

    private static void setScore(Player player, Main main, String player_lang, int xp, int level, int playtime, int xp_need, Status status, Scoreboard scoreboard, Objective objective) {
        final Score scoreLevel = objective.getScore(ChatColor.AQUA + main.filesManager.getMessage(Messages.UI_Level, player_lang) + " : " + level);
        final Score scoreXp = objective.getScore(ChatColor.BLUE + "Xp : " + xp + " / " + xp_need);
        final Score scoreStatus = objective.getScore(status.getChatColor() + "Status : " + status.getDisplayName(player_lang));
        final Score scorePlaytime = objective.getScore(ChatColor.RED + main.filesManager.getMessage(Messages.UI_Playtime, player_lang) + " : " + playtime + " min");

        scoreLevel.setScore(4);
        scoreXp.setScore(3);
        scoreStatus.setScore(2);
        scorePlaytime.setScore(1);

        player.setScoreboard(scoreboard);
    }

    public enum ManageType {
        Make,
        Update
    }
}
