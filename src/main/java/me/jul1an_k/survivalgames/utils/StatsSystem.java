package me.jul1an_k.survivalgames.utils;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class StatsSystem {
	
	public static boolean isInTable(Player p) {
		try {
			Statement st = MySQL.con.createStatement();
			
			ResultSet rs = null;
			
			rs = st.executeQuery("SELECT * FROM sSurvivalGames WHERE UUID='" + p.getUniqueId().toString() + "'");
			
			while(rs.next()) {
				if(rs != null) {
				}
				return true;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static void setKills(Player p, int kills) {
		if(MySQL.hasConnection()) {
			try {
				PreparedStatement st = null;
				if(isInTable(p)) {
					st = MySQL.con.prepareStatement("UPDATE sSurvivalGames SET Kills='" + kills + "' WHERE UUID='" + p.getUniqueId().toString() + "'");
				} else {
					st = MySQL.con.prepareStatement("INSERT INTO sSurvivalGames(UUID, Kills, Deaths) VALUES ('" + p.getUniqueId().toString() + "', '" + kills + "', '0')");
				}
				st.executeUpdate();
			} catch(SQLException e) {
				
			}
		} else {
			File f = new File("plugins/sSurvivalGames", "stats.yml");
			FileConfiguration fc = YamlConfiguration.loadConfiguration(f);
			
			fc.set(p.getUniqueId().toString() + ".Kills", kills);
			
			try {
				fc.save(f);
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void setDeaths(Player p, int deaths) {
		if(MySQL.hasConnection()) {
			try {
				PreparedStatement st = null;
				if(isInTable(p)) {
					st = MySQL.con.prepareStatement("UPDATE sSurvivalGames SET Deaths='" + deaths + "' WHERE UUID='" + p.getUniqueId().toString() + "'");
				} else {
					st = MySQL.con.prepareStatement("INSERT INTO sSurvivalGames(UUID, Kills, Deaths) VALUES ('" + p.getUniqueId().toString() + "', '0', '" + deaths + "')");
				}
				st.executeUpdate();
			} catch(SQLException e) {
				
			}
		} else {
			File f = new File("plugins/sSurvivalGames", "stats.yml");
			FileConfiguration fc = YamlConfiguration.loadConfiguration(f);
			
			fc.set(p.getUniqueId().toString() + ".Deaths", deaths);
			
			try {
				fc.save(f);
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void addKills(Player p, int kills) {
		setKills(p, getKills(p) + kills);
	}
	
	public static void addDeaths(Player p, int deaths) {
		setDeaths(p, getDeaths(p) + deaths);
	}
	
	public static int getKills(Player p) {
		if(MySQL.hasConnection()) {
			try {
				PreparedStatement st = MySQL.con.prepareStatement("SELECT * FROM sSurvivalGames WHERE UUID='" + p.getUniqueId().toString() + "'");
				ResultSet rs = st.executeQuery();
				
				while(rs.next()) {
					return rs.getInt("Kills");
				}
			} catch(SQLException e) {
				
			}
		} else {
			File f = new File("plugins/sSurvivalGames", "stats.yml");
			FileConfiguration fc = YamlConfiguration.loadConfiguration(f);
			
			return fc.getInt(p.getUniqueId().toString() + ".Kills");
		}
		return 0;
	}
	
	public static int getDeaths(Player p) {
		if(MySQL.hasConnection()) {
			try {
				PreparedStatement st = MySQL.con.prepareStatement("SELECT * FROM sSurvivalGames WHERE UUID='" + p.getUniqueId().toString() + "'");
				ResultSet rs = st.executeQuery();
				
				while(rs.next()) {
					return rs.getInt("Deaths");
				}
			} catch(SQLException e) {
				
			}
		} else {
			File f = new File("plugins/sSurvivalGames", "stats.yml");
			FileConfiguration fc = YamlConfiguration.loadConfiguration(f);
			
			return fc.getInt(p.getUniqueId().toString() + ".Deaths");
		}
		return 0;
	}
	
}
