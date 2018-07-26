package me.jul1an_k.survivalgames.mapreset;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class MapReset {
	
	private static List<String> breakedBlocks = new ArrayList<>();
	
	private static boolean enabled = false;
	
	public static void start() {
		enabled = true;
	}
	
	public static void stop() {
		enabled = false;
	}

	public static void reset() {
		System.out.println("[sSurvivalGames] Resetting " + breakedBlocks.size() + " breaked Blocks!");
		long start = System.currentTimeMillis();
		
		for(String s : breakedBlocks) {
			String[] array = s.split(";");
			
			World w = Bukkit.getWorld(array[0]);
			int x = Integer.parseInt(array[1]);
			int y = Integer.parseInt(array[2]);
			int z = Integer.parseInt(array[3]);
			Material m = Material.getMaterial(array[4].toUpperCase());
			
			Location loc = new Location(w, x, y, z);
			
			loc.getBlock().setType(m);
		}
		
		long stop = System.currentTimeMillis();
		System.out.println("[sSurvivalGames] " + breakedBlocks.size() + " Blocks have been resetted (" + (start - stop) + " ms)");
	}
	
	public static List<String> getBreakedBlocks() {
		return breakedBlocks;
	}
	
	public static boolean isEnabled() {
		return enabled;
	}
	
	public static class MapResetListener implements Listener {

		@EventHandler
		public void onPlace(BlockPlaceEvent e) {
			if(MapReset.isEnabled()) {
				Block b = e.getBlockAgainst();
				
				breakedBlocks.add(b.getLocation().getWorld().getName() + ";" + b.getLocation().getBlockX() + ";" + b.getLocation().getBlockY() + ";" + b.getLocation().getBlockZ() + ";" + b.getType().toString());
			}
		}

		@EventHandler
		public void onBreak(BlockBreakEvent e) {
			if(MapReset.isEnabled()) {
				Block b = e.getBlock();
				
				breakedBlocks.add(b.getLocation().getWorld().getName() + ";" + b.getLocation().getBlockX() + ";" + b.getLocation().getBlockY() + ";" + b.getLocation().getBlockZ() + ";" + b.getType().toString());
			}
		}

		@EventHandler
		public void onExplode(EntityExplodeEvent e) {
			if(MapReset.isEnabled()) {
				for(Block b : e.blockList()) {
					breakedBlocks.add(b.getLocation().getWorld().getName() + ";" + b.getLocation().getBlockX() + ";" + b.getLocation().getBlockY() + ";" + b.getLocation().getBlockZ() + ";" + b.getType().toString());
				}
			}
		}
		
	}
	
}
