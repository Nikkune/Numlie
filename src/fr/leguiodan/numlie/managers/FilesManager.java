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

    public void messageUpdate() {
        final String key = "Messages.";
        final String lang_en = ".EN";
        final String lang_fr = ".FR";

        messageYaml.set(key + "databaseOk" + lang_en, "Successful connection to the database !");
        messageYaml.set(key + "databaseOk" + lang_fr, "Connexion réussie à la base de données !");

        messageYaml.set(key + "accountCreate" + lang_en, "Account created for the player : ");
        messageYaml.set(key + "accountCreate" + lang_fr, "Compte créé pour le joueur : ");

        messageYaml.set(key + "cashCreate" + lang_en, "Cash created for the player : ");
        messageYaml.set(key + "cashCreate" + lang_fr, "Cash créé pour le joueur : ");

        messageYaml.set(key + "accountUpdate" + lang_en, "Account updated for the player : ");
        messageYaml.set(key + "accountUpdate" + lang_fr, "Compte mis à jour pour le joueur : ");

        messageYaml.set(key + "statsYamlOk" + lang_en, "The Stats.yml file has saved the level : ");
        messageYaml.set(key + "statsYamlOk" + lang_fr, "Le fichier Stats.yml a enregistré le niveau : ");

        messageYaml.set(key + "congratulations" + lang_en, "Congratulations !");
        messageYaml.set(key + "congratulations" + lang_fr, "Félicitations !");

        messageYaml.set(key + "shame" + lang_en, "Oh no !");
        messageYaml.set(key + "shame" + lang_fr, "Oh non !");

        messageYaml.set(key + "levelPlus" + lang_en, "You have gained a level! You are now at the level : ");
        messageYaml.set(key + "levelPlus" + lang_fr, "Vous avez gagné un niveau ! Vous êtes maintenant au niveau : ");

        messageYaml.set(key + "statusPlus" + lang_en, "You have a new status! You are now a : ");
        messageYaml.set(key + "statusPlus" + lang_fr, "Vous avez un nouveau statut ! Vous êtes maintenant un : ");

        messageYaml.set(key + "xpPlus" + lang_en, "You have gained : ");
        messageYaml.set(key + "xpPlus" + lang_fr, "Vous avez gagné : ");

        messageYaml.set(key + "xpMinus" + lang_en, "You lost 25% of your xp !");
        messageYaml.set(key + "xpMinus" + lang_fr, "Vous avez perdu 25% de votre xp !");

        messageYaml.set(key + "backupErr" + lang_en, "Backup error on the file : ");
        messageYaml.set(key + "backupErr" + lang_fr, "Erreur de sauvegarde sur le fichier : ");

        messageYaml.set(key + "backupOk" + lang_en, "Successful backup for file : ");
        messageYaml.set(key + "backupOk" + lang_fr, "Sauvegarde réussie pour le fichier : ");

        messageYaml.set(key + "deleteErr" + lang_en, "Delete error for the file : ");
        messageYaml.set(key + "deleteErr" + lang_fr, "Erreur de suppression pour le fichier : ");

        messageYaml.set(key + "deleteOk" + lang_en, "Successful deletion for the file : ");
        messageYaml.set(key + "deleteOk" + lang_fr, "Suppression réussie du fichier : ");

        messageYaml.set(key + "UILevel" + lang_en, "Level");
        messageYaml.set(key + "UILevel" + lang_fr, "Niveau");

        messageYaml.set(key + "UIPlaytime" + lang_en, "Playtime");
        messageYaml.set(key + "UIPlaytime" + lang_fr, "Temps De Jeu");

        messageYaml.set(key + "UIChangeLang" + lang_en, "Change language to : ");
        messageYaml.set(key + "UIChangeLang" + lang_fr, "Changer la langue pour : ");

        messageYaml.set(key + "UIChangeTitle" + lang_en, "Change announce in : ");
        messageYaml.set(key + "UIChangeTitle" + lang_fr, "Modifier l'annonce en : ");

        messageYaml.set(key + "UIGetLinkKey" + lang_en, "Get the link_key !");
        messageYaml.set(key + "UIGetLinkKey" + lang_fr, "Récupérer la link_key !");

        messageYaml.set(key + "UIChat" + lang_en, "Chat message");
        messageYaml.set(key + "UIChat" + lang_fr, "Message du chat");

        messageYaml.set(key + "UITitle" + lang_en, "Title");
        messageYaml.set(key + "UITitle" + lang_fr, "Titre");

        messageYaml.set(key + "ChatSelector" + lang_en, "You must put a selector (!g,!f,!p) to send a message in the chat ex:!gHello everyone !");
        messageYaml.set(key + "ChatSelector" + lang_fr, "Vous devez mettre un sélecteur (!g,!f,!p) pour envoyer un message dans le chat ex :!gBonjour à tous !");

        messageYaml.set(key + "PartyNo" + lang_en, "You must be at a party!");
        messageYaml.set(key + "PartyNo" + lang_fr, "Vous devez être dans une party !");

        saveYaml(messageYaml, false);
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
