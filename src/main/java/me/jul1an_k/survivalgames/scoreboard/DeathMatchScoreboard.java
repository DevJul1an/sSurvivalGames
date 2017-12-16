package me.jul1an_k.survivalgames.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import me.jul1an_k.survivalgames.SurvivalGames;
import me.jul1an_k.survivalgames.countdown.Countdown_DeathMatch;
import me.jul1an_k.survivalgames.utils.manager.MessageManager;

public class DeathMatchScoreboard {
	
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
		
		obj.setDisplayName(mana.getMessage("Scoreboard.DeathMatch.Title"));
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		// addScore(obj, "�0");
		// addScore(obj, "�6Spieler: �e" + SurvivalGames.alive.size() + "�7/�e"
		// + Bukkit.getMaxPlayers());
		// addScore(obj, "�6Status: �eDeathMatch");
		// if(SurvivalGames.winMap == null) {
		// addScore(obj, "�6Map: �eVoting");
		// } else {
		// addScore(obj, "�6Map: �e" + SurvivalGames.winMap.getName());
		// }
		// if(sek < 10) {
		// addScore(obj, "�6Endet in: �e" + min + "�c:�e0" + sek + " Minuten");
		// } else {
		// addScore(obj, "�6Endet in: �e" + min + "�c:�e" + sek + " Minuten");
		// }
		// addScore(obj, "�3");
		
		int min = (int) Countdown_DeathMatch.counter / 60;
		int sek = (int) Countdown_DeathMatch.counter - min * 60;
		
		for(String s : mana.getMessages("Scoreboard.DeathMatch.Lines")) {
			addScore(obj, ChatColor.translateAlternateColorCodes('&', s).replace("%ingame%", SurvivalGames.alive.size() + "").replace("%max_players%", Bukkit.getMaxPlayers() + "").replace("%map%", SurvivalGames.getWinMap().getName()).replace("%minutes%", min + "").replace("%seconds%", ((sek <= 9) ? "0" + sek : "" + sek)));
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
