package me.MnMaxon.LonksKits;

import java.util.ArrayList;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class IP {

	public static void add(Player p) {
		ArrayList<String> ips = get(p);
		if (!ips.contains(p.getAddress().getHostName())) {
			ips.add(p.getAddress().getHostName());
			Main.playerData.set("Players." + p.getName() + ".IPs", ips);
		}
	}

	public static ArrayList<String> get(OfflinePlayer oP) {
		ArrayList<String> ips = new ArrayList<String>();
		if (Main.playerData.get("Players." + oP.getName() + ".IPs") != null)
			for (String ip : Main.playerData.getStringList("Players." + oP.getName() + ".IPs"))
				ips.add(ip);
		return ips;
	}

	public static Boolean match(OfflinePlayer oP1, OfflinePlayer oP2) {
		for (String ip1 : get(oP1))
			for (String ip2 : get(oP2))
				if (ip1.equals(ip2))
					return true;
		return false;
	}
}
