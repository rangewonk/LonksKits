package me.MnMaxon.LonksKits;

import java.util.ArrayList;

import me.MnMaxon.LonksKits.MetaLists;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TpCountdown {
	public int time;
	public Player p = null;
	public Location loc = null;
	public String command = null;
	public static ArrayList<TpCountdown> toTp = new ArrayList<TpCountdown>();

	public TpCountdown(Player p, Location loc, int time) {
		this.p = p;
		this.loc = loc;
		this.time = time;
	}

	public TpCountdown(Player p, String cmd, int time) {
		this.p = p;
		command = cmd;
		this.time = time;
	}

	public static void teleport(Player p, Location loc) {
		if (p.hasPermission("Lonkskit.cmd.clearcooldown")) {
			MetaLists.TP_AROUND.add(p);
			p.teleport(loc);
			return;
		}
		for (TpCountdown tpInfo : toTp)
			if (tpInfo.p.equals(p)) {
				p.sendMessage(ChatColor.RED + "You are already trying to teleport");
				return;
			}
		p.sendMessage(ChatColor.GOLD + "Teleportation will commence in " + ChatColor.RED + "4 seconds" + ChatColor.GOLD
				+ ". Don't move!");
		toTp.add(new TpCountdown(p, loc, 4));
	}

	public static void cancel(Player p) {
		ArrayList<TpCountdown> remover = new ArrayList<TpCountdown>();
		for (TpCountdown tpc : TpCountdown.toTp)
			if (tpc.p.equals(p))
				remover.add(tpc);
		for (TpCountdown remove : remover) {
			if (remove.p.isOnline())
				remove.p.sendMessage(ChatColor.DARK_RED + "Pending teleportation request cancelled.");
			TpCountdown.toTp.remove(remove);
		}
	}

	public static void teleport(Player p, String cmd) {
		if (p.hasPermission("Lonkskit.cmd.clearcooldown")) {
			MetaLists.TP_AROUND_PLOT.add(p);
			p.chat("/" + cmd);
			return;
		}
		for (TpCountdown tpInfo : toTp)
			if (tpInfo.p.equals(p)) {
				p.sendMessage(ChatColor.RED + "You are already trying to teleport");
				return;
			}
		p.sendMessage(ChatColor.GOLD + "Teleportation will commence in " + ChatColor.RED + "4 seconds" + ChatColor.GOLD
				+ ". Don't move!");
		toTp.add(new TpCountdown(p, cmd, 4));
	}
}
