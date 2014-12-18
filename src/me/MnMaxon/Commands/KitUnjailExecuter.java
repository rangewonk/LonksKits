package me.MnMaxon.Commands;

import me.MnMaxon.LonksKits.Jail;
import me.MnMaxon.LonksKits.Messages;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitUnjailExecuter implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command command, String label, String[] args) {

		if ((!(s instanceof Player) || ((Player) s).hasPermission("kitjail.jail")))
			if (args.length == 1) {
				if (Bukkit.getOfflinePlayer(args[0]) != null) {
					s.sendMessage(Bukkit.getOfflinePlayer(args[0]).getName() + " has been unkicked from kitpvp");
					Jail.add(Bukkit.getOfflinePlayer(args[0]), 0);
				} else
					s.sendMessage(ChatColor.DARK_RED + args[0] + " has never played on this server before");
			} else
				s.sendMessage(ChatColor.DARK_RED + "Use like /kitjail <playername> <time> <S/M/H/D>");
		else
			s.sendMessage(Messages.PERMISSION_COMMAND);

		return true;
	}

}
