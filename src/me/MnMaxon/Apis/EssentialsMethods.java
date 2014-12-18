package me.MnMaxon.Apis;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class EssentialsMethods {

	public static boolean hasGodMode(Player p) {
		if (Bukkit.getPluginManager().getPlugin("Essentials") == null)
			return false;
		return ((com.earth2me.essentials.Essentials) Bukkit.getPluginManager().getPlugin("Essentials")).getUser(p)
				.isGodModeEnabled();
	}

	public static void setGodMode(Player p, boolean god) {
		if (Bukkit.getPluginManager().getPlugin("Essentials") == null)
			return;
		((com.earth2me.essentials.Essentials) Bukkit.getPluginManager().getPlugin("Essentials")).getUser(p)
				.setGodModeEnabled(god);
	}

	public static boolean hasVanish(Player p) {
		if (Bukkit.getPluginManager().getPlugin("Essentials") == null)
			return false;
		return ((com.earth2me.essentials.Essentials) Bukkit.getPluginManager().getPlugin("Essentials")).getUser(p)
				.isVanished();
	}

	public static String getPrefix(Player p) {
		if (Bukkit.getPluginManager().getPlugin("Essentials") == null)
			return "";
		com.earth2me.essentials.Essentials ess = (com.earth2me.essentials.Essentials) Bukkit.getPluginManager()
				.getPlugin("Essentials");
		final StringBuilder prefix = new StringBuilder();
		if (p.isOp()) {
			try {
				final ChatColor opPrefix = ess.getSettings().getOperatorColor();
				if (opPrefix != null && opPrefix.toString().length() > 0) {
					prefix.insert(0, opPrefix.toString());
				}
			} catch (Exception e) {
			}
		}
		final String ptext = ess.getPermissionsHandler().getPrefix(p).replace('&', '§');
		prefix.insert(0, ptext);
		final String strPrefix = prefix.toString();
		return strPrefix;
	}
}