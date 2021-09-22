package fr.leguiodan.numlie.commands;

import fr.leguiodan.numlie.Main;
import fr.leguiodan.numlie.utilities.handlers.ChatHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class partyCmds implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (label.equals("party")) {
            if (args.length >= 1) {
                if (commandSender instanceof Player) {
                    final Player player = ((Player) commandSender).getPlayer();
                    switch (args[0]) {
                        case "create":
                            Main.INSTANCE.partyManager.createParty(player);
                            break;
                        case "leave":
                        case "refuse":
                            Main.INSTANCE.partyManager.partyLeave(player);
                            break;
                        case "accept":
                            Main.INSTANCE.partyManager.partyAccept(player);
                            break;
                        case "list":
                            player.sendMessage(Main.INSTANCE.partyManager.showPartyList(player));
                            break;
                        case "invite":
                            if (args.length == 2) {
                                Main.INSTANCE.partyManager.partyInvite(player, args[1]);
                            } else {
                                player.sendMessage(ChatHandler.setErrorMessage() + "You should invite a player ex: /party invite [player] ");
                            }
                            break;
                        default:
                            player.sendMessage(ChatHandler.setErrorMessage() + "Commands is : /party [create,invite,leave,accept,refuse,list]");
                    }
                }
            } else {
                commandSender.sendMessage(ChatHandler.setErrorMessage() + "Commands is : /party [create,invite,leave,accept,refuse,list]");
            }
            return true;
        }
        return false;
    }
}
