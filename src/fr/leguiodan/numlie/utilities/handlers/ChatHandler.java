package fr.leguiodan.numlie.utilities.handlers;

import com.sun.istack.internal.NotNull;
import fr.leguiodan.numlie.Main;
import fr.leguiodan.numlie.utilities.enumerations.Chat_Type;
import fr.leguiodan.numlie.utilities.enumerations.Status;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class ChatHandler {

    public static String setJoinMessage(@NotNull Main main, @NotNull Player player) {
        final String uuid = player.getUniqueId().toString();
        final YamlConfiguration playersYaml = main.filesManager.getPlayersYaml();
        final int status_id = playersYaml.getInt("Players." + uuid + ".status");
        final Status status = Status.idToStatus(status_id);
        return ChatColor.DARK_GRAY + "[+] " + status.getChatColor() + player.getDisplayName() + ChatColor.RESET;
    }

    public static String setLeaveMessage(@NotNull Main main, @NotNull Player player) {
        final String uuid = player.getUniqueId().toString();
        final YamlConfiguration playersYaml = main.filesManager.getPlayersYaml();
        final int status_id = playersYaml.getInt("Players." + uuid + ".status");
        final Status status = Status.idToStatus(status_id);
        return ChatColor.DARK_GRAY + "[-] " + status.getChatColor() + player.getDisplayName() + ChatColor.RESET;
    }

    public static String setChatMessage(@NotNull Chat_Type chat_type, @NotNull Main main, String message, @NotNull Player sender) {
        final String uuid = sender.getUniqueId().toString();
        final YamlConfiguration playersYaml = main.filesManager.getPlayersYaml();
        final int status_id = playersYaml.getInt("Players." + uuid + ".status");
        final Status status = Status.idToStatus(status_id);
        return status.getChatColor() + sender.getDisplayName() + ChatColor.DARK_GRAY + " >> " + chat_type.getColor() + message.substring(2);
    }

    public static String setInfoMessage() {
        return Chat_Type.INFO.getPrefix() + Chat_Type.INFO.getColor();
    }

    public static String setWarningMessage() {
        return Chat_Type.WARNING.getPrefix() + Chat_Type.WARNING.getColor();
    }

    public static String setErrorMessage() {
        return Chat_Type.ERROR.getPrefix() + Chat_Type.ERROR.getColor();
    }

    public static String setSuccessMessage() {
        return Chat_Type.SUCCESS.getPrefix() + Chat_Type.SUCCESS.getColor();
    }

    public static String setGlobalMessage() {
        return Chat_Type.GLOBAL.getPrefix() + Chat_Type.GLOBAL.getColor();
    }

    public static String setPartyMessage() {
        return Chat_Type.PARTY.getPrefix() + Chat_Type.PARTY.getColor();
    }

    public static String setBroadcastMessage() {
        return Chat_Type.BROADCAST.getPrefix() + Chat_Type.BROADCAST.getColor() + Chat_Type.BROADCAST.getSecondary();
    }

    public static String getSelector(String message) {
        return message.substring(0, 2);
    }

    public static Chat_Type getMessageType(String selector) {
        if (Chat_Type.GLOBAL.getSelector().equalsIgnoreCase(selector)) {
            return Chat_Type.GLOBAL;
        } else if (Chat_Type.PARTY.getSelector().equalsIgnoreCase(selector)) {
            return Chat_Type.PARTY;
        } else if (Chat_Type.BROADCAST.getSelector().equalsIgnoreCase(selector)) {
            return Chat_Type.BROADCAST;
        } else {
            return Chat_Type.WRONG;
        }
    }

    public static String setMessageViaType(Chat_Type messageType) {
        switch (messageType) {
            case GLOBAL:
                return setGlobalMessage();
            case BROADCAST:
                return setBroadcastMessage();
            case INFO:
                return setInfoMessage();
            case ERROR:
                return setErrorMessage();
            case WARNING:
                return setWarningMessage();
            case SUCCESS:
                return setSuccessMessage();
            case PARTY:
                return setPartyMessage();
            default:
                return "error";
        }
    }
}
