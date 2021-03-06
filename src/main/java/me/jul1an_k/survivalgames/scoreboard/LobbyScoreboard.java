package me.jul1an_k.survivalgames.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import me.jul1an_k.survivalgames.SurvivalGames;
import me.jul1an_k.survivalgames.utils.Voting_Map;
import me.jul1an_k.survivalgames.utils.manager.MessageManager;

public class LobbyScoreboard {
	
	private static MessageManager mana = SurvivalGames.getMessageManager();
	
	private static int currentScore = 0;
	
	private static void addScore(Objective obj, String name) {
		obj.getScore(name).setScore(currentScore);
		currentScore--;
	}

	public static void show(Player p) {
		currentScore = 0;
		Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
		Objective obj = board.getObjective("sgboard") != null ? board.getObjective("sgboard") : board.registerNewObjective("sgboard", "dummy", "sgboard");
		
		obj.setDisplayName(mana.getMessage("Scoreboard.Lobby.Title"));
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		for(String s : mana.getMessages("Scoreboard.Lobby.Lines")) {
			if(s.equalsIgnoreCase("%map_listing%")) {
				for(Voting_Map m : SurvivalGames.getMaps()) {
					addScore(obj, ChatColor.translateAlternateColorCodes('&', mana.getMessage("Scoreboard.Lobby.Map_Listing_Design")).replace("%map%", m.getName()).replace("%map_votes%", m.getVotes() + ""));
				}
				continue;
			}
			addScore(obj, ChatColor.translateAlternateColorCodes('&', s).replace("%ingame%", Bukkit.getOnlinePlayers().size() + "").replace("%max_players%", Bukkit.getMaxPlayers() + "").replace("%map%", ((SurvivalGames.getWinMap() == null) ? mana.getMessage("Messages.MapNotSelected") : SurvivalGames.getWinMap().getName())));
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
