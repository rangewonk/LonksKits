package me.MnMaxon.LonksKits;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Killstreak {
	private static Map<String, Integer> killStreak = new HashMap<String, Integer>();

	public static void add(String playerName, int points) {
		set(playerName, get(playerName) + points);
		displayMessage(playerName);
	}

	private static void displayMessage(String playerName) {
		int points = get(playerName);
		String name = playerName;
		if (points == 5)
			Bukkit.broadcastMessage(ChatColor.GOLD + "[" + ChatColor.AQUA + "LonksKits" + ChatColor.GOLD + "] "
					+ ChatColor.GREEN + name + " is on a killing spree!");
		else if (points == 10)
			Bukkit.broadcastMessage(ChatColor.GOLD + "[" + ChatColor.AQUA + "LonksKits" + ChatColor.GOLD + "] "
					+ ChatColor.GREEN + name + " is dominating the battle field!");
		else if (points == 15)
			Bukkit.broadcastMessage(ChatColor.GOLD + "[" + ChatColor.AQUA + "LonksKits" + ChatColor.GOLD + "] "
					+ ChatColor.GREEN + name + " is on a RAMPAGE!");
		else if (points == 20)
			Bukkit.broadcastMessage(ChatColor.GOLD + "[" + ChatColor.AQUA + "LonksKits" + ChatColor.GOLD + "] "
					+ ChatColor.GREEN + name + " is UNSTOPPABLE!");
		else if (points == 25)
			Bukkit.broadcastMessage(ChatColor.GOLD + "[" + ChatColor.AQUA + "LonksKits" + ChatColor.GOLD + "] "
					+ ChatColor.GREEN + name + " is LEGENDARY!");
		else if (points == 30)
			Bukkit.broadcastMessage(ChatColor.GOLD + "[" + ChatColor.AQUA + "LonksKits" + ChatColor.GOLD + "] "
					+ ChatColor.GREEN + name + " is IMMORTAL!");
		else if (points == 35)
			Bukkit.broadcastMessage(ChatColor.GOLD + "[" + ChatColor.AQUA + "LonksKits" + ChatColor.GOLD + "] "
					+ ChatColor.GREEN + name + " is DIVINE!");
		else if (points == 40)
			Bukkit.broadcastMessage(ChatColor.GOLD + "[" + ChatColor.AQUA + "LonksKits" + ChatColor.GOLD + "] "
					+ ChatColor.GREEN + name + " is GOD-LIKE!");
		else if (points == 45)
			Bukkit.broadcastMessage(ChatColor.GOLD + "[" + ChatColor.AQUA + "LonksKits" + ChatColor.GOLD + "] "
					+ ChatColor.GREEN + name + " is TRANSCENDENT");
		else if (points == 50)
			Bukkit.broadcastMessage(ChatColor.GOLD + "[" + ChatColor.AQUA + "LonksKits" + ChatColor.GOLD + "] "
					+ ChatColor.GREEN + "Seriously? Someone kill " + name + ", stop that MASS MURDERER!");
		else if (points == 55)
			Bukkit.broadcastMessage(ChatColor.GOLD + "[" + ChatColor.AQUA + "LonksKits" + ChatColor.GOLD + "] "
					+ ChatColor.GREEN + name + " is SUCH A NERD!");
		else if (points == 60)
			Bukkit.broadcastMessage(ChatColor.GOLD + "[" + ChatColor.AQUA + "LonksKits" + ChatColor.GOLD + "] "
					+ ChatColor.GREEN + "The battle field can barely handle the SHEER AWESOMENESS of " + name + "!");
		else if (points == 65)
			Bukkit.broadcastMessage(ChatColor.GOLD + "[" + ChatColor.AQUA + "LonksKits" + ChatColor.GOLD + "] "
					+ ChatColor.GREEN + name + " has killed so many fighters, someone please shut him down!");
		else if (points == 70)
			Bukkit.broadcastMessage(ChatColor.GOLD + "[" + ChatColor.AQUA + "LonksKits" + ChatColor.GOLD + "] "
					+ ChatColor.GREEN + name + " please just stop killing people already!");
		else if (points == 75 || points == 80 || points == 85 || points == 90 || points == 95 || points == 100
				|| points == 105 || points == 110 || points == 115 || points == 125 || points == 120 || points == 135
				|| points == 140 || points == 145 || points == 150 || points == 155 || points == 160 || points == 165
				|| points == 170 || points == 175 || points == 180 || points == 185 || points == 190 || points == 195
				|| points == 200)
			Bukkit.broadcastMessage(ChatColor.GOLD + "[" + ChatColor.AQUA + "LonksKits" + ChatColor.GOLD + "] "
					+ ChatColor.GREEN + name + " got a killstreak of " + points + "!");
	}

	public static void set(String killedName, int points) {
		killStreak.remove(killedName);
		if (points > 0)
			killStreak.put(killedName, points);
	}

	public static int get(String playerName) {
		if (killStreak.containsKey(playerName))
			return killStreak.get(playerName);
		return 0;
	}

	public static void end(String killerName, String killedName) {
		if (killStreak.containsKey(killedName) && killerName != null) {
			int kills = get(killedName);
			if (kills >= 30)
				Bukkit.broadcastMessage(ChatColor.GOLD + "[" + ChatColor.AQUA + "LonksKits" + ChatColor.GOLD + "] "
						+ ChatColor.GREEN + killedName + "'s killstreak of " + kills + " was shut down by "
						+ killerName);
			else if (kills >= 25)
				Bukkit.broadcastMessage(ChatColor.GOLD + "[" + ChatColor.AQUA + "LonksKits" + ChatColor.GOLD + "] "
						+ ChatColor.GREEN + killerName + " defeated the legendary " + killedName);
			else if (kills >= 20)
				Bukkit.broadcastMessage(ChatColor.GOLD + "[" + ChatColor.AQUA + "LonksKits" + ChatColor.GOLD + "] "
						+ ChatColor.GREEN + killerName + " stopped " + killedName + "'s \"unstoppable\" killstreak.");
			else if (kills >= 15)
				Bukkit.broadcastMessage(ChatColor.GOLD + "[" + ChatColor.AQUA + "LonksKits" + ChatColor.GOLD + "] "
						+ ChatColor.GREEN + killerName + " stopped " + killedName + "'s rampage.");
			else if (kills >= 10)
				Bukkit.broadcastMessage(ChatColor.GOLD + "[" + ChatColor.AQUA + "LonksKits" + ChatColor.GOLD + "] "
						+ ChatColor.GREEN + killerName + " ended " + killedName + "'s domination");
			else if (kills >= 5)
				Bukkit.broadcastMessage(ChatColor.GOLD + "[" + ChatColor.AQUA + "LonksKits" + ChatColor.GOLD + "] "
						+ ChatColor.GREEN + killerName + " ended " + killedName + "'s killing spree.");
		}
		set(killedName, 0);
	}
}