package me.jul1an_k.survivalgames.utils.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import me.jul1an_k.survivalgames.SurvivalGames;

public class ChestManager {
	
	public Map<Location, Chest> chests = new HashMap<>();
	
	private List<ItemStack> items = new ArrayList<>();
	
	public ChestManager() {
		for(String s : SurvivalGames.getInstance().getConfig().getStringList("Chest.Items")) {
			String[] split = s.split(":");
			
			Material material = Material.valueOf(split[0].toUpperCase());
			short subid = Short.parseShort(split[1]);
			int amount = Integer.parseInt(split[2]);
			int chance = Integer.parseInt(split[3]);
			
			for(int i = 0; i < chance; i++) {
				items.add(new ItemStack(material, amount, subid));
			}
		}
	}
	
	public List<ItemStack> getItems() {
		return items;
	}
	
}
