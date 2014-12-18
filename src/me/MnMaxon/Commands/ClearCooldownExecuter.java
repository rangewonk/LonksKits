package me.MnMaxon.Commands;

import me.MnMaxon.LonksKits.Main;
import me.MnMaxon.LonksKits.Messages;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearCooldownExecuter implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command command, String label, String[] args) {
		if (!(s instanceof Player)) {
			if (args.length == 1) {
				if (Bukkit.getPlayer(args[0]) != null) {
					Main.ClearCooldown(Bukkit.getPlayer(args[0]));
					s.sendMessage(ChatColor.GREEN + Bukkit.getPlayer(args[0]).getName() + "'s cooldown was cleared");
					Bukkit.getPlayer(args[0]).sendMessage(ChatColor.GREEN + "Cooldown cleared");
				} else
					s.sendMessage(ChatColor.RED + args[0] + " is not online");
			} else
				s.sendMessage(ChatColor.RED + "Use like /cc [Player]");
		} else {
			Player p = (Player) s;
			if (args.length == 0) {
				if (p.hasPermission("Lonkskit.cmd.clearcooldown")) {
					Main.ClearCooldown(p);
					p.sendMessage(Messages.CLEAR_COOLDOWN);
				} else
					p.sendMessage(Messages.PERMISSION_COMMAND);
			} else if (args.length == 1) {
				if (p.isOp()) {
					if (Bukkit.getPlayer(args[0]) != null) {
						Main.ClearCooldown(Bukkit.getPlayer(args[0]));
						p.sendMessage(ChatColor.GREEN + Bukkit.getPlayer(args[0]).getName() + "'s cooldown was cleared");
						Bukkit.getPlayer(args[0]).sendMessage(ChatColor.GREEN + "Cooldown cleared");
					} else
						p.sendMessage(ChatColor.DARK_RED + args[0] + " is not online");
				} else
					p.sendMessage(Messages.PERMISSION_COMMAND);
			} else {
				String opMessage = "";
				if (p.isOp())
					opMessage = " or /cc [Player]";
				p.sendMessage(ChatColor.DARK_RED + "Use like /cc" + opMessage);
			}
		}
		return true;
	}

}
