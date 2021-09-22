package fr.leguiodan.numlie.commands;

import fr.leguiodan.numlie.Main;
import fr.leguiodan.numlie.utilities.handlers.ChatHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class nlvlCmds implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (label.equals("nlvl")) {
            if (args.length == 2) {
                Player player = Bukkit.getPlayerExact(args[0]);
                int level = Integer.parseInt(args[1]);
                Main.INSTANCE.playersManager.entityKilled(player, level);
                commandSender.sendMessage(ChatHandler.setSuccessMessage() + "Successful nlvl for " + player.getDisplayName() + " level : " + level);
            } else {
                commandSender.sendMessage("Commands is : /nlvl {player} {level}");
            }
            return true;
        }
        return false;
    }
}
