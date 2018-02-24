package me.jul1an_k.survivalgames.countdown;

import me.jul1an_k.survivalgames.SurvivalGames;
import me.jul1an_k.survivalgames.scoreboard.WarmUpScoreboard;
import me.jul1an_k.survivalgames.utils.GameState;
import me.jul1an_k.survivalgames.utils.TabActionTitle;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class Countdown_WarmUp extends Countdown {
	
	private static int warmUp = mana.getInteger("Countdown.WarmUp.TimeInSeconds") + 1;
	
	public void start() {
		if(SurvivalGames.getStatus() == GameState.WARMUP) {
			Bukkit.getScheduler().scheduleSyncRepeatingTask(SurvivalGames.getInstance(), () -> {
				warmUp--;
                if(warmUp == 20 | warmUp == 15 | warmUp == 10 | warmUp == 5 | warmUp == 4 | warmUp == 3 | warmUp == 2) {
                    if(mana.getBoolean("Countdown.WarmUp.Broadcast.Chat")) {
                        Bukkit.broadcastMessage(mana.getMessage("Countdown.WarmUp.Message").replace("%seconds%", warmUp + ""));
                    }
                }
                if(warmUp == 1) {
                    if(mana.getBoolean("Countdown.WarmUp.Broadcast.Chat")) {
                        Bukkit.broadcastMessage(mana.getMessage("Countdown.WarmUp.Message").replace("%seconds%", warmUp + ""));
                    }
                }
                if(warmUp == 0) {
                    SurvivalGames.setStatus(GameState.GRACE);
                    if(mana.getBoolean("Countdown.WarmUp.Broadcast.Chat")) {
                        Bukkit.broadcastMessage(mana.getMessage("Countdown.WarmUp.EndMessage"));
                    }
                    new Countdown_Grace().start();
                }
                if(warmUp >= 0) {
                    for(Player all : Bukkit.getOnlinePlayers()) {
                        all.setLevel(warmUp);
                        all.setExp(0);
                        if(mana.getBoolean("Countdown.WarmUp.Broadcast.ActionBar")) {
                            TabActionTitle.sendActionBar(all, mana.getMessage("Countdown.WarmUp.Message").replace("%seconds%", warmUp + ""));
                        }
                        WarmUpScoreboard.update(all);
                        all.setGameMode(GameMode.SURVIVAL);
                    }
                }
            }, 0L, 20L);
		} else {
			warmUp = mana.getInteger("Countdown.WarmUp.TimeInSeconds") + 1;
		}
	}
	
}
