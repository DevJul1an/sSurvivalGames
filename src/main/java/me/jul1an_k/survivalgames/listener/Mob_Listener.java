package me.jul1an_k.survivalgames.listener;

import org.bukkit.entity.Cow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class Mob_Listener implements Listener {
	
	@EventHandler
	public void onMobSpawn(CreatureSpawnEvent e) {
		Entity es = e.getEntity();
		if(!(es instanceof Pig) | !(es instanceof Cow) | !(es instanceof Sheep)) {
			e.setCancelled(true);
		}
	}
	
}
