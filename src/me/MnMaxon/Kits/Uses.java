package me.MnMaxon.Kits;

import me.MnMaxon.LonksKits.Main;

public class Uses {
	public static int infinite = 9999;

	public static void remove(String playerName, int amount, String kitName) {
		set(playerName, getAmount(playerName, kitName) - amount, kitName);
	}

	public static int getAmount(String playerName, String kitName) {
		return Main.playerData.getInt(playerName + ".Uses." + kitName);
	}

	public static void set(String playerName, int amount, String kitName) {
		if (amount <= 0)
			Main.playerData.set(playerName + ".Uses." + kitName, null);
		else if (!hasInfinite(playerName, kitName))
			Main.playerData.set(playerName + ".Uses." + kitName, amount);
	}

	public static void add(String playerName, int amount, String kitName) {
		set(playerName, getAmount(playerName, kitName) + amount, kitName);
	}

	public static void giveInfinite(String playerName, String kitName) {
		set(playerName, infinite, kitName);
	}

	public static void removeInfinite(String playerName, String kitName) {
		set(playerName, 0, kitName);
	}

	public static boolean hasInfinite(String playerName, String kitName) {
		return getAmount(playerName, kitName) == infinite;
	}

	public static boolean hasUses(String playerName, String kitName) {
		return getAmount(playerName, kitName) > 0;
	}
}
