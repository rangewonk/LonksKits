package me.MnMaxon.Commands;

import me.MnMaxon.LonksKits.Locations;
import me.MnMaxon.LonksKits.Messages;
import me.MnMaxon.LonksKits.MetaLists;
import me.MnMaxon.LonksKits.Points;
import me.MnMaxon.LonksKits.TpCountdown;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitPvPExecuter implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "You must be a player to do this!");
			return true;
		}
		Player p = (Player) sender;
		if (args.length == 1
				&& (args[0].equals("highscores") || args[0].equals("highscore") || args[0].equalsIgnoreCase("top"))) {
			Points.sendHighscore(p);
		} else {
			if (!MetaLists.PLAYERS.contains(p)) {
				TpCountdown.teleport(p, Locations.spawn);
			} else
				p.sendMessage(Messages.PREFIX + ChatColor.DARK_RED + "You are already in kitpvp!");
		}
		return true;
	}

}
