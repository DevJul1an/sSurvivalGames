package me.jul1an_k.survivalgames.countdown;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.jul1an_k.survivalgames.SurvivalGames;
import me.jul1an_k.survivalgames.scoreboard.DeathMatchScoreboard;
import me.jul1an_k.survivalgames.utils.GameState;
import me.jul1an_k.survivalgames.utils.TabActionTitle;

public class Countdown_DeathMatch extends Countdown {
	
	public static int counter = mana.getInteger("Countdown.DeathMatch.TimeInSeconds") + 1;
	
	public void start() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(SurvivalGames.getInstance(), () -> {
            if(SurvivalGames.getStatus() == GameState.DEATHMATCH) {
                int min = counter / 60;
                int sec = counter - min * 60;
                for(Player all : Bukkit.getOnlinePlayers()) {
                    if(mana.getBoolean("Countdown.DeathMatch.Broadcast.ActionBar")) {
                        if(sec < 10) {
                            TabActionTitle.sendActionBar(all, mana.getMessage("Countdown.DeathMatch.Message").replace("%seconds%", "0" + sec + "").replace("%minutes%", min + ""));
                        } else {
                            TabActionTitle.sendActionBar(all, mana.getMessage("Countdown.DeathMatch.Message").replace("%seconds%", sec + "").replace("%minutes%", min + ""));
                        }
                    }

                    DeathMatchScoreboard.update(all);
                }
                if(counter == 0) {
                    SurvivalGames.setStatus(GameState.RESTART);
                    Bukkit.broadcastMessage(mana.getMessage("Countdown.DeathMatch.EndMessage"));
                    for(Player all : Bukkit.getOnlinePlayers()) {
                        if(mana.getBoolean("Countdown.DeathMatch.Broadcast.ActionBar")) {
                            TabActionTitle.sendActionBar(all, mana.getMessage("Countdown.DeathMatch.EndMessage"));
                        }
                    }

                    new Countdown_Restart().start();
                }

                counter--;
            }
        }, 0L, 20L);
	}
}
