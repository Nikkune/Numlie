package fr.leguiodan.numlie.managers;

import fr.leguiodan.numlie.Main;
import fr.leguiodan.numlie.utilities.Logger;
import fr.leguiodan.numlie.utilities.enumerations.LoggerType;
import fr.leguiodan.numlie.utilities.enumerations.Messages;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
public class FilesManager {

    private final Main main;

    private final File playersFile;
    private final File statsFile;
    private final File configFile;
    private final File messageFile;

    private final YamlConfiguration playersYaml;
    private final YamlConfiguration statsYaml;
    private final YamlConfiguration configYaml;
    private final YamlConfiguration messageYaml;

    public FilesManager(Main main) {
        this.main = main;
        this.configFile = new File(this.main.getDataFolder(), "/config.yml");
        this.playersFile = new File(this.main.getDataFolder(), "/players.yml");
        this.statsFile = new File(this.main.getDataFolder(), "/stats.yml");
        this.messageFile = new File(this.main.getDataFolder(), "/messages.yml");

        this.configYaml = YamlConfiguration.loadConfiguration(configFile);
        this.playersYaml = YamlConfiguration.loadConfiguration(playersFile);
        this.statsYaml = YamlConfiguration.loadConfiguration(statsFile);
        this.messageYaml = YamlConfiguration.loadConfiguration(messageFile);
    }

    public void init() {
        messageUpdate();
        if (!configFile.exists()) {
            configYaml.set("Main.Reload", true);
            configYaml.set("Main.Language", "EN");
            saveYaml(configYaml, true);
            saveYaml(playersYaml, true);
            saveYaml(messageYaml, true);
        }
        Logger.log(LoggerType.SUCCESS, "Files OK !");
    }

    public void setLink_Key(Player player, String link_key) {
        final String uuid = player.getUniqueId().toString();
        playersYaml.set("Players." + uuid + ".link_key", link_key);
        saveYaml(playersYaml, false);
    }

    public String getLink_Key(Player player) {
        final String uuid = player.getUniqueId().toString();
        return playersYaml.getString("Players." + uuid + ".link_key");
    }

    public void setPlayerLang(Player player, String lang) {
        final String uuid = player.getUniqueId().toString();
        playersYaml.set("Players." + uuid + ".lang", lang);
        saveYaml(playersYaml, false);
    }

    public String getPlayerLang(Player player) {
        final String uuid = player.getUniqueId().toString();
        return playersYaml.getString("Players." + uuid + ".lang");
    }

    public void setPlayerTitle(Player player, int title_type) {
        final String uuid = player.getUniqueId().toString();
        playersYaml.set("Players." + uuid + ".title", title_type);
        saveYaml(playersYaml, true);
    }

    public int getPlayerTitle(Player player) {
        final String uuid = player.getUniqueId().toString();
        return playersYaml.getInt("Players." + uuid + ".title");
    }

    public int[] getPlayersStats(Player player) {
        final String uuid = player.getUniqueId().toString();
        int[] playerStats = new int[4];
        playerStats[0] = playersYaml.getInt("Players." + uuid + ".xp");
        playerStats[1] = playersYaml.getInt("Players." + uuid + ".level");
        playerStats[2] = playersYaml.getInt("Players." + uuid + ".status");
        playerStats[3] = playersYaml.getInt("Players." + uuid + ".playtime");
        return playerStats;
    }

    public void setPlayersStats(Player player, int[] playerStats) {
        final String uuid = player.getUniqueId().toString();
        playersYaml.set("Players." + uuid + ".xp", playerStats[0]);
        playersYaml.set("Players." + uuid + ".level", playerStats[1]);
        playersYaml.set("Players." + uuid + ".status", playerStats[2]);
        playersYaml.set("Players." + uuid + ".playtime", playerStats[3]);
        saveYaml(playersYaml, false);
    }

    public int getPlayerXp(Player player) {
        final String uuid = player.getUniqueId().toString();
        return playersYaml.getInt("Players." + uuid + ".xp");
    }

    public void setPlayerXp(Player player, int xp) {
        final String uuid = player.getUniqueId().toString();
        playersYaml.set("Players." + uuid + ".xp", xp);
    }

