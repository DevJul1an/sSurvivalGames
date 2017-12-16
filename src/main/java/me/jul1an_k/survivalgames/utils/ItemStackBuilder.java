package me.jul1an_k.survivalgames.utils;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class ItemStackBuilder {
	
	private Material material;
	private int amount;
	private short damage;
	
	private String displayname;
	private String[] lore;
	private ItemFlag[] itemFlags;
	
	private ISBEnchantment[] enchantments;
	
	private ItemStackBuilder() {
		this.amount = 1;
		this.damage = 0;
		this.lore = new String[0];
		this.itemFlags = new ItemFlag[0];
		this.enchantments = new ISBEnchantment[0];
	}
	
	public static ItemStackBuilder builder() {
		return new ItemStackBuilder();
	}
	
	public ItemStackBuilder material(Material material) {
		this.material = material;
		return this;
	}
	
	public ItemStackBuilder amount(int amount) {
		this.amount = amount;
		return this;
	}
	
	public ItemStackBuilder damage(short damage) {
		this.damage = damage;
		return this;
	}
	
	public ItemStackBuilder displayname(String displayname) {
		this.displayname = displayname;
		return this;
	}
	
	public ItemStackBuilder lore(String... lore) {
		this.lore = lore;
		return this;
	}
	
	public ItemStackBuilder itemflags(ItemFlag... flags) {
		this.itemFlags = flags;
		return this;
	}
	
	public ItemStackBuilder enchantment(ISBEnchantment... enchantments) {
		this.enchantments = enchantments;
		return this;
	}
	
	public ItemStack build() {
		ItemStack itemStack = new ItemStack(material, amount, damage);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(displayname);
		itemMeta.setLore(Arrays.asList(lore));
		itemMeta.addItemFlags(itemFlags);
		itemStack.setItemMeta(itemMeta);
		
		for(ISBEnchantment ench : enchantments) {
			itemStack.addUnsafeEnchantment(ench.getEnch(), ench.getLevel());
		}
		
		return itemStack;
	}
	
	public static class ISBEnchantment {
		
		private Enchantment ench;
		private int level;
		
		public ISBEnchantment(Enchantment ench, int level) {
			this.ench = ench;
			this.level = level;
		}
		
		public Enchantment getEnch() {
			return ench;
		}
		
		public int getLevel() {
			return level;
		}
		
	}
	
}
