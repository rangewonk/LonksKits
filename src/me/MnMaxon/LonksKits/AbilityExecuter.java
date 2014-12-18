package me.MnMaxon.LonksKits;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AbilityExecuter implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command command, String label, String[] args) {
		if (args.length == 0)
			sendMessages(s);
		else if (args[0].equalsIgnoreCase("see")) {
			if (args.length == 1) {
				if (!(s instanceof Player))
					s.sendMessage("Use like: /Ability See [Player_Name]");
				else
					sendSeeInfo(s, (Player) s);
			} else {
				if (Main.hasPermision(s, "Shop.see")) {
					OfflinePlayer oP = Bukkit.getOfflinePlayer(args[1]);
					if (oP == null) {
						s.sendMessage(Messages.PREFIX + ChatColor.RED + args[1] + " does not exist");
					} else {
						sendSeeInfo(s, oP);
					}
				} else {
					s.sendMessage(Messages.PERMISSION_COMMAND);
					sendSeeInfo(s, (Player) s);
				}
			}
		} else if (args[0].equalsIgnoreCase("remove")) {
			if (Main.hasPermision(s, "Shop.remove")) {
				if (args.length == 1) {
					if (!(s instanceof Player))
						s.sendMessage("Use like: /Ability Remove [Player_Name]");
					else {
						Shop.remove((Player) s);
						s.sendMessage(Messages.PREFIX + ChatColor.GREEN + "Your abilities were removed");
					}
				} else {
					OfflinePlayer oP = Bukkit.getOfflinePlayer(args[1]);
					if (oP == null) {
						s.sendMessage(Messages.PREFIX + ChatColor.RED + args[1] + " does not exist");
					} else {
						Shop.remove(oP);
						s.sendMessage(Messages.PREFIX + ChatColor.GREEN + oP.getName() + "'s abilities were removed");
					}
				}
			} else
				s.sendMessage(Messages.PERMISSION_COMMAND);
		} else
			sendMessages(s);
		return true;
	}

	private void sendSeeInfo(CommandSender sender, OfflinePlayer target) {
		sender.sendMessage(ChatColor.GOLD + target.getName() + "'s Abilities:");
		ArrayList<String> abilities = Shop.getAbilities(target);
		if (abilities.size() == 0)
			sender.sendMessage(ChatColor.RED + "NONE!");
		else {
			for (String ability : abilities)
				sender.sendMessage(ChatColor.GREEN + ability);
		}
	}

	private void sendMessages(CommandSender s) {
		s.sendMessage(ChatColor.GOLD + "===== " + ChatColor.AQUA + ChatColor.BOLD + "Ability Help" + ChatColor.RESET
				+ ChatColor.GOLD + " =====");
		s.sendMessage(ChatColor.AQUA + "/Ability See [PLAYER_NAME]" + ChatColor.DARK_PURPLE + " - " + ChatColor.AQUA
				+ "Allows you to see players' abilities");
		s.sendMessage(ChatColor.AQUA + "/Ability Remove [PLAYER_NAME]" + ChatColor.DARK_PURPLE + " - " + ChatColor.AQUA
				+ "Allows you to see players' abilities");
	}
}
