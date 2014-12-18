package me.MnMaxon.LonksKits;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Cooldown {
	public int time;
	public Player player;
	public String ability;

	public Cooldown(int Time, Player Player, String Ability) {
		player = Player;
		time = Time;
		ability = Ability;
	}

	public static boolean hasCooldown(Player p, String cooldown, boolean message) {
		for (Cooldown cd : Main.cool)
			if (cd.player.equals(p) && cd.ability.equalsIgnoreCase(cooldown)) {
				if (message)
					p.sendMessage(Messages.PREFIX + ChatColor.DARK_RED + "You are on cooldown for another " + cd.time
							+ " seconds");
				return true;
			}
		return false;
	}
}
