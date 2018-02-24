package me.jul1an_k.survivalgames.update;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.UnknownDependencyException;

import me.jul1an_k.survivalgames.SurvivalGames;

public class FileUpdate {
	
	public void check() {
		try {
			String newVersion = getLine("http://mc.jumpy91.de/Jul1anUpdater/sSurvivalGamesUpdate.txt");
			String oldVersion = SurvivalGames.getInstance().getDescription().getVersion();
			System.out.println("[sSurvivalGames-AutoUpdater] Newest Version: " + newVersion + "! Current Version: " + oldVersion);
			if(!newVersion.equals(oldVersion)) {
				String changesString = getLine("http://mc.jumpy91.de/Jul1anUpdater/sSurvivalGamesChanges.txt");
				System.out.println("[sSurvivalGames-AutoUpdater] New in this version: " + changesString);
				for(Player all : Bukkit.getOnlinePlayers()) {
					if(all.hasPermission("sSurvivalGames.Update")) {
						all.sendMessage("§aAn Update for sSurvivalGames is available!");
						all.sendMessage("§cCurrent Version: " + oldVersion);
						all.sendMessage("§2New Version: " + newVersion);
						all.sendMessage("§3New in this version: " + changesString);
						all.sendMessage("§aDownloading update...");
					}
				}
				downloadUpdate(Bukkit.getConsoleSender());
				for(Player all : Bukkit.getOnlinePlayers()) {
					if(all.hasPermission("sSurvivalGames.Update")) {
						all.sendMessage("§aDownloaded update!");
					}
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private String getLine(String URL) throws IOException {
		URL site = new URL(URL);
		URLConnection conn = site.openConnection();
		conn.addRequestProperty("User-Agent", "Chrome/52.0");
		
		InputStreamReader isr = new InputStreamReader(conn.getInputStream());
		BufferedReader br = new BufferedReader(isr);
		
		return br.readLine();
	}
	
	private String getLink() {
		return "http://mc.jumpy91.de/Jul1anUpdater/sSurvivalGames.jar";
	}
	
	public boolean downloadUpdate(final CommandSender sender) {
		String path = null;
		
		path = getLink();
		
		boolean success = false;
		String pluginPath = "plugins/" + SurvivalGames.getInstance().getDescription().getName() + ".jar";
		
		if(path != null) {
			try {
				URL site = new URL(path);
				URLConnection conn = site.openConnection();
				conn.addRequestProperty("User-Agent", "Chrome/52.0");
				
				InputStream is = new BufferedInputStream(conn.getInputStream());
				OutputStream os = new BufferedOutputStream(new FileOutputStream(pluginPath));
				
				byte[] chunk = new byte[1024];
				int chunkSize;
				int count = -1;
				while((chunkSize = is.read(chunk)) != -1) {
					os.write(chunk, 0, chunkSize);
					count++;
				}
				os.flush(); // Necessary for Java < 6
				os.close();
				is.close();
				
				sender.sendMessage(ChatColor.GREEN + "Downloading...");
				
				sender.sendMessage(ChatColor.GREEN + "Plugin downloaded! (" + count / 1024 + "KB)");
				sender.sendMessage(ChatColor.GREEN + "Reloading plugin...");
				
				reload();
				
				sender.sendMessage(ChatColor.GREEN + "Update successfully.");
				success = true;
				
			} catch(Exception e) {
				sender.sendMessage(ChatColor.RED + "Failed to update: " + e.getMessage());
			}
			
		}
		
		return success;
	}
	
	@SuppressWarnings("unchecked")
	private void reload() throws UnknownDependencyException, InvalidPluginException, InvalidDescriptionException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		PluginManager manager = SurvivalGames.getInstance().getServer().getPluginManager();
		SimplePluginManager spmanager = (SimplePluginManager) manager;
		// unload
		Plugin pluginunload = manager.getPlugin(SurvivalGames.getInstance().getDescription().getName());
		manager.disablePlugin(pluginunload);
		
		if(spmanager != null) {
			Field pluginsField = spmanager.getClass().getDeclaredField("plugins");
			pluginsField.setAccessible(true);
			List<Plugin> plugins = (List<Plugin>) pluginsField.get(spmanager);
			
			Field lookupNamesField = spmanager.getClass().getDeclaredField("lookupNames");
			lookupNamesField.setAccessible(true);
			Map<String, Plugin> lookupNames = (Map<String, Plugin>) lookupNamesField.get(spmanager);
			
			Field commandMapField = spmanager.getClass().getDeclaredField("commandMap");
			commandMapField.setAccessible(true);
			SimpleCommandMap commandMap = (SimpleCommandMap) commandMapField.get(spmanager);
			
			Field knownCommandsField = null;
			Map<String, Command> knownCommands = null;
			
			if(commandMap != null) {
				knownCommandsField = commandMap.getClass().getDeclaredField("knownCommands");
				knownCommandsField.setAccessible(true);
				knownCommands = (Map<String, Command>) knownCommandsField.get(commandMap);
			}
			
			for(Plugin plugin : manager.getPlugins()) {
				if(plugin.getDescription().getName().equalsIgnoreCase(SurvivalGames.getInstance().getDescription().getName())) {
					manager.disablePlugin(plugin);
					
					if(plugins != null && plugins.contains(plugin)) {
						plugins.remove(plugin);
					}
					
					if(lookupNames != null && lookupNames.containsKey(SurvivalGames.getInstance().getDescription().getName())) {
						lookupNames.remove(SurvivalGames.getInstance().getDescription().getName());
					}
					
					if(commandMap != null) {
						for(Iterator<Map.Entry<String, Command>> it = knownCommands.entrySet().iterator(); it.hasNext();) {
							Map.Entry<String, Command> entry = it.next();
							
							if(entry.getValue() instanceof PluginCommand) {
								PluginCommand command = (PluginCommand) entry.getValue();
								
								if(command.getPlugin() == plugin) {
									command.unregister(commandMap);
									it.remove();
								}
							}
						}
					}
				}
			}
		}
		
		// load
		Plugin pluginload = manager.loadPlugin(new File("plugins", SurvivalGames.getInstance().getDescription().getName() + ".jar"));
		pluginload.onLoad();
		manager.enablePlugin(pluginload);
	}
	
}