    public int getPlayerLevel(Player player) {
        final String uuid = player.getUniqueId().toString();
        return playersYaml.getInt("Players." + uuid + ".level");
    }

    public void setPlayerLevel(Player player, int level) {
        final String uuid = player.getUniqueId().toString();
        playersYaml.set("Players." + uuid + ".level", level);
    }

    public void statsUpdate(int level, int max_pv, int xp_need, int xp_win) {
        final String key = "Stats.Level " + level;
        statsYaml.set(key + ".max_pv", max_pv);
        statsYaml.set(key + ".xp_need", xp_need);
        statsYaml.set(key + ".xp_win", xp_win);
        saveYaml(statsYaml, false);
        Logger.log(LoggerType.SUCCESS, getMessage(Messages.Level_Saved, getLanguage()) + level + " !");
    }

    public int getXpNeed(int level) {
        final String key = "Stats.Level " + level;
        return statsYaml.getInt(key + ".xp_need");
    }

    public int getXpWin(int level) {
        final String key = "Stats.Level " + level;
        return statsYaml.getInt(key + ".xp_win");
    }

    public int getMaxPv(int level) {
        final String key = "Stats.Level " + level;
        return statsYaml.getInt(key + ".max_pv");
    }

    private void setMessage(Messages messages, String trad_en, String trad_fr) {
        messageYaml.set("Messages." + messages.getKey() + ".EN", trad_en);
        messageYaml.set("Messages." + messages.getKey() + ".FR", trad_fr);
        saveYaml(messageYaml, false);
    }

    public void messageUpdate() {
        setMessage(Messages.Database_Connected, "Successful connection to the database !", "Connexion réussie à la base de données !");
        setMessage(Messages.Database_Disconnected, "Disconnected from the database !", "Deconnecté de la base de données");
        setMessage(Messages.Account_Created, "Account created for the player : ", "Compte créé pour le joueur : ");
        setMessage(Messages.Cash_Created, "Cash created for the player : ", "Cash créé pour le joueur : ");
        setMessage(Messages.Account_Updated, "Account updated for the player : ", "Compte mis à jour pour le joueur : ");
        setMessage(Messages.Level_Saved, "The Stats.yml file has saved the level : ", "Le fichier Stats.yml a enregistré le niveau : ");
        setMessage(Messages.Congratulations, "Congratulations !", "Félicitations !");
        setMessage(Messages.Shame, "Oh no !", "Oh non !");
        setMessage(Messages.Level_Up, "You have gained a level! You are now at the level : ", "Vous avez gagné un niveau ! Vous êtes maintenant au niveau : ");
        setMessage(Messages.Status_Up, "You have a new status! You are now a : ", "Vous avez un nouveau statut ! Vous êtes maintenant un : ");
        setMessage(Messages.Xp_Up, "You have gained : ", "Vous avez gagné : ");
        setMessage(Messages.Xp_Down, "You lost 25% of your xp !", "Vous avez perdu 25% de votre xp !");
        setMessage(Messages.Backup_Error, "Backup error on the file : ", "Erreur de sauvegarde sur le fichier : ");
        setMessage(Messages.Backup_Success, "Successful backup for file : ", "Sauvegarde réussie pour le fichier : ");
        setMessage(Messages.UI_Level, "Level", "Niveau");
        setMessage(Messages.UI_Playtime, "Playtime", "Temps De Jeu");
        setMessage(Messages.UI_Change_Lang, "Change language to : ", "Changer la langue pour : ");
        setMessage(Messages.UI_Change_Title, "Change announce in : ", "Modifier l'annonce en : ");
        setMessage(Messages.UI_Get_Link_Key, "Get the link_key !", "Récupérer la link_key !");
        setMessage(Messages.UI_Chat, "Message in chat", "Message dans le chat");
        setMessage(Messages.UI_Title, "Title", "Titre");
        setMessage(Messages.Chat_Selector, "You must put a selector (!g,!p) to send a message in the chat ex:!gHello everyone !", "Vous devez mettre un sélecteur (!g,!p) pour envoyer un message dans le chat ex :!gBonjour à tous !");
        setMessage(Messages.Party_No, "You must be at a party !", "Vous devez être dans une party !");
        setMessage(Messages.Party_Created, "The party has been created !", "La party a bien été créé !");
        setMessage(Messages.Party_Dissolved, "The leader of the group dissolved the party!", "Le chef du groupe à dissolu la party !");
        setMessage(Messages.Party_Joined, "You joined the party !", "Vous avez rejoint la party !");
        setMessage(Messages.Party_Joined_1, " a rejoint la party !", " joined the party !");
        setMessage(Messages.Party_Invite, "You have invited :", "Vous avez bien invité : ");
        setMessage(Messages.Party_Invited, " invited you to his party!", " vous a invité dans sa party !");
        setMessage(Messages.Party_Leave, "You left the party!", "Vous avez quitté la party !");
        setMessage(Messages.Party_Left, " left the party!", " a quitté  la party !");
        setMessage(Messages.Player_Offline, "The player is offline !", "Le joueur est hors ligne !");
        setMessage(Messages.Command_Error, "Command is :", "La commande est :");
        setMessage(Messages.Successful_Add, "Successfully adding ", "Ajout avec succès de ");
        setMessage(Messages.Successful_Add_1, " for ", " pour ");
        setMessage(Messages.Successful_Remove, "Successfully removed ", "Suppression avec succès de ");
        setMessage(Messages.Successful_Remove_1, " for ", " pour ");
        setMessage(Messages.Successful_Set, "Successfully set ", "Fixation avec succès de ");
        setMessage(Messages.Successful_Set_1, " for ", " pour ");

        Logger.log(LoggerType.SUCCESS, "Messages successful updated");
    }

