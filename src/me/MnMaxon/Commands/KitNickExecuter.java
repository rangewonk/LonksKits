package me.MnMaxon.Commands;

import me.MnMaxon.LonksKits.Locations;
import me.MnMaxon.LonksKits.Main;
import me.MnMaxon.LonksKits.Messages;
import me.MnMaxon.LonksKits.NickName;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitNickExecuter implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command command, String label, String[] args) {
		if (args.length != 2) {
			s.sendMessage(ChatColor.DARK_RED + "Use like /kitnick [PLAYER_NAME] [NICK_NAME/reset]");
			return true;
		}
		if (!(s instanceof Player) || ((Player) s).hasPermission("Kitpvp.nickname")) {
			if (Bukkit.getPlayer(args[0]) != null) {
				if (args[1].equalsIgnoreCase("off") || args[1].equalsIgnoreCase("reset")) {
					NickName.set(Bukkit.getPlayer(args[0]), null);
					s.sendMessage(ChatColor.GREEN + Bukkit.getPlayer(args[0]).getName() + "'s nickname was reset");
				} else {
					NickName.set(Bukkit.getPlayer(args[0]), args[1]);
					s.sendMessage(ChatColor.GREEN + Bukkit.getPlayer(args[0]).getName() + "'s nickname is now "
							+ Main.addColor(args[1]));
				}
				if (Locations.gameWorld != null && Bukkit.getPlayer(args[0]).equals(Locations.gameWorld))
					NickName.setCustomName(Bukkit.getPlayer(args[0]));
			} else
				s.sendMessage(ChatColor.DARK_RED + args[0] + " is not online");
		} else
			s.sendMessage(Messages.PERMISSION_COMMAND);

		return true;
	}

}
