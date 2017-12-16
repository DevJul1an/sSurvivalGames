package me.jul1an_k.survivalgames.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import me.jul1an_k.survivalgames.SurvivalGames;
import me.jul1an_k.survivalgames.utils.GameState;

public class Damage_Listener implements Listener {
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if(SurvivalGames.getStatus() == GameState.INGAME || SurvivalGames.getStatus() == GameState.DEATHMATCH) {
			if(e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
				Player d = (Player) e.getDamager();
				Player en = (Player) e.getEntity();
				if(SurvivalGames.alive.contains(en.getName()) && SurvivalGames.alive.contains(d.getName())) {
					e.setCancelled(false);
					if(SurvivalGames.lastdamage.containsKey(en)) {
						SurvivalGames.lastdamage.remove(en);
					}
					SurvivalGames.lastdamage.put(en, d);
				} else {
					e.setCancelled(true);
				}
			}
		} else {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onDamage2(EntityDamageEvent e) {
		if(SurvivalGames.getStatus() != GameState.INGAME && SurvivalGames.getStatus() != GameState.DEATHMATCH) {
			e.setCancelled(true);
		}
	}
	
}
