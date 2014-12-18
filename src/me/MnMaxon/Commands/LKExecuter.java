package me.MnMaxon.Commands;

import me.MnMaxon.LonksKits.Locations;
import me.MnMaxon.LonksKits.Main;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LKExecuter implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(ChatColor.DARK_PURPLE + "/LK Help [Page#]" + ChatColor.GREEN + "-" + ChatColor.AQUA
					+ " Displays help");
			sender.sendMessage(ChatColor.DARK_PURPLE + "/LK SetSpawn " + ChatColor.GREEN + "-" + ChatColor.AQUA
					+ " Sets kitpvp spawn to your location");
			sender.sendMessage(ChatColor.DARK_PURPLE + "/LK SetHub " + ChatColor.GREEN + "-" + ChatColor.AQUA
					+ " Sets server spawn to your location");
			return true;
		}
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "You must be a player to do this!");
			return true;
		}
		Player p = (Player) sender;
		if (args.length == 2 && (args[0].equals("help") || args[0].equals("h"))) {
			try {
				Integer.parseInt(args[1]);
			} catch (NumberFormatException ex) {
				p.sendMessage(ChatColor.RED + "Use like /lk help [PageNumber]");
				return true;
			}
			Main.displayHelp(p, Integer.parseInt(args[1]));
		} else if (p.isOp() && args.length == 1) {
			if (args[0].equalsIgnoreCase("SetSpawn")) {
				Main.data.set("Spawn.X", p.getLocation().getX());
				Main.data.set("Spawn.Y", p.getLocation().getY());
				Main.data.set("Spawn.Z", p.getLocation().getZ());
				Main.data.set("Spawn.Pitch", p.getLocation().getPitch());
				Main.data.set("Spawn.Yaw", p.getLocation().getYaw());
				Main.data.save();
				p.sendMessage(ChatColor.DARK_GREEN + "Spawn successfully set!");
				Locations.reload();
			} else if (args[0].equalsIgnoreCase("SetHub")) {
				Main.data.set("Hub.World", p.getLocation().getWorld().getName());
				Main.data.set("Hub.X", p.getLocation().getX());
				Main.data.set("Hub.Y", p.getLocation().getY());
				Main.data.set("Hub.Z", p.getLocation().getZ());
				Main.data.set("Hub.Pitch", p.getLocation().getPitch());
				Main.data.set("Hub.Yaw", p.getLocation().getYaw());
				Main.data.save();
				p.sendMessage(ChatColor.DARK_GREEN + "Hub successfully set!");
				Locations.reload();
			} else if (args[0].equals("reload")) {
				Main.reloadConfigs();
				p.sendMessage(ChatColor.GREEN + "Configuration files reloaded!");
			} else
				Main.displayHelp(p, 1);
		} else
			Main.displayHelp(p, 1);

		return true;
	}
}
