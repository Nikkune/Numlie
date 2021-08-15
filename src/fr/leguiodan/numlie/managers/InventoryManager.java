package fr.leguiodan.numlie.managers;

import fr.leguiodan.numlie.Main;
import fr.leguiodan.numlie.utilities.Logger;
import fr.leguiodan.numlie.utilities.enumerations.Messages;
import fr.leguiodan.numlie.utilities.handlers.ChatHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryManager {

	private final Main main;

	public InventoryManager(Main main)
	{
		this.main = main;
	}

	public void createInventory(int size, Player player)
	{
		String lang = main.filesManager.getPlayerLang(player);
		int title = main.filesManager.getPlayerTitle(player);
		String invertedLang;
		if ("EN".equals(lang))
		{
			invertedLang = "FR";
		} else
		{
			invertedLang = "EN";
		}

		String invertedTitle;
		int id;
		if (0 == title)
		{
			invertedTitle = main.filesManager.getMessage(Messages.UI_Chat, lang);
			id = 1;
		} else
		{
			invertedTitle = main.filesManager.getMessage(Messages.UI_Title, lang);
			id = 2;
		}
		Inventory inv = Bukkit.createInventory(null, size, "§8Test Inventory");

		ItemStack langButton = getItem(Material.STAINED_GLASS, 1, 14, "§91 " + main.filesManager.getMessage(Messages.UI_Change_Lang, lang) + invertedLang);
		ItemStack linkKeyButton = getItem(Material.STAINED_GLASS, 1, 11, "§b2 " + main.filesManager.getMessage(Messages.UI_Get_Link_Key, lang));
		ItemStack titleButton = getItem(Material.STAINED_GLASS, id, 13, "§e3 " + main.filesManager.getMessage(Messages.UI_Change_Title, lang) + invertedTitle);

		inv.setItem(2, langButton);
		inv.setItem(6, linkKeyButton);
		inv.setItem(4, titleButton);
		player.openInventory(inv);
	}

	public ItemStack getItem(Material material, int amount, int damage, String customName)
	{
		ItemStack stack = new ItemStack(material, amount, (byte) damage);
		ItemMeta stackItemMeta = stack.getItemMeta();
		stackItemMeta.setDisplayName(customName);
		stack.setItemMeta(stackItemMeta);
		return stack;
	}

	public void clickEvent(InventoryClickEvent event)
	{
		Inventory inventory = event.getClickedInventory();
		Player player = (Player) event.getWhoClicked();
		ItemStack current = event.getCurrentItem();
		if (inventory.getName().equalsIgnoreCase("§8Test Inventory"))
		{
			if (current != null)
			{
				int itemId = Integer.parseInt(current.getItemMeta().getDisplayName().substring(2, 3));
				switch (itemId)
				{
					case 1:
						int length = current.getItemMeta().getDisplayName().length();
						main.filesManager.setPlayerLang(player, current.getItemMeta().getDisplayName().substring(length - 2));
						break;
					case 2:
						player.sendMessage(ChatHandler.setInfoMessage("Link Key : " + main.filesManager.getLink_Key(player)));
						break;
					case 3:
						int id = 0;
						if (current.getAmount() == 1)
						{
							id = 1;
						}
						main.filesManager.setPlayerTitle(player, id);
						player.sendMessage(String.valueOf(id));
						break;
					default:
						break;
				}
			}
			player.closeInventory();
			event.setCancelled(true);
		}
		if (inventory.getName().equalsIgnoreCase("container.inventory"))
		{
			assert current != null;
			if (current.getType() == Material.COMPASS)
			{
				if (current.hasItemMeta())
				{
					if (current.getItemMeta().getDisplayName().equalsIgnoreCase("Settings"))
					{
						event.setCancelled(true);
						createInventory(9, player);
					}
				}
			}
		}
	}
}
