package me.jul1an_k.survivalgames.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import me.jul1an_k.survivalgames.SurvivalGames;

public class Place_Listener implements Listener {
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		if(!SurvivalGames.getMessageManager().getBoolean("EnableBuilding")) {
			e.setCancelled(true);
			return;
		}
		
		Player p = e.getPlayer();

		e.setCancelled(p.hasPermission("SG.PlaceBlock") | e.getBlock().getType() == Material.MELON);
	}
	
}
