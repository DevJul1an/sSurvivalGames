package me.jul1an_k.survivalgames.utils;

import static me.jul1an_k.survivalgames.utils.Reflections.getCBClass;
import static me.jul1an_k.survivalgames.utils.Reflections.getNMSClass;
import static me.jul1an_k.survivalgames.utils.Reflections.sendPacket;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Hologram {
	
	private Collection<Object> destroyCache;
	private Collection<Object> spawnCache;
	private List<UUID> players;
	private List<String> lines;
	private Location loc;
	
	private static final double ABS = 0.23D;
	
	/*
	 * Cache for getPacket()-Method
	 */
	private static Class<?> armorStand;
	private static Class<?> horse;
	private static Class<?> worldClass;
	private static Class<?> nmsEntity;
	private static Class<?> craftWorld;
	private static Class<?> packetClass;
	private static Class<?> entityLivingClass;
	private static Constructor<?> armorStandConstructor;
	private static Constructor<?> horseConstructor;
	
	/*
	 * Cache for getDestroyPacket()-Method
	 */
	private static Class<?> destroyPacketClass;
	private static Constructor<?> destroyPacketConstructor;
	
	static {
		
		try {
			armorStand = getNMSClass("EntityArmorStand");
			horse = getNMSClass("EntityHorse");
			worldClass = getNMSClass("World");
			nmsEntity = getNMSClass("Entity");
			craftWorld = getCBClass("CraftWorld");
			packetClass = getNMSClass("PacketPlayOutSpawnEntityLiving");
			entityLivingClass = getNMSClass("EntityLiving");
			armorStandConstructor = armorStand.getConstructor(worldClass);
			horseConstructor = horse.getConstructor(worldClass);
			
			destroyPacketClass = getNMSClass("PacketPlayOutEntityDestroy");
			destroyPacketConstructor = destroyPacketClass.getConstructor(int[].class);
		} catch(NoSuchMethodException | SecurityException ex) {
			System.err.println("Error - Classes not initialized!");
			ex.printStackTrace();
		}
	}
	
	/**
	 * Create a new hologram Note: The constructor will automatically initialize
	 * the internal cache; it may take some millis
	 * 
	 * @param loc
	 *            The location where this hologram is shown
	 * @param lines
	 *            The text-lines, from top to bottom, color codes are possible
	 */
	public Hologram(Location loc, List<String> lines) {
		this.lines = lines;
		this.loc = loc;
		this.players = new ArrayList<>();
		this.spawnCache = new HashSet<>();
		this.destroyCache = new HashSet<>();
		
		// Init
		Location displayLoc = loc.clone().add(0, (ABS * lines.size()) - 1.97D, 0);
		for(int i = 0; i < lines.size(); i++) {
			Object packet = this.getPacket(this.loc.getWorld(), displayLoc.getX(), displayLoc.getY(), displayLoc.getZ(), this.lines.get(i));
			this.spawnCache.add(packet);
			try {
				Field field = packetClass.getDeclaredField("a");
				field.setAccessible(true);
				this.destroyCache.add(this.getDestroyPacket((int) field.get(packet)));
			} catch(Exception ex) {
				ex.printStackTrace();
			}
			displayLoc.add(0, ABS * (-1), 0);
		}
	}
	
	/**
	 * Shows this hologram to the given player
	 * 
	 * @param p
	 *            The player who will see this hologram at the location
	 *            specified by calling the constructor
	 * @return true, if the action was successful, else false
	 */
	public boolean display(Player p) {
		for(Object packet : spawnCache) {
			sendPacket(p, packet);
		}

		this.players.add(p.getUniqueId());
		return true;
	}
	
	/**
	 * Removes this hologram from the players view
	 * 
	 * @param p
	 *            The target player
	 * @return true, if the action was successful, else false (including the try
	 *         to remove a non-existing hologram)
	 */
	public boolean destroy(Player p) {
		if(this.players.contains(p.getUniqueId())) {
			for(Object packet : this.destroyCache) {
				sendPacket(p, packet);
			}
			this.players.remove(p.getUniqueId());
			return true;
		}
		return false;
	}
	
	private Object getPacket(World w, double x, double y, double z, String text) {
		try {
			if(TabActionTitle.compareMinecraftVersionServerIsHigherOrEqual("1.12")) {
				Object craftWorldObj = craftWorld.cast(w);
				Method getHandleMethod = craftWorldObj.getClass().getMethod("getHandle");
				Object entityObject = armorStandConstructor.newInstance(getHandleMethod.invoke(craftWorldObj));
				Method setCustomName = entityObject.getClass().getMethod("setCustomName", String.class);
				setCustomName.invoke(entityObject, text);
				Method setCustomNameVisible = nmsEntity.getMethod("setCustomNameVisible", boolean.class);
				setCustomNameVisible.invoke(entityObject, true);
				Method setNoGravity = entityObject.getClass().getMethod("setNoGravity", boolean.class);
				setNoGravity.invoke(entityObject, true);
				Method setLocation = entityObject.getClass().getMethod("setLocation", double.class, double.class, double.class, float.class, float.class);
				setLocation.invoke(entityObject, x, y, z, 0.0F, 0.0F);
				Method setInvisible = entityObject.getClass().getMethod("setInvisible", boolean.class);
				setInvisible.invoke(entityObject, true);
				Method setSmall = entityObject.getClass().getMethod("setSmall", boolean.class);
				setSmall.invoke(entityObject, true);
				Constructor<?> cw = packetClass.getConstructor(entityLivingClass);
				return cw.newInstance(entityObject);
			} else if(TabActionTitle.compareMinecraftVersionServerIsHigherOrEqual("1.8")) {
				Object craftWorldObj = craftWorld.cast(w);
				Method getHandleMethod = craftWorldObj.getClass().getMethod("getHandle");
				Object entityObject = armorStandConstructor.newInstance(getHandleMethod.invoke(craftWorldObj));
				Method setCustomName = entityObject.getClass().getMethod("setCustomName", String.class);
				setCustomName.invoke(entityObject, text);
				Method setCustomNameVisible = nmsEntity.getMethod("setCustomNameVisible", boolean.class);
				setCustomNameVisible.invoke(entityObject, true);
				Method setGravity = entityObject.getClass().getMethod("setGravity", boolean.class);
				setGravity.invoke(entityObject, false);
				Method setLocation = entityObject.getClass().getMethod("setLocation", double.class, double.class, double.class, float.class, float.class);
				setLocation.invoke(entityObject, x, y, z, 0.0F, 0.0F);
				Method setInvisible = entityObject.getClass().getMethod("setInvisible", boolean.class);
				setInvisible.invoke(entityObject, true);
				Method setSmall = entityObject.getClass().getMethod("setSmall", boolean.class);
				setSmall.invoke(entityObject, true);
				Constructor<?> cw = packetClass.getConstructor(entityLivingClass);
				return cw.newInstance(entityObject);
			} else {
				Object craftWorldObj = craftWorld.cast(w);
				Method getHandleMethod = craftWorldObj.getClass().getMethod("getHandle");
				Object entityObject = horseConstructor.newInstance(getHandleMethod.invoke(craftWorldObj));
				Method setCustomName = entityObject.getClass().getMethod("setCustomName", String.class);
				setCustomName.invoke(entityObject, text);
				Method setCustomNameVisible = nmsEntity.getMethod("setCustomNameVisible", boolean.class);
				setCustomNameVisible.invoke(entityObject, true);
				Method setGravity = entityObject.getClass().getMethod("setGravity", boolean.class);
				setGravity.invoke(entityObject,false);
				Method setLocation = entityObject.getClass().getMethod("setLocation", double.class, double.class, double.class, float.class, float.class);
				setLocation.invoke(entityObject, x, y, z, 0.0F, 0.0F);
				Method setInvisible = entityObject.getClass().getMethod("setInvisible", boolean.class);
				setInvisible.invoke(entityObject, true);
				Method setSmall = entityObject.getClass().getMethod("setSmall", boolean.class);
				setSmall.invoke(entityObject, true);
				Constructor<?> cw = packetClass.getConstructor(entityLivingClass);
				return cw.newInstance(entityObject);
			}
		} catch(NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private Object getDestroyPacket(int... id) {
		try {
			return destroyPacketConstructor.newInstance((Object) id);
		} catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
