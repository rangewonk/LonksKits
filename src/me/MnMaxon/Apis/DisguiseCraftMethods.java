package me.MnMaxon.Apis;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class DisguiseCraftMethods {
	public static Boolean hasDisguiseCraft = false;

	public static void disguise(Player p, EntityType entType) {
		if (hasDisguiseCraft)
			dcMeth.disguise(p, entType);
	}

	public static void unDisguise(Player p) {
		if (hasDisguiseCraft)
			dcMeth.unDisguise(p);
	}

	public static void setup() {
		if (Bukkit.getPluginManager().getPlugin("DisguiseCraft") == null)
			hasDisguiseCraft = false;
		else {
			hasDisguiseCraft = true;
			dcMeth.setup();
		}
	}

	public static boolean isDisguised(Player p) {
		if (!hasDisguiseCraft)
			return false;
		return dcMeth.isDisguised(p);
	}

	public static EntityType getDisguiseType(Player p) {
		if (isDisguised(p))
			return dcMeth.getDisguiseType(p);
		else
			return null;
	}
}