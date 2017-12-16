package me.jul1an_k.survivalgames.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class Chat_Listener implements Listener {
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		String msg = e.getMessage();
		
		msg = msg.replace("%", "%%");
		
		if(p.hasPermission("SG.Chat.Color")) {
			msg = ChatColor.translateAlternateColorCodes('&', msg);
		}
		if(p.isOp()) {
			e.setFormat("§a" + p.getDisplayName() + "§7 » §f" + msg);
		} else {
			e.setFormat("§7" + p.getDisplayName() + "§7 » §f" + msg);
		}
	}
	
}
