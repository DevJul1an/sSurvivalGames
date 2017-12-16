package me.jul1an_k.survivalgames;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import lombok.Setter;
import me.jul1an_k.survivalgames.commands.Command_SurvivalGames;
import me.jul1an_k.survivalgames.countdown.Countdown_Lobby;
import me.jul1an_k.survivalgames.listener.Break_Listener;
import me.jul1an_k.survivalgames.listener.Chat_Listener;
import me.jul1an_k.survivalgames.listener.Chest_Listener;
import me.jul1an_k.survivalgames.listener.Damage_Listener;
import me.jul1an_k.survivalgames.listener.Death_Listener;
import me.jul1an_k.survivalgames.listener.Food_Listener;
import me.jul1an_k.survivalgames.listener.Interact_Listener;
import me.jul1an_k.survivalgames.listener.InventoryClick_Listener;
import me.jul1an_k.survivalgames.listener.ItemDrop_Listener;
import me.jul1an_k.survivalgames.listener.Join_Listener;
import me.jul1an_k.survivalgames.listener.Mob_Listener;
import me.jul1an_k.survivalgames.listener.Move_Listener;
import me.jul1an_k.survivalgames.listener.Ping_Listener;
import me.jul1an_k.survivalgames.listener.Place_Listener;
import me.jul1an_k.survivalgames.listener.Quit_Listener;
import me.jul1an_k.survivalgames.listener.Respawn_Listener;
import me.jul1an_k.survivalgames.mapreset.MapReset;
import me.jul1an_k.survivalgames.mapreset.MapReset.MapResetListener;
import me.jul1an_k.survivalgames.scoreboard.LobbyScoreboard;
import me.jul1an_k.survivalgames.update.FileUpdate;
import me.jul1an_k.survivalgames.utils.GameState;
import me.jul1an_k.survivalgames.utils.Map;
import me.jul1an_k.survivalgames.utils.MySQL;
import me.jul1an_k.survivalgames.utils.manager.ChestManager;
import me.jul1an_k.survivalgames.utils.manager.ConfigManager;
import me.jul1an_k.survivalgames.utils.manager.MessageManager;
import me.jul1an_k.survivalgames.utils.manager.VoteManager;

public class SurvivalGames extends JavaPlugin {
	
	@Getter private static SurvivalGames instance;
	
	@Getter @Setter private static GameState status;
	
	@Getter private static ChestManager chestManager;
	
	@Getter private static FileConfiguration cfg;
	
	@Getter private static MessageManager messageManager;
	
	@Getter @Setter private static boolean voting;
	
	@Getter @Setter private static Map winMap;
	
	@Getter private static List<Map> maps = new ArrayList<>();
	
	public static List<String> alive = new ArrayList<>();
	
	public static HashMap<String, Location> locs = new HashMap<>();
	
	public static HashMap<Player, Player> lastdamage = new HashMap<>();
	
	public void onLoad() {
		instance = this;
	}
	
	public void onEnable() {
		// === INSTANZEN === \\
		
		saveDefaultConfig();
		cfg = ConfigManager.getFileConfiguration();
		voting = true;
		chestManager = new ChestManager();
		status = GameState.LOBBY;
		messageManager = new MessageManager();
		ConfigManager.load();
		
		// === LADEN (REGISTRIEREN ETC) === \\
		
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		new Countdown_Lobby().start();
		VoteManager.startVote();
		System.out.println("[sSurvivalGames] Version " + this.getDescription().getVersion() + " by " + this.getDescription().getAuthors().get(0) + " loaded!");
		if(!registerCommands()) {
			System.err.println("[sSurvivalGames] Error: The Commands can't load!" + "[sSurvivalGames] Is the Plugin up to date?");
		}
		if(!registerListener()) {
			System.err.println("[sSurvivalGames] Error: The Events can't load!" + "[sSurvivalGames] Is the Plugin up to date?");
		}
		if(cfg.getBoolean("MySQL.Enable")) {
			MySQL.connect(cfg.getString("MySQL.Host"), cfg.getInt("MySQL.Port"), cfg.getString("MySQL.Database"), cfg.getString("MySQL.User"), cfg.getString("MySQL.Password"));
			if(MySQL.hasConnection()) {
				try {
					PreparedStatement st = MySQL.con.prepareStatement("CREATE TABLE IF NOT EXISTS sSurvivalGames(UUID varchar(100), Kills int(100), Deaths int(100))");
					st.executeUpdate();
				} catch(SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		if(messageManager.getBoolean("MapReset")) {
			MapReset.start();
		}
		
		for(Player all : Bukkit.getOnlinePlayers()) {
			LobbyScoreboard.update(all);
		}
		
		FileUpdate fu = new FileUpdate();
		fu.check();
	}
	
	public void onDisable() {
		for(Player all : Bukkit.getOnlinePlayers()) {
			LobbyScoreboard.hide(all);
		}
		
		if(MapReset.isEnabled()) {
			MapReset.stop();
			MapReset.reset();
		}
	}
	
	private boolean registerCommands() {
		this.getCommand("survivalgames").setExecutor(new Command_SurvivalGames());
		return true;
	}

	private boolean registerListener() {
		Bukkit.getPluginManager().registerEvents(new Join_Listener(), this);
		Bukkit.getPluginManager().registerEvents(new Ping_Listener(), this);
		Bukkit.getPluginManager().registerEvents(new Break_Listener(), this);
		Bukkit.getPluginManager().registerEvents(new Move_Listener(), this);
		Bukkit.getPluginManager().registerEvents(new Place_Listener(), this);
		Bukkit.getPluginManager().registerEvents(new Respawn_Listener(), this);
		Bukkit.getPluginManager().registerEvents(new Quit_Listener(), this);
		Bukkit.getPluginManager().registerEvents(new Mob_Listener(), this);
		Bukkit.getPluginManager().registerEvents(new Death_Listener(), this);
		Bukkit.getPluginManager().registerEvents(new Damage_Listener(), this);
		Bukkit.getPluginManager().registerEvents(new Chat_Listener(), this);
		Bukkit.getPluginManager().registerEvents(new Food_Listener(), this);
		Bukkit.getPluginManager().registerEvents(new Chest_Listener(), this);
		Bukkit.getPluginManager().registerEvents(new Interact_Listener(), this);
		Bukkit.getPluginManager().registerEvents(new InventoryClick_Listener(), this);
		Bukkit.getPluginManager().registerEvents(new ItemDrop_Listener(), this);
		
		Bukkit.getPluginManager().registerEvents(new MapResetListener(), this);
		return true;
	}
	
	public static void addMap(Map m) {
		maps.add(m);
	}
	
}
