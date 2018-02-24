package me.jul1an_k.survivalgames.utils.manager;

import java.io.File;
import java.util.*;

import me.jul1an_k.survivalgames.SurvivalGames;
import me.jul1an_k.survivalgames.utils.Voting_Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class VoteManager {
	
	public static void startVote() {
		File file = new File("plugins/sSurvivalGames", "maps.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		for(String arena : cfg.getConfigurationSection("").getKeys(false)) {
			List<Location> loclist = new ArrayList<>();
			for(String loc : cfg.getConfigurationSection(arena).getKeys(false)) {
				if(loc.equalsIgnoreCase("Name")) {
					continue;
				}
				World w = Bukkit.getWorld(cfg.getString(arena + "." + loc + ".World"));
				double x = cfg.getDouble(arena + "." + loc + ".X");
				double y = cfg.getDouble(arena + "." + loc + ".Y");
				double z = cfg.getDouble(arena + "." + loc + ".Z");
				float yaw = (float) cfg.getDouble(arena + "." + loc + ".Yaw");
				float pitch = (float) cfg.getDouble(arena + "." + loc + ".Pitch");
				Location location = new Location(w, x, y, z, yaw, pitch);
				loclist.add(location);
			}
			Voting_Map m = new Voting_Map(arena, loclist);
			SurvivalGames.addMap(m);
			System.out.println("[sSurvivalGames] Map " + arena + " added (" + loclist.size() + " Spawns)");
		}
	}
	
	public static void endVote() {
		Collection<Voting_Map> maps = SurvivalGames.getMaps();
		Voting_Map m;
		int votes = -1;

		for(Voting_Map map : maps) {
			if(map.getVotes() > votes) {
				votes = map.getVotes();
			}
		}

		List<Voting_Map> ms = new ArrayList<>();

		for(Voting_Map map : maps) {
			if(map.getVotes() == votes) {
				ms.add(map);
			}
		}
		
		if(ms.size() <= 1) {
			m = ms.get(0);
		} else {
			Random ran = new Random();
			int gn = ran.nextInt(ms.size());
			m = ms.get(gn);
		}
		
		if(m != null) SurvivalGames.setWinMap(m);
	}
	
}
