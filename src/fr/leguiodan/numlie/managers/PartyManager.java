package fr.leguiodan.numlie.managers;

import fr.leguiodan.numlie.Main;
import fr.leguiodan.numlie.utilities.enumerations.Chat_Type;
import fr.leguiodan.numlie.utilities.handlers.ChatHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PartyManager {
    private final Main main;

    public PartyManager(Main main) {
        this.main = main;
    }

    public void createParty(Player host) {
        main.databaseManager.createParty(host);
    }

    public void partyInvite(Player host, String member) {
        Player invited = Bukkit.getPlayer(member);
        if (invited != null) {
            main.databaseManager.inviteParty(host, invited);
            invited.sendMessage(ChatHandler.setErrorMessage() + ChatHandler.getMessageType(Chat_Type.INFO.getSelector()) + host.getDisplayName() + " vous a invité dans sa party !");
        } else {
            host.sendMessage(ChatHandler.setErrorMessage() + ChatHandler.getMessageType(Chat_Type.ERROR.getSelector()) + "Le Joueur n'est pas en ligne !");
        }
    }

    public void partyAccept(Player invited) {
        main.databaseManager.acceptParty(invited);
    }

    public void partyLeave(Player invited) {
        main.databaseManager.leaveParty(invited);
    }
}
