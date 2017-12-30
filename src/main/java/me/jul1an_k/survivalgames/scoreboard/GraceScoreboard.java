package me.jul1an_k.survivalgames.scoreboard;

import me.jul1an_k.survivalgames.SurvivalGames;
import me.jul1an_k.survivalgames.countdown.Countdown_Grace;
import me.jul1an_k.survivalgames.utils.manager.MessageManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class GraceScoreboard {
	
	private static MessageManager mana = SurvivalGames.getMessageManager();
	
	private static int currentScore = 0;
	
	private static void addScore(Objective obj, String name) {
		obj.getScore(name).setScore(currentScore);
		currentScore--;
	}

	private static void show(Player p) {
		currentScore = 0;
		Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
		Objective obj = board.getObjective("sgboard") != null ? board.getObjective("sgboard") : board.registerNewObjective("sgboard", "sgboard");
		
		obj.setDisplayName(mana.getMessage("Scoreboard.Grace.Title"));
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		for(String s : mana.getMessages("Scoreboard.Grace.Lines")) {
			addScore(obj, ChatColor.translateAlternateColorCodes('&', s).replace("%ingame%", SurvivalGames.alive.size() + "").replace("%max_players%", Bukkit.getMaxPlayers() + "").replace("%map%", SurvivalGames.getWinMap().getName()).replace("%seconds%", Countdown_Grace.grace + ""));
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
