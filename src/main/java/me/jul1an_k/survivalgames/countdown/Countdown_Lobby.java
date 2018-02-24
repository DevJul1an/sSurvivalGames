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
	
	public static int lobbyCounter = mana.getInteger("Countdown.Lobby.TimeInSeconds") + 1;
	
	public void start() {
		if(SurvivalGames.getStatus() == GameState.LOBBY) {
			Bukkit.getScheduler().scheduleSyncRepeatingTask(SurvivalGames.getInstance(), () -> {
                if(Bukkit.getOnlinePlayers().size() >= mana.getInteger("Countdown.Lobby.PlayersToStart")) {
                    lobbyCounter--;
                    if(lobbyCounter == 120 || lobbyCounter == 90 || lobbyCounter == 60 || lobbyCounter == 30 || lobbyCounter == 10 || lobbyCounter == 5 || lobbyCounter == 4 || lobbyCounter == 3 || lobbyCounter == 2) {
                        if(mana.getBoolean("Countdown.Lobby.Broadcast.Chat")) {
                            Bukkit.broadcastMessage(mana.getMessage("Countdown.Lobby.Message").replace("%seconds%", lobbyCounter + ""));
                        }

                        for(Player all : Bukkit.getOnlinePlayers()) {
                            SoundManager.sendSound(all, Sound.RANDOM_LEVEL_UP, 1, 2);
                        }
                    }
                    if(lobbyCounter == 1) {
                        if(mana.getBoolean("Countdown.Lobby.Broadcast.Chat")) {
                            Bukkit.broadcastMessage(mana.getMessage("Countdown.Lobby.Message").replace("%seconds%", lobbyCounter + ""));
                        }

                    }

                    if(lobbyCounter == 15) {
                        SurvivalGames.setVoting(false);
                        VoteManager.endVote();
                        if(mana.getBoolean("Countdown.Lobby.Broadcast.Chat")) {
                            Bukkit.broadcastMessage(mana.getMessage("Messages.MapWin").replace("%map%", SurvivalGames.getWinMap().getName()).replace("%votes%", SurvivalGames.getWinMap().getVotes() + ""));
                        }
                        for(Player all : Bukkit.getOnlinePlayers()) {
                            LobbyScoreboard.update(all);
                        }
                    }

                    if(lobbyCounter == 0) {
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
                            SurvivalGames.alive.add(all.getUniqueId());
                        }
                    }
                    if(lobbyCounter >= 0) {
                        for(Player all : Bukkit.getOnlinePlayers()) {
                            all.setLevel(lobbyCounter);
                            all.setExp(0);
                            if(mana.getBoolean("Countdown.Lobby.Broadcast.ActionBar")) {
                                TabActionTitle.sendActionBar(all, mana.getMessage("Countdown.Lobby.Message").replace("%seconds%", lobbyCounter + ""));
                            }
                        }
                    }
                } else {
                    for(Player all : Bukkit.getOnlinePlayers()) {
                        all.setLevel(0);
                        all.setExp(0);
                        TabActionTitle.sendActionBar(all, mana.getMessage("Countdown.Lobby.NotEnoughPlayers"));
                    }
                    lobbyCounter = mana.getInteger("Countdown.Lobby.TimeInSeconds") + 1;
                }
            }, 0L, 20L);
			
		}
	}
	
}
