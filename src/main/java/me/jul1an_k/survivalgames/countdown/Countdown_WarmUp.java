package me.jul1an_k.survivalgames.countdown;

import me.jul1an_k.survivalgames.SurvivalGames;
import me.jul1an_k.survivalgames.scoreboard.WarmUpScoreboard;
import me.jul1an_k.survivalgames.utils.GameState;
import me.jul1an_k.survivalgames.utils.TabActionTitle;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class Countdown_WarmUp extends Countdown {
	
	public static int WarmUp = mana.getInteger("Countdown.WarmUp.TimeInSeconds") + 1;
	
	public void start() {
		if(SurvivalGames.getStatus() == GameState.WARMUP) {
			Bukkit.getScheduler().scheduleSyncRepeatingTask(SurvivalGames.getInstance(), new Runnable() {
				public void run() {
					WarmUp--;
					if(WarmUp == 20 | WarmUp == 15 | WarmUp == 10 | WarmUp == 5 | WarmUp == 4 | WarmUp == 3 | WarmUp == 2) {
						if(mana.getBoolean("Countdown.WarmUp.Broadcast.Chat")) {
							Bukkit.broadcastMessage(mana.getMessage("Countdown.WarmUp.Message").replace("%seconds%", WarmUp + ""));
						}
					}
					if(WarmUp == 1) {
						if(mana.getBoolean("Countdown.WarmUp.Broadcast.Chat")) {
							Bukkit.broadcastMessage(mana.getMessage("Countdown.WarmUp.Message").replace("%seconds%", WarmUp + ""));
						}
					}
					if(WarmUp == 0) {
						SurvivalGames.setStatus(GameState.GRACE);
						if(mana.getBoolean("Countdown.WarmUp.Broadcast.Chat")) {
							Bukkit.broadcastMessage(mana.getMessage("Countdown.WarmUp.EndMessage"));
						}
						new Countdown_Grace().start();
					}
					if(WarmUp >= 0) {
						for(Player all : Bukkit.getOnlinePlayers()) {
							all.setLevel(WarmUp);
							all.setExp(0);
							if(mana.getBoolean("Countdown.WarmUp.Broadcast.ActionBar")) {
								TabActionTitle.sendActionBar(all, mana.getMessage("Countdown.WarmUp.Message").replace("%seconds%", WarmUp + ""));
							}
							WarmUpScoreboard.update(all);
							all.setGameMode(GameMode.SURVIVAL);
						}
					}
				}
			}, 0L, 20L);
		} else {
			WarmUp = 21;
		}
	}
	
}
