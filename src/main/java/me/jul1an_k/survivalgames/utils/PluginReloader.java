package me.jul1an_k.survivalgames.utils;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import me.jul1an_k.survivalgames.SurvivalGames;

import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.UnknownDependencyException;

public class PluginReloader {
	
	@SuppressWarnings("unchecked")
	public static void reload() throws UnknownDependencyException, InvalidPluginException, InvalidDescriptionException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
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
