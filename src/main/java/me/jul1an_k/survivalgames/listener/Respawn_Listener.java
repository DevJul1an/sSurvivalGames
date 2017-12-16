package me.jul1an_k.survivalgames.listener;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import me.jul1an_k.survivalgames.SurvivalGames;
import me.jul1an_k.survivalgames.utils.GameState;
import me.jul1an_k.survivalgames.utils.manager.MessageManager;

public class Respawn_Listener implements Listener {
	
	private MessageManager mana = SurvivalGames.getMessageManager();
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		Player p = e.getPlayer();
		
		if(SurvivalGames.getStatus() == GameState.LOBBY) {
			File f = new File("plugins/SurvivalGames", "location.yml");
			FileConfiguration fc = YamlConfiguration.loadConfiguration(f);
			
			e.setRespawnLocation(new Location(Bukkit.getWorld(fc.getString("Lobby.World")), fc.getDouble("Lobby.X"), fc.getDouble("Lobby.Y"), fc.getDouble("Lobby.Z"), (float) fc.getDouble("Lobby.Yaw"), (float) fc.getDouble("Lobby.Pitch")));
		} else {
			p.setGameMode(GameMode.SPECTATOR);
			
			p.sendMessage(mana.getMessage("YouAreNowSpectator"));
			
			e.setRespawnLocation(SurvivalGames.getWinMap().getLocations().get(0));
		}
	}
	
}
