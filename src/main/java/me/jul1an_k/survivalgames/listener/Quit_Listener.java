package me.jul1an_k.survivalgames.listener;

import me.jul1an_k.survivalgames.SurvivalGames;
import me.jul1an_k.survivalgames.countdown.Countdown_Restart;
import me.jul1an_k.survivalgames.utils.GameState;
import me.jul1an_k.survivalgames.utils.StatsSystem;
import me.jul1an_k.survivalgames.utils.manager.MessageManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class Quit_Listener implements Listener {
	
	private MessageManager mana = SurvivalGames.getMessageManager();
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		
		int living = SurvivalGames.alive.size();
		
		e.setQuitMessage(null);
		
		if(SurvivalGames.getStatus() == GameState.LOBBY) {
			e.setQuitMessage(mana.getMessage("Messages.Quit").replace("%player%", p.getName()).replace("%ingame%", Bukkit.getOnlinePlayers().size() - 1 + "").replace("%max_players%", Bukkit.getMaxPlayers() + ""));
		} else if(SurvivalGames.getStatus() == GameState.INGAME) {
			if(SurvivalGames.lastdamage.containsKey(p) && SurvivalGames.alive.contains(p.getUniqueId())) {
				SurvivalGames.alive.remove(p.getUniqueId());
				
				Player k = SurvivalGames.lastdamage.get(p);
				k.sendMessage(mana.getMessage("Messages.YouKilled").replace("%player%", p.getName()));
				Bukkit.broadcastMessage(mana.getMessage("Messages.PlayerKilled").replace("%player%", p.getName()).replace("%killer%", k.getName()));
				StatsSystem.addDeaths(p, 1);
				StatsSystem.addKills(k, 1);
				if(living >= 2) {
					Bukkit.broadcastMessage(mana.getMessage("Messages.RemainingPlayers").replace("%remaining%", living + ""));
				}
				if(SurvivalGames.alive.size() == 1) {
					UUID uuid = SurvivalGames.alive.get(0);
					SurvivalGames.setStatus(GameState.RESTART);
					
					Bukkit.broadcastMessage(mana.getMessage("Messages.Win").replace("%player%", Bukkit.getPlayer(uuid).getName()));
					
					new Countdown_Restart().start();
				}
			}
		}
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent e) {
		Player p = e.getPlayer();
		
		int living = SurvivalGames.alive.size();
		
		e.setLeaveMessage(null);
		
		if(SurvivalGames.getStatus() == GameState.LOBBY) {
			e.setLeaveMessage(mana.getMessage("Messages.Quit").replace("%player%", p.getName()).replace("%ingame%", Bukkit.getOnlinePlayers().size() - 1 + "").replace("%max_players%", Bukkit.getMaxPlayers() + ""));
		} else if(SurvivalGames.getStatus() == GameState.INGAME) {
			if(SurvivalGames.lastdamage.containsKey(p) && SurvivalGames.alive.contains(p.getUniqueId())) {
				SurvivalGames.alive.remove(p.getUniqueId());
				
				Player k = SurvivalGames.lastdamage.get(p);
				k.sendMessage(mana.getMessage("Messages.YouKilled").replace("%player%", p.getName()));
				Bukkit.broadcastMessage(mana.getMessage("Messages.PlayerKilled").replace("%player%", p.getName()).replace("%killer%", k.getName()));
				StatsSystem.addDeaths(p, 1);
				StatsSystem.addKills(k, 1);
				if(living >= 2) {
					Bukkit.broadcastMessage(mana.getMessage("Messages.RemainingPlayers").replace("%remaining%", living + ""));
				}
				
				if(SurvivalGames.alive.size() == 1) {
					UUID uuid = SurvivalGames.alive.get(0);
					SurvivalGames.setStatus(GameState.RESTART);
					
					Bukkit.broadcastMessage(mana.getMessage("Messages.Win").replace("%player%", Bukkit.getPlayer(uuid).getName()));
					
					new Countdown_Restart().start();
				}
			}
		}
	}
	
}
