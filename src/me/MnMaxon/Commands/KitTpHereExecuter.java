package me.MnMaxon.Commands;

import me.MnMaxon.LonksKits.Locations;
import me.MnMaxon.LonksKits.Messages;
import me.MnMaxon.LonksKits.MetaLists;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitTpHereExecuter implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "You must be a player to do this!");
			return true;
		}
		Player p = (Player) sender;

		if (p.hasPermission("Lonkskit.tphere"))
			if (args.length == 1)
				if (Bukkit.getServer().getPlayer(args[0]) != null)
					if (Locations.gameWorld != null && Locations.gameWorld.equals(Bukkit.getServer().getPlayer(args[0]).getWorld()))
						if (Locations.gameWorld.equals(p.getWorld())) {
							MetaLists.TP_AROUND.add(Bukkit.getServer().getPlayer(args[0]));
							Bukkit.getServer().getPlayer(args[0]).teleport(p.getLocation());
							p.sendMessage(ChatColor.GOLD + "[" + ChatColor.AQUA + "LonksKits" + ChatColor.GOLD + "] "
									+ ChatColor.GREEN + Bukkit.getServer().getPlayer(args[0]).getName()
									+ " has been teleported to your location");
						} else
							p.sendMessage("You are not in the right world to do this!");
					else
						p.sendMessage(Bukkit.getServer().getPlayer(args[0]).getName()
								+ " is not in the right world for you to do this!");
				else
					p.sendMessage(ChatColor.DARK_RED + args[0] + " is not online");
			else
				p.sendMessage(ChatColor.DARK_RED + "Use like: /kittphere [PLAYER]");
		else
			p.sendMessage(Messages.PERMISSION_COMMAND);

		return true;
	}

}