    public String getMessage(Messages message, String lang) {
        return messageYaml.getString("Messages." + message.getKey() + "." + lang);
    }

    public String getLanguage() {
        return configYaml.getString("Main.Language");
    }

    public void saveYaml(YamlConfiguration ymlConfig, boolean log) {
        if (playersYaml.equals(ymlConfig)) {
            try {
                playersYaml.save(playersFile);
                if (log) {
                    String message = getMessage(Messages.Backup_Success, getLanguage()) + "players_yml";
                    Logger.log(LoggerType.SUCCESS, message);
                }
            } catch (IOException e) {
                String message = getMessage(Messages.Backup_Error, getLanguage()) + "players_yml";
                Logger.log(LoggerType.ERROR, message);
                e.printStackTrace();
            }
        } else if (statsYaml.equals(ymlConfig)) {
            try {
                statsYaml.save(statsFile);
                if (log) {
                    String message = getMessage(Messages.Backup_Success, getLanguage()) + "stats_yml";
                    Logger.log(LoggerType.SUCCESS, message);
                }
            } catch (IOException e) {
                String message = getMessage(Messages.Backup_Error, getLanguage()) + "stats_yml";
                Logger.log(LoggerType.ERROR, message);
                e.printStackTrace();
            }
        } else if (configYaml.equals(ymlConfig)) {
            try {
                configYaml.save(configFile);
                if (log) {
                    String message = getMessage(Messages.Backup_Success, getLanguage()) + "config_yml";
                    Logger.log(LoggerType.SUCCESS, message);
                }
            } catch (IOException e) {
                String message = getMessage(Messages.Backup_Error, getLanguage()) + "config_yml";
                Logger.log(LoggerType.ERROR, message);
                e.printStackTrace();
            }
        } else if (messageYaml.equals(ymlConfig)) {
            try {
                messageYaml.save(messageFile);
                if (log) {
                    String message = getMessage(Messages.Backup_Success, getLanguage()) + "messages_yml";
                    Logger.log(LoggerType.SUCCESS, message);
                }
            } catch (IOException e) {
                String message = getMessage(Messages.Backup_Error, getLanguage()) + "messages_yml";
                Logger.log(LoggerType.ERROR, message);
                e.printStackTrace();
            }
        }
    }

    public YamlConfiguration getPlayersYaml() {
        return playersYaml;
    }

    public YamlConfiguration getConfigYaml() {
        return configYaml;
    }
}
