package fr.leguiodan.numlie.managers;

import fr.leguiodan.numlie.Main;
import fr.leguiodan.numlie.utilities.enumerations.Chat_Type;
import fr.leguiodan.numlie.utilities.enumerations.Messages;
import fr.leguiodan.numlie.utilities.enumerations.Status;
import fr.leguiodan.numlie.utilities.handlers.ChatHandler;
import fr.leguiodan.numlie.utilities.handlers.ScoreboardsHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class EventsManager implements Listener {

    private final Main main;
    private final Map<Player, Integer> taskMin_map;
    private final Map<Player, Integer> task_map;

    public EventsManager(Main main) {
        this.main = main;
        this.taskMin_map = new HashMap<>();
        this.task_map = new HashMap<>();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        main.databaseManager.createAccount(player);
        main.databaseManager.createPlayerCash(player);
        main.databaseManager.setOnline(player.getUniqueId().toString());
        ScoreboardsHandler.createScoreboard(player, main);
        final int task = Bukkit.getScheduler().scheduleSyncRepeatingTask(main, () -> main.playersManager.updatePlayerMin(player), 20L * 60, 20L * 60);
        final int task2 = Bukkit.getScheduler().scheduleSyncRepeatingTask(main, () -> main.playersManager.updatePlayer(player), 0L, 20L);
        taskMin_map.put(player, task);
        task_map.put(player, task2);
        event.setJoinMessage(ChatHandler.setJoinMessage(main, player));

        ItemStack stack = new ItemStack(Material.COMPASS, 1);
        ItemMeta stackItemMeta = stack.getItemMeta();
        stackItemMeta.setDisplayName("Settings");
        stack.setItemMeta(stackItemMeta);
        player.getInventory().setItem(17, stack);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        Bukkit.getScheduler().cancelTask(taskMin_map.get(player));
        Bukkit.getScheduler().cancelTask(task_map.get(player));
        main.databaseManager.updatesPlayers(player);
        main.databaseManager.setOffline(player.getUniqueId().toString());
        if (main.databaseManager.isInParty(player)) {
            main.partyManager.partyLeave(player);
        }
        event.setQuitMessage(ChatHandler.setLeaveMessage(main, player));
    }

    @EventHandler
    public void onMobKill(EntityDeathEvent event) {
        Entity death = event.getEntity();
        event.setDroppedExp(0);
        Player killer = event.getEntity().getKiller();
        EntityType[] HOSTILE_TYPE = new EntityType[]{EntityType.BLAZE, EntityType.CREEPER, EntityType.ELDER_GUARDIAN, EntityType.ENDERMITE, EntityType.EVOKER, EntityType.GHAST, EntityType.GUARDIAN, EntityType.HUSK, EntityType.MAGMA_CUBE, EntityType.PIG_ZOMBIE, EntityType.SHULKER, EntityType.SILVERFISH, EntityType.SKELETON, EntityType.SLIME, EntityType.SPIDER, EntityType.STRAY, EntityType.VEX, EntityType.VINDICATOR, EntityType.WITCH, EntityType.WITHER_SKELETON, EntityType.ZOMBIE, EntityType.ZOMBIE_VILLAGER, EntityType.CAVE_SPIDER, EntityType.ENDERMAN};
        List<EntityType> HOSTILE_LIST = Arrays.asList(HOSTILE_TYPE);
        if (killer != null) {
            if (main.databaseManager.isInParty(killer)) {
                if (main.databaseManager.isTheHost(killer)) {
                    List<Player> playersOfParty = main.databaseManager.getAllPlayerOfParty(killer);
                    for (Player player : playersOfParty) {
                        updateAPlayer(death, HOSTILE_LIST, player);
                    }
                }
            } else {
                updateAPlayer(death, HOSTILE_LIST, killer);
            }
        }
    }

    private void updateAPlayer(Entity death, List<EntityType> HOSTILE_LIST, Player player) {
        int[] playerStats = main.filesManager.getPlayersStats(player);
        int level = playerStats[1];
        if (HOSTILE_LIST.contains(death.getType())) {
            main.playersManager.entityKilled(player, level);
        }
        if (death.getType() == EntityType.PLAYER) {
            Player killed = (Player) death;
            int[] killedStats = main.filesManager.getPlayersStats(killed);
            int killedLevel = killedStats[1];
            main.playersManager.entityKilled(player, killedLevel);
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        main.playersManager.respawn(player);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        Player sender = event.getPlayer();
        String playerLang = main.filesManager.getPlayerLang(sender);
        String selector = ChatHandler.getSelector(message);
        if (!message.substring(2).equalsIgnoreCase("")) {
            if (selector.contains("!")) {
                Chat_Type chat_type = ChatHandler.getMessageType(selector);
                switch (chat_type) {
                    case PARTY:
                        if (main.databaseManager.isInParty(sender)) {
                            if (main.databaseManager.isTheHost(sender)) {
                                List<Player> playersList = main.databaseManager.getAllPlayerOfParty(sender);
                                for (Player player : playersList) {
                                    player.sendMessage(ChatHandler.setPartyMessage() + ChatHandler.setChatMessage(chat_type, main, message, sender));
                                }
                            } else {
                                Player host = Bukkit.getPlayer(main.databaseManager.getHost(sender));
                                List<Player> playersList = main.databaseManager.getAllPlayerOfParty(host);
                                for (Player player : playersList) {
                                    player.sendMessage(ChatHandler.setPartyMessage() + ChatHandler.setChatMessage(chat_type, main, message, sender));
                                }
                            }
                        } else {
                            sender.sendMessage(ChatHandler.setWarningMessage() + main.filesManager.getMessage(Messages.Party_No, playerLang));
                            event.setCancelled(true);
                        }
                        break;
                    case GLOBAL:
                    case BROADCAST:
                        event.setFormat(ChatHandler.setMessageViaType(chat_type) + ChatHandler.setChatMessage(chat_type, main, message, sender));
                        break;
                    case FRIENDS:
                        List<String> friends = main.databaseManager.getPlayerFriends(sender);
                        List<Player> onlineFriends = new ArrayList<>();
                        if (friends != null) {
                            for (String friend : friends) {
                                onlineFriends.add(Bukkit.getPlayerExact(friend));
                            }
                            if (!onlineFriends.isEmpty()) {
                                for (Player friend : onlineFriends) {
                                    friend.sendMessage(ChatHandler.setFriendMessage() + ChatHandler.setChatMessage(chat_type, main, message, sender));
                                }
                            } else {
                                event.setCancelled(true);
                            }
                        }
                        break;
                    default:
                        event.setCancelled(true);
                        sender.sendMessage(ChatHandler.setErrorMessage() + main.filesManager.getMessage(Messages.Chat_Selector, playerLang));
                }
            } else {
                event.setCancelled(true);
                sender.sendMessage(ChatHandler.setErrorMessage() + main.filesManager.getMessage(Messages.Chat_Selector, playerLang));
            }
        } else {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        final String uuid = event.getEntity().getUniqueId().toString();
        final YamlConfiguration playersYaml = main.filesManager.getPlayersYaml();
        final int status_id = playersYaml.getInt("Players." + uuid + ".status");
        final Status status = Status.idToStatus(status_id);
        event.setDeathMessage(ChatHandler.setGlobalMessage() + status.getChatColor() + event.getEntity().getDisplayName() + " died !");
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getSlotType() == InventoryType.SlotType.OUTSIDE) return;
        main.inventoryManager.clickEvent(event);
    }
}
