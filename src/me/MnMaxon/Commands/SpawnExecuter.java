package me.MnMaxon.Commands;

import me.MnMaxon.LonksKits.Death;
import me.MnMaxon.LonksKits.Locations;
import me.MnMaxon.LonksKits.MetaLists;
import me.MnMaxon.LonksKits.TpCountdown;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnExecuter implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "You must be a player to do this!");
			return true;
		}
		Player p = (Player) sender;

		if (MetaLists.PLAYERS.contains(p)) {
			if (Locations.gameWorld != null && p.getLocation().getWorld().equals(Locations.gameWorld)) {
				if (Death.getInfo(p) != null) {
					p.sendMessage(ChatColor.DARK_RED + "You're in pvp!");
					return true;
				}
			}
		}
		TpCountdown.teleport(p, Locations.hub);
		return true;
	}
}
