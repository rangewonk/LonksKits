package me.MnMaxon.LonksKits;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Spectator {
	private static ArrayList<Player> spectators = new ArrayList<Player>();

	public static void give(Player p) {
		give(p, p.getWorld());
	}

	public static void give(Player p, World world) {
		if (Locations.gameWorld != null && Locations.gameWorld.equals(world)) {
			if (!isSpectator(p))
				spectators.add(p);
			p.setAllowFlight(true);
			p.setNoDamageTicks(Integer.MAX_VALUE);
			p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0));
			Main.ghostManager.setGhost(p, true);
			p.sendMessage(ChatColor.GREEN + "You are now a spectator!");
		}
	}

	public static void remove(Player p) {
		spectators.remove(p);
		if (!p.getGameMode().equals(GameMode.CREATIVE))
			p.setAllowFlight(false);
		p.setNoDamageTicks(0);
		Main.ghostManager.setGhost(p, false);
		p.removePotionEffect(PotionEffectType.INVISIBILITY);
	}

	public static boolean isSpectator(Player p) {
		if (spectators.contains(p))
			return true;
		else
			return false;
	}
}
