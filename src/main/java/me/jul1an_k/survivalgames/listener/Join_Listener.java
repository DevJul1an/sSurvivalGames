package me.jul1an_k.survivalgames.listener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.jul1an_k.survivalgames.SurvivalGames;
import me.jul1an_k.survivalgames.scoreboard.LobbyScoreboard;
import me.jul1an_k.survivalgames.utils.GameState;
import me.jul1an_k.survivalgames.utils.Hologram;
import me.jul1an_k.survivalgames.utils.StatsSystem;
import me.jul1an_k.survivalgames.utils.manager.ConsoleManger;
import me.jul1an_k.survivalgames.utils.manager.MessageManager;

public class Join_Listener implements Listener {
	
	private MessageManager mana = SurvivalGames.getMessageManager();
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		final Player p = e.getPlayer();
		p.setHealth(20D);
		p.setFoodLevel(20);
		p.getInventory().clear();
		p.getInventory().setArmorContents(null);
		p.getEnderChest().clear();
		p.setAllowFlight(false);
		p.setFlying(false);
		p.setGameMode(GameMode.ADVENTURE);
		
		final File f = new File("plugins/sSurvivalGames", "location.yml");
		final FileConfiguration fc = YamlConfiguration.loadConfiguration(f);
		Location loc = new Location(Bukkit.getWorld(fc.getString("Hologram.World")), fc.getDouble("Hologram.X"), fc.getDouble("Hologram.Y"), fc.getDouble("Hologram.Z"));
		List<String> hololines = new ArrayList<String>();
		for(String line : mana.getMessages("Stats.Lines")) {
			int kills = StatsSystem.getKills(p);
			int deaths = StatsSystem.getDeaths(p);
			double kdr = ((double) kills) / ((double) deaths);
			
			String new_line = line;
			new_line = ChatColor.translateAlternateColorCodes('&', new_line);
			new_line = new_line.replace("%player%", p.getName());
			new_line = new_line.replace("%kills%", kills + "");
			new_line = new_line.replace("%deaths%", deaths + "");
			new_line = new_line.replace("%kdr%", kdr + "");
			hololines.add(new_line);
		}
		Hologram h = new Hologram(loc, hololines);
		h.display(p);
		
		for(Player all : Bukkit.getOnlinePlayers()) {
			all.showPlayer(p);
			p.showPlayer(all);
		}
		
		if(SurvivalGames.getStatus() == GameState.LOBBY) {
			e.setJoinMessage(mana.getMessage("Messages.Join").replace("%player%", p.getName()).replace("%ingame%", Bukkit.getOnlinePlayers().size() + "").replace("%max_players%", Bukkit.getMaxPlayers() + ""));
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(SurvivalGames.getInstance(), new Runnable() {
				public void run() {
					p.teleport(new Location(Bukkit.getWorld(fc.getString("Lobby.World")), fc.getDouble("Lobby.X"), fc.getDouble("Lobby.Y"), fc.getDouble("Lobby.Z"), (float) fc.getDouble("Lobby.Yaw"), (float) fc.getDouble("Lobby.Pitch")));
				}
			}, 1L);
			
			LobbyScoreboard.hide(p);
			
			LobbyScoreboard.show(p);
			
			for(Player all : Bukkit.getOnlinePlayers()) {
				LobbyScoreboard.update(all);
			}
			
			for(String s : mana.getSection("LobbyItems").getKeys(false)) {
				try {
					Material m = Material.getMaterial(mana.getMessage("LobbyItems." + s + ".Material").toUpperCase());
					int slot = mana.getInteger("LobbyItems." + s + ".Slot");
					String displayName = mana.getMessage("LobbyItems." + s + ".DisplayName");
					
					ItemStack is = new ItemStack(m);
					ItemMeta im = is.getItemMeta();
					im.setDisplayName(displayName);
					is.setItemMeta(im);
					
					for(Player all : Bukkit.getOnlinePlayers()) {
						all.getInventory().setItem(slot - 1, is);
					}
				} catch(Exception ex) {
					ConsoleManger.sendStacktrace("Give LobbyItems", ex);
				}
			}
		} else {
			e.setJoinMessage(null);
			p.setAllowFlight(true);
			p.setFlying(true);
			for(Player all : Bukkit.getOnlinePlayers()) {
				if(all != p) {
					all.hidePlayer(p);
				}
			}
			p.sendMessage("§b[§6SurvivalGames§b] §aDu bist jetzt Spectator");
			
			// Spectator
			Bukkit.getScheduler().scheduleSyncDelayedTask(SurvivalGames.getInstance(), new Runnable() {
				public void run() {
					p.teleport(new Location(Bukkit.getWorld(fc.getString("Spectator.World")), fc.getDouble("Spectator.X"), fc.getDouble("Spectator.Y"), fc.getDouble("Spectator.Z"), (float) fc.getDouble("Spectator.Yaw"), (float) fc.getDouble("Spectator.Pitch")));
				}
			}, 1L);
		}
	}
	
}
