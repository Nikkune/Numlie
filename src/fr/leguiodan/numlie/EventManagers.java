package fr.leguiodan.numlie;

import fr.leguiodan.numlie.utilities.Database.DbConnection;
import fr.leguiodan.numlie.utilities.Logger;
import fr.leguiodan.numlie.utilities.ScoreboardsHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManagers implements Listener {

	private final Main main;
	private final Map<Player, Integer> taskMin_map;
	private final Map<Player, Integer> task_map;

	public EventManagers(Main main)
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
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		ScoreboardsHandler.createScoreboard(player, main);
		final int task = Bukkit.getScheduler().scheduleSyncRepeatingTask(main, () -> main.playersManagers.updatePlayerMin(player), 20L * 60, 20L * 60);
		final int task2 = Bukkit.getScheduler().scheduleSyncRepeatingTask(main, () -> main.playersManagers.updatePlayer(player), 0L, 20L);
		taskMin_map.put(player, task);
		task_map.put(player, task2);
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
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	@EventHandler
	public void onMobKill(EntityDeathEvent event)
	{
		Entity death = event.getEntity();
		Player killer = event.getEntity().getKiller();
		if (killer != null)
		{
			EntityType[] HOSTILE_TYPE = new EntityType[]{EntityType.BLAZE, EntityType.CREEPER, EntityType.ELDER_GUARDIAN, EntityType.ENDERMITE, EntityType.EVOKER, EntityType.GHAST, EntityType.GUARDIAN, EntityType.HUSK, EntityType.MAGMA_CUBE, EntityType.PIG_ZOMBIE, EntityType.SHULKER, EntityType.SILVERFISH, EntityType.SKELETON, EntityType.SLIME, EntityType.SPIDER, EntityType.STRAY, EntityType.VEX, EntityType.VINDICATOR, EntityType.WITCH, EntityType.WITHER_SKELETON, EntityType.ZOMBIE, EntityType.ZOMBIE_VILLAGER, EntityType.CAVE_SPIDER, EntityType.ENDERMAN};
			List<EntityType> HOSTILE_LIST = Arrays.asList(HOSTILE_TYPE);
			int[] playerStats = main.filesManagers.getPlayersStats(killer);
			int level = playerStats[1];
			if (HOSTILE_LIST.contains(death.getType()))
			{
				main.playersManagers.entityKilled(killer, level);
			}
			if (death.getType() == EntityType.PLAYER)
			{
				Player killed = (Player) death;
				int[] killedStats = main.filesManagers.getPlayersStats(killed);
				int killedLevel = killedStats[1];
				main.playersManagers.entityKilled(killer, killedLevel);
			}
		}
	}
}
