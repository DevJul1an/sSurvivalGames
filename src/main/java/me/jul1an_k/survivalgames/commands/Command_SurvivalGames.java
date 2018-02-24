package me.jul1an_k.survivalgames.commands;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.UnknownDependencyException;

import me.jul1an_k.survivalgames.SurvivalGames;
import me.jul1an_k.survivalgames.countdown.Countdown_Lobby;
import me.jul1an_k.survivalgames.update.FileUpdate;
import me.jul1an_k.survivalgames.utils.ItemStackBuilder;
import me.jul1an_k.survivalgames.utils.Voting_Map;
import me.jul1an_k.survivalgames.utils.MySQL;
import me.jul1an_k.survivalgames.utils.PluginReloader;
import me.jul1an_k.survivalgames.utils.manager.ConsoleManger;
import me.jul1an_k.survivalgames.utils.manager.MessageManager;

public class Command_SurvivalGames implements CommandExecutor {
	
	private MessageManager mana = SurvivalGames.getMessageManager();
	
	public static Collection<UUID> voted = new HashSet<>();
	
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if(args.length == 0) {
			sendHelp(cs);
		} else if(args.length == 1) {
			if(args[0].equalsIgnoreCase("SetHologram")) {
				if(!(cs instanceof Player)) {
					System.err.println("You must be a player.");
					return true;
				}
				if(cs.hasPermission("SurvivalGames.SetHologram")) {
					Player p = (Player) cs;
					
					File f = new File("plugins/sSurvivalGames", "location.yml");
					FileConfiguration fc = YamlConfiguration.loadConfiguration(f);
					
					fc.set("Hologram.World", p.getLocation().getWorld().getName());
					fc.set("Hologram.X", p.getLocation().getX());
					fc.set("Hologram.Y", p.getLocation().getY());
					fc.set("Hologram.Z", p.getLocation().getZ());
					try {
						fc.save(f);
					} catch(IOException e) {
						e.printStackTrace();
					}
					p.sendMessage(mana.getMessage("Messages.SetHologram"));
				} else {
					cs.sendMessage(mana.getMessage("Messages.NoPermission"));
				}
			} else if(args[0].equalsIgnoreCase("SetLobby")) {
				if(!(cs instanceof Player)) {
					System.err.println("You must be a player.");
					return true;
				}
				if(cs.hasPermission("SurvivalGames.SetLobby")) {
					Player p = (Player) cs;
					
					File f = new File("plugins/sSurvivalGames", "location.yml");
					FileConfiguration fc = YamlConfiguration.loadConfiguration(f);
					
					fc.set("Lobby.World", p.getLocation().getWorld().getName());
					fc.set("Lobby.X", p.getLocation().getX());
					fc.set("Lobby.Y", p.getLocation().getY());
					fc.set("Lobby.Z", p.getLocation().getZ());
					fc.set("Lobby.Yaw", p.getLocation().getYaw());
					fc.set("Lobby.Pitch", p.getLocation().getPitch());
					try {
						fc.save(f);
					} catch(IOException e) {
						e.printStackTrace();
					}
					p.sendMessage(mana.getMessage("Messages.SetLobby"));
				} else {
					cs.sendMessage(mana.getMessage("Messages.NoPermission"));
				}
			} else if(args[0].equalsIgnoreCase("Start")) {
				if(cs.hasPermission("SurvivalGames.Start")) {
					if(Bukkit.getOnlinePlayers().size() >= 2 && Countdown_Lobby.lobbyCounter > -1) {
						Countdown_Lobby.lobbyCounter = 16;
						cs.sendMessage(mana.getMessage("Messages.Start"));
					}
				} else {
					cs.sendMessage(mana.getMessage("Messages.NoPermission"));
				}
			} else if(args[0].equalsIgnoreCase("Reload")) {
				if(cs.hasPermission("SurvivalGames.Reload")) {
					try {
						PluginReloader.reload();
					} catch(UnknownDependencyException | NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException | InvalidPluginException | InvalidDescriptionException e) {
						e.printStackTrace();
					}
					cs.sendMessage(mana.getMessage("Messages.Reloaded"));
				} else {
					cs.sendMessage(mana.getMessage("Messages.NoPermission"));
				}
			} else if(args[0].equalsIgnoreCase("ImportDataToSQL")) {
				if(cs.hasPermission("SurvivalGames.ImportDataToSQL")) {
					if(MySQL.hasConnection()) {
						File f = new File("plugins/sSurvivalGames", "stats.yml");
						if(f.exists()) {
							FileConfiguration fc = YamlConfiguration.loadConfiguration(f);
							
							for(String uuid : fc.getConfigurationSection("").getKeys(false)) {
								int deaths = fc.getInt(uuid + ".Deaths");
								int kills = fc.getInt(uuid + ".Kills");
								
								try {
									PreparedStatement st = MySQL.con.prepareStatement("INSERT INTO sSurvivalGames(UUID, Kills, Deaths) VALUES ('" + uuid + "', '" + kills + "', '" + deaths + "')");
									st.executeUpdate();
								} catch(SQLException e) {
									ConsoleManger.sendStacktrace("ImportDataToSQL", e);
								}
							}
							
							cs.sendMessage(ChatColor.translateAlternateColorCodes('&', mana.getPrefix()) + "§aStats successfully imported to MySQL.");
						} else {
							cs.sendMessage(ChatColor.translateAlternateColorCodes('&', mana.getPrefix()) + "§cThere is no stats file.");
						}
					} else {
						cs.sendMessage(ChatColor.translateAlternateColorCodes('&', mana.getPrefix()) + "§cThere is no MySQL Connection.");
					}
				}
			} else if(args[0].equalsIgnoreCase("Update")) {
				if(cs.hasPermission("SurvivalGames.Update")) {
					FileUpdate fu = new FileUpdate();
					fu.downloadUpdate(cs);
				} else {
					cs.sendMessage(mana.getMessage("Messages.NoPermission"));
				}
			} else if(args[0].equalsIgnoreCase("Vote")) {
				if(voted.contains(((Player) cs).getUniqueId()))
					return true;
				
				Inventory inv = Bukkit.createInventory(null, 9, mana.getMessage("Vote.InventoryName"));
				
				for(Voting_Map m : SurvivalGames.getMaps()) {
					inv.addItem(ItemStackBuilder.builder().material(Material.MAP).displayname("§c" + m.getName()).lore("§6Votes: §c" + m.getVotes()).build());
				}
				
				((Player) cs).openInventory(inv);
			} else {
				sendHelp(cs);
			}
		} else if(args.length == 2) {
			if(args[0].equalsIgnoreCase("CreateMap")) {
				if(cs.hasPermission("SurvivalGames.CreateMap")) {
					File file = new File("plugins/sSurvivalGames", "maps.yml");
					FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
					String MapName = args[1];
					if(!(cfg.contains(MapName + ".Name"))) {
						cfg.set(MapName + ".Name", MapName);
						try {
							cfg.save(file);
						} catch(IOException e) {
							e.printStackTrace();
						}
						cs.sendMessage(mana.getMessage("Messages.CreateMap").replace("%map%", MapName));
					} else {
						cs.sendMessage(mana.getMessage("Messages.CreateMapFailed"));
					}
				} else {
					cs.sendMessage(mana.getMessage("Messages.NoPermission"));
				}
			} else if(args[0].equalsIgnoreCase("ForceMap")) {
				if(cs.hasPermission("SurvivalGames.ForceMap")) {
					int vote;
					if(SurvivalGames.isVoting()) {
						try {
							vote = Integer.parseInt(args[1]);
						} catch(NumberFormatException e) {
							e.printStackTrace();
							cs.sendMessage(mana.getMessage("Messages.ForceMapNotANumber"));
							return true;
						}
						
						if(vote > 0 && vote <= SurvivalGames.getMaps().size()) {
							vote--;
							Voting_Map m = SurvivalGames.getMaps().get(vote);
							if(m.getVotes() >= 999) {
								cs.sendMessage(mana.getMessage("Messages.ForceMapAlreadyForced"));
								return true;
							}
							for(int i = 0; i < 999; i++) {
								if(!(m.getVotes() == 999)) {
									m.addVote();
								}
							}
							cs.sendMessage(mana.getMessage("Messages.ForceMap"));
						} else {
							cs.sendMessage(mana.getMessage("ForceMapNumberToHigh"));
						}
						
					}
				} else {
					cs.sendMessage(mana.getMessage("Messages.NoPermission"));
				}
			} else {
				sendHelp(cs);
			}
		} else if(args.length == 3) {
			if(args[0].equalsIgnoreCase("SetSpawn")) {
				if(!(cs instanceof Player)) {
					System.err.println(ChatColor.translateAlternateColorCodes('&', mana.getPrefix()) + "Die Konsole kann den Spawn nicht setzen.");
					return true;
				}
				if(cs.hasPermission("SurvivalGames.SetSpawn")) {
					Player p = (Player) cs;
					File file = new File("plugins/sSurvivalGames", "maps.yml");
					FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
					int number = 0;
					String MapName = args[1];
					
					if(cfg.contains(MapName + ".Name")) {
						
						try {
							number = Integer.parseInt(args[2]);
							if(number < 0 || number > 25) {
								p.sendMessage(mana.getMessage("Messages.SetSpawnNumberToLowOrToHigh"));
								return true;
							}
						} catch(NumberFormatException e) {
							p.sendMessage(mana.getMessage("Messages.SetSpawnNotANumber"));
						}
						
						cfg.set(MapName + "." + number + ".World", p.getLocation().getWorld().getName());
						cfg.set(MapName + "." + number + ".X", p.getLocation().getX());
						cfg.set(MapName + "." + number + ".Y", p.getLocation().getY());
						cfg.set(MapName + "." + number + ".Z", p.getLocation().getZ());
						cfg.set(MapName + "." + number + ".Yaw", p.getLocation().getYaw());
						cfg.set(MapName + "." + number + ".Pitch", p.getLocation().getPitch());
						
						try {
							cfg.save(file);
						} catch(IOException e) {
							e.printStackTrace();
						}
						p.sendMessage(mana.getMessage("Messages.SetSpawn").replace("%spawn%", number + "").replace("%map%", MapName));
					}
				} else {
					cs.sendMessage(mana.getMessage("Messages.NoPermission"));
				}
			}
		} else {
			sendHelp(cs);
		}
		return true;
	}
	
	private void sendHelp(CommandSender cs) {
		cs.sendMessage(ChatColor.translateAlternateColorCodes('&', mana.getPrefix()) + "§3/SurvivalGames Vote §f- §7Vote for a Map.");
		cs.sendMessage(ChatColor.translateAlternateColorCodes('&', mana.getPrefix()) + "§3/SurvivalGames Start §f- §7Starts the Game.");
		cs.sendMessage(ChatColor.translateAlternateColorCodes('&', mana.getPrefix()) + "§3/SurvivalGames SetHologram §f- §7Sets the Stats hologram.");
		cs.sendMessage(ChatColor.translateAlternateColorCodes('&', mana.getPrefix()) + "§3/SurvivalGames SetLobby §f- §7Sets the Lobby position.");
		cs.sendMessage(ChatColor.translateAlternateColorCodes('&', mana.getPrefix()) + "§3/SurvivalGames CreateMap <Name> §f- §7Creates a Map with the name <Name>.");
		cs.sendMessage(ChatColor.translateAlternateColorCodes('&', mana.getPrefix()) + "§3/SurvivalGames ForceMap <ID> §f- §7Forced the Map <ID>.");
		cs.sendMessage(ChatColor.translateAlternateColorCodes('&', mana.getPrefix()) + "§3/SurvivalGames SetSpawn <Map> <Spawn> §f- §7Sets Spawn No. <Spawn> In Map <Map>.");
		cs.sendMessage(ChatColor.translateAlternateColorCodes('&', mana.getPrefix()) + "§3/SurvivalGames Reload §f- §7Reloads the plugin.");
		cs.sendMessage(ChatColor.translateAlternateColorCodes('&', mana.getPrefix()) + "§3/SurvivalGames ImportDataToSQL §f- §7Imports the Stats to MySQL");
	}
	
}
