package me.MnMaxon.Commands;

import me.MnMaxon.Kits.Kit;
import me.MnMaxon.LonksKits.Locations;
import me.MnMaxon.LonksKits.Main;
import me.MnMaxon.LonksKits.MetaLists;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SafeZoneExecuter implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(ChatColor.DARK_PURPLE + "/SZ Wand " + ChatColor.GREEN + "-" + ChatColor.AQUA
					+ " Gives you a safezone selection wand");
			sender.sendMessage(ChatColor.DARK_PURPLE + "/SZ Set " + ChatColor.GREEN + "-" + ChatColor.AQUA
					+ " Sets the safezone to your safezone wand selection");
			sender.sendMessage(ChatColor.DARK_PURPLE + "/SZ Bypass " + ChatColor.GREEN + "-" + ChatColor.AQUA
					+ " Allows you to bypass the safezone border");
			return true;
		}
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "You must be a player to do this!");
			return true;
		}
		Player p = (Player) sender;
		if (p.isOp()) {
			if (args.length == 1 && args[0].equalsIgnoreCase("wand")) {
				if (p.getInventory().firstEmpty() != -1) {
					p.getInventory().addItem(
							Kit.easyItem(ChatColor.BOLD + "" + ChatColor.RED + "SafeZone Wand", Material.BLAZE_ROD, 0,
									null, 1));
					p.sendMessage(ChatColor.GREEN + "Left click to set point 1");
					p.sendMessage(ChatColor.GREEN + "Right click to set point 2");
				} else
					p.sendMessage(ChatColor.DARK_RED + "Your inventory is full");
			} else if (args.length == 1 && args[0].equalsIgnoreCase("set")) {
				if (Main.loc1 != null && Main.loc2 != null)
					if (Locations.gameWorld != null && Locations.gameWorld.equals(Main.loc1.getWorld())
							&& Locations.gameWorld.equals(Main.loc2.getWorld())) {
						if (Main.loc1.getBlockX() > Main.loc2.getBlockX()) {
							Main.data.set("Safe Zone.Low Point.X", Main.loc2.getBlockX());
							Main.data.set("Safe Zone.Max Point.X", Main.loc1.getBlockX());
						} else {
							Main.data.set("Safe Zone.Low Point.X", Main.loc1.getBlockX());
							Main.data.set("Safe Zone.Max Point.X", Main.loc2.getBlockX());
						}
						if (Main.loc1.getBlockZ() > Main.loc2.getBlockZ()) {
							Main.data.set("Safe Zone.Low Point.Z", Main.loc2.getBlockZ());
							Main.data.set("Safe Zone.Max Point.Z", Main.loc1.getBlockZ());
						} else {
							Main.data.set("Safe Zone.Low Point.Z", Main.loc1.getBlockZ());
							Main.data.set("Safe Zone.Max Point.Z", Main.loc2.getBlockZ());
						}
						Main.data.save();
						p.sendMessage(ChatColor.DARK_GREEN + "SafeZone Successfully Set!");
					} else
						p.sendMessage(ChatColor.DARK_RED + "Both points need to be in the world: "
								+ ChatColor.DARK_AQUA + Locations.gameWorld.getName());
				else
					p.sendMessage(ChatColor.DARK_RED + "You have to select to points you can set with "
							+ ChatColor.DARK_AQUA + "/SZ Wand");
			} else if ((args.length == 1 || args.length == 2) && args[0].equalsIgnoreCase("bypass")) {
				if (Locations.gameWorld != null && p.getWorld().equals(Locations.gameWorld))
					if (p.hasPermission("safezone.bypass")) {
						if (args.length == 1)
							if (MetaLists.BYPASS_SAFEZONE.contains(p)) {
								p.sendMessage(ChatColor.GOLD + "Safezone bypass is now " + ChatColor.RED + "disabled");
								MetaLists.BYPASS_SAFEZONE.remove(p);
							} else {
								p.sendMessage(ChatColor.GOLD + "Safezone bypass is now " + ChatColor.GREEN + "enabled");
								MetaLists.BYPASS_SAFEZONE.add(p);
							}
						else if (args.length == 2)
							if (Bukkit.getPlayer(args[1]) != null) {
								if (MetaLists.BYPASS_SAFEZONE.contains(Bukkit.getPlayer(args[1]))) {
									Bukkit.getPlayer(args[1]).sendMessage(
											ChatColor.GOLD + "Safezone bypass is now " + ChatColor.RED + "disabled");
									p.sendMessage(ChatColor.GOLD + "Safezone bypass is now " + "disabled for "
											+ ChatColor.GREEN + Bukkit.getPlayer(args[1]).getName());
									MetaLists.BYPASS_SAFEZONE.remove(Bukkit.getPlayer(args[1]));
								} else {
									Bukkit.getPlayer(args[1]).sendMessage(
											ChatColor.GOLD + "Safezone bypass is now " + ChatColor.GREEN + "enabled");
									p.sendMessage(ChatColor.GOLD + "Safezone bypass is now " + "enabled for "
											+ ChatColor.GREEN + Bukkit.getPlayer(args[1]).getName());
									MetaLists.BYPASS_SAFEZONE.add(Bukkit.getPlayer(args[1]));
								}
							} else
								p.sendMessage(ChatColor.RED + args[1] + " is not online");
					} else
						p.sendMessage(ChatColor.DARK_RED + "You do not have permission to do this");
				else
					p.sendMessage(ChatColor.DARK_RED + "You are not in the right world!");
			} else
				Main.displayHelp(p, 1);
		} else
			p.sendMessage(ChatColor.DARK_RED + "You do not have permission to do this");

		return true;
	}

}
