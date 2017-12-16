package me.jul1an_k.survivalgames.countdown;

import me.jul1an_k.survivalgames.SurvivalGames;
import me.jul1an_k.survivalgames.utils.manager.MessageManager;

public abstract class Countdown {
	
	public static MessageManager mana = SurvivalGames.getMessageManager();
	
	public abstract void start();
	
}
