package me.jul1an_k.survivalgames.utils;

import static me.jul1an_k.survivalgames.utils.Reflections.getField;
import static me.jul1an_k.survivalgames.utils.Reflections.getNMSClass;
import static me.jul1an_k.survivalgames.utils.Reflections.getProtocolInjectorClass;
import static me.jul1an_k.survivalgames.utils.Reflections.sendPacket;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TabActionTitle {
	
	private static Class<?> chatserial;
	private static Class<?> title;
	private static Class<?> enumtitleaction;
	
	static {
		if(compareMinecraftVersionServerIsHigherOrEqual("1.8.3")) {
			chatserial = getNMSClass("IChatBaseComponent$ChatSerializer");
			title = getNMSClass("PacketPlayOutTitle");
			enumtitleaction = getNMSClass("PacketPlayOutTitle$EnumTitleAction");
		} else if(compareMinecraftVersionServerIsHigherOrEqual("1.8")) {
			chatserial = getNMSClass("ChatSerializer");
			title = getNMSClass("PacketPlayOutTitle");
			enumtitleaction = getNMSClass("EnumTitleAction");
		} else if(compareMinecraftVersionServerIsHigherOrEqual("1.7.10")) {
			chatserial = getNMSClass("ChatSerializer");
			title = getProtocolInjectorClass("PacketTitle");
			enumtitleaction = getProtocolInjectorClass("PacketTitle$Action");
		}
	}
	
	public static void sendTablist(Player p, String header, String footer) {
		try {
			if(compareMinecraftVersionServerIsHigherOrEqual("1.8")) {
				Object packet = getNMSClass("PacketPlayOutPlayerListHeaderFooter").newInstance();
				getField(packet.getClass().getDeclaredField("a")).set(packet, chatserial.getMethod("a", String.class).invoke(null, "{'text': '" + header + "'}"));
				getField(packet.getClass().getDeclaredField("b")).set(packet, chatserial.getMethod("a", String.class).invoke(null, "{'text': '" + footer + "'}"));
				sendPacket(p, packet);
			} else if(compareMinecraftVersionServerIsHigherOrEqual("1.7.10")) {
				Object packet = getProtocolInjectorClass("PacketTabHeader").newInstance();
				getField(packet.getClass().getDeclaredField("a")).set(packet, chatserial.getMethod("a", String.class).invoke(null, "{'text': '" + header + "'}"));
				getField(packet.getClass().getDeclaredField("b")).set(packet, chatserial.getMethod("a", String.class).invoke(null, "{'text': '" + footer + "'}"));
				sendPacket(p, packet);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void sendActionBar(Player p, String message) {
		if(message == null) {
			message = "";
		}
		
		try {
			
			if(compareMinecraftVersionServerIsHigherOrEqual("1.12")) {
				sendPacket(p, "PacketPlayOutChat", new Class[] { getNMSClass("IChatBaseComponent"), getNMSClass("ChatMessageType") }, chatserial.getMethod("a", String.class).invoke(null, "{\"text\": \"" + message + "\"}"), getNMSClass("ChatMessageType").getMethod("a", byte.class).invoke(null, (byte) 2));
			} else if(compareMinecraftVersionServerIsHigherOrEqual("1.8")) {
				sendPacket(p, "PacketPlayOutChat", new Class[] { getNMSClass("IChatBaseComponent"), byte.class }, chatserial.getMethod("a", String.class).invoke(null, "{\"text\": \"" + message + "\"}"), (byte) 2);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void sendTitle(Player p, String title, String subtitle, int fadein, int stay, int fadeout, boolean clear, boolean reset) {
		
		try {
			
			if(compareMinecraftVersionServerIsHigherOrEqual("1.8")) {
				Object t = TabActionTitle.title.newInstance();
				Field f = t.getClass().getDeclaredField("a");
				f.setAccessible(true);
				f.set(t, getField(enumtitleaction.getDeclaredField("TITLE")).get(null));
				f = t.getClass().getDeclaredField("b");
				f.setAccessible(true);
				f.set(t, chatserial.getMethod("a", String.class).invoke(null, "{'text': '" + title + "'}"));
				sendPacket(p, t);
				
				t = TabActionTitle.title.newInstance();
				f = t.getClass().getDeclaredField("a");
				f.setAccessible(true);
				f.set(t, getField(enumtitleaction.getDeclaredField("SUBTITLE")).get(null));
				f = t.getClass().getDeclaredField("b");
				f.setAccessible(true);
				f.set(t, chatserial.getMethod("a", String.class).invoke(null, "{'text': '" + subtitle + "'}"));
				sendPacket(p, t);
				
				t = TabActionTitle.title.newInstance();
				f = t.getClass().getDeclaredField("a");
				f.setAccessible(true);
				f.set(t, getField(enumtitleaction.getDeclaredField("TIMES")).get(null));
				f = t.getClass().getDeclaredField("c");
				f.setAccessible(true);
				f.set(t, fadein);
				f = t.getClass().getDeclaredField("d");
				f.setAccessible(true);
				f.set(t, stay);
				f = t.getClass().getDeclaredField("e");
				f.setAccessible(true);
				f.set(t, fadeout);
				sendPacket(p, t);
				
				if(clear == true) {
					t = TabActionTitle.title.newInstance();
					f = t.getClass().getDeclaredField("a");
					f.setAccessible(true);
					f.set(t, getField(enumtitleaction.getDeclaredField("CLEAR")).get(null));
					sendPacket(p, t);
				}
				
				if(reset == true) {
					t = TabActionTitle.title.newInstance();
					f = t.getClass().getDeclaredField("a");
					f.setAccessible(true);
					f.set(t, getField(enumtitleaction.getDeclaredField("RESET")).get(null));
					sendPacket(p, t);
				}
			} else if(compareMinecraftVersionServerIsHigherOrEqual("1.7.10")) {
				Object t = TabActionTitle.title.newInstance();
				Field f = t.getClass().getDeclaredField("a");
				f.setAccessible(true);
				f.set(t, getField(enumtitleaction.getDeclaredField("TITLE")).get(null));
				f = t.getClass().getDeclaredField("b");
				f.setAccessible(true);
				f.set(t, chatserial.getMethod("a", String.class).invoke(null, "{'text': '" + title + "'}"));
				sendPacket(p, t);
				
				f.setAccessible(true);
				f.set(t, getField(enumtitleaction.getDeclaredField("SUBTITLE")).get(null));
				f = t.getClass().getDeclaredField("b");
				f.setAccessible(true);
				f.set(t, chatserial.getMethod("a", String.class).invoke(null, "{'text': '" + subtitle + "'}"));
				sendPacket(p, t);
				
				f.setAccessible(true);
				f.set(t, getField(enumtitleaction.getDeclaredField("TIMES")).get(null));
				f = t.getClass().getDeclaredField("c");
				f.setAccessible(true);
				f.set(t, fadein);
				f = t.getClass().getDeclaredField("d");
				f.setAccessible(true);
				f.set(t, stay);
				f = t.getClass().getDeclaredField("e");
				f.setAccessible(true);
				f.set(t, fadeout);
				sendPacket(p, t);
				
				if(clear == true) {
					f.setAccessible(true);
					f.set(t, getField(enumtitleaction.getDeclaredField("CLEAR")).get(null));
					sendPacket(p, t);
				}
				
				if(reset == true) {
					f.setAccessible(true);
					f.set(t, getField(enumtitleaction.getDeclaredField("RESET")).get(null));
					sendPacket(p, t);
				}
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean compareMinecraftVersionServerIsHigherOrEqual(String version) {
		String serverVersion = Bukkit.getVersion();
		serverVersion = serverVersion.substring(serverVersion.indexOf("(MC: ") + 5, serverVersion.length());
		serverVersion = serverVersion.substring(0, serverVersion.lastIndexOf(")"));
		String[] serverVersionArray = serverVersion.split("\\.");
		String[] toCompareVersionArray = version.split("\\.");
		
		if(serverVersionArray.length == 2) {
			int serverFirst = Integer.valueOf(serverVersionArray[0]);
			int toCompareFirst = Integer.valueOf(toCompareVersionArray[0]);
			if(toCompareFirst != serverFirst) {
				return toCompareFirst < serverFirst;
			}
			int serverSecond = Integer.valueOf(serverVersionArray[1]);
			int toCompareSecond = Integer.valueOf(toCompareVersionArray[1]);
			if(toCompareSecond != serverSecond) {
				return toCompareSecond < serverSecond;
			}
			if(toCompareVersionArray.length == 3) {
				return false;
			}
			return true;
		}
		if(serverVersionArray.length == 3) {
			int serverFirst = Integer.valueOf(serverVersionArray[0]);
			int toCompareFirst = Integer.valueOf(toCompareVersionArray[0]);
			if(toCompareFirst != serverFirst) {
				return toCompareFirst < serverFirst;
			}
			int serverSecond = Integer.valueOf(serverVersionArray[1]);
			int toCompareSecond = Integer.valueOf(toCompareVersionArray[1]);
			if(toCompareSecond != serverSecond) {
				return toCompareSecond < serverSecond;
			}
			if(toCompareVersionArray.length != 3) {
				return true;
			}
			int serverThird = Integer.valueOf(serverVersionArray[2]);
			int toCompareThird = Integer.valueOf(toCompareVersionArray[2]);
			if(toCompareThird != serverThird) {
				return toCompareThird < serverThird;
			}
			return true;
		}
		return false;
	}
	
}
