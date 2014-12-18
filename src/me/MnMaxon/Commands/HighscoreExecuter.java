package me.MnMaxon.Commands;

import me.MnMaxon.LonksKits.Points;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class HighscoreExecuter implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Points.sendHighscore(sender);
		return true;
	}

}
