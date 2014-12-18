package me.MnMaxon.Apis;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class VanishAPI {
	public static Boolean hasVanish = false;

	public static void setup() {
		if (Bukkit.getPluginManager().getPlugin("VanishNoPacket") == null)
			hasVanish = false;
		else {
			hasVanish = true;
			VanishMethods.setup();
		}
	}

	public static void setVanish(Player p, boolean vanish) {
		if (hasVanish)
			VanishMethods.setVanished(p, vanish);
	}

	public static boolean isVanished(Player p) {
		if (hasVanish)
			return VanishMethods.isVanished(p);
		else
			return false;
	}
}
