package fr.leguiodan.numlie.commands;

import fr.leguiodan.numlie.Main;
import fr.leguiodan.numlie.utilities.handlers.ChatHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class nxpCmds implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (label.equals("nxp")) {
            if (commandSender instanceof Player) {
                final Player player = ((Player) commandSender).getPlayer();
                if (player.isOp()) {
                    if (args.length == 4) {
                        final Player player1 = Bukkit.getPlayerExact(args[0]);
                        final int amount = Integer.parseInt(args[2]);
                        final String type = args[3];
                        switch (args[1]) {
                            case "set":
                                if (type.equals("level")) {
                                    if (amount <= 103) {
                                        Main.INSTANCE.filesManager.setPlayerLevel(player1, amount);
                                        player.sendMessage(ChatHandler.setSuccessMessage() + "Successful set " + ChatColor.LIGHT_PURPLE + amount + ChatColor.DARK_GREEN + " level for" + player1.getDisplayName());
                                    } else player.sendMessage(ChatHandler.setWarningMessage() + "Max : 103 level");
                                } else if (type.equals("xp")) {
                                    if (amount <= 61000) {
                                        Main.INSTANCE.filesManager.setPlayerXp(player1, amount);
                                        player.sendMessage(ChatHandler.setSuccessMessage() + "Successful set " + ChatColor.LIGHT_PURPLE + amount + ChatColor.DARK_GREEN + " xp for" + player1.getDisplayName());
                                    } else player.sendMessage(ChatHandler.setWarningMessage() + "Max : 61000 xp");
                                }
                                break;
                            case "add":
                                if (type.equals("level")) {
                                    if (Main.INSTANCE.filesManager.getPlayerLevel(player1) + amount <= 100) {
                                        Main.INSTANCE.filesManager.setPlayerLevel(player1, Main.INSTANCE.filesManager.getPlayerLevel(player1) + amount);
                                        player.sendMessage(ChatHandler.setSuccessMessage() + "Successful add " + ChatColor.LIGHT_PURPLE + amount + ChatColor.DARK_GREEN + " level for" + player1.getDisplayName());
                                    } else player.sendMessage(ChatHandler.setWarningMessage() + "Max : 100 level");
                                } else if (type.equals("xp")) {
                                    if (Main.INSTANCE.filesManager.getPlayerXp(player1) + amount <= 61000) {
                                        Main.INSTANCE.filesManager.setPlayerXp(player1, Main.INSTANCE.filesManager.getPlayerXp(player1) + amount);
                                        player.sendMessage(ChatHandler.setSuccessMessage() + "Successful add " + ChatColor.LIGHT_PURPLE + amount + ChatColor.DARK_GREEN + " xp for" + player1.getDisplayName());
                                    } else player.sendMessage(ChatHandler.setWarningMessage() + "Max : 61000 xp");
                                }
                                break;
                            case "remove":
                                if (type.equals("level")) {
                                    if (Main.INSTANCE.filesManager.getPlayerLevel(player1) - amount >= 1) {
                                        Main.INSTANCE.filesManager.setPlayerLevel(player1, Main.INSTANCE.filesManager.getPlayerLevel(player1) - amount);
                                        player.sendMessage(ChatHandler.setSuccessMessage() + "Successful remove " + ChatColor.LIGHT_PURPLE + amount + ChatColor.DARK_GREEN + " level for" + player1.getDisplayName());
                                    } else player.sendMessage(ChatHandler.setWarningMessage() + "Min : 1 level");
                                } else if (type.equals("xp")) {
                                    if (Main.INSTANCE.filesManager.getPlayerXp(player1) - amount >= 0) {
                                        Main.INSTANCE.filesManager.setPlayerXp(player1, Main.INSTANCE.filesManager.getPlayerXp(player1) - amount);
                                        player.sendMessage(ChatHandler.setSuccessMessage() + "Successful remove " + ChatColor.LIGHT_PURPLE + amount + ChatColor.DARK_GREEN + " xp for" + player1.getDisplayName());
                                    } else player.sendMessage(ChatHandler.setWarningMessage() + "Min : 0 xp");
                                }
                                break;
                            default:
                                player.sendMessage(ChatHandler.setErrorMessage() + "Commands is : /nxp {player} [set,add,remove] {amount} [level,xp]");
                        }
                    } else {
                        commandSender.sendMessage(ChatHandler.setErrorMessage() + "Commands is : /nxp {player} [set,add,remove] {amount} [level,xp]");
                    }
                }
            }
            return true;
        }
        return false;
    }
}
