package me.jul1an_k.survivalgames.countdown;

import me.jul1an_k.survivalgames.SurvivalGames;
import me.jul1an_k.survivalgames.scoreboard.GraceScoreboard;
import me.jul1an_k.survivalgames.utils.GameState;
import me.jul1an_k.survivalgames.utils.TabActionTitle;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Countdown_Grace extends Countdown {
	
	public static int grace = mana.getInteger("Countdown.Grace.TimeInSeconds") + 1;
	
	public void start() {
		if(SurvivalGames.getStatus() == GameState.GRACE) {
			for(Player all : Bukkit.getOnlinePlayers()) {
				if(mana.getBoolean("Tracker.OnStart")) {
					ItemStack is = new ItemStack(Material.COMPASS);
					ItemMeta im = is.getItemMeta();
					im.setDisplayName(mana.getMessage("Tracker.Name"));
					is.setItemMeta(im);
					all.getInventory().setItem(0, is);
				}
			}
			
			Bukkit.getScheduler().scheduleSyncRepeatingTask(SurvivalGames.getInstance(), () -> {
                grace--;
                if(grace == 30 | grace == 20 | grace == 15 | grace == 10 | grace == 5 | grace == 4 | grace == 3 | grace == 2) {
                    for(Player all : Bukkit.getOnlinePlayers()) {
                        if(mana.getBoolean("Countdown.Grace.Broadcast.ActionBar")) {
                            TabActionTitle.sendActionBar(all, mana.getMessage("Countdown.Grace.Message").replace("%seconds%", grace + ""));
                        }
                    }
                    if(mana.getBoolean("Countdown.Grace.Broadcast.Chat")) {
                        Bukkit.broadcastMessage(mana.getMessage("Countdown.Grace.Message").replace("%seconds%", grace + ""));
                    }
                }
                if(grace == 1) {
                    for(Player all : Bukkit.getOnlinePlayers()) {
                        if(mana.getBoolean("Countdown.Grace.Broadcast.ActionBar")) {
                            TabActionTitle.sendActionBar(all, mana.getMessage("Countdown.Grace.Message").replace("%seconds%", grace + ""));
                        }
                    }
                    if(mana.getBoolean("Countdown.Grace.Broadcast.Chat")) {
                        Bukkit.broadcastMessage(mana.getMessage("Countdown.Grace.Message").replace("%seconds%", grace + ""));
                    }
                }
                if(grace == 0) {
                    for(Player all : Bukkit.getOnlinePlayers()) {
                        if(mana.getBoolean("Countdown.Grace.Broadcast.ActionBar")) {
                            TabActionTitle.sendActionBar(all, mana.getMessage("Countdown.Grace.EndMessage"));
                        }
                    }
                    SurvivalGames.setStatus(GameState.INGAME);
                    if(mana.getBoolean("Countdown.Grace.Broadcast.Chat")) {
                        Bukkit.broadcastMessage(mana.getMessage("Countdown.Grace.EndMessage"));
                    }
                    new Countdown_Game().start();
                }
                if(grace >= 0) {
                    for(Player all : Bukkit.getOnlinePlayers()) {
                        if(mana.getBoolean("Countdown.Grace.Broadcast.ActionBar")) {
                            TabActionTitle.sendActionBar(all, mana.getMessage("Countdown.Grace.Message").replace("%seconds%", grace + ""));
                        }
                        GraceScoreboard.update(all);
                    }
                }
            }, 0L, 20L);
		}
	}
}
