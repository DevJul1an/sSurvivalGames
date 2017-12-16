package me.jul1an_k.survivalgames.listener;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import me.jul1an_k.survivalgames.SurvivalGames;
import me.jul1an_k.survivalgames.utils.GameState;

public class Break_Listener implements Listener {
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		if(!SurvivalGames.getMessageManager().getBoolean("EnableBuilding")) {
			e.setCancelled(true);
			return;
		}
		
		if(SurvivalGames.getStatus() == GameState.INGAME | SurvivalGames.getStatus() == GameState.GRACE | e.getPlayer().hasPermission("SG.BreakBlock")) {
			if(e.getBlock().getType() == Material.LEAVES | e.getBlock().getType() == Material.LEAVES_2 | e.getPlayer().hasPermission("SG.BreakBlock")) {
				e.setCancelled(false);
			} else {
				e.setCancelled(true);
			}
		} else {
			e.setCancelled(true);
		}
	}
	
}
