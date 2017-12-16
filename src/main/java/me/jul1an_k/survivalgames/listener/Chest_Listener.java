package me.jul1an_k.survivalgames.listener;

import java.util.Map;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import me.jul1an_k.survivalgames.SurvivalGames;
import me.jul1an_k.survivalgames.utils.GameState;
import me.jul1an_k.survivalgames.utils.manager.ChestManager;

public class Chest_Listener implements Listener {
	
	private ChestManager chestManager = SurvivalGames.getChestManager();
	
	@EventHandler
	public void onOpenChest(PlayerInteractEvent e) {
		if(e.getClickedBlock() != null) {
			if(e.getClickedBlock().getLocation().getBlock().getType() == Material.CHEST) {
				if(SurvivalGames.getStatus() != GameState.INGAME && SurvivalGames.getStatus() != GameState.GRACE) {
					e.setCancelled(true);
					return;
				}
				Chest chest = (Chest) e.getClickedBlock().getState();
				Location loc = e.getClickedBlock().getLocation();
				Map<Location, Chest> chests = SurvivalGames.getChestManager().chests;
				
				if(!chests.containsValue(chest)) {
					setupInventory(chest);
					chests.put(loc, chest);
				}
			}
		}
	}
	
	public void setupInventory(Chest chest) {
		// Inventory inv = Bukkit.createInventory(null, 27,
		// mana.getMessage("Chest.Title"));
		Inventory inv = chest.getBlockInventory();
		
		Random random = new Random();
		int l = random.nextInt(15);
		
		while(l != 0) {
			l--;
			
			int l2 = random.nextInt(inv.getSize());
			int l3 = random.nextInt(chestManager.getItems().size());
			
			inv.setItem(l2, chestManager.getItems().get(l3));
		}
	}
	
}
