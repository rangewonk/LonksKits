package me.MnMaxon.LonksKits;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import me.MnMaxon.LonksKits.MetaLists;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class Jail {
	public static void add(OfflinePlayer oP, long time) {
		Date now = new Date();
		Date futureDate = new Date();
		futureDate.setTime(now.getTime() + time);

		Main.playerData.set("Players." + oP.getName() + ".ReleaseDate.Year",
				Integer.parseInt(new SimpleDateFormat("yyyy").format(futureDate)));
		Main.playerData.set("Players." + oP.getName() + ".ReleaseDate.Day",
				Integer.parseInt(new SimpleDateFormat("dd").format(futureDate)));
		Main.playerData.set("Players." + oP.getName() + ".ReleaseDate.Hour",
				Integer.parseInt(new SimpleDateFormat("HH").format(futureDate)));
		Main.playerData.set("Players." + oP.getName() + ".ReleaseDate.Minute",
				Integer.parseInt(new SimpleDateFormat("mm").format(futureDate)));
		Main.playerData.set("Players." + oP.getName() + ".ReleaseDate.Second",
				Integer.parseInt(new SimpleDateFormat("ss").format(futureDate)));
		Main.playerData.set("Players." + oP.getName() + ".ReleaseDate.Month",
				Integer.parseInt(new SimpleDateFormat("MM").format(futureDate)));
		Main.playerData.save();
		if (oP.isOnline()) {
			Player p = (Player) oP;
			if (!inJail(oP))
				p.sendMessage(ChatColor.RED + "You have been unkicked from kitpvp");
			else {
				p.sendMessage(ChatColor.RED + "You have been kicked from kitpvp for " + getMessage(p));
				MetaLists.TP_AROUND.add(p);
				p.teleport(Locations.hub);
			}
		}
	}

	public static long get(OfflinePlayer oP) {
		if (Main.playerData.get("Players." + oP.getName() + ".ReleaseDate") == null)
			return -1;
		Date now = new Date();
		int yearThen = Main.playerData.getInt("Players." + oP.getName() + ".ReleaseDate.Year"), monthThen = Main.playerData
				.getInt("Players." + oP.getName() + ".ReleaseDate.Month") - 1, dayThen = Main.playerData
				.getInt("Players." + oP.getName() + ".ReleaseDate.Day"), hourThen = Main.playerData.getInt("Players."
				+ oP.getName() + ".ReleaseDate.Hour"), minuteThen = Main.playerData.getInt("Players." + oP.getName()
				+ ".ReleaseDate.Minute"), secondThen = Main.playerData.getInt("Players." + oP.getName()
				+ ".ReleaseDate.Second");
		Calendar cal = new GregorianCalendar();
		cal.set(yearThen, monthThen, dayThen, hourThen, minuteThen, secondThen);
		long timeLong = cal.getTime().getTime() - now.getTime();
		if (timeLong <= 0) {
			Main.playerData.set("Players." + oP.getName() + ".ReleaseDate", null);
			timeLong = -1;
			Main.playerData.save();
		}
		return timeLong;
	}

	public static String getMessage(OfflinePlayer oP) {
		long diff = get(oP);
		if (diff == -1)
			return "NOPE";

		Date date = new Date();
		date.setTime(diff + new Date().getTime());

		long diffSeconds = diff / 1000 % 60;
		long diffMinutes = diff / (60 * 1000) % 60;
		long diffHours = diff / (60 * 60 * 1000) % 24;
		long diffDays = diff / (24 * 60 * 60 * 1000);

		String days = "", hours = "", minutes = "", seconds = "";
		if (diffDays > 0)
			days = diffDays + " Days, ";
		if (diffMinutes > 0)
			minutes = diffMinutes + " Minutes, ";
		if (diffHours > 0)
			hours = diffHours + " Hours, ";
		if (diffSeconds > 0)
			seconds = diffSeconds + " Seconds";
		return days + hours + minutes + seconds;
	}

	public static Boolean inJail(OfflinePlayer oP) {
		if (get(oP) == -1)
			return false;
		return true;
	}
}
