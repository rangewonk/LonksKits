package me.MnMaxon.Commands;

import me.MnMaxon.LonksKits.Locations;
import me.MnMaxon.LonksKits.Messages;
import me.MnMaxon.LonksKits.MetaLists;
import me.MnMaxon.LonksKits.TpCountdown;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitTpExecuter implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "You must be a player to do this!");
			return true;
		}
		Player p = (Player) sender;
		if (p.hasPermission("Lonkskit.tp"))
			if (args.length == 1)
				if (Bukkit.getServer().getPlayer(args[0]) != null) {
					if (Locations.gameWorld != null && Locations.gameWorld.equals(p.getLocation().getWorld())) {
						MetaLists.TP_AROUND.add(p);
						TpCountdown.teleport(p, Bukkit.getServer().getPlayer(args[0]).getLocation());
						p.sendMessage(ChatColor.GOLD + "[" + ChatColor.AQUA + "LonksKits" + ChatColor.GOLD + "] "
								+ ChatColor.GREEN + "You have been teleported to "
								+ Bukkit.getServer().getPlayer(args[0]).getName());
					} else
						p.sendMessage(ChatColor.GOLD + "[" + ChatColor.AQUA + "LonksKits" + ChatColor.GOLD + "] "
								+ ChatColor.RED + Bukkit.getServer().getPlayer(args[0]).getName()
								+ " is not in the KitPvP world");
				} else
					p.sendMessage(ChatColor.DARK_RED + args[0] + " is not online");
			else
				p.sendMessage(ChatColor.DARK_RED + "Use like: /kittphere [PLAYER]");
		else
			p.sendMessage(Messages.PERMISSION_COMMAND);

		return true;
	}

}
