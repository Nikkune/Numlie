package fr.leguiodan.numlie.managers;

import fr.leguiodan.numlie.Main;
import fr.leguiodan.numlie.utilities.ChatHandler;
import fr.leguiodan.numlie.utilities.ScoreboardsHandler;
import fr.leguiodan.numlie.utilities.database.DbConnection;
import fr.leguiodan.numlie.utilities.enumerations.Status;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventsManager implements Listener {

	private final Main main;
	private final Map<Player, Integer> taskMin_map;
	private final Map<Player, Integer> task_map;

	public EventsManager(Main main)
	{
		this.main = main;
		this.taskMin_map = new HashMap<>();
		this.task_map = new HashMap<>();
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		final Player player = event.getPlayer();
		final DbConnection dbConnection = main.getDatabaseManager().getDbConnection();
		try
		{
			final Connection connection = dbConnection.getConnection();
			main.databaseManager.createAccount(connection, player);
			main.databaseManager.createPlayerCash(connection, player);
			main.databaseManager.setOnline(connection, player.getUniqueId().toString());
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		ScoreboardsHandler.createScoreboard(player, main);
		final int task = Bukkit.getScheduler().scheduleSyncRepeatingTask(main, () -> main.playersManager.updatePlayerMin(player), 20L * 60, 20L * 60);
		final int task2 = Bukkit.getScheduler().scheduleSyncRepeatingTask(main, () -> main.playersManager.updatePlayer(player), 0L, 20L);
		taskMin_map.put(player, task);
		task_map.put(player, task2);
		event.setJoinMessage(ChatHandler.setJoinMessage(main, player));
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		final Player player = event.getPlayer();
		final DbConnection dbConnection = main.getDatabaseManager().getDbConnection();
		Bukkit.getScheduler().cancelTask(taskMin_map.get(player));
		Bukkit.getScheduler().cancelTask(task_map.get(player));
		try
		{
			final Connection connection = dbConnection.getConnection();
			main.databaseManager.updatesPlayers(connection, player);
			main.databaseManager.setOffline(connection, player.getUniqueId().toString());
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		event.setQuitMessage(ChatHandler.setLeaveMessage(main, player));
	}

	@EventHandler
	public void onMobKill(EntityDeathEvent event)
	{
		Entity death = event.getEntity();
		event.setDroppedExp(0);
		Player killer = event.getEntity().getKiller();
		if (killer != null)
		{
			EntityType[] HOSTILE_TYPE = new EntityType[]{EntityType.BLAZE, EntityType.CREEPER, EntityType.ELDER_GUARDIAN, EntityType.ENDERMITE, EntityType.EVOKER, EntityType.GHAST, EntityType.GUARDIAN, EntityType.HUSK, EntityType.MAGMA_CUBE, EntityType.PIG_ZOMBIE, EntityType.SHULKER, EntityType.SILVERFISH, EntityType.SKELETON, EntityType.SLIME, EntityType.SPIDER, EntityType.STRAY, EntityType.VEX, EntityType.VINDICATOR, EntityType.WITCH, EntityType.WITHER_SKELETON, EntityType.ZOMBIE, EntityType.ZOMBIE_VILLAGER, EntityType.CAVE_SPIDER, EntityType.ENDERMAN};
			List<EntityType> HOSTILE_LIST = Arrays.asList(HOSTILE_TYPE);
			int[] playerStats = main.filesManager.getPlayersStats(killer);
			int level = playerStats[1];
			if (HOSTILE_LIST.contains(death.getType()))
			{
				main.playersManager.entityKilled(killer, level);
				if (main.instancesManager.isInAInstance(killer))
				{
					List<Player> players = main.filesManager.getPlayersOfInstance(main.filesManager.getInstanceOfPlayer(killer));
					for (Player player : players)
					{
						if (player != killer)
						{
							main.playersManager.entityKilled(killer, level);
						}
					}
				}
			}
			if (death.getType() == EntityType.PLAYER)
			{
				Player killed = (Player) death;
				int[] killedStats = main.filesManager.getPlayersStats(killed);
				int killedLevel = killedStats[1];
				main.playersManager.entityKilled(killer, killedLevel);
				if (main.instancesManager.isInAInstance(killer))
				{
					List<Player> players = main.filesManager.getPlayersOfInstance(main.filesManager.getInstanceOfPlayer(killer));
					for (Player player : players)
					{
						if (player != killer)
						{
							main.playersManager.entityKilled(killer, killedLevel);
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event)
	{
		Player player = event.getPlayer();
		main.playersManager.respawn(player);
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event)
	{
		String message = event.getMessage();
		Player sender = event.getPlayer();
		event.setFormat(ChatHandler.setChatMessage(main, message, sender));
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event)
	{
		final String uuid = event.getEntity().getUniqueId().toString();
		final YamlConfiguration playersYaml = main.filesManager.getPlayersYaml();
		final int status_id = playersYaml.getInt("Players." + uuid + ".status");
		final Status status = Status.idToStatus(status_id);
		event.setDeathMessage(ChatHandler.setGlobalMessage(status.getChatColor() + event.getEntity().getDisplayName() + " died !"));
	}
}
