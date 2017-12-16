package me.jul1an_k.survivalgames.countdown;

import me.jul1an_k.survivalgames.SurvivalGames;
import me.jul1an_k.survivalgames.scoreboard.LobbyScoreboard;
import me.jul1an_k.survivalgames.utils.GameState;
import me.jul1an_k.survivalgames.utils.TabActionTitle;
import me.jul1an_k.survivalgames.utils.manager.SoundManager;
import me.jul1an_k.survivalgames.utils.manager.VoteManager;
import me.jul1an_k.survivalgames.utils.manager.SoundManager.Sound;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Countdown_Lobby extends Countdown {
	
	public static int LobbyCounter = mana.getInteger("Countdown.Lobby.TimeInSeconds") + 1;
	
	public void start() {
		
		if(SurvivalGames.getStatus() == GameState.LOBBY) {
			
			Bukkit.getScheduler().scheduleSyncRepeatingTask(SurvivalGames.getInstance(), new Runnable() {
				public void run() {
					if(Bukkit.getOnlinePlayers().size() >= mana.getInteger("Countdown.Lobby.PlayersToStart")) {
						LobbyCounter--;
						if(LobbyCounter == 120 || LobbyCounter == 90 || LobbyCounter == 60 || LobbyCounter == 30 || LobbyCounter == 10 || LobbyCounter == 5 || LobbyCounter == 4 || LobbyCounter == 3 || LobbyCounter == 2) {
							if(mana.getBoolean("Countdown.Lobby.Broadcast.Chat")) {
								Bukkit.broadcastMessage(mana.getMessage("Countdown.Lobby.Message").replace("%seconds%", LobbyCounter + ""));
							}
							
							for(Player all : Bukkit.getOnlinePlayers()) {
								SoundManager.sendSound(all, Sound.RANDOM_LEVEL_UP, 1, 2);
							}
						}
						if(LobbyCounter == 1) {
							if(mana.getBoolean("Countdown.Lobby.Broadcast.Chat")) {
								Bukkit.broadcastMessage(mana.getMessage("Countdown.Lobby.Message").replace("%seconds%", LobbyCounter + ""));
							}
							
						}
						
						if(LobbyCounter == 15) {
							SurvivalGames.setVoting(false);
							VoteManager.endVote();
							if(mana.getBoolean("Countdown.Lobby.Broadcast.Chat")) {
								Bukkit.broadcastMessage(mana.getMessage("Messages.MapWin").replace("%map%", SurvivalGames.getWinMap().getName()).replace("%votes%", SurvivalGames.getWinMap().getVotes() + ""));
							}
							for(Player all : Bukkit.getOnlinePlayers()) {
								LobbyScoreboard.update(all);
							}
						}
						
						if(LobbyCounter == 0) {
							SurvivalGames.setStatus(GameState.WARMUP);
							if(mana.getBoolean("Countdown.Lobby.Broadcast.Chat")) {
								Bukkit.broadcastMessage(mana.getMessage("Countdown.Lobby.EndMessage"));
							}
							new Countdown_WarmUp().start();
							
							// INVCLEAR & PLAYERTP
							SurvivalGames.getWinMap().spawnPlayers();
							for(Player all : Bukkit.getOnlinePlayers()) {
								all.getInventory().clear();
								all.getInventory().setHelmet(null);
								all.getInventory().setChestplate(null);
								all.getInventory().setLeggings(null);
								all.getInventory().setBoots(null);
								all.setLevel(0);
								all.setExp(0);
								SurvivalGames.alive.add(all.getName());
							}
						}
						if(LobbyCounter >= 0) {
							for(Player all : Bukkit.getOnlinePlayers()) {
								all.setLevel(LobbyCounter);
								all.setExp(0);
								if(mana.getBoolean("Countdown.Lobby.Broadcast.ActionBar")) {
									TabActionTitle.sendActionBar(all, mana.getMessage("Countdown.Lobby.Message").replace("%seconds%", LobbyCounter + ""));
								}
							}
						}
					} else {
						for(Player all : Bukkit.getOnlinePlayers()) {
							all.setLevel(0);
							all.setExp(0);
							TabActionTitle.sendActionBar(all, mana.getMessage("Countdown.Lobby.NotEnoughPlayers"));
						}
						LobbyCounter = mana.getInteger("Countdown.Lobby.TimeInSeconds") + 1;
					}
				}
				
			}, 0L, 20L);
			
		}
	}
	
}
