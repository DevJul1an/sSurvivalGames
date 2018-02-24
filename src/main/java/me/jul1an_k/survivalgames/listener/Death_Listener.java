package me.jul1an_k.survivalgames.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.jul1an_k.survivalgames.SurvivalGames;
import me.jul1an_k.survivalgames.countdown.Countdown_Restart;
import me.jul1an_k.survivalgames.utils.GameState;
import me.jul1an_k.survivalgames.utils.StatsSystem;
import me.jul1an_k.survivalgames.utils.manager.MessageManager;

import java.util.UUID;

public class Death_Listener implements Listener {
	
	private MessageManager mana = SurvivalGames.getMessageManager();
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		int living = 0;
		
		e.setDeathMessage(null);
		if(SurvivalGames.getStatus() == GameState.INGAME || SurvivalGames.getStatus() == GameState.DEATHMATCH) {
			Player d = e.getEntity();
			
			if(SurvivalGames.alive.contains(d.getUniqueId())) {
				SurvivalGames.alive.remove(d.getUniqueId());
			}

			if(d.getKiller() != null) {
				Player k = d.getKiller();
				living = SurvivalGames.alive.size();
				k.sendMessage(mana.getMessage("Messages.YouKilled").replace("%player%", d.getName()));
				Bukkit.broadcastMessage(mana.getMessage("Messages.PlayerKilled").replace("%player%", d.getName()).replace("%killer%", k.getName()));
				StatsSystem.addDeaths(d, 1);
				StatsSystem.addKills(k, 1);
			} else {
				Bukkit.broadcastMessage(mana.getMessage("Messages.PlayerKilled").replace("%player%", d.getName()).replace("%killer%", mana.getMessage("Messages.PlayerKilledWordNature")));
			}
			
			if(living >= 2) {
				Bukkit.broadcastMessage(mana.getMessage("Messages.RemainingPlayers").replace("%remaining%", living + ""));
			}
			
			if(living <= 4) {
				// TODO DeathMatch
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
