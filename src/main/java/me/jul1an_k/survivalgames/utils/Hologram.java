package me.jul1an_k.survivalgames.utils;

import static me.jul1an_k.survivalgames.utils.Reflections.getCBClass;
import static me.jul1an_k.survivalgames.utils.Reflections.getNMSClass;
import static me.jul1an_k.survivalgames.utils.Reflections.sendPacket;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Hologram {
	
	private List<Object> destroyCache;
	private List<Object> spawnCache;
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
			armorStandConstructor = armorStand.getConstructor(new Class[] { worldClass });
			horseConstructor = horse.getConstructor(new Class[] { worldClass });
			
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
	 *            The text-lines, from top to bottom, farbcodes are possible
	 */
	public Hologram(Location loc, String... lines) {
		this(loc, Arrays.asList(lines));
	}
	
	/**
	 * Create a new hologram Note: The constructor will automatically initialize
	 * the internal cache; it may take some millis
	 * 
	 * @param loc
	 *            The location where this hologram is shown
	 * @param lines
	 *            The text-lines, from top to bottom, farbcodes are possible
	 */
	public Hologram(Location loc, List<String> lines) {
		this.lines = lines;
		this.loc = loc;
		this.players = new ArrayList<>();
		this.spawnCache = new ArrayList<>();
		this.destroyCache = new ArrayList<>();
		
		// Init
		Location displayLoc = loc.clone().add(0, (ABS * lines.size()) - 1.97D, 0);
		for(int i = 0; i < lines.size(); i++) {
			Object packet = this.getPacket(this.loc.getWorld(), displayLoc.getX(), displayLoc.getY(), displayLoc.getZ(), this.lines.get(i));
			this.spawnCache.add(packet);
			try {
				Field field = packetClass.getDeclaredField("a");
				field.setAccessible(true);
				this.destroyCache.add(this.getDestroyPacket(new int[] { (int) field.get(packet) }));
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
		for(int i = 0; i < spawnCache.size(); i++) {
			sendPacket(p, spawnCache.get(i));
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
			for(int i = 0; i < this.destroyCache.size(); i++) {
				sendPacket(p, this.destroyCache.get(i));
			}
			this.players.remove(p.getUniqueId());
			return true;
		}
		return false;
	}
	
	private Object getPacket(World w, double x, double y, double z, String text) {
		try {
			if(TabActionTitle.compareMinecraftVersionServerIsHigherOrEqual("1.8")) {
				Object craftWorldObj = craftWorld.cast(w);
				Method getHandleMethod = craftWorldObj.getClass().getMethod("getHandle", new Class<?>[0]);
				Object entityObject = armorStandConstructor.newInstance(new Object[] { getHandleMethod.invoke(craftWorldObj, new Object[0]) });
				Method setCustomName = entityObject.getClass().getMethod("setCustomName", new Class<?>[] { String.class });
				setCustomName.invoke(entityObject, new Object[] { text });
				Method setCustomNameVisible = nmsEntity.getMethod("setCustomNameVisible", new Class[] { boolean.class });
				setCustomNameVisible.invoke(entityObject, new Object[] { true });
				try {
					Method setGravity = entityObject.getClass().getMethod("setGravity", new Class<?>[] { boolean.class });
					setGravity.invoke(entityObject, new Object[] { false });
				} catch(Exception ex) {
					
				}
				Method setLocation = entityObject.getClass().getMethod("setLocation", new Class<?>[] { double.class, double.class, double.class, float.class, float.class });
				setLocation.invoke(entityObject, new Object[] { x, y, z, 0.0F, 0.0F });
				Method setInvisible = entityObject.getClass().getMethod("setInvisible", new Class<?>[] { boolean.class });
				setInvisible.invoke(entityObject, new Object[] { true });
				Method setSmall = entityObject.getClass().getMethod("setSmall", new Class<?>[] { boolean.class });
				setSmall.invoke(entityObject, new Object[] { true });
				Constructor<?> cw = packetClass.getConstructor(new Class<?>[] { entityLivingClass });
				Object packetObject = cw.newInstance(new Object[] { entityObject });
				return packetObject;
			} else {
				Object craftWorldObj = craftWorld.cast(w);
				Method getHandleMethod = craftWorldObj.getClass().getMethod("getHandle", new Class<?>[0]);
				Object entityObject = horseConstructor.newInstance(new Object[] { getHandleMethod.invoke(craftWorldObj, new Object[0]) });
				Method setCustomName = entityObject.getClass().getMethod("setCustomName", new Class<?>[] { String.class });
				setCustomName.invoke(entityObject, new Object[] { text });
				Method setCustomNameVisible = nmsEntity.getMethod("setCustomNameVisible", new Class[] { boolean.class });
				setCustomNameVisible.invoke(entityObject, new Object[] { true });
				Method setLocation = entityObject.getClass().getMethod("setLocation", new Class<?>[] { double.class, double.class, double.class, float.class, float.class });
				setLocation.invoke(entityObject, new Object[] { x, y, z, 0.0F, 0.0F });
				Method setInvisible = entityObject.getClass().getMethod("setInvisible", new Class<?>[] { boolean.class });
				setInvisible.invoke(entityObject, new Object[] { true });
				Constructor<?> cw = packetClass.getConstructor(new Class<?>[] { entityLivingClass });
				Object packetObject = cw.newInstance(new Object[] { entityObject });
				
				return packetObject;
			}
		} catch(NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private Object getDestroyPacket(int... id) {
		try {
			return destroyPacketConstructor.newInstance(id);
		} catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
