package fr.leguiodan.numlie.managers;

import fr.leguiodan.numlie.Main;
import fr.leguiodan.numlie.utilities.enumerations.Messages;
import fr.leguiodan.numlie.utilities.handlers.ChatHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class PartyManager {
    private final Main main;

    public PartyManager(Main main) {
        this.main = main;
    }

    public void createParty(Player host) {
        main.databaseManager.createParty(host);
    }

    public void partyInvite(Player host, String member) {
        String playerLang = main.filesManager.getPlayerLang(host);
        Player invited = Bukkit.getPlayer(member);
        if (main.databaseManager.isInParty(host)) {
            if (main.databaseManager.isTheHost(host)) {
                if (invited != null) {
                    main.databaseManager.inviteParty(host, invited);
                    invited.sendMessage(ChatHandler.setErrorMessage() + host.getDisplayName() + " vous a invité dans sa party !");
                } else {
                    host.sendMessage(ChatHandler.setErrorMessage() + "Le Joueur n'est pas en ligne !");
                }
            }
        } else {
            host.sendMessage(ChatHandler.setWarningMessage() + main.filesManager.getMessage(Messages.Party_No, playerLang));
        }
    }

    public void partyAccept(Player invited) {
        main.databaseManager.acceptParty(invited);
    }

    public void partyLeave(Player invited) {
        main.databaseManager.leaveParty(invited);
    }

    public String showPartyList(Player sender) {
        if (main.databaseManager.isInParty(sender)) {
            StringBuilder membersString = new StringBuilder();
            membersString.append(ChatHandler.setPartyMessage());
            if (main.databaseManager.isTheHost(sender)) {
                List<Player> playerList = main.databaseManager.getAllPlayerOfParty(sender);
                for (Player player : playerList) {
                    membersString.append(player.getDisplayName()).append(" ");
                }
            } else {
                List<Player> playerList = main.databaseManager.getAllPlayerOfParty(Bukkit.getPlayerExact(main.databaseManager.getHost(sender)));
                for (Player player : playerList) {
                    membersString.append(player.getDisplayName()).append(" ");
                }
            }
            return membersString.toString();
        } else {
            sender.sendMessage(ChatHandler.setWarningMessage() + main.filesManager.getMessage(Messages.Party_No, main.filesManager.getPlayerLang(sender)));
            return null;
        }
    }
}
