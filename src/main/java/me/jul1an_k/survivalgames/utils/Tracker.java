package me.jul1an_k.survivalgames.utils;

import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class Tracker {
	
	public static Player getNearest(Player from, int radius) {
		double distance = Double.MAX_VALUE;
		Player target = null;
		
		for(Entity en : from.getNearbyEntities(radius, radius, radius)) {
			if(en instanceof Player) {
				if(((Player) en).getGameMode().equals(GameMode.SPECTATOR))
					continue;
				
				double dis = from.getLocation().distance(en.getLocation());
				
				if(dis < distance) {
					distance = dis;
					target = (Player) en;
				}
			}
		}
		
		return target;
	}
}
