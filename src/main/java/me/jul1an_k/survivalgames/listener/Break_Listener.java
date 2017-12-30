package me.jul1an_k.survivalgames.listener;

import me.jul1an_k.survivalgames.utils.manager.MessageManager;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import me.jul1an_k.survivalgames.SurvivalGames;
import me.jul1an_k.survivalgames.utils.GameState;

import java.util.List;

public class Break_Listener implements Listener {

	private MessageManager mana = SurvivalGames.getMessageManager();

	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		if(!SurvivalGames.getMessageManager().getBoolean("EnableBuilding")) {
			e.setCancelled(true);
			return;
		}

		List<String> allowedToBreak = mana.getMessages("BreakableBlocks");

		if(SurvivalGames.getStatus() == GameState.INGAME | SurvivalGames.getStatus() == GameState.GRACE | e.getPlayer().hasPermission("SG.BreakBlock")) {
			if(allowedToBreak.contains(e.getBlock().getType().name()) | e.getPlayer().hasPermission("SG.BreakBlock")) {
				e.setCancelled(false);
			} else {
				e.setCancelled(true);
			}
		} else {
			e.setCancelled(true);
		}
	}
	
}
