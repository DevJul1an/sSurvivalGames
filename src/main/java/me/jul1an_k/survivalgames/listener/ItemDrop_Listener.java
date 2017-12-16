package me.jul1an_k.survivalgames.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import me.jul1an_k.survivalgames.SurvivalGames;
import me.jul1an_k.survivalgames.utils.GameState;

public class ItemDrop_Listener implements Listener {
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		e.setCancelled(SurvivalGames.getStatus() == GameState.LOBBY);
	}
	
	@EventHandler
	public void onSwapHandItems(PlayerSwapHandItemsEvent e) {
		e.setCancelled(SurvivalGames.getStatus() == GameState.LOBBY);
	}
}
