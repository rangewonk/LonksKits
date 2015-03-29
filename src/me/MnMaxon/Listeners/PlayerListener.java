package me.MnMaxon.Listeners;

import java.util.ArrayList;
import java.util.Random;

import me.MnMaxon.Apis.CloneManager;
import me.MnMaxon.Apis.DisguiseCraftMethods;
import me.MnMaxon.Apis.PLib;
import me.MnMaxon.Apis.VanishAPI;
import me.MnMaxon.Kits.BeastMaster;
import me.MnMaxon.Kits.Casper;
import me.MnMaxon.Kits.Cowboy;
import me.MnMaxon.Kits.Fisherman;
import me.MnMaxon.Kits.Hooker;
import me.MnMaxon.Kits.HotHead;
import me.MnMaxon.Kits.John;
import me.MnMaxon.Kits.Hulk;
import me.MnMaxon.Kits.Jumper;
import me.MnMaxon.Kits.Kangaroo;
import me.MnMaxon.Kits.Kit;
import me.MnMaxon.Kits.Mooshroom;
import me.MnMaxon.Kits.Shark;
import me.MnMaxon.Kits.Snowman;
import me.MnMaxon.Kits.Spider;
import me.MnMaxon.LonksKits.Cooldown;
import me.MnMaxon.LonksKits.Death;
import me.MnMaxon.LonksKits.IP;
import me.MnMaxon.LonksKits.Jail;
import me.MnMaxon.LonksKits.Locations;
import me.MnMaxon.LonksKits.Main;
import me.MnMaxon.LonksKits.Messages;
import me.MnMaxon.LonksKits.MetaLists;
import me.MnMaxon.LonksKits.NickName;
import me.MnMaxon.LonksKits.Points;
import me.MnMaxon.LonksKits.Spectator;
import me.MnMaxon.LonksKits.TpCountdown;
import me.confuser.barapi.BarAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class PlayerListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onTeleport(PlayerTeleportEvent e) {
		if (Locations.loc1 != null && Locations.loc2 != null
				&& (e.getTo().equals(Locations.loc1) || e.getTo().equals(Locations.loc2)))
			return;
		if (CloneManager.isClone(e.getPlayer()))
			return;
		MetaLists.TP_AROUND_PLOT.remove(e.getPlayer());
		TpCountdown.cancel(e.getPlayer());
		BarAPI.removeBar(e.getPlayer());
		Points.update(e.getPlayer().getName(), e.getTo().getWorld());
		if (!e.getPlayer().getGameMode().equals(GameMode.CREATIVE) && !Spectator.isSpectator(e.getPlayer())) {
			e.getPlayer().setFlying(false);
			e.getPlayer().setAllowFlight(false);
		}
		if (!e.getPlayer().isValid())
			return;
		if (Locations.gameWorld != null && MetaLists.PLAYERS.contains(e.getPlayer())
				&& Locations.gameWorld.equals(e.getFrom().getWorld())
				&& !e.getTo().getWorld().equals(e.getFrom().getWorld()) && !MetaLists.TP_AROUND.contains(e.getPlayer())) {
			e.setTo(e.getPlayer().getLocation());
			e.getPlayer().sendMessage(ChatColor.DARK_RED + "To leave, type /hub");
		} else if (Locations.gameWorld != null && Locations.gameWorld.equals(e.getTo().getWorld())
				&& e.getTo().getWorld() != e.getFrom().getWorld()) {
			final Player fPlayer = e.getPlayer();
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
				@Override
				public void run() {
					if (fPlayer.isOnline() && fPlayer.getWorld().equals(Locations.gameWorld)) {
						Main.ghostManager.addPlayer(fPlayer);
						Main.ClearKit(fPlayer);
						Main.giveSpawnItems(fPlayer);
						MetaLists.PLAYERS.add(fPlayer, null);
					}
				}
			}, 20L);
			if (Locations.spawn != null && !MetaLists.TP_AROUND.contains(e.getPlayer()))
				e.setTo(Locations.spawn);
		}
		if (Locations.gameWorld != null && Locations.gameWorld.equals(e.getFrom().getWorld())
				&& Locations.gameWorld.equals(e.getTo().getWorld()))
			if (!MetaLists.TP_AROUND.contains(e.getPlayer()) && !Locations.inSafe(e.getFrom())) {
				e.setCancelled(true);
				Boolean tpMessage = true;
				if (Cooldown.hasCooldown(e.getPlayer(), "tpMessage", false))
					tpMessage = false;
				Main.cool.add(new Cooldown(10, e.getPlayer(), "tpMessage"));
				if (tpMessage)
					e.getPlayer().sendMessage(Messages.UNABLE_TO_TELEPORT);
			}
		MetaLists.TP_AROUND.remove(e.getPlayer());
		if (!e.isCancelled() && !e.getFrom().equals(e.getTo())) {
			MetaLists.IGNORE_DAMAGE_FALL.remove(e.getPlayer());
			MetaLists.BYPASS_SAFEZONE.remove(e.getPlayer());
		}
		if (!e.isCancelled() && Locations.gameWorld != null && !Locations.gameWorld.equals(e.getTo().getWorld()))
			MetaLists.PLAYERS.remove(e.getPlayer());
		if (Locations.gameWorld != null && Locations.gameWorld.equals(e.getTo().getWorld())
				&& e.getTo().getWorld() != e.getFrom().getWorld()) {
			if (Jail.inJail(e.getPlayer())) {
				e.getPlayer().sendMessage(
						ChatColor.RED + "You are not allowed in kitpvp for another " + Jail.getMessage(e.getPlayer()));
				e.setTo(Locations.hub);
			}
		}
	}

	@EventHandler
	public void onDisconnect(PlayerQuitEvent e) {
		CloneManager.remove(e.getPlayer());
		onInteract.removeMinions(e.getPlayer());
		MetaLists.IGNORE_DAMAGE_FALL.remove(e.getPlayer());
		for (Entity ent : e.getPlayer().getWorld().getEntities())
			if (ent instanceof Wolf && ((Wolf) ent).getCustomName() != null
					&& ((Wolf) ent).getCustomName().contains(e.getPlayer().getName()))
				((Wolf) ent).damage(999);
		MetaLists.CAN_JUMP.remove(e.getPlayer());
		if (MetaLists.PLAYERS.contains(e.getPlayer()) && e.getPlayer().getVehicle() != null)
			e.getPlayer().getVehicle().remove();
		if (Death.getInfo(e.getPlayer()) != null && !MetaLists.KICK_LIST.contains(e.getPlayer()))
			Main.kick(e.getPlayer());
		MetaLists.SPAWN_TO.remove(e.getPlayer());
		Main.ghostManager.removePlayer(e.getPlayer());
		MetaLists.PLAYERS.remove(e.getPlayer());
		MetaLists.KICK_LIST.remove(e.getPlayer());
		MetaLists.BYPASS_BUILD.remove(e.getPlayer());
		Spectator.remove(e.getPlayer());
		MetaLists.BYPASS_SAFEZONE.remove(e.getPlayer());

		if (MetaLists.ONE_V_ONE_FIGHT.contains(e.getPlayer()))
			Main.endOneVOne(e.getPlayer(), (String) MetaLists.ONE_V_ONE_FIGHT.get(e.getPlayer()));
	}

	@EventHandler
	public void onKick(PlayerToggleFlightEvent e) {
		if (MetaLists.PLAYERS.contains(e.getPlayer()) && MetaLists.PLAYERS.get(e.getPlayer()) instanceof Spider
				&& e.getPlayer().getAllowFlight())
			e.setCancelled(true);
	}

	@EventHandler
	public void onKick(PlayerKickEvent e) {
		if (MetaLists.PLAYERS.contains(e.getPlayer()) && MetaLists.PLAYERS.get(e.getPlayer()) instanceof Cowboy
				&& (e.getReason().toLowerCase().contains("fly") || e.getReason().toLowerCase().contains("float"))) {
			e.setCancelled(true);
		}
		Main.ghostManager.removePlayer(e.getPlayer());
		MetaLists.PLAYERS.remove(e.getPlayer());
		MetaLists.KICK_LIST.add(e.getPlayer());
		if (!MetaLists.SPAWN_TO.contains(e.getPlayer()) && Locations.gameWorld != null
				&& e.getPlayer().getWorld().equals(Locations.gameWorld))
			MetaLists.SPAWN_TO.add(e.getPlayer());
	}

	public static boolean nearWeb(Location loc) {
		double range = .3;
		if (loc.clone().add(0, 0, 0).getBlock().getType().equals(Material.WEB)
				|| loc.clone().add(range, 0, 0).getBlock().getType().equals(Material.WEB)
				|| loc.clone().add(-range, 0, 0).getBlock().getType().equals(Material.WEB)
				|| loc.clone().add(range, 0, range).getBlock().getType().equals(Material.WEB)
				|| loc.clone().add(-range, 0, range).getBlock().getType().equals(Material.WEB)
				|| loc.clone().add(-range, 0, -range).getBlock().getType().equals(Material.WEB)
				|| loc.clone().add(0, 0, range).getBlock().getType().equals(Material.WEB)
				|| loc.clone().add(0, 0, -range).getBlock().getType().equals(Material.WEB))
			return true;
		return false;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		final Player p = e.getPlayer();
		if (!e.getTo().getBlock().equals(e.getFrom().getBlock()))
			TpCountdown.cancel(p);
		if (!MetaLists.PLAYERS.contains(p))
			return;

		Object kit = MetaLists.PLAYERS.get(p);
		if (kit instanceof Kangaroo) {

			Block fromBlock = e.getFrom().add(0, -.00000000001, 0).getBlock();
			Block toBlock = e.getTo().add(0, -.00000000001, 0).getBlock();
			if (toBlock.getType().isSolid() && !fromBlock.getType().isSolid() && !MetaLists.CAN_JUMP.contains(p))
				MetaLists.CAN_JUMP.add(p);

		} else if (kit instanceof Spider) {

			if (nearWeb(e.getTo().clone()) || nearWeb(e.getTo().clone().add(0, 1, 0))
					|| nearWeb(e.getTo().clone().add(0, 1.3, 0)) || nearWeb(e.getTo().clone().add(0, -.3, 0))) {
				if (!p.getAllowFlight()) {
					p.setAllowFlight(true);
					p.setFlying(true);
				}

				if (e.getFrom().getY() < e.getTo().getY()) {
					Location newLoc = e.getTo();
					newLoc.setY(e.getFrom().getBlockY());
					e.setCancelled(true);
					MetaLists.TP_AROUND.add(p);
					p.teleport(newLoc);
					return;
				}

				p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 10 * 20, 0));
				p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10 * 20, 1));
			} else if (p.getAllowFlight() && !nearishWeb(e.getTo().clone())
					&& !nearishWeb(e.getTo().clone().add(0, 1, 0)) && !nearishWeb(e.getTo().clone().add(0, 1.3, 0))
					&& !nearishWeb(e.getTo().clone().add(0, -.3, 0))) {
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
					@Override
					public void run() {
						p.setAllowFlight(false);
						p.setFlying(false);
					}
				}, 2L);
			}
		}

		if (e.getTo().getBlock().equals(e.getFrom().getBlock()))
			return;
		if (MetaLists.FREEZE.contains(p) && e.getFrom().getX() != e.getTo().getX()
				&& e.getFrom().getZ() != e.getTo().getZ()) {
			MetaLists.TP_AROUND.add(p);
			Location newLoc = e.getFrom();
			newLoc.setX(newLoc.getBlockX() + 0.5);
			newLoc.setY(newLoc.getBlockY());
			newLoc.setZ(newLoc.getBlockZ() + 0.5);
			e.setTo(newLoc);
			return;
		}
		if (kit instanceof Shark || kit instanceof HotHead || kit instanceof John) {
			if ((p.getLocation().getBlock().getType().equals(Material.WATER)
					|| p.getLocation().getBlock().getType().equals(Material.STATIONARY_WATER)
					|| p.getEyeLocation().getBlock().getType().equals(Material.WATER) || p.getEyeLocation().getBlock()
					.getType().equals(Material.STATIONARY_WATER))
					&& !p.hasPotionEffect(PotionEffectType.SPEED)
					&& !p.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
				if (kit instanceof Shark) {
					p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 10 * 20, 0));
					p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10 * 20, 1));
				} else if (kit instanceof HotHead) {
					p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 6 * 20, 0));
					p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 6 * 20, 0));
				} else if (kit instanceof John) {
					p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 6 * 20, 0));
					p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 6 * 20, 0));
				}
			}
		}
		if (kit instanceof Casper) {
			if (MetaLists.PLAYERS_INVISIBLE.contains(p)) {
				MetaLists.TP_AROUND.add(p);
				Location newLoc = e.getFrom().getBlock().getLocation().add(.5, 0, .5)
						.setDirection(e.getTo().getDirection());
				newLoc.setY(e.getTo().getY());
				e.setTo(newLoc);
			}
		} else if (kit instanceof Jumper) {
			if (e.getFrom().getY() < e.getTo().getY()) {
				Location loc = p.getLocation();
				Block block = loc.add(0, -.05, 0).getBlock();
				if (!MetaLists.CAN_FLY.contains(p)) {
					if (block.getType().equals(Material.AIR)) {
						MetaLists.CAN_FLY.add(p);
						double maxVelocity = .5;
						double x = p.getLocation().getDirection().getX() * maxVelocity;
						double y = 1;
						double z = p.getLocation().getDirection().getZ() * maxVelocity;
						p.setVelocity(new Vector(x, y, z));
					}
				}
				if (!block.getType().equals(Material.AIR))
					MetaLists.CAN_FLY.remove(p);
			}
		} else if (kit instanceof Cowboy) {
			if (Locations.nearSafe(e.getTo())) {
				if (p.getVehicle() != null) {
					MetaLists.TP_AROUND.add(p);
					p.getVehicle().remove();
				}
			} else if (!Locations.nearishSafe(e.getTo())) {
				if (p.getVehicle() == null) {
					Vector velocity = p.getVelocity();
					Horse horse = p.getWorld().spawn(p.getLocation(), Horse.class);
					horse.setAdult();
					horse.setVariant(Variant.HORSE);
					horse.setColor(Horse.Color.BLACK);
					horse.setCanPickupItems(false);
					horse.setTamed(true);
					horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
					horse.getInventory().setArmor(new ItemStack(Material.DIAMOND_BARDING));
					MetaLists.TP_AROUND.add(p);
					horse.setPassenger(p);
					horse.setVelocity(velocity);
				}
			}
		} else if (kit instanceof Mooshroom) {
			if (p.isValid() && e.getTo().getBlock().getRelative(0, -1, 0).getType().equals(Material.MYCEL)
					&& !e.getFrom().getBlock().getRelative(0, -1, 0).equals(e.getTo().getBlock().getRelative(0, -1, 0))
					&& new Random().nextInt(10) == 0)
				if (p.getHealth() + 2 <= p.getMaxHealth())
					p.setHealth(p.getHealth() + 2);
				else if (p.getHealth() + 1 <= p.getMaxHealth())
					p.setHealth(p.getHealth() + 1);
		} else if (kit instanceof Snowman) {
			for (Entity ent : p.getWorld().getEntitiesByClass(Player.class)) {
				if (ent instanceof Player) {
					fixBlock(((Player) ent), p.getLocation().add(0, 0, 0));
					fixBlock(((Player) ent), p.getLocation().add(1, 0, 0));
					fixBlock(((Player) ent), p.getLocation().add(-1, 0, 0));
					fixBlock(((Player) ent), p.getLocation().add(0, 0, 1));
					fixBlock(((Player) ent), p.getLocation().add(0, 0, -1));
					fixBlock(((Player) ent), p.getLocation().add(1, 0, 1));
					fixBlock(((Player) ent), p.getLocation().add(-1, 0, 1));
					fixBlock(((Player) ent), p.getLocation().add(1, 0, -1));
					fixBlock(((Player) ent), p.getLocation().add(-1, 0, -1));
					fixBlock(((Player) ent), p.getLocation().add(1, 0, -1));
					fixBlock(((Player) ent), p.getLocation().add(-1, 0, 1));

					fixBlock(((Player) ent), p.getLocation().add(0, 1, 0));
					fixBlock(((Player) ent), p.getLocation().add(1, 1, 0));
					fixBlock(((Player) ent), p.getLocation().add(-1, 1, 0));
					fixBlock(((Player) ent), p.getLocation().add(0, 1, 1));
					fixBlock(((Player) ent), p.getLocation().add(0, 1, -1));
					fixBlock(((Player) ent), p.getLocation().add(1, 1, 1));
					fixBlock(((Player) ent), p.getLocation().add(-1, 1, 1));
					fixBlock(((Player) ent), p.getLocation().add(1, 1, -1));
					fixBlock(((Player) ent), p.getLocation().add(-1, 1, -1));
					fixBlock(((Player) ent), p.getLocation().add(1, 1, -1));
					fixBlock(((Player) ent), p.getLocation().add(-1, 1, 1));

					fixBlock(((Player) ent), p.getLocation().add(0, -1, 0));
					fixBlock(((Player) ent), p.getLocation().add(1, -1, 0));
					fixBlock(((Player) ent), p.getLocation().add(-1, -1, 0));
					fixBlock(((Player) ent), p.getLocation().add(0, -1, 1));
					fixBlock(((Player) ent), p.getLocation().add(0, -1, -1));
					fixBlock(((Player) ent), p.getLocation().add(1, -1, 1));
					fixBlock(((Player) ent), p.getLocation().add(-1, -1, 1));
					fixBlock(((Player) ent), p.getLocation().add(1, -1, -1));
					fixBlock(((Player) ent), p.getLocation().add(-1, -1, -1));
					fixBlock(((Player) ent), p.getLocation().add(1, -1, -1));
					fixBlock(((Player) ent), p.getLocation().add(-1, -1, 1));
				}
			}
			if ((e.getTo().getBlock().getRelative(0, -1, 0).getType().equals(Material.ICE)
					|| e.getTo().getBlock().getRelative(0, -1, 0).getType().equals(Material.SNOW_BLOCK) || e.getTo()
					.getBlock().getRelative(0, 0, 0).getType().equals(Material.SNOW))
					&& !e.getFrom().getBlock().getRelative(0, -1, 0).equals(e.getTo().getBlock().getRelative(0, -1, 0))
					&& new Random().nextInt(10) == 0) {
				int x = 0, snowballs = 1;
				while (snowballs > 0 && x < p.getInventory().getSize()) {
					if (p.getInventory().getContents()[x] != null) {
						ItemStack is = p.getInventory().getContents()[x].clone();
						is.setAmount(p.getInventory().getContents()[x].getAmount() + 1);
						if (is.getType().equals(Material.SNOW_BALL)
								&& p.getInventory().getContents()[x].getAmount() < 16) {
							p.getInventory().setItem(x, is);
							snowballs--;
						} else
							x++;
					} else
						x++;
				}
				x = 0;
				while (snowballs > 0 && x < p.getInventory().getSize()) {
					if (p.getInventory().getContents()[x] == null) {
						ItemStack is = Kit.easyItem(null, Material.SNOW_BALL, 0, null, snowballs);
						p.getInventory().setItem(x, is);
						snowballs = 0;
					}
					x++;
				}
				if (snowballs != 1)
					p.updateInventory();
			}
		}
		if (Locations.inSafe(e.getFrom())) {
			if (!Locations.inSafe(e.getTo())) {
				if (kit == null && !Spectator.isSpectator(p) && !MetaLists.BYPASS_SAFEZONE.contains(p)
						&& !p.getGameMode().equals(GameMode.CREATIVE)) {
					MetaLists.TP_AROUND.add(p);
					Location newLoc = e.getFrom();
					newLoc.setX(newLoc.getBlockX() + 0.5);
					newLoc.setY(newLoc.getBlockY());
					newLoc.setZ(newLoc.getBlockZ() + 0.5);
					e.setTo(newLoc);

					if (Cooldown.hasCooldown(p, "Kitzone", false))
						return;
					Main.cool.add(new Cooldown(2, p, "Kitzone"));
					p.sendMessage(Messages.SAFEZONE_NO_KIT);
					return;
				}
				if (!Spectator.isSpectator(p) && !MetaLists.BYPASS_SAFEZONE.contains(p)
						&& !p.getGameMode().equals(GameMode.CREATIVE)) {
					Boolean message = true;
					if (Cooldown.hasCooldown(p, "SafeZone", false))
						message = false;
					if (message) {
						Main.cool.add(new Cooldown(2, p, "Safezone"));
						p.sendMessage(Messages.SAFEZONE_LEAVE);
					}
				}
			}
		} else {
			if (Locations.inSafe(e.getTo()) && !Spectator.isSpectator(p) && !p.getGameMode().equals(GameMode.CREATIVE)
					&& !MetaLists.BYPASS_SAFEZONE.contains(p)) {
				Boolean message = true;
				if (Cooldown.hasCooldown(p, "SafeZone", false))
					message = false;
				if (message) {
					Main.cool.add(new Cooldown(3, p, "safezone"));
					p.sendMessage(Messages.SAFEZONE_ENTER);
				}
				Vector last = e.getFrom().toVector();
				int y = 1;
				Vector velocity = Locations.calculateVelocity(e.getTo().toVector(), last, 1);
				double x = velocity.getX();
				double z = velocity.getZ();
				double a = x / (Math.abs(x) + Math.abs(z));
				double b = z / (Math.abs(x) + Math.abs(z));
				double maxVelocity = 1.5;
				velocity = new Vector(a * maxVelocity, y, b * maxVelocity);

				MetaLists.TP_AROUND.add(p);
				Location newLoc = e.getFrom();
				newLoc.setX(newLoc.getBlockX() + 0.5);
				newLoc.setY(newLoc.getBlockY());
				newLoc.setZ(newLoc.getBlockZ() + 0.5);
				e.setTo(newLoc);

				p.setVelocity(velocity);
			}
		}
	}

	private boolean nearishWeb(Location loc) {
		double range = .4;
		if (loc.clone().add(0, 0, 0).getBlock().getType().equals(Material.WEB)
				|| loc.clone().add(range, 0, 0).getBlock().getType().equals(Material.WEB)
				|| loc.clone().add(-range, 0, 0).getBlock().getType().equals(Material.WEB)
				|| loc.clone().add(range, 0, range).getBlock().getType().equals(Material.WEB)
				|| loc.clone().add(-range, 0, range).getBlock().getType().equals(Material.WEB)
				|| loc.clone().add(-range, 0, -range).getBlock().getType().equals(Material.WEB)
				|| loc.clone().add(0, 0, range).getBlock().getType().equals(Material.WEB)
				|| loc.clone().add(0, 0, -range).getBlock().getType().equals(Material.WEB))
			return true;
		return false;
	}

	@SuppressWarnings("deprecation")
	private void fixBlock(Player p, Location loc) {
		p.sendBlockChange(loc.getBlock().getLocation(), loc.getBlock().getType(), loc.getBlock().getData());
	}

	@EventHandler
	public void onFood(FoodLevelChangeEvent e) {
		if (MetaLists.PLAYERS.contains(e.getEntity()))
			e.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		MetaLists.ONE_V_ONE_FIGHT.remove(p);
		IP.add(p);
		if (Main.uuidData.get(p.getUniqueId().toString()) == null) {
			Main.uuidData.set(p.getUniqueId().toString(), p.getName());
			Main.uuidData.save();
		} else if (!Main.uuidData.getString(p.getUniqueId().toString()).equals(p.getName())) {
			String oldName = Main.uuidData.getString(p.getUniqueId().toString());
			Main.uuidData.set(p.getUniqueId().toString(), p.getName());
			Main.uuidData.save();
			Main.config.set(p.getName(), Main.config.get(oldName));
			Main.config.set(oldName, null);
			Main.config.save();
		}
		if (p.isValid() && Locations.gameWorld != null && p.getLocation().getWorld().equals(Locations.gameWorld))
			Points.update(p.getName());
		else
			BarAPI.removeBar(p);
		DisguiseCraftMethods.unDisguise(p);
		if (!p.getGameMode().equals(GameMode.CREATIVE)) {
			p.setFlying(false);
			p.setAllowFlight(false);
		}
		if (!p.isDead() && Locations.gameWorld != null && p.getLocation().getWorld().equals(Locations.gameWorld)) {
			Main.ghostManager.removePlayer(p);
			MetaLists.PLAYERS.remove(p);
			p.getInventory().setArmorContents(null);
			p.getInventory().clear();
			ArrayList<PotionEffectType> remover = new ArrayList<PotionEffectType>();
			for (PotionEffect pot : p.getActivePotionEffects())
				remover.add(pot.getType());
			for (PotionEffectType effects : remover)
				p.removePotionEffect(effects);
			if (Locations.spawn != null) {
				if (p.isOnline() && p.getWorld().equals(Locations.gameWorld)) {
					MetaLists.TP_AROUND.add(p);
					p.teleport(Locations.spawn);
					Main.giveSpawnItems(p);
					Main.ghostManager.addPlayer(p);
					MetaLists.PLAYERS.add(p, null);
				}
			}
		}
		if (!p.hasPlayedBefore())
			Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "Welcome " + p.getDisplayName() + " to the Lonks server!");
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onSpawn(PlayerRespawnEvent e) {
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			if (!VanishAPI.isVanished(e.getPlayer()))
				p.showPlayer(e.getPlayer());
			if (!VanishAPI.isVanished(p))
				e.getPlayer().showPlayer(p);
		}
		Player p = e.getPlayer();
		if (!p.getGameMode().equals(GameMode.CREATIVE)) {
			p.setFlying(false);
			p.setAllowFlight(false);
		}
		if (Locations.hub != null)
			e.setRespawnLocation(Locations.hub);
		if (MetaLists.SPAWN_TO.contains(p) && Locations.spawn != null) {
			e.setRespawnLocation(Locations.spawn);
			Main.ghostManager.addPlayer(p);
			MetaLists.PLAYERS.add(p, null);
			NickName.setCustomName(p);
			Main.giveSpawnItems(p);
			final String playerName = p.getName();
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
				@Override
				public void run() {
					Points.update(playerName, Locations.spawn.getWorld());
				}
			}, 5L);
		}
		if (Locations.gameWorld != null && !Locations.gameWorld.equals(e.getRespawnLocation().getWorld())) {
			Main.ghostManager.removePlayer(p);
			MetaLists.PLAYERS.remove(p);
		}
		MetaLists.KICK_LIST.remove(p);
		PLib.removeArrows(p);
		MetaLists.SPAWN_TO.remove(p);
	}

	@EventHandler
	public void onVelocityChange(PlayerVelocityEvent e) {
		if (MetaLists.CREEPER_JUMP.contains(e.getPlayer())) {
			MetaLists.CREEPER_JUMP.remove(e.getPlayer());
			e.getPlayer().setVelocity(new Vector(0, 1, 0));
		}
	}

	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent e) {
		if (e.getMessage() == null || e.getMessage().equals("")) {

			return;

		} else if (e.getMessage().split(" ")[0].contains(":")) {

			e.setMessage(e.getMessage().substring(e.getMessage().indexOf(":") + 1, e.getMessage().length()));
			e.getPlayer().sendMessage(Messages.PERMISSION_COMMAND);
			return;

		} else if (MetaLists.ONE_V_ONE_FIGHT.contains(e.getPlayer())
				&& !e.getMessage().toLowerCase().contains("suicide")) {

			e.getPlayer().sendMessage(Messages.PREFIX + ChatColor.RED + "You can't use that in the arena");
			e.getPlayer().sendMessage(Messages.HINT_SUICIDE);
			e.setCancelled(true);
			return;

		} else if (!MetaLists.TP_AROUND_PLOT.contains(e.getPlayer())
				&& (e.getMessage().contains("p home") || e.getMessage().contains("plotme home") || e.getMessage().contains("p h") || e.getMessage().contains("plotme h"))) {

			e.setCancelled(true);
			TpCountdown.teleport(e.getPlayer(), "p home");

		} else if (e.getMessage().contains("f ally") || e.getMessage().contains("faction ally")
				|| e.getMessage().contains("factions ally")) {

			e.getPlayer()
					.sendMessage(
							ChatColor.DARK_RED
									+ "WARNING: Faction Allys can open doors/levers/buttons in your land! Only ally people you 100% Trust!");

		}
	}

	@EventHandler
	public void onFish(PlayerFishEvent e) {
		if (MetaLists.PLAYERS.contains(e.getPlayer()) && e.getCaught() != null && e.getCaught() instanceof LivingEntity)
			if (!Locations.inSafe(e.getCaught().getLocation()) && !Locations.inSafe(e.getPlayer().getLocation())) {
				if (MetaLists.PLAYERS.get(e.getPlayer()) instanceof Fisherman) {
					if (e.getCaught() instanceof Player) {
						MetaLists.TP_AROUND.add(e.getCaught());
						Death.update((Player) e.getCaught(), e.getPlayer(), "was fished");
					}
					if (e.getCaught().getVehicle() == null)
						e.getCaught().teleport(e.getPlayer().getLocation());
					else if (e.getCaught().getVehicle() instanceof Horse) {
						MetaLists.HORSE_AROUND.add(e.getCaught());
						e.getCaught().getVehicle().remove();
						e.getCaught().teleport(e.getPlayer().getLocation().add(0, 1, 0));
						Horse horse = e.getCaught().getWorld()
								.spawn(e.getPlayer().getLocation().add(0, 1, 0), Horse.class);
						horse.setAdult();
						horse.setVariant(Variant.HORSE);
						horse.setColor(Horse.Color.BLACK);
						horse.setCanPickupItems(false);
						horse.setTamed(true);
						horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
						horse.getInventory().setArmor(new ItemStack(Material.DIAMOND_BARDING));
						horse.setPassenger(e.getCaught());
					}
				} else if (MetaLists.PLAYERS.get(e.getPlayer()) instanceof Hooker) {
					MetaLists.TP_AROUND.add(e.getPlayer());
					e.getPlayer().teleport(e.getCaught().getLocation());
				}
			} else {
				Boolean tpMessage = true;
				if (Cooldown.hasCooldown(e.getPlayer(), "tpMessage", false))
					tpMessage = false;
				Main.cool.add(new Cooldown(10, e.getPlayer(), "tpMessage"));
				if (tpMessage)
					e.getPlayer().sendMessage(Messages.SAFEZONE_TELEPORT);
			}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerEntityInteract(PlayerInteractEntityEvent e) {
		if (MetaLists.PLAYERS.contains(e.getPlayer()) && MetaLists.PLAYERS.get(e.getPlayer()) instanceof BeastMaster
				&& e.getRightClicked() instanceof Wolf)
			e.setCancelled(true);
		else if (MetaLists.PLAYERS.contains(e.getPlayer()) && MetaLists.PLAYERS.get(e.getPlayer()) instanceof Hulk
				&& (!(e.getRightClicked() instanceof Player) || !Spectator.isSpectator((Player) e.getRightClicked()))
				&& e.getPlayer().getItemInHand().getType().equals(Material.AIR)) {
			if (Locations.inSafe(e.getPlayer()) || Locations.inSafe(e.getRightClicked())) {
				e.getPlayer().sendMessage(Messages.SAFEZONE_IN);
				return;
			}
			if (MetaLists.PLAYERS.contains(e.getRightClicked())
					&& MetaLists.PLAYERS.get(e.getRightClicked()) instanceof Cowboy
					&& e.getRightClicked().getVehicle() != null) {
				MetaLists.TP_AROUND.add(e.getRightClicked());
				MetaLists.HORSE_AROUND.add(e.getRightClicked());
				e.getRightClicked().getVehicle().remove();
				Horse horse = e.getRightClicked().getWorld().spawn(e.getRightClicked().getLocation(), Horse.class);
				horse.setAdult();
				horse.setVariant(Variant.HORSE);
				horse.setColor(Horse.Color.BLACK);
				horse.setCanPickupItems(false);
				horse.setTamed(true);
				horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
				horse.getInventory().setArmor(new ItemStack(Material.DIAMOND_BARDING));
				MetaLists.TP_AROUND.add(e.getRightClicked());
				e.getPlayer().setPassenger(horse);
				horse.setPassenger(e.getRightClicked());
			} else if (!(MetaLists.PLAYERS.contains(e.getRightClicked()) && MetaLists.PLAYERS.get(e.getRightClicked()) instanceof Cowboy)) {
				if (e.getRightClicked() instanceof Player)
					MetaLists.TP_AROUND.add(e.getRightClicked());
				e.getPlayer().setPassenger(e.getRightClicked());
			}
		}

		if (!Locations.inSafe(e.getPlayer().getLocation()) && !Locations.inSafe(e.getRightClicked().getLocation())
				&& e.getPlayer().getItemInHand() != null && e.getPlayer().getItemInHand().hasItemMeta()
				&& e.getPlayer().getItemInHand().getItemMeta().hasDisplayName()) {
			String name = ChatColor.stripColor(e.getPlayer().getItemInHand().getItemMeta().getDisplayName());
			if (name.equalsIgnoreCase("staff")) {
				if (MetaLists.PLAYERS.contains(e.getRightClicked())
						&& !Spectator.isSpectator((Player) e.getRightClicked())) {
					if (Cooldown.hasCooldown(e.getPlayer(), "The Staff", true))
						return;
					Main.cool.add(new Cooldown(5, e.getPlayer(), "The Staff"));
					if (((Player) e.getRightClicked()).getItemInHand() != null) {
						int randomInt = new Random().nextInt(e.getPlayer().getInventory().getSize());
						ItemStack is1 = ((Player) e.getRightClicked()).getItemInHand();
						ItemStack is2 = ((Player) e.getRightClicked()).getInventory().getItem(randomInt);
						((Player) e.getRightClicked()).setItemInHand(is2);
						((Player) e.getRightClicked()).getInventory().setItem(randomInt, is1);
					}
					Death.update((Player) e.getRightClicked(), e.getPlayer(), "Monked");
					((Player) e.getRightClicked()).sendMessage(ChatColor.RED + "You have been Monked by "
							+ e.getPlayer().getName());
				}
			} else if (name.equalsIgnoreCase("cane")) {
				if (Locations.inSafe(e.getRightClicked()) || Locations.inSafe(e.getPlayer())) {
					e.getPlayer().sendMessage(Messages.SAFEZONE_IN);
					return;
				}
				if (MetaLists.PLAYERS.contains(e.getRightClicked())
						&& MetaLists.PLAYERS.get(e.getRightClicked()) != null
						&& !Spectator.isSpectator((Player) e.getRightClicked())) {
					if (Cooldown.hasCooldown(e.getPlayer(), "The Cane", false))
						return;
					Main.cool.add(new Cooldown(15, e.getPlayer(), "The Cane"));
					Death.update((Player) e.getRightClicked(), e.getPlayer(), "Caned");
					int random = new Random().nextInt(4);
					final Player cp = (Player) e.getRightClicked();
					final Kit kit = (Kit) MetaLists.PLAYERS.get(e.getRightClicked());
					ItemStack[] contents = cp.getInventory().getArmorContents();
					if (random == 0)
						cp.sendMessage(ChatColor.RED + e.getPlayer().getName() + " stole your shoes!");
					else if (random == 1)
						cp.sendMessage(ChatColor.RED + e.getPlayer().getName() + " stole your pants!");
					else if (random == 2)
						cp.sendMessage(ChatColor.RED + e.getPlayer().getName() + " stole your chestplate!");
					else if (random == 3)
						cp.sendMessage(ChatColor.RED + e.getPlayer().getName() + " stole your helmet!");
					contents[random] = null;
					cp.getInventory().setArmorContents(contents);
					cp.updateInventory();
					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
						@Override
						public void run() {
							if (MetaLists.PLAYERS.contains(cp) && MetaLists.PLAYERS.get(cp) != null
									&& MetaLists.PLAYERS.get(cp).equals(kit)) {
								cp.getInventory().setArmorContents(kit.getArmorContents());
								cp.updateInventory();
							}
						}
					}, 10 * 20L);
				}
			} else if (name.equalsIgnoreCase("ripper")) {
				if (Cooldown.hasCooldown(e.getPlayer(), "The Ripper", false))
					return;
				Main.cool.add(new Cooldown(20, e.getPlayer(), "the ripper"));
				if (MetaLists.PLAYERS.contains(e.getRightClicked()))
					Death.update((Player) e.getRightClicked(), e.getPlayer(), "was ripped");
				((LivingEntity) e.getRightClicked()).damage(6);
				((LivingEntity) e.getRightClicked()).setFireTicks(20 * 5);
			}
		}
	}

	@EventHandler
	public void onTalk(AsyncPlayerChatEvent e) {
		if (Locations.gameWorld != null && e.getPlayer().getWorld().equals(Locations.gameWorld)) {
			NickName.setCustomName(e.getPlayer());
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerChangedWorldEvent(PlayerChangedWorldEvent e) {
		// e.getPlayer().setResourcePack("http://download733.mediafire.com/b3gowwkjvkbg/ix27bsfb4bv4tms/Unsimple+1.4.zip");
		MetaLists.CAN_JUMP.remove(e.getPlayer());
		MetaLists.BYPASS_SAFEZONE.remove(e.getPlayer());
		DisguiseCraftMethods.unDisguise(e.getPlayer());
		CloneManager.remove(e.getPlayer());
		onInteract.removeMinions(e.getPlayer());
		Spectator.remove(e.getPlayer());

		for (Entity ent : Locations.gameWorld.getEntities())
			if (ent instanceof Wolf && ((Wolf) ent).getCustomName() != null
					&& ((Wolf) ent).getCustomName().contains(e.getPlayer().getName()))
				((Wolf) ent).damage(999);

		final Player p = e.getPlayer();
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
			@Override
			public void run() {
				if (Locations.gameWorld != null && p.getWorld().equals(Locations.gameWorld) && p.isOnline())
					NickName.setCustomName(p);
			}
		}, 5L);
	}
}
