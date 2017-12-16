package me.jul1an_k.survivalgames.utils;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Reflections {
	
	public static void sendPacket(Player p, Object packet) {
		try {
			Object nmsPlayer = getNMSPlayer(p);
			Object connection = nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);
			connection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(connection, packet);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void sendPacket(Player p, String packetName, Class<?>[] parameterclass, Object... parameters) {
		try {
			Object nmsPlayer = getNMSPlayer(p);
			Object connection = nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);
			Object packet = Class.forName(nmsPlayer.getClass().getPackage().getName() + "." + packetName).getConstructor(parameterclass).newInstance(parameters);
			connection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(connection, packet);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getVersion() {
		String name = Bukkit.getServer().getClass().getPackage().getName();
		return name.substring(name.lastIndexOf('.') + 1) + ".";
	}
	
	public static Class<?> getNMSClass(String className) {
		String fullName = "net.minecraft.server." + getVersion() + className;
		Class<?> clazz = null;
		try {
			clazz = Class.forName(fullName);
		} catch(ClassNotFoundException e) {
			System.err.println("[sSurvivalGames] Can't find the Class '" + fullName + "'!");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return clazz;
	}
	
	public static Class<?> getCBClass(String className) {
		String fullName = "org.bukkit.craftbukkit." + getVersion() + className;
		Class<?> clazz = null;
		try {
			clazz = Class.forName(fullName);
		} catch(ClassNotFoundException e) {
			System.err.println("[sSurvivalGames] Can't find the Class '" + fullName + "'!");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return clazz;
	}
	
	public static Class<?> getProtocolInjectorClass(String className) {
		String fullName = "org.spigotmc.ProtocolInjector$" + className;
		Class<?> clazz = null;
		try {
			clazz = Class.forName(fullName);
		} catch(ClassNotFoundException e) {
			System.err.println("[sSurvivalGames] Can't find the Class '" + fullName + "'!");
		}
		return clazz;
	}
	
	public static Object getNMSPlayer(Player p) {
		try {
			return p.getClass().getMethod("getHandle").invoke(p);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Field getField(Field f) {
		f.setAccessible(true);
		return f;
	}
	
}
