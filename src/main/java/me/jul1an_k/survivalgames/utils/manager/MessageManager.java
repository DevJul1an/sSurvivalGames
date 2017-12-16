package me.jul1an_k.survivalgames.utils.manager;

import java.util.List;

import me.jul1an_k.survivalgames.SurvivalGames;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class MessageManager {
	
	private static FileConfiguration config;
	
	public MessageManager() {
		config = SurvivalGames.getCfg();
	}
	
	public String getPrefix() {
		return config.getString("Prefix");
	}
	
	public String getMessage(String path) {
		return ChatColor.translateAlternateColorCodes('&', config.getString(path).replace("%prefix%", getPrefix()));
	}
	
	public List<String> getMessages(String path) {
		return config.getStringList(path);
	}
	
	public int getInteger(String path) {
		return config.getInt(path);
	}
	
	public boolean getBoolean(String path) {
		return config.getBoolean(path);
	}
	
	public ConfigurationSection getSection(String path) {
		return config.getConfigurationSection(path);
	}
	
}
