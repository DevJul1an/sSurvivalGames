package me.jul1an_k.survivalgames.countdown;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import me.jul1an_k.survivalgames.SurvivalGames;
import me.jul1an_k.survivalgames.utils.GameState;
import me.jul1an_k.survivalgames.utils.TabActionTitle;

public class Countdown_Restart extends Countdown {
	
	private int restart = mana.getInteger("Countdown.Restart.TimeInSeconds") + 1;
	
	public void start() {
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(SurvivalGames.getInstance(), new Runnable() {
			public void run() {
				if(SurvivalGames.getStatus() == GameState.RESTART) {
					restart--;
					if(restart == 20 | restart == 15 | restart == 10 | restart == 5 | restart == 4 | restart == 3 | restart == 2) {
						// Bukkit.broadcastMessage("§b[§6SurvivalGames§b] §aDas
						// Spiel beginnt in §e"
						// + Restart + "§a Sekunden!");
						if(mana.getBoolean("Countdown.Restart.Broadcast.Chat")) {
							Bukkit.broadcastMessage(mana.getMessage("Countdown.Restart.Message").replace("%seconds%", restart + ""));
						}
					}
					if(restart == 1) {
						if(mana.getBoolean("Countdown.Restart.Broadcast.Chat")) {
							Bukkit.broadcastMessage(mana.getMessage("Countdown.Restart.Message").replace("%seconds%", restart + ""));
						}
					}
					if(restart == 0) {
						if(mana.getBoolean("Countdown.Restart.Broadcast.Chat")) {
							Bukkit.broadcastMessage(mana.getMessage("Countdown.Restart.EndMessage"));
						}
						
						for(Player all : Bukkit.getOnlinePlayers()) {
							ByteArrayDataOutput out = ByteStreams.newDataOutput();
							out.writeUTF("Connect");
							out.writeUTF(mana.getMessage("BungeeCord.fallback_server"));
							
							all.sendPluginMessage(SurvivalGames.getInstance(), "BungeeCord", out.toByteArray());
						}
						
						Bukkit.shutdown();
					}
					if(restart >= 0) {
						for(Player all : Bukkit.getOnlinePlayers()) {
							all.setLevel(restart);
							all.setExp(0);
							if(mana.getBoolean("Countdown.Restart.Broadcast.ActionBar")) {
								TabActionTitle.sendActionBar(all, mana.getMessage("Countdown.Restart.Message").replace("%seconds%", restart + ""));
							}
						}
					}
				}
			}
		}, 0L, 20L);
	}
	
}
