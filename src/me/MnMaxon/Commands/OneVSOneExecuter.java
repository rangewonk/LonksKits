package me.MnMaxon.Commands;

import java.util.ArrayList;
import java.util.Random;

import me.MnMaxon.Apis.EssentialsMethods;
import me.MnMaxon.Apis.VanishAPI;
import me.MnMaxon.Kits.Kit;
import me.MnMaxon.Kits.OneVSOne;
import me.MnMaxon.LonksKits.Death;
import me.MnMaxon.LonksKits.Locations;
import me.MnMaxon.LonksKits.Main;
import me.MnMaxon.LonksKits.Messages;
import me.MnMaxon.LonksKits.MetaLists;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class OneVSOneExecuter implements CommandExecutor {

	@SuppressWarnings("unchecked")
	@Override
	public boolean onCommand(CommandSender s, Command command, String label, String[] args) {
		if (Locations.gameWorld != null && s instanceof Player && !((Player) s).getWorld().equals(Locations.gameWorld)) {
			s.sendMessage(Messages.WRONG_WORLD);
			return true;
		}
		if (args.length == 0) {
			sendHelp(s);
			return true;
		}
		if (!(s instanceof Player)) {
			s.sendMessage(Messages.PREFIX + ChatColor.RED + "You must be a player to do this!");
			sendHelp(s);
			return true;
		}
		final Player p = (Player) s;

		if (args[0].equalsIgnoreCase("accept")) {
			if (args.length == 2) {
				final Player target = Bukkit.getPlayer(args[1]);
				if (target == null || !target.isOnline()) {
					p.sendMessage(Messages.PREFIX + ChatColor.RED + args[1] + " is offline");
					return true;
				} else if (!target.isValid() || !p.isValid()) {
					p.sendMessage(Messages.PREFIX + ChatColor.RED + target.getName() + " is dead");
					return true;
				} else if (Locations.loc1 == null || Locations.loc2 == null) {
					p.sendMessage(Messages.PREFIX + ChatColor.RED + "1v1 has not been set up yet");
					return true;
				}
				if (MetaLists.ONE_V_ONE_WAITING_RESPONSE.contains(target)
						&& MetaLists.ONE_V_ONE_WAITING_RESPONSE.get(target).equals(p.getName())) {
					start(target, p);
				} else {
					p.sendMessage(Messages.PREFIX + ChatColor.RED + "You do not have a request from " + ChatColor.GREEN
							+ target.getName());
					return true;
				}
			} else {
				p.sendMessage(Messages.PREFIX + ChatColor.RED + "Use like: " + ChatColor.GREEN
						+ "/1v1 Accept [Player_Name]");
				return true;
			}
		} else if (args[0].equalsIgnoreCase("set1") || args[0].equalsIgnoreCase("loc1")) {
			if (!p.hasPermission("1v1.Set")) {
				p.sendMessage(Messages.PERMISSION_COMMAND);
				return true;
			}
			Main.data.set("1v1.Loc1.X", p.getLocation().getX());
			Main.data.set("1v1.Loc1.Y", p.getLocation().getY());
			Main.data.set("1v1.Loc1.Z", p.getLocation().getZ());
			Main.data.set("1v1.Loc1.Pitch", p.getLocation().getPitch());
			Main.data.set("1v1.Loc1.Yaw", p.getLocation().getYaw());
			Main.data.save();
			Locations.reload();
			p.sendMessage(Messages.PREFIX + ChatColor.GREEN + "Point set!");
		} else if (args[0].equalsIgnoreCase("set2") || args[0].equalsIgnoreCase("loc2")) {
			if (!p.hasPermission("1v1.Set")) {
				p.sendMessage(Messages.PERMISSION_COMMAND);
				return true;
			}
			Main.data.set("1v1.Loc2.X", p.getLocation().getX());
			Main.data.set("1v1.Loc2.Y", p.getLocation().getY());
			Main.data.set("1v1.Loc2.Z", p.getLocation().getZ());
			Main.data.set("1v1.Loc2.Pitch", p.getLocation().getPitch());
			Main.data.set("1v1.Loc2.Yaw", p.getLocation().getYaw());
			Main.data.save();
			Locations.reload();
			p.sendMessage(Messages.PREFIX + ChatColor.GREEN + "Point set!");
		} else if (args.length == 1) {
			if (Bukkit.getPlayer(args[0]) != null) {
				if (MetaLists.ONE_V_ONE_WAITING_RESPONSE.contains(p)) {
					p.sendMessage(Messages.PREFIX + ChatColor.RED + "You are already waiting for "
							+ MetaLists.ONE_V_ONE_WAITING_RESPONSE.get(p) + "'s response!");
					return true;
				}
				final Player target = Bukkit.getPlayer(args[0]);
				if (p.equals(target)) {
					p.sendMessage(Messages.PREFIX + ChatColor.RED + "You can't  fight yourself!");
					return true;
				} else if (Death.getInfo(p) != null) {
					p.sendMessage(Messages.PREFIX + ChatColor.RED + "You're in PvP!");
					return true;
				} else if (Death.getInfo(target) != null) {
					p.sendMessage(Messages.PREFIX + ChatColor.RED + target.getName() + "is in PvP!");
					return true;
				}
				int requestTime = 10;
				p.sendMessage(Messages.PREFIX + ChatColor.GOLD + "You have challenged " + target.getName() + " to 1v1");
				target.sendMessage(Messages.PREFIX + ChatColor.GOLD + p.getName() + " has challenged you in 1v1");
				target.sendMessage(ChatColor.GOLD + "Type: " + ChatColor.GREEN + "/1v1 Accept " + p.getName()
						+ ChatColor.GOLD + " to accept");
				target.sendMessage(ChatColor.GOLD + "This request will expire in " + requestTime + " seconds");
				ArrayList<String> names = new ArrayList<String>();
				if (MetaLists.ONE_V_ONE_REQUEST.contains(target))
					names = (ArrayList<String>) MetaLists.ONE_V_ONE_REQUEST.get(target);
				names.add(p.getName());
				MetaLists.ONE_V_ONE_REQUEST.add(target, names);
				MetaLists.ONE_V_ONE_WAITING_RESPONSE.add(p, target.getName());
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
					@Override
					public void run() {
						if (MetaLists.ONE_V_ONE_REQUEST.contains(target)) {
							ArrayList<String> nameList = (ArrayList<String>) MetaLists.ONE_V_ONE_REQUEST.get(target);
							if (nameList.contains(p.getName())) {
								nameList.remove(p.getName());
								if (target.isOnline())
									target.sendMessage(Messages.PREFIX + ChatColor.RED + "Your 1v1 request from "
											+ ChatColor.GREEN + p.getName() + ChatColor.RED + " has expired!");
								if (nameList.isEmpty())
									MetaLists.ONE_V_ONE_REQUEST.add(target, nameList);
								else
									MetaLists.ONE_V_ONE_REQUEST.remove(target);
							}
						}

						if (MetaLists.ONE_V_ONE_WAITING_RESPONSE.contains(p)) {
							p.sendMessage(Messages.PREFIX + ChatColor.RED + "Your 1v1 request to " + ChatColor.GREEN
									+ target.getName() + ChatColor.RED + " has been declined!");
							MetaLists.ONE_V_ONE_WAITING_RESPONSE.remove(p);
						}
					}
				}, requestTime * 20L);
			} else if (Bukkit.getOfflinePlayer(args[0]) != null)
				s.sendMessage(Messages.PREFIX + ChatColor.DARK_RED + Bukkit.getOfflinePlayer(args[0]).getName()
						+ " is not online");
			else
				s.sendMessage(Messages.PREFIX + ChatColor.DARK_RED + args[0]
						+ " has never played on this server before");
		} else
			sendHelp(s);
		return true;
	}

	private void start(Player tempP1, Player tempP2) {
		if (new Random().nextBoolean()) {
			Player tmpPlayer = tempP2;
			tempP2 = tempP1;
			tempP1 = tmpPlayer;
		}
		final Player p1 = tempP1;
		final Player p2 = tempP2;
		MetaLists.ONE_V_ONE_WAITING_RESPONSE.remove(p1);
		MetaLists.ONE_V_ONE_REQUEST.remove(p1);
		MetaLists.ONE_V_ONE_WAITING_RESPONSE.remove(p2);
		MetaLists.ONE_V_ONE_REQUEST.remove(p2);
		MetaLists.ONE_V_ONE_FIGHT.add(p1, p2.getName());
		MetaLists.ONE_V_ONE_FIGHT.add(p2, p1.getName());
		p1.teleport(Locations.loc1);
		p2.teleport(Locations.loc2);
		for (Entity ent : p1.getNearbyEntities(200, 200, 200))
			if (ent instanceof Player) {
				Player p = (Player) ent;
				if (!p.equals(p1) && !p.equals(p2) && MetaLists.ONE_V_ONE_FIGHT.contains(p)) {
					p.hidePlayer(p1);
					p1.hidePlayer(p);
					p.hidePlayer(p2);
					p2.hidePlayer(p);
				}
			}
		p1.showPlayer(p2);
		p2.showPlayer(p1);
		String kit = new OneVSOne().getName();
		Main.ClearKit(p1);
		Main.ClearKit(p2);
		Kit.setClass(p1, kit);
		Kit.setClass(p2, kit);
		VanishAPI.setVanish(p1, false);
		VanishAPI.setVanish(p2, false);
		// TODO add unvanish code
		EssentialsMethods.setGodMode(p1, false);
		EssentialsMethods.setGodMode(p2, false);
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
			public void run() {
				p1.teleport(Locations.loc1);
				p2.teleport(Locations.loc2);
			}
		}, 20L);
	}

	private void sendHelp(CommandSender s) {
		s.sendMessage(ChatColor.DARK_PURPLE + "/1v1 [PlayerName]" + ChatColor.GREEN + "-" + ChatColor.AQUA
				+ " Challenge a player to a 1v1 fight");
		s.sendMessage(ChatColor.DARK_PURPLE + "/1v1 Accept [PlayerName]" + ChatColor.GREEN + "-" + ChatColor.AQUA
				+ " Accept a 1v1 challenge");
		s.sendMessage(ChatColor.DARK_PURPLE + "/1v1 Set1" + ChatColor.GREEN + "-" + ChatColor.AQUA
				+ " Sets first spawn point");
		s.sendMessage(ChatColor.DARK_PURPLE + "/1v1 Set2" + ChatColor.GREEN + "-" + ChatColor.AQUA
				+ " Sets first second point");
	}
}
