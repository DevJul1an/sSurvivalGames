package me.jul1an_k.survivalgames.utils.manager;

import org.bukkit.entity.Player;

import me.jul1an_k.survivalgames.utils.TabActionTitle;

public class SoundManager {
	
	public static void sendSound(Player p, Sound sound, float f1, float f2) {
		if(TabActionTitle.compareMinecraftVersionServerIsHigherOrEqual("1.9")) {
			p.playSound(p.getLocation(), org.bukkit.Sound.valueOf(sound.getOnePointNine_Sound()), f1, f2);
		} else {
			p.playSound(p.getLocation(), org.bukkit.Sound.valueOf(sound.getOnePointEight_Sound()), f1, f2);
		}
	}
	
	public enum Sound {
		
		RANDOM_LEVEL_UP("LEVEL_UP", "ENTITY_PLAYER_LEVELUP");
		
		private String OnePointEight_Sound;
		private String OnePointNine_Sound;
		
		private Sound(String OnePointEight_Sound, String OnePointNine_Sound) {
			this.OnePointEight_Sound = OnePointEight_Sound;
			this.OnePointNine_Sound = OnePointNine_Sound;
		}
		
		public String getOnePointEight_Sound() {
			return OnePointEight_Sound;
		}
		
		public String getOnePointNine_Sound() {
			return OnePointNine_Sound;
		}
		
	}
	
}
