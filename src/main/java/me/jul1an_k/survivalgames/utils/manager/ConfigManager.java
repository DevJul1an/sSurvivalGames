package me.jul1an_k.survivalgames.utils.manager;

import java.io.File;

import me.jul1an_k.survivalgames.SurvivalGames;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigManager {
	
	public static void load() {
		SurvivalGames.getInstance().saveDefaultConfig();
	}
	
	public static FileConfiguration getFileConfiguration() {
		return YamlConfiguration.loadConfiguration(getFile());
	}
	
	public static File getFile() {
		return new File(SurvivalGames.getInstance().getDataFolder(), "config.yml");
	}
	
}
