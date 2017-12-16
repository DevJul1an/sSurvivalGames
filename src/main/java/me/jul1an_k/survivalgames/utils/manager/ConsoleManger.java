package me.jul1an_k.survivalgames.utils.manager;

public class ConsoleManger {
	
	public static void sendStacktrace(String s, Throwable throwable) {
		System.err.println("<--------------------------------------------->");
		System.err.println(" ");
		System.err.println("            sSurvivalGames");
		System.err.println("Error while executing '" + s + "'");
		System.err.println(" ");
		System.err.println("Stacktrace:");
		throwable.printStackTrace();
		System.err.println(" ");
		System.err.println("<--------------------------------------------->");
	}
	
	public static void sendInformation(String s) {
		System.out.println("<--------------------------------------------->");
		System.out.println(" ");
		System.out.println("            sSurvivalGames");
		System.out.println("Information:");
		System.out.println(" ");
		System.out.println(s);
		System.out.println(" ");
		System.out.println("<--------------------------------------------->");
	}
	
}
