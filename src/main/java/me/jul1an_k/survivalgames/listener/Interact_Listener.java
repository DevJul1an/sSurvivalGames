package me.jul1an_k.survivalgames.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import me.jul1an_k.survivalgames.SurvivalGames;
import me.jul1an_k.survivalgames.utils.GameState;
import me.jul1an_k.survivalgames.utils.Tracker;
import me.jul1an_k.survivalgames.utils.manager.ConsoleManger;
import me.jul1an_k.survivalgames.utils.manager.MessageManager;

public class Interact_Listener implements Listener {
	
	private MessageManager mana = SurvivalGames.getMessageManager();
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		try {
			Player p = e.getPlayer();
			
			if(e.getItem().getType() == Material.COMPASS) {
				Player nearest = Tracker.getNearest(p, mana.getInteger("Tracker.Radius"));
				
				if(nearest == null) {
					p.sendMessage(mana.getMessage("Messages.NoPlayerInRadius"));
					return;
				}
				
				p.setCompassTarget(nearest.getLocation());
				
				int blocks = (int) p.getLocation().distance(nearest.getLocation());
				
				p.sendMessage(mana.getMessage("Messages.NearestPlayer").replace("%player%", nearest.getDisplayName()).replace("%blocks%", blocks + ""));
			}
			
			if(SurvivalGames.getStatus() != GameState.LOBBY)
				return;
			for(String s : mana.getSection("LobbyItems").getKeys(false)) {
				try {
					Material m = Material.getMaterial(mana.getMessage("LobbyItems." + s + ".Material").toUpperCase());
					String displayName = mana.getMessage("LobbyItems." + s + ".DisplayName");
					String execute = mana.getMessage("LobbyItems." + s + ".Execute");
					
					ItemStack is = new ItemStack(m);
					ItemMeta im = is.getItemMeta();
					im.setDisplayName(displayName);
					is.setItemMeta(im);
					
					if(e.getItem().equals(is)) {
						e.setCancelled(true);
						
						if(execute.equalsIgnoreCase("ConnectToFallbackServer")) {
							ByteArrayDataOutput out = ByteStreams.newDataOutput();
							out.writeUTF("Connect");
							out.writeUTF(mana.getMessage("BungeeCord.fallback_server"));
							
							p.sendPluginMessage(SurvivalGames.getInstance(), "BungeeCord", out.toByteArray());
						} else {
							Bukkit.dispatchCommand(p, execute.replaceFirst("/", ""));
						}
					}
				} catch(Exception ex) {
					ConsoleManger.sendStacktrace("Execute LobbyItem", ex);
				}
			}
		} catch(Exception ex) {
			
		}
	}
}
