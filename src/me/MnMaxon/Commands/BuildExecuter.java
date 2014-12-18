package me.MnMaxon.Commands;

import me.MnMaxon.LonksKits.Main;
import me.MnMaxon.LonksKits.Messages;
import me.MnMaxon.LonksKits.MetaLists;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BuildExecuter implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command command, String label, String[] args) {
		if (!(s instanceof Player)) {
			if (args.length == 2 && args[0].equalsIgnoreCase("bypass"))
				if (Bukkit.getPlayer(args[1]) != null) {
					if (MetaLists.BYPASS_BUILD.contains(Bukkit.getPlayer(args[1]))) {
						MetaLists.BYPASS_BUILD.remove(Bukkit.getPlayer(args[1]));
						Bukkit.getPlayer(args[1]).sendMessage(ChatColor.RED + "You cannot build anymore");
						s.sendMessage(ChatColor.RED + Bukkit.getPlayer(args[1]).getName() + " cannot build anymore");
					} else {
						MetaLists.BYPASS_BUILD.add(Bukkit.getPlayer(args[1]));
						Bukkit.getPlayer(args[1]).sendMessage(ChatColor.GREEN + "You can now build");
						s.sendMessage(ChatColor.GREEN + Bukkit.getPlayer(args[1]).getName() + " can now build");
					}
				} else
					s.sendMessage(ChatColor.DARK_RED + args[1] + "is not online!");
			else
				s.sendMessage(ChatColor.RED + "Use like /Build Bypass [PLAYER]");
			return true;
		} else {
			Player p = (Player) s;
			if (p.isOp()) {
				if (args.length == 1 && args[0].equalsIgnoreCase("bypass"))
					if (MetaLists.BYPASS_BUILD.contains(p)) {
						MetaLists.BYPASS_BUILD.remove(p);
						p.sendMessage(ChatColor.RED + "You cannot build anymore");
					} else {
						MetaLists.BYPASS_BUILD.add(p);
						p.sendMessage(ChatColor.GREEN + "You can now build");
					}
				else if (args.length == 2 && args[0].equalsIgnoreCase("bypass"))
					if (Bukkit.getPlayer(args[1]) != null) {
						if (MetaLists.BYPASS_BUILD.contains(Bukkit.getPlayer(args[1]))) {
							MetaLists.BYPASS_BUILD.remove(Bukkit.getPlayer(args[1]));
							Bukkit.getPlayer(args[1]).sendMessage(ChatColor.RED + "You cannot build anymore");
							p.sendMessage(ChatColor.RED + Bukkit.getPlayer(args[1]).getName() + " cannot build anymore");
						} else {
							MetaLists.BYPASS_BUILD.add(Bukkit.getPlayer(args[1]));
							Bukkit.getPlayer(args[1]).sendMessage(ChatColor.GREEN + "You can now build");
							p.sendMessage(ChatColor.GREEN + Bukkit.getPlayer(args[1]).getName() + " can now build");
						}
					} else
						p.sendMessage(ChatColor.DARK_RED + args[1] + "is not online!");
				else
					Main.displayHelp(p, 1);
			} else
				p.sendMessage(Messages.PERMISSION_COMMAND);
			return true;
		}
	}
}
