package me.jul1an_k.survivalgames.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.jul1an_k.survivalgames.SurvivalGames;
import me.jul1an_k.survivalgames.commands.Command_SurvivalGames;
import me.jul1an_k.survivalgames.scoreboard.LobbyScoreboard;
import me.jul1an_k.survivalgames.utils.Voting_Map;
import me.jul1an_k.survivalgames.utils.manager.MessageManager;

public class InventoryClick_Listener implements Listener {
	
	private MessageManager mana = SurvivalGames.getMessageManager();
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if(e.getInventory().getName().equalsIgnoreCase(mana.getMessage("Vote.InventoryName"))) {
			String name = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
			
			e.setCancelled(true);
			
			for(Voting_Map map : SurvivalGames.getMaps()) {
				if(map.getName().equalsIgnoreCase(name)) {
					map.addVote();
				}
			}
			
			for(Player all : Bukkit.getOnlinePlayers()) {
				LobbyScoreboard.update(all);
			}
			
			Command_SurvivalGames.voted.add(e.getWhoClicked().getUniqueId());
			
			e.getView().close();
		}
	}
	
}
