package me.MnMaxon.Commands;

import me.MnMaxon.LonksKits.Jail;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitJailExecuter implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command command, String label, String[] args) {
		if (s instanceof Player) {
			Player p = (Player) s;
			if (args.length == 0) {
				p.sendMessage(Jail.getMessage(p));
				return true;
			} else if (!(p.hasPermission("kitjail.jail"))) {
				p.sendMessage(Jail.getMessage(p));
				return true;
			}
		}

		if (args.length == 2) {
			if (Bukkit.getOfflinePlayer(args[0]) != null) {
				try {
					long time = Long.parseLong(args[1].toLowerCase().replace("s", "").replace("m", "").replace("h", "")
							.replace("d", ""));
					String unit;
					if (args[1].toLowerCase().contains("s")) {
						unit = "seconds";
						time = time * 1000;
					} else if (args[1].toLowerCase().contains("m")) {
						unit = "minutes";
						time = time * 1000 * 60;
					} else if (args[1].toLowerCase().contains("h")) {
						unit = "hours";
						time = time * 1000 * 60 * 60;
					} else if (args[1].toLowerCase().contains("d")) {
						unit = "days";
						time = time * 1000 * 60 * 60 * 24;
					} else {
						s.sendMessage(ChatColor.DARK_RED + "Use like /kitjail <playername> <time>" + ChatColor.BOLD
								+ "<S/M/H/D>");
						return true;
					}
					Jail.add(Bukkit.getOfflinePlayer(args[0]), time);
					s.sendMessage(Bukkit.getOfflinePlayer(args[0]).getName()
							+ " has been kicked from kitpvp for another "
							+ args[1].toLowerCase().replace("s", "").replace("m", "").replace("h", "").replace("d", "")
							+ " " + unit + ".");
				} catch (NumberFormatException ex) {
					s.sendMessage(ChatColor.RED + args[1] + " is not a whole number");
				}
			} else
				s.sendMessage(ChatColor.DARK_RED + args[0] + " has never played on this server before");
		} else
			s.sendMessage(ChatColor.DARK_RED + "Use like /kitjail <playername> <time> <S/M/H/D>");
		return true;
	}
}
