package me.MnMaxon.Commands;

import me.MnMaxon.LonksKits.Main;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DonateExecuter implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command command, String label, String[] args) {
		s.sendMessage(ChatColor.GOLD + ">>> " + ChatColor.GREEN + Main.config.getString("Donate") + " "
				+ ChatColor.GOLD + " <<<");
		return true;
	}

}
