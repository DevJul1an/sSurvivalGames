package me.jul1an_k.survivalgames.utils;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.jul1an_k.survivalgames.SurvivalGames;

public class Voting_Map {
	
	private List<Location> locations;
	private String name;
	private int vote;
	
	public Voting_Map(String name, List<Location> locations) {
		this.locations = locations;
		this.name = name;
		vote = 0;
	}
	
	public String getName() {
		return name;
	}
	
	public List<Location> getLocations() {
		return locations;
	}
	
	public void addVote() {
		vote++;
	}
	
	public void spawnPlayers() {
		int i = 0;
		for(Player all : Bukkit.getOnlinePlayers()) {
			all.teleport(locations.get(i));
			SurvivalGames.locs.put(all.getUniqueId(), locations.get(i));
			i++;
		}
	}
	
	public void spawnPlayersDeathMatch() {
		SurvivalGames.locs.clear();
		
		int i = 0;
		for(Player all : Bukkit.getOnlinePlayers()) {
			if(!SurvivalGames.alive.contains(all.getUniqueId()))
				continue;
			
			all.teleport(locations.get(i));
			SurvivalGames.locs.put(all.getUniqueId(), locations.get(i));
			i++;
		}
	}
	
	public int getVotes() {
		return vote;
	}
	
	public int spawnSize() {
		return locations.size();
	}
	
}
