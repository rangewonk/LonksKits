package me.MnMaxon.Commands;

import me.MnMaxon.Kits.Kit;
import me.MnMaxon.LonksKits.Locations;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitsExecuter implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "You must be a player to do this!");
			return true;
		}
		Player p = (Player) sender;
		if (Locations.gameWorld != null && p.getWorld().equals(Locations.gameWorld))
			Kit.openGui(p, 1);
		return true;
	}
}
