package me.MnMaxon.Commands;

import me.MnMaxon.LonksKits.Messages;
import me.MnMaxon.LonksKits.Points;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PointExecuter implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command command, String label, String[] args) {
		if (args.length == 0){
			if(!(s instanceof Player)) return true;
			int points = Points.get(s.getName());
			Player p = (Player)s;
			if(points > 0){
				int rank = Points.positions.indexOf(p.getName())+1;
				s.sendMessage(ChatColor.GOLD + "[" + ChatColor.AQUA + "LonksKits" + ChatColor.GOLD + "] " + ChatColor.GREEN 
					+ "You are " + ChatColor.YELLOW + rank + Points.getPlaceSuffix(rank)
					+ ChatColor.RESET + ChatColor.GREEN
					+ " with " + ChatColor.YELLOW + points + ChatColor.RESET + ChatColor.GREEN
					+ " points");
			}
			else
				s.sendMessage(ChatColor.GOLD + "[" + ChatColor.AQUA + "LonksKits" + ChatColor.GOLD + "] " + ChatColor.GREEN
						+ "You have " + ChatColor.YELLOW + "0" + ChatColor.RESET + ChatColor.GREEN
						+ " points");
		}
		else if (args.length == 1
				&& (args[0].equalsIgnoreCase("Highscore") || args[0].equalsIgnoreCase("Highscores") || args[0]
						.equalsIgnoreCase("top")))

			Points.sendHighscore(s);

		else if (args.length == 2 && args[0].equalsIgnoreCase("show"))

			if (Bukkit.getOfflinePlayer(args[1]) != null){
				int points = Points.get(args[1]);
				if(points > 0){
					int rank = Points.positions.indexOf(Bukkit.getOfflinePlayer(args[1]).getName())+1;
				s.sendMessage(ChatColor.GOLD + "[" + ChatColor.AQUA + "LonksKits" + ChatColor.GOLD + "] "
						+ ChatColor.GREEN + Bukkit.getOfflinePlayer(args[1]).getName() + ChatColor.GREEN 
						+ " is " + ChatColor.YELLOW + rank + Points.getPlaceSuffix(rank)
						+ ChatColor.RESET + ChatColor.GREEN
						+ " with " + ChatColor.YELLOW + points + ChatColor.GREEN
						+ " points");
				}
				else
					s.sendMessage(ChatColor.GOLD + "[" + ChatColor.AQUA + "LonksKits" + ChatColor.GOLD + "] "
							+ ChatColor.GREEN + Bukkit.getOfflinePlayer(args[1]).getName() + ChatColor.GREEN 
							+ " has " + ChatColor.YELLOW + "0" + ChatColor.GREEN
							+ " points");
					
			}
			else{
				int points = Points.get(args[1]);
				if(points > 0){
					int rank = Points.positions.indexOf(args[1])+1;
					s.sendMessage(ChatColor.GOLD + "[" + ChatColor.AQUA + "LonksKits" + ChatColor.GOLD + "] "
							+ ChatColor.GREEN + args[1] + ChatColor.GREEN 
							+ " is " + ChatColor.YELLOW + rank + Points.getPlaceSuffix(rank)
							+ ChatColor.RESET + ChatColor.GREEN
							+ " with " + ChatColor.YELLOW + points + ChatColor.GREEN
							+ " points");
				}else{
					s.sendMessage(ChatColor.GOLD + "[" + ChatColor.AQUA + "LonksKits" + ChatColor.GOLD + "] "
							+ ChatColor.DARK_RED + args[1] + ChatColor.GREEN + " has " + ChatColor.YELLOW
							+ "0" + ChatColor.GREEN + " points");
				}
			}

		else if (args.length != 3)

			Points.help(s);

		else if (args[0].equalsIgnoreCase("set"))

			if (s.hasPermission("points.set")) {
				try {
					Points.set(args[1], Integer.parseInt(args[2]));
					s.sendMessage(ChatColor.GOLD + "[" + ChatColor.AQUA + "LonksKits" + ChatColor.GOLD + "] "
							+ ChatColor.GREEN + "You set " + args[1] + "'s points to " + args[2]);
				} catch (NumberFormatException e) {
					s.sendMessage(ChatColor.GOLD + "[" + ChatColor.AQUA + "LonksKits" + ChatColor.GOLD + "] "
							+ ChatColor.DARK_RED + args[2] + " is not a number");
				}
			} else
				s.sendMessage(Messages.PERMISSION_COMMAND);

		else if (args[0].equalsIgnoreCase("add"))

			if (s.hasPermission("points.add")) {
				try {
					Points.add(args[1], Integer.parseInt(args[2]));
					s.sendMessage(ChatColor.GOLD + "[" + ChatColor.AQUA + "LonksKits" + ChatColor.GOLD + "] "
							+ ChatColor.GREEN + "You added " + args[2] + " points to " + args[1]);
				} catch (NumberFormatException e) {
					s.sendMessage(ChatColor.GOLD + "[" + ChatColor.AQUA + "LonksKits" + ChatColor.GOLD + "] "
							+ ChatColor.DARK_RED + args[2] + " is not a number");
				}
			} else
				s.sendMessage(Messages.PERMISSION_COMMAND);

		else if (args[0].equalsIgnoreCase("remove"))

			if (s.hasPermission("points.remove")) {
				try {
					if (Points.remove(args[1], Integer.parseInt(args[2])))
						s.sendMessage(ChatColor.GOLD + "[" + ChatColor.AQUA + "LonksKits" + ChatColor.GOLD + "] "
								+ ChatColor.GREEN + "You removed " + args[2] + " points from " + args[1]);
					else
						s.sendMessage(ChatColor.GOLD + "[" + ChatColor.AQUA + "LonksKits" + ChatColor.GOLD + "] "
								+ ChatColor.DARK_RED + args[1] + " does not have " + args[2] + " points");
				} catch (NumberFormatException e) {
					s.sendMessage(ChatColor.GOLD + "[" + ChatColor.AQUA + "LonksKits" + ChatColor.GOLD + "] "
							+ ChatColor.DARK_RED + args[2] + " is not a number");
				}
			} else
				s.sendMessage(Messages.PERMISSION_COMMAND);

		else if (args[0].equals("help"))

			Points.help(s);

		else

			s.sendMessage(ChatColor.GOLD + "[" + ChatColor.AQUA + "LonksKits" + ChatColor.GOLD + "] " + ChatColor.GREEN
					+ args[0] + " has " + ChatColor.YELLOW + Points.get(s.getName()) + ChatColor.RESET
					+ ChatColor.GREEN + " points");

		return true;
	}

}
