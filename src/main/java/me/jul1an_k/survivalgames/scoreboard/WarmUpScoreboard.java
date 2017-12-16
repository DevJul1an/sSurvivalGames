package me.jul1an_k.survivalgames.scoreboard;

import me.jul1an_k.survivalgames.SurvivalGames;
import me.jul1an_k.survivalgames.countdown.Countdown_WarmUp;
import me.jul1an_k.survivalgames.utils.manager.MessageManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class WarmUpScoreboard {
	
	private static MessageManager mana = SurvivalGames.getMessageManager();
	
	private static int currentScore = 0;
	
	private static void addScore(Objective obj, String name) {
		obj.getScore(name).setScore(currentScore);
		currentScore--;
	}
	
	public static void show(Player p) {
		currentScore = 0;
		Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
		Objective obj = board.getObjective("sgboard") != null ? board.getObjective("sgboard") : board.registerNewObjective("sgboard", "sgboard");
		
		obj.setDisplayName(mana.getMessage("Scoreboard.WarmUp.Title"));
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		// addScore(obj, "§0");
		// addScore(obj, "§6Spieler: §e" + SurvivalGames.alive.size() + "§7/§e"
		// + Bukkit.getMaxPlayers());
		// addScore(obj, "§6Status: §eWarmUp");
		// if(SurvivalGames.winMap == null) {
		// addScore(obj, "§6Map: §eFehler");
		// } else {
		// addScore(obj, "§6Map: §e" + SurvivalGames.winMap.getName());
		// }
		// addScore(obj, "§6Startet in: §e" + WarmUp.WarmUp + " Sekunden");
		// addScore(obj, "§3");
		
		for(String s : mana.getMessages("Scoreboard.WarmUp.Lines")) {
			addScore(obj, ChatColor.translateAlternateColorCodes('&', s).replace("%ingame%", SurvivalGames.alive.size() + "").replace("%max_players%", Bukkit.getMaxPlayers() + "").replace("%map%", SurvivalGames.getWinMap().getName()).replace("%seconds%", Countdown_WarmUp.WarmUp + ""));
		}
		
		p.setScoreboard(board);
	}
	
	public static void update(Player p) {
		Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
		
		board.getObjective(DisplaySlot.SIDEBAR).unregister();
		
		show(p);
	}
	
	public static void hide(Player p) {
		Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
		
		p.setScoreboard(board);
	}
	
	public static boolean isShowing(Player p) {
		return p.getScoreboard() != null;
	}
	
}
