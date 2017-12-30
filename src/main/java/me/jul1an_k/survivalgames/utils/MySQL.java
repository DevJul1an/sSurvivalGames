package me.jul1an_k.survivalgames.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.TimeZone;

import org.bukkit.Bukkit;

public class MySQL {
	
	public static Connection con;
	
	public static Connection connect(String host, int port, String database, String user, String password) {
		try {
			con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true&serverTimezone=" + TimeZone.getDefault().getID(), user, password);
			Bukkit.getConsoleSender().sendMessage("§aConnected with MySQL");
		} catch(SQLException e) {
			Bukkit.getConsoleSender().sendMessage("§4Error while connecting with MySQL");
		}
		
		return con;
	}
	
	public static void disconnect() {
		try {
			con.close();
			Bukkit.getConsoleSender().sendMessage("§aDisconnected from MySQL");
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean hasConnection() {
		if(con != null) {
			return true;
		}
		return false;
	}
	
}
