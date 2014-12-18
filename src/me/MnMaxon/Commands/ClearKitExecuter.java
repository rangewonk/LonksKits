package me.MnMaxon.Commands;

import me.MnMaxon.LonksKits.Locations;
import me.MnMaxon.LonksKits.Main;
import me.MnMaxon.LonksKits.Messages;
import me.MnMaxon.LonksKits.MetaLists;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearKitExecuter implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command command, String label, String[] args) {
		if (!(s instanceof Player)) {
			if (args.length == 1) {
				if (Bukkit.getPlayer(args[0]) != null) {
					if (Locations.gameWorld != null && Locations.gameWorld.equals(Bukkit.getPlayer(args[0]).getWorld())) {
						Main.ClearKit(Bukkit.getPlayer(args[0]));
						MetaLists.PLAYERS.add(Bukkit.getPlayer(args[0]), null);
						Main.giveSpawnItems(Bukkit.getPlayer(args[0]));
						s.sendMessage(Messages.CLEAR_KIT);
						Bukkit.getPlayer(args[0]).sendMessage(Messages.CLEAR_KIT);
					} else
						s.sendMessage(ChatColor.DARK_RED + Bukkit.getPlayer(args[0]).getName()
								+ " is not in the kitpvp world");
				} else
					s.sendMessage(ChatColor.DARK_RED + args[0] + "is not online!");
			} else {
				s.sendMessage(ChatColor.RED + "Use like /ck [PlayerName]");
			}
			return true;
		} else {
			Player p = (Player) s;
			if (p.hasPermission("Lonkskit.cmd.clearkit")) {
				if (MetaLists.PLAYERS.contains(p)) {
					if (p.isOp() && args.length > 0) {
						if (Bukkit.getPlayer(args[0]) != null) {
							if (Locations.gameWorld != null
									&& Locations.gameWorld.equals(Bukkit.getPlayer(args[0]).getWorld())) {
								Main.ClearKit(Bukkit.getPlayer(args[0]));
								MetaLists.PLAYERS.add(Bukkit.getPlayer(args[0]), null);
								Main.giveSpawnItems(Bukkit.getPlayer(args[0]));
								p.sendMessage(ChatColor.GREEN + "Kit cleared");
								Bukkit.getPlayer(args[0]).sendMessage(Messages.CLEAR_KIT);
							} else
								p.sendMessage(ChatColor.DARK_RED + Bukkit.getPlayer(args[0]).getName()
										+ " is not in the kitpvp world");
						} else
							p.sendMessage(ChatColor.DARK_RED + args[0] + "is not online!");
					} else {
						Main.ClearKit(p);
						MetaLists.PLAYERS.add(p, null);
						Main.giveSpawnItems(p);
						p.sendMessage(ChatColor.GREEN + "Kit cleared");
					}
				} else
					p.sendMessage(ChatColor.DARK_RED + "You are not in the right world to do this");

			} else
				p.sendMessage(Messages.PERMISSION_COMMAND);

			return true;
		}
	}
}
