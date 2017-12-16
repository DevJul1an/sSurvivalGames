package me.jul1an_k.survivalgames.listener;

import me.jul1an_k.survivalgames.SurvivalGames;
import me.jul1an_k.survivalgames.utils.GameState;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class Move_Listener implements Listener {
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		
		if(SurvivalGames.getStatus() == GameState.WARMUP) {
			double X = p.getLocation().getBlockX();
			double Z = p.getLocation().getBlockZ();
			if(SurvivalGames.locs.containsKey(p.getName())) {
				Location locs = SurvivalGames.locs.get(p.getName());
				double X2 = locs.getBlockX();
				double Z2 = locs.getBlockZ();
				if(X != X2 || Z != Z2) {
					locs.setDirection(p.getLocation().getDirection());
					p.teleport(locs);
				}
			}
		}
		
	}
	
}
