package me.MnMaxon.Listeners;

import java.util.ArrayList;
import java.util.Map.Entry;

import me.MnMaxon.LonksKits.MetaLists;
import me.MnMaxon.Kits.DemoMan;
import me.MnMaxon.Kits.Tele;
import me.MnMaxon.LonksKits.CloneBlock;
import me.MnMaxon.LonksKits.Cooldown;
import me.MnMaxon.LonksKits.Death;
import me.MnMaxon.LonksKits.Locations;
import me.MnMaxon.LonksKits.Main;
import me.MnMaxon.LonksKits.Points;
import me.MnMaxon.LonksKits.Spectator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.material.MaterialData;

public class BlockListener implements Listener {
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		if (Locations.gameWorld == null || !e.getPlayer().getWorld().equals(Locations.gameWorld))
			return;
		e.setCancelled(true);
		if (MetaLists.BYPASS_BUILD.contains(e.getPlayer()))
			e.setCancelled(false);
		if (MetaLists.PLAYERS.get(e.getPlayer()) instanceof DemoMan
				&& e.getBlockPlaced().getType().equals(Material.STONE_PLATE)) {
			int mines = 0;
			ArrayList<Block> remover = new ArrayList<Block>();
			for (Entry<Block, String> entry : Main.mines.entrySet())
				if (entry.getValue().equals(e.getPlayer().getName()))
					if (entry.getKey().getType().equals(Material.STONE_PLATE))
						mines++;
					else {
						remover.add(entry.getKey());
					}
			for (Block block : remover)
				Main.mines.remove(block);
			if (mines < 5) {
				Material type = e.getBlockPlaced().getRelative(0, -1, 0).getType();
				if (!Locations.inSafe(e.getBlock().getLocation())
						&& !e.getBlockReplacedState().getType().equals(Material.LONG_GRASS)
						&& !Locations.inSafe(e.getPlayer().getLocation())
						&& (type.equals(Material.GRASS) || type.equals(Material.NETHER_BRICK)
								|| type.equals(Material.ENDER_STONE) || type.equals(Material.NETHERRACK)
								|| type.equals(Material.MYCEL) || type.equals(Material.SOUL_SAND)
								|| type.equals(Material.OBSIDIAN) || type.equals(Material.SAND)
								|| type.equals(Material.DIRT) || type.equals(Material.SPONGE) || type
									.equals(Material.SMOOTH_BRICK))
						&& !e.getBlock().getRelative(BlockFace.NORTH).getType().equals(Material.CACTUS)
						&& !e.getBlock().getRelative(BlockFace.SOUTH).getType().equals(Material.CACTUS)
						&& !e.getBlock().getRelative(BlockFace.EAST).getType().equals(Material.CACTUS)
						&& !e.getBlock().getRelative(BlockFace.WEST).getType().equals(Material.CACTUS)) {
					e.getPlayer().sendMessage(ChatColor.DARK_GREEN + "Trap Set!");
					Main.mines.put(e.getBlock(), e.getPlayer().getName());
					Main.changeBack.put(e.getBlock().getLocation(), new CloneBlock(Material.AIR));
					e.setCancelled(false);
				} else {
					e.setCancelled(true);
					e.getPlayer().sendMessage(ChatColor.DARK_RED + "You cannot place a trap there.");
				}
			} else {
				e.setCancelled(true);
				e.getPlayer().sendMessage(ChatColor.DARK_RED + "You have already placed 5 traps!");
			}
		} else if (MetaLists.PLAYERS.get(e.getPlayer()) instanceof Tele
				&& e.getBlock().getType().equals(Material.ENDER_PORTAL_FRAME)) {
			if (Locations.inSafe(e.getBlockAgainst().getLocation()) || Locations.inSafe(e.getPlayer().getLocation())
					|| e.getBlockAgainst().getType().equals(Material.SIGN)
					|| e.getBlockAgainst().getType().equals(Material.SIGN_POST)
					|| e.getBlockAgainst().getType().equals(Material.WALL_SIGN)
					|| e.getBlockAgainst().getType().equals(Material.SKULL)) {
				e.getPlayer().sendMessage(ChatColor.DARK_RED + "You cannot place a portal there.");
				return;
			}
			for (Cooldown cd : Main.cool)
				if (cd.player.equals(e.getPlayer()) && cd.ability.equalsIgnoreCase("portal")) {
					e.getPlayer().sendMessage(
							ChatColor.DARK_RED + "You are on cooldown for another " + cd.time + " seconds");
					return;
				}
			final Location loc = e.getBlockAgainst().getLocation();
			if (!(loc.getBlock().getRelative(0, 1, 0).getType().equals(Material.AIR) || e.getBlock().equals(
					loc.getBlock().getRelative(0, 1, 0)))
					|| !loc.getBlock().getRelative(0, 2, 0).getType().equals(Material.AIR)) {
				e.getPlayer().sendMessage(ChatColor.DARK_RED + "There must be 2 blocks of air above the portal");
				return;
			}
			loc.add(.5, 1, .5);
			Main.cool.add(new Cooldown(6, e.getPlayer(), "portal"));
			e.setCancelled(true);
			final Material oldType = e.getBlockAgainst().getType();
			final MaterialData oldData = e.getBlockAgainst().getState().getData();
			Main.changeBack.put(e.getBlockAgainst().getLocation(), new CloneBlock(e.getBlockAgainst()));
			e.getBlockAgainst().setType(Material.ENDER_PORTAL_FRAME);
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
				@Override
				public void run() {
					if (loc.getBlock().getRelative(0, -1, 0).getType().equals(Material.ENDER_PORTAL_FRAME)) {
						for (Entity ent : loc.getWorld().getEntities())
							if (ent.getLocation().getBlock().equals(loc.getBlock().getRelative(0, -1, 0)))
								ent.setVelocity(ent.getVelocity().setY(.2));
						loc.getBlock().getRelative(0, -1, 0).setType(oldType);
						loc.getBlock().getRelative(0, -1, 0).getState().setData(oldData);
						Main.changeBack.remove(loc);
					}
				}
			}, 3 * 20L);
			for (Entity ent : e.getPlayer().getNearbyEntities(5, 256, 5)) {
				if (!Locations.inSafe(ent.getLocation()) && ent instanceof LivingEntity) {
					if (MetaLists.PLAYERS.contains(ent)) {
						if (!(MetaLists.PLAYERS.get(ent) instanceof Tele) && !Spectator.isSpectator((Player) ent)) {
							MetaLists.TP_AROUND.add(ent);
							Death.update((Player) ent, e.getPlayer(), "was tele'd");
							ent.teleport(loc);
						}
					} else if (ent.getPassenger() != null && MetaLists.PLAYERS.contains(ent.getPassenger())) {
						Player p = (Player) ent.getPassenger();
						MetaLists.TP_AROUND.add(p);
						MetaLists.HORSE_AROUND.add(p);
						ent.eject();
						ent.teleport(loc);
						MetaLists.TP_AROUND.add(p);
						ent.setPassenger(p);
						MetaLists.TP_AROUND.remove(p);
					} else
						ent.teleport(loc);
				}
			}
		}
	}

	@EventHandler
	public void onSignChange(SignChangeEvent e) {
		if (e.getLine(0).equalsIgnoreCase("[HighScore]") || e.getLine(0).equalsIgnoreCase("[HighScores]")
				|| e.getLine(0).equalsIgnoreCase("[HS]")) {
			if (!e.getPlayer().isOp()) {
				e.getPlayer().sendMessage(ChatColor.RED + "You do not have permission to do this");
				return;
			}
			for (int x = 0; x < 100; x++)
				if (Main.signData.get("Signs." + x) == null) {
					int place = -1;
					try {
						place = Integer.parseInt(e.getLine(1));
					} catch (NumberFormatException exception) {
						e.getBlock().breakNaturally();
						e.getPlayer().sendMessage(ChatColor.DARK_RED + e.getLine(1) + " is not a number");
						return;
					}
					Main.signData.set("Signs." + x + ".world", e.getBlock().getWorld().getName());
					Main.signData.set("Signs." + x + ".place", place);
					Main.signData.set("Signs." + x + ".x", e.getBlock().getX());
					Main.signData.set("Signs." + x + ".y", e.getBlock().getY());
					Main.signData.set("Signs." + x + ".z", e.getBlock().getZ());
					Main.signData.save();
					ArrayList<Location> locs = new ArrayList<Location>();
					if (Main.signs.containsKey(x))
						locs = Main.signs.get(x);
					locs.add(e.getBlock().getLocation());
					Main.signs.put(place, locs);
					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
						@Override
						public void run() {
							Points.updateSigns(Points.highScore());
						}
					}, 1L);
					return;
				}
		}
	}

	@EventHandler
	public void onBreak(final BlockBreakEvent e) {
		if (Locations.gameWorld != null & e.getPlayer().getWorld().equals(Locations.gameWorld)
				&& !MetaLists.BYPASS_BUILD.contains(e.getPlayer())) {
			e.setCancelled(true);
			if (e.getPlayer().getLocation().add(0, .1, 0).getBlock().getRelative(0, -1, 0).getY() == e.getBlock()
					.getY() && e.getBlock().getType().isSolid()) {
				MetaLists.TP_AROUND.add(e.getPlayer());
				final Location loc = e.getPlayer().getLocation();
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
					@Override
					public void run() {
						if (e.getPlayer().isValid() && e.getPlayer().getLocation().getY() < loc.getY()) {
							MetaLists.TP_AROUND.add(e.getPlayer());
							e.getPlayer().teleport(loc.add(0, .2, 0));
						}
					}
				}, 2L);
			}
		}
	}
}
