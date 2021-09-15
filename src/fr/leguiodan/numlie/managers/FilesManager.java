package fr.leguiodan.numlie.managers;

import fr.leguiodan.numlie.Main;
import fr.leguiodan.numlie.utilities.Logger;
import fr.leguiodan.numlie.utilities.enumerations.Files;
import fr.leguiodan.numlie.utilities.enumerations.LoggerType;
import fr.leguiodan.numlie.utilities.enumerations.Messages;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
public class FilesManager {

    private final Main main;
    private final ArrayList<Files> filesList = new ArrayList<>();
    private final Map<Files, YamlConfiguration> yamlList = new HashMap<>();

    public FilesManager(Main main) {
        this.main = main;
    }

    public void init() {
        filesList.add(Files.PLAYERS);
        filesList.add(Files.STATS);
        filesList.add(Files.CONFIG);
        filesList.add(Files.MESSAGES);

        for (Files file : filesList) {
            yamlList.put(file, loadYaml(file));
        }
        if (!createFile(Files.CONFIG)) {
            YamlConfiguration configYaml = loadYaml(Files.CONFIG);
            configYaml.set("Main.Reload", false);
            configYaml.set("Main.Language", "EN");
            saveYaml(Files.CONFIG, true);
        }
        messageUpdate();
        Logger.log(LoggerType.SUCCESS, "Files OK !");
    }

    public void setLink_Key(Player player, String link_key) {
        final String uuid = player.getUniqueId().toString();
        loadYaml(Files.PLAYERS).set("Players." + uuid + ".link_key", link_key);
        saveYaml(Files.PLAYERS, false);
    }

    public String getLink_Key(Player player) {
        final String uuid = player.getUniqueId().toString();
        return loadYaml(Files.PLAYERS).getString("Players." + uuid + ".link_key");
    }

    public void setPlayerLang(Player player, String lang) {
        final String uuid = player.getUniqueId().toString();
        loadYaml(Files.PLAYERS).set("Players." + uuid + ".lang", lang);
        saveYaml(Files.PLAYERS, false);
    }

    public String getPlayerLang(Player player) {
        final String uuid = player.getUniqueId().toString();
        return loadYaml(Files.PLAYERS).getString("Players." + uuid + ".lang");
    }

    public void setPlayerTitle(Player player, int title_type) {
        final String uuid = player.getUniqueId().toString();
        loadYaml(Files.PLAYERS).set("Players." + uuid + ".title", title_type);
        saveYaml(Files.PLAYERS, true);
    }

    public int getPlayerTitle(Player player) {
        final String uuid = player.getUniqueId().toString();
        return loadYaml(Files.PLAYERS).getInt("Players." + uuid + ".title");
    }

    public int[] getPlayersStats(Player player) {
        final String uuid = player.getUniqueId().toString();
        int[] playerStats = new int[5];
        YamlConfiguration playersYaml = loadYaml(Files.PLAYERS);
        playerStats[0] = playersYaml.getInt("Players." + uuid + ".xp");
        playerStats[1] = playersYaml.getInt("Players." + uuid + ".level");
        playerStats[2] = playersYaml.getInt("Players." + uuid + ".money");
        playerStats[3] = playersYaml.getInt("Players." + uuid + ".status");
        playerStats[4] = playersYaml.getInt("Players." + uuid + ".playtime");
        return playerStats;
    }

    public void setPlayersStats(Player player, int[] playerStats) {
        final String uuid = player.getUniqueId().toString();
        YamlConfiguration playersYaml = loadYaml(Files.PLAYERS);
        playersYaml.set("Players." + uuid + ".xp", playerStats[0]);
        playersYaml.set("Players." + uuid + ".level", playerStats[1]);
        playersYaml.set("Players." + uuid + ".money", playerStats[2]);
        playersYaml.set("Players." + uuid + ".status", playerStats[3]);
        playersYaml.set("Players." + uuid + ".playtime", playerStats[4]);
        saveYaml(Files.PLAYERS, false);
    }

    public void statsUpdate(int level, int max_pv, int xp_need, int xp_win) {
        final String key = "Stats.Level " + level;
        YamlConfiguration statsYaml = loadYaml(Files.STATS);
        statsYaml.set(key + ".max_pv", max_pv);
        statsYaml.set(key + ".xp_need", xp_need);
        statsYaml.set(key + ".xp_win", xp_win);
        saveYaml(Files.STATS, false);
        Logger.log(LoggerType.SUCCESS, getMessage(Messages.Level_Saved, getLanguage()) + level + " !");
    }

    public int getXpNeed(int level) {
        final String key = "Stats.Level " + level;
        return loadYaml(Files.STATS).getInt(key + ".xp_need");
    }

    public int getXpWin(int level) {
        final String key = "Stats.Level " + level;
        return loadYaml(Files.STATS).getInt(key + ".xp_win");
    }

    public int getMaxPv(int level) {
        final String key = "Stats.Level " + level;
        return loadYaml(Files.STATS).getInt(key + ".max_pv");
    }

    public void messageUpdate() {
        YamlConfiguration messageYaml = loadYaml(Files.MESSAGES);
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

        messageYaml.set(key + "UIMoney" + lang_en, "Money");
        messageYaml.set(key + "UIMoney" + lang_fr, "Argent");

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

        saveYaml(Files.MESSAGES, true);
    }

    public String getMessage(Messages message, String lang) {
        return loadYaml(Files.MESSAGES).getString("Messages." + message.getKey() + "." + lang);
    }

    public String getLanguage() {
        return loadYaml(Files.CONFIG).getString("Main.Language");
    }

    public boolean createFile(Files file) {
        String fileName = file.getFileName();
        File newFile = new File(this.main.getDataFolder(), "/" + fileName + ".yml");
        if (!newFile.exists()) {
            if (newFile.mkdir()) {
                Logger.log(LoggerType.SUCCESS, fileName + ".yml" + ", Successful created !");
                return true;
            } else {
                Logger.log(LoggerType.ERROR, fileName + ".yml" + ", Can't be created !");
                return false;
            }
        } else {
            return true;
        }
    }

    public void deleteFile(Files file) {
        String fileName = file.getFileName();
        File newFile = new File(this.main.getDataFolder(), "/" + fileName + ".yml");
        if (newFile.delete()) {
            Logger.log(LoggerType.SUCCESS, getMessage(Messages.Delete_Success, getLanguage()) + fileName);
        } else {
            Logger.log(LoggerType.ERROR, getMessage(Messages.Delete_Error, getLanguage()) + fileName);
        }
    }

    public YamlConfiguration loadYaml(Files file) {
        String fileName = file.getFileName();
        File ymlFile = new File(this.main.getDataFolder(), "/" + fileName + ".yml");
        if (ymlFile.exists()) {
            return YamlConfiguration.loadConfiguration(ymlFile);
        }
        return null;
    }

    public void saveYaml(Files file, boolean log) {
        String fileName = file.getFileName();
        File ymlFile = new File(this.main.getDataFolder(), "/" + fileName + ".yml");
        YamlConfiguration ymlConf = YamlConfiguration.loadConfiguration(ymlFile);
        try {
            ymlConf.save(ymlFile);
            if (log) {
                Logger.log(LoggerType.SUCCESS, getMessage(Messages.Backup_Success, getLanguage()) + fileName + ".yml");
            }
        } catch (IOException e) {
            Logger.log(LoggerType.ERROR, getMessage(Messages.Backup_Error, getLanguage()) + fileName + ".yml");
            e.printStackTrace();
        }
    }
}
