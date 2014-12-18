package me.MnMaxon.LonksKits;

import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Death {
	public static ArrayList<HitInfo> damageInfo = new ArrayList<HitInfo>();

	public static String getMessage(Player p) {
		if (getInfo(p) == null)
			return "";
		String name = p.getName();
		String reason = getReason(p);
		String attacker = "";
		if (!getAttacker(p).equals(""))
			attacker = " by " + getAttacker(p);
		if (reason.equals("was stomped on"))
			p.sendMessage(ChatColor.RED + "HINT: " + ChatColor.GRAY
					+ "If you are stomped while shifted, you will take half the damage.");
		return ChatColor.GOLD + "[" + ChatColor.AQUA + "LonksKits" + ChatColor.GOLD + "] " + ChatColor.DARK_RED + name
				+ " " + reason + attacker;
	}

	public static HitInfo getInfo(Player p) {
		for (HitInfo hi : damageInfo)
			if (hi != null && hi.playerName != null && hi.playerName.equals(p.getName()))
				return hi;
		return null;
	}

	public static void update(Player p, Player attacker, String reason) {
		if (getInfo(p) != null)
			damageInfo.remove(getInfo(p));
		if (attacker == null && reason == null)
			return;
		HitInfo info = new HitInfo();
		if (attacker != null)
			info.attackerName = attacker.getName();
		else
			info.attackerName = "";
		if (reason != null)
			info.attackReason = reason;
		else
			info.attackReason = "";
		if (attacker != null && attacker.isOnline())
			info.timer = 7;
		else
			info.timer = -1;
		info.playerName = p.getName();
		damageInfo.add(info);
	}

	public static String getReason(Player p) {
		if (getInfo(p) != null) {
			return getInfo(p).attackReason;
		}
		return "";
	}

	public static String getAttacker(Player p) {
		if (getInfo(p) != null) {
			return getInfo(p).attackerName;
		}
		return "";
	}

	public static ArrayList<HitInfo> coolDownList() {
		ArrayList<HitInfo> cdList = new ArrayList<HitInfo>();
		for (HitInfo hi : damageInfo)
			if (hi.timer > 0)
				cdList.add(hi);
		return cdList;
	}

	public static boolean wasHit(Player p) {
		if (getInfo(p) != null)
			return true;
		return false;
	}
}