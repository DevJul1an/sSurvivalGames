package me.jul1an_k.survivalgames.listener;

import me.jul1an_k.survivalgames.SurvivalGames;
import me.jul1an_k.survivalgames.utils.GameState;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class Food_Listener implements Listener {
	
	@EventHandler
	public void onFood(FoodLevelChangeEvent e) {
		if(SurvivalGames.getStatus() != GameState.INGAME) {
			e.setCancelled(true);
		}
	}
	
}
