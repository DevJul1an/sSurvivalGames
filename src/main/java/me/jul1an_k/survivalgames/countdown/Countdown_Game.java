package me.jul1an_k.survivalgames.countdown;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.jul1an_k.survivalgames.SurvivalGames;
import me.jul1an_k.survivalgames.scoreboard.InGameScoreboard;
import me.jul1an_k.survivalgames.utils.GameState;
import me.jul1an_k.survivalgames.utils.TabActionTitle;

public class Countdown_Game extends Countdown {
	
	public static int counter = mana.getInteger("Countdown.Game.TimeInSeconds") + 1;
	
	public void start() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(SurvivalGames.getInstance(), () -> {
            if(SurvivalGames.getStatus() == GameState.INGAME) {
                double counterd = counter;
                int min = (int) counterd / 60;
                int sec = counter - min * 60;
                for(Player all : Bukkit.getOnlinePlayers()) {
                    if(mana.getBoolean("Countdown.Game.Broadcast.ActionBar")) {
                        if(sec < 10) {
                            TabActionTitle.sendActionBar(all, mana.getMessage("Countdown.Game.Message").replace("%seconds%", "0" + sec + "").replace("%minutes%", min + ""));
                        } else {
                            TabActionTitle.sendActionBar(all, mana.getMessage("Countdown.Game.Message").replace("%seconds%", sec + "").replace("%minutes%", min + ""));
                        }
                    }

                    InGameScoreboard.update(all);
                }
                if(counter == 0) {
                    SurvivalGames.setStatus(GameState.DEATHMATCH);
                    Bukkit.broadcastMessage(mana.getMessage("Countdown.Game.EndMessage"));
                    for(Player all : Bukkit.getOnlinePlayers()) {
                        if(mana.getBoolean("Countdown.Game.Broadcast.ActionBar")) {
                            TabActionTitle.sendActionBar(all, mana.getMessage("Countdown.Game.EndMessage"));
                        }
                    }

                    // PLAYERTP
                    SurvivalGames.getWinMap().spawnPlayersDeathMatch();

                    new Countdown_DeathMatch().start();
                }

                counter--;
            }
        }, 0L, 20L);
	}
}
