package me.jul1an_k.survivalgames.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.server.ServerListPingEvent;

import me.jul1an_k.survivalgames.SurvivalGames;
import me.jul1an_k.survivalgames.utils.GameState;
import me.jul1an_k.survivalgames.utils.manager.MessageManager;

public class Ping_Listener implements Listener {
	
	private MessageManager mana = SurvivalGames.getMessageManager();
	
	@EventHandler
	public void onPing(ServerListPingEvent e) {
		if(SurvivalGames.getStatus() == GameState.LOBBY) {
			e.setMotd(mana.getMessage("Motd.Lobby"));
		} else if(SurvivalGames.getStatus() == GameState.WARMUP) {
			e.setMotd(mana.getMessage("Motd.WarmUp"));
		} else if(SurvivalGames.getStatus() == GameState.GRACE) {
			e.setMotd(mana.getMessage("Motd.Grace"));
		} else if(SurvivalGames.getStatus() == GameState.INGAME) {
			e.setMotd(mana.getMessage("Motd.InGame"));
		} else if(SurvivalGames.getStatus() == GameState.DEATHMATCH) {
			e.setMotd(mana.getMessage("Motd.DeathMatch"));
		} else if(SurvivalGames.getStatus() == GameState.RESTART) {
			e.setMotd(mana.getMessage("Motd.Restart"));
		}
	}
	
	@EventHandler
	public void onLogin(PlayerLoginEvent e) {
		if(SurvivalGames.getStatus() == GameState.INGAME) {
			e.disallow(Result.KICK_OTHER, mana.getMessage("Login.InGame"));
		} else if(SurvivalGames.getStatus() == GameState.GRACE) {
			e.disallow(Result.KICK_OTHER, mana.getMessage("Login.Grace"));
		} else if(SurvivalGames.getStatus() == GameState.WARMUP) {
			e.disallow(Result.KICK_OTHER, mana.getMessage("Login.WarmUp"));
		} else if(SurvivalGames.getStatus() == GameState.DEATHMATCH) {
			e.disallow(Result.KICK_OTHER, mana.getMessage("Login.DeathMatch"));
		} else if(SurvivalGames.getStatus() == GameState.RESTART) {
			e.disallow(Result.KICK_OTHER, mana.getMessage("Login.Restart"));
		}
	}
	
}
