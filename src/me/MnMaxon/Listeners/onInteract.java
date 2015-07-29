package me.MnMaxon.Listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import me.MnMaxon.Apis.CloneManager;
import me.MnMaxon.Apis.DisguiseCraftMethods;
import me.MnMaxon.Kits.Hulk;
import me.MnMaxon.Kits.Hyper;
import me.MnMaxon.Kits.Kit;
import me.MnMaxon.Kits.Snowman;
import me.MnMaxon.LonksKits.CustomEntityPig;
import me.MnMaxon.LonksKits.Locations;
import me.MnMaxon.LonksKits.Messages;
import me.MnMaxon.LonksKits.MetaLists;
import me.MnMaxon.LonksKits.Cooldown;
import me.MnMaxon.LonksKits.Death;
import me.MnMaxon.LonksKits.Main;
import me.MnMaxon.LonksKits.Shop;
import me.MnMaxon.LonksKits.Spectator;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftCreature;
import org.bukkit.entity.Fireball;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.lenis0012.bukkit.npc.NPC;

public class onInteract implements Listener {
	public static Map<Player, ArrayList<Creature>> minions = new HashMap<Player, ArrayList<Creature>>();

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (p.isOp() && e.getItem() != null && e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasDisplayName()
				&& ChatColor.stripColor(e.getItem().getItemMeta().getDisplayName()).equalsIgnoreCase("SafeZone Wand"))
			if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				Main.loc2 = e.getClickedBlock().getLocation();
				p.sendMessage(ChatColor.AQUA + "Point 2 set at: " + e.getClickedBlock().getLocation().getBlockX()
						+ ", " + e.getClickedBlock().getLocation().getBlockZ());
			} else if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
				Main.loc1 = e.getClickedBlock().getLocation();
				p.sendMessage(ChatColor.AQUA + "Point 1 set at: " + e.getClickedBlock().getLocation().getBlockX()
						+ ", " + e.getClickedBlock().getLocation().getBlockZ());
			}

		if (MetaLists.PLAYERS.contains(p)) {

			if ((e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)
					|| e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
					&& e.getItem() != null && e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasDisplayName()) {
				String name = e.getItem().getItemMeta().getDisplayName();
				if (name.equals(Messages.ITEM_SELECTOR))
					Kit.openGui(p, 1);
				else if (name.equals(Messages.ITEM_SHOP))
					Shop.openGui(p);
				else if (name.equals(Messages.ITEM_SPECTATOR))
					if (p.hasPermission("Lonkskit.Spectator"))
						Spectator.give(p);
					else
						p.sendMessage(Messages.PERMISSION_ULTIMATE);
			}

			if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)
					&& e.getClickedBlock().getState() instanceof InventoryHolder && !MetaLists.BYPASS_BUILD.contains(p)) {
				e.setCancelled(true);
				p.sendMessage(Messages.MUST_BUILD);
			}

			if ((e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
					&& e.getItem() != null) {
				if (e.getItem().getType().equals(Material.SUGAR) && MetaLists.PLAYERS.get(p) instanceof Hyper)
					Sugar(p);
				else if (e.getItem().getType().equals(Material.SNOW_BALL)
						&& MetaLists.PLAYERS.get(p) instanceof Snowman) {
					if (Locations.inSafe(p)) {
						p.sendMessage(Messages.SAFEZONE_IN);
						e.setCancelled(true);
						p.updateInventory();
						return;
					}
					if (Cooldown.hasCooldown(p, "snowball", true)) {
						e.setCancelled(true);
						p.updateInventory();
						return;
					}
					Main.cool.add(new Cooldown(6, p, "snowball"));
				}
				if (e.getItem() != null && e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasDisplayName()) {
					String name = ChatColor.stripColor(e.getItem().getItemMeta().getDisplayName());
					String fullName = e.getItem().getItemMeta().getDisplayName();
					if (fullName.equals(Messages.ITEM_HEALTH)) {
						e.setCancelled(true);
						Heal(p);
					} else if (name.equalsIgnoreCase("spear"))
						Spear(p);
					else if (name.equalsIgnoreCase("wand"))
						Wand(p);
					else if (name.equalsIgnoreCase("Jump Boost"))
						JumpBoost(p);
					else if (name.equalsIgnoreCase("Fly"))
						Fly(p);
					else if (name.equalsIgnoreCase("claw"))
						Claw(p);
					else if (name.equalsIgnoreCase("lasso"))
						Lasso(p);
					else if (name.equalsIgnoreCase("Thors Hammer"))
						ThorsAxe(p, e);
					else if (name.equalsIgnoreCase("Blinker"))
						Blink(p);
					else if (name.equalsIgnoreCase("Boost"))
						Boost(p);
					else if (name.equalsIgnoreCase("Speed Boost"))
						SpeedBoost(p);
					else if (name.equalsIgnoreCase("Invisibility"))
						Invisibility(p);
					else if (name.equalsIgnoreCase("Grenade Launcher"))
						launchGrenade(p);
					else if (name.equalsIgnoreCase("Release the Hounds"))
						hounds(p);
					else if (name.equalsIgnoreCase("Machine Gun"))
						MachineGun(p);
					else if (name.equalsIgnoreCase("The Shadow Blade")) {
						Fireball(p);
						e.setCancelled(true);
					} else if (name.equalsIgnoreCase("Launch")) {
						launch(p);
						e.setCancelled(true);
					} else if (name.equalsIgnoreCase("switcher")) {
						if (Locations.inSafe(p)) {
							p.sendMessage(Messages.SAFEZONE_IN);
							e.setCancelled(true);
							return;
						}
						Switcher(p);
						if (e.getItem().getAmount() == 1)
							p.getInventory().setItemInHand(null);
						else
							e.getItem().setAmount(e.getItem().getAmount() - 1);
						e.setCancelled(true);
					} else if (name.equalsIgnoreCase("cloner"))
						CloneManager.add(p);
					else if (name.equalsIgnoreCase("Shape Shifter"))
						ShapeShift(p);
					else if (name.equalsIgnoreCase("Kamikaze"))
						Kamikaze(p);
					else if (name.equalsIgnoreCase("Scepter"))
						Scepter(p);
					else if (name.equalsIgnoreCase("Flinger"))
						Flinger(p);
					else if (name.equalsIgnoreCase("Shuriken"))
						Shuriken(p);
					else if (name.equalsIgnoreCase("Clone"))
						NinjaClone(p);
					else if (name.equalsIgnoreCase("The Force"))
						Force(p);
					else if (name.equalsIgnoreCase("Porkchop"))
						PorkChop(p);
				}
			} else if (e.getAction().equals(Action.PHYSICAL)) {
				if (e.getClickedBlock().getType() == Material.STONE_PLATE
						&& Main.mines.containsKey(e.getClickedBlock())) {
					if (Bukkit.getPlayer(Main.mines.get(e.getClickedBlock())) != null
							&& Bukkit.getPlayer(Main.mines.get(e.getClickedBlock())).isOnline()) {
						if (Spectator.isSpectator(p))
							return;
						Death.update(p, Bukkit.getPlayer(Main.mines.get(e.getClickedBlock())), "boom");
						for (Entity ent : p.getNearbyEntities(6, 6, 6))
							if (ent instanceof Player)
								Death.update((Player) ent, Bukkit.getPlayer(Main.mines.get(e.getClickedBlock())),
										"boom");
					}
					p.sendMessage(ChatColor.DARK_RED + "You have activated " + Main.mines.get(e.getClickedBlock())
							+ "'s trap.");
					p.getLocation().getWorld().createExplosion(p.getLocation(), 4f);
					Main.mines.remove(e.getClickedBlock());
					e.getClickedBlock().setType(Material.AIR);
				}
			}
			if ((e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)
					|| e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK))
					&& p.getPassenger() != null && MetaLists.PLAYERS.get(p) instanceof Hulk) {
				if (p.getPassenger() instanceof Player)
					Death.update((Player) p.getPassenger(), p, "was thrown");
				Entity passenger = p.getPassenger();
				p.eject();
				double maxVelocity = 1;
				double x = p.getLocation().getDirection().getX() * maxVelocity;
				double y = 1;
				double z = p.getLocation().getDirection().getZ() * maxVelocity;
				passenger.setVelocity(new Vector(x, y, z));
			}
		}
	}

	private void PorkChop(Player p) {
		if (Locations.inSafe(p)) {
			p.sendMessage(Messages.SAFEZONE_IN);
			return;
		}
		if (Cooldown.hasCooldown(p, "The Porkchop", true))
			return;
		Main.cool.add(new Cooldown(20, p, "The Porkchop"));
		final CustomEntityPig cep = new CustomEntityPig(((CraftWorld) p.getWorld()).getHandle());
		cep.setLocation(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), 0F, 0F);
		((CraftWorld) p.getWorld()).getHandle().addEntity(cep);
		Pig ent = (Pig) cep.getBukkitEntity();
		ent.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
		MetaLists.TP_AROUND.add(p);
		MetaLists.PIGGY.add(ent, p);
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
			public void run() {
				if (cep.getBukkitEntity().isValid()) {
					cep.explode();
				}
			}
		}, 120L);
	}

	public static void Spear(Player p) {
		if (Locations.inSafe(p.getLocation())) {
			p.sendMessage(Messages.SAFEZONE_IN);
			return;
		}
		if (Cooldown.hasCooldown(p, "the spear", true))
			return;
		Item spear = p.getLocation().getWorld().dropItem(p.getLocation().add(0, 1, 0), p.getItemInHand());
		double maxVelocity = 1;
		double x = p.getLocation().getDirection().getX() * maxVelocity;
		double y = .5;
		double z = p.getLocation().getDirection().getZ() * maxVelocity;
		p.chat("This... Is... SPARTA!");
		spear.setVelocity(new Vector(x, y, z));
		Main.cool.add(new Cooldown(20, p, "the spear"));
		SpearLoop(spear, p);

	}

	private static void SpearLoop(final Item spear, final Player p) {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
			@Override
			public void run() {
				for (Entity ent : spear.getNearbyEntities(.5, 2, .5)) {
					if (Locations.inSafe(ent.getLocation())) {
						p.sendMessage(Messages.SAFEZONE_IN);
					} else if (ent instanceof LivingEntity && !ent.equals(p)) {
						if (ent instanceof Player)
							Death.update((Player) ent, p, "was stabbed with a spear");
						((LivingEntity) ent).damage(10, p);
						((LivingEntity) ent).setVelocity(((LivingEntity) ent).getVelocity().add(new Vector(0, .5, 0)));
					}
				}
				if (spear != null
						&& (spear.getLocation().add(0, -1, 0).getBlock().getType().equals(Material.AIR) || spear
								.getLocation().add(0, -1, 0).getBlock().getType().equals(Material.LONG_GRASS)))
					SpearLoop(spear, p);
				else
					spear.remove();
			}
		}, 1L);
	}

	public static void Wand(Player p) {
		if (Locations.inSafe(p.getLocation())) {
			p.sendMessage(Messages.SAFEZONE_IN);
			return;
		}
		if (Cooldown.hasCooldown(p, "the wand", true))
			return;
		Main.cool.add(new Cooldown(10, p, "the wand"));
		ItemStack is = new ItemStack(Material.SNOW_BALL);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName("wand");
		is.setItemMeta(im);
		Item snowBall = p.getLocation().getWorld().dropItem(p.getLocation().add(0, 1, 0), is);
		double maxVelocity = 1;
		double x = p.getLocation().getDirection().getX() * maxVelocity;
		double y = p.getLocation().getDirection().getY();
		double z = p.getLocation().getDirection().getZ() * maxVelocity;
		snowBall.setVelocity(new Vector(x, y, z));
		WandLoop(snowBall, p);
	}

	private static void WandLoop(final Item snowBall, final Player p) {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
			@Override
			public void run() {
				boolean hit = false;
				for (Entity ent : snowBall.getNearbyEntities(.5, 2, .5))
					if (ent instanceof LivingEntity && !ent.equals(p))
						hit = true;
				if (hit) {
					for (Entity ent : snowBall.getNearbyEntities(5, 5, 5))
						if (ent instanceof Player && !ent.equals(p))
							Death.update((Player) ent, p, "wand");
					snowBall.getLocation().getWorld().createExplosion(snowBall.getLocation(), 4F);
					snowBall.remove();
					return;
				}
				if (snowBall != null && !snowBall.isOnGround())
					WandLoop(snowBall, p);
				else {
					for (Entity ent : snowBall.getNearbyEntities(5, 5, 5))
						if (ent instanceof Player && !ent.equals(p))
							Death.update((Player) ent, p, "wand");
					snowBall.getLocation().getWorld().createExplosion(snowBall.getLocation(), 4F);
					snowBall.remove();
					return;
				}
			}
		}, 1L);
	}

	public static void Sugar(Player p) {
		if (Locations.inSafe(p.getLocation())) {
			p.sendMessage(Messages.SAFEZONE_IN);
			return;
		}
		if (Cooldown.hasCooldown(p, "Sugar", true))
			return;
		Main.cool.add(new Cooldown(40, p, "sugar"));
		p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 7 * 20, 3));
		p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 7 * 20, 0));
		p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 7 * 20, 2));

	}

	public static void Switcher(Player p) {
		Snowball snowBall = p.getLocation().getWorld().spawn(p.getEyeLocation(), Snowball.class);
		snowBall.setShooter(p);
		MetaLists.SWITCHERS.add(snowBall, p);
		double maxVelocity = 1.5;
		double x = p.getLocation().getDirection().getX() * maxVelocity;
		double y = p.getLocation().getDirection().getY() * maxVelocity;
		double z = p.getLocation().getDirection().getZ() * maxVelocity;
		snowBall.setVelocity(new Vector(x, y, z));
		SwitcherLoop(snowBall, p);
	}

	private static void SwitcherLoop(final Snowball snowBall, final Player p) {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
			@Override
			public void run() {
				if (snowBall == null || !snowBall.isValid() || !p.isOnline()) {
					MetaLists.SWITCHERS.remove(snowBall);
					return;
				} else
					SwitcherLoop(snowBall, p);
			}
		}, 20L);
	}

	public static void JumpBoost(Player p) {
		if (!MetaLists.CAN_JUMP.contains(p))
			return;
		MetaLists.CAN_JUMP.remove(p);
		double maxVelocity = 2;
		p.setFireTicks(0);
		if (p.isSneaking()) {
			double x = p.getLocation().getDirection().getX() * maxVelocity;
			double y = maxVelocity / 3;
			double z = p.getLocation().getDirection().getZ() * maxVelocity;
			p.setVelocity(new Vector(x, y, z));
		} else {
			double x = p.getLocation().getDirection().getX() * maxVelocity / 3;
			double y = maxVelocity / 2;
			double z = p.getLocation().getDirection().getZ() * maxVelocity / 3;
			p.setVelocity(new Vector(x, y, z));
		}
	}

	public static void Fly(final Player p) {
		if (Locations.inSafe(p.getLocation())) {
			p.sendMessage(Messages.SAFEZONE_IN);
			return;
		} else if (p.getAllowFlight()) {
			p.sendMessage(Messages.PREFIX + ChatColor.DARK_RED + "You're all ready flying!");
			return;
		}
		if (Cooldown.hasCooldown(p, "fly", true))
			return;
		Main.cool.add(new Cooldown(40, p, "fly"));
		if (Death.wasHit(p))
			Death.getInfo(p).timer = 12;
		p.sendMessage(Messages.PREFIX + "You can fly for 6 seconds");
		p.setAllowFlight(true);
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
			@Override
			public void run() {
				if (!p.isOnline())
					return;
				p.sendMessage(Messages.PREFIX + ChatColor.DARK_RED + "You can fly for 3 more seconds.");
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
					@Override
					public void run() {
						if (!p.isOnline())
							return;
						p.sendMessage(Messages.PREFIX + ChatColor.DARK_RED + "You can fly for 2 more seconds.");
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
							@Override
							public void run() {
								if (!p.isOnline())
									return;
								p.sendMessage(Messages.PREFIX + ChatColor.DARK_RED + "You can fly for 1 more second.");
								Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
									@Override
									public void run() {
										if (!p.isOnline())
											return;
										p.sendMessage(Messages.PREFIX + ChatColor.DARK_RED + "You can no longer fly.");
										p.setFallDistance(0);
										p.setAllowFlight(false);
									}
								}, 1 * 20L);
							}
						}, 1 * 20L);
					}
				}, 1 * 20L);
			}
		}, 3 * 20L);
	}

	public static void Claw(final Player p) {
		if (Cooldown.hasCooldown(p, "the claw", true))
			return;
		Main.cool.add(new Cooldown(50, p, "the Claw"));
		MetaLists.DOUBLE_DAMAGE.add(p);
		p.chat("Feel the claw of the Tiger!");
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
			@Override
			public void run() {
				MetaLists.DOUBLE_DAMAGE.remove(p);
				if (p.isOnline())
					p.sendMessage(Messages.PREFIX + "You feel the tiger effect wear off.");
			}
		}, 10 * 20L);
	}

	public static void Lasso(Player p) {
		if (Locations.inSafe(p.getLocation())) {
			p.sendMessage(Messages.SAFEZONE_IN);
			return;
		}
		if (Cooldown.hasCooldown(p, "the lasso", true))
			return;
		Main.cool.add(new Cooldown(15, p, "the lasso"));
		ItemStack is = new ItemStack(Material.LEASH);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName("Lasso");
		is.setItemMeta(im);
		Item lasso = p.getLocation().getWorld().dropItem(p.getLocation().add(0, 1, 0), is);
		double maxVelocity = 2;
		double x = p.getLocation().getDirection().getX() * maxVelocity;
		double y = 0;
		if (p.getLocation().getDirection().getY() > 0)
			y = p.getLocation().getDirection().getY() * maxVelocity * 2;
		else
			y = p.getLocation().getDirection().getY();
		double z = p.getLocation().getDirection().getZ() * maxVelocity;
		lasso.setVelocity(new Vector(x, y, z));
		LassoLoop(lasso, p);
	}

	private static void LassoLoop(final Item lasso, final Player p) {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
			@Override
			public void run() {
				for (final Entity ent : lasso.getNearbyEntities(.5, 2, .5))
					if (ent instanceof Player && !ent.equals(p)) {
						Death.update((Player) ent, p, "lasso'd");
						lasso.remove();
						((Player) ent).sendMessage(ChatColor.RED + "You've been lasso'd");
						MetaLists.FREEZE.add(ent);
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
							@Override
							public void run() {
								((Player) ent).sendMessage(ChatColor.GREEN + "The lasso has worn off!");
								MetaLists.FREEZE.remove(ent);
							}
						}, 3 * 20L);
						return;
					}
				if (lasso.isOnGround())
					lasso.remove();
				else if (lasso != null)
					LassoLoop(lasso, p);
			}
		}, 1L);
	}

	public static void Blink(Player p) {
		if (Cooldown.hasCooldown(p, "blink", true))
			return;
		int range = 30;
		Location from = p.getEyeLocation();
		Location to = p.getEyeLocation();
		while (distance(to, p.getEyeLocation()) <= range) {
			from = to;
			to = from.add(p.getLocation().getDirection().normalize().multiply(.5));
			if (!to.getBlock().getType().equals(Material.AIR) && !to.getBlock().getType().equals(Material.LONG_GRASS)
					&& !to.getBlock().getType().equals(Material.DOUBLE_PLANT)) {
				if (Locations.inSafe(from.clone().add(0, 1, 0))) {
					p.sendMessage(Messages.SAFEZONE_TELEPORT);
					return;
				}
				MetaLists.TP_AROUND.add(p);
				p.teleport(from.clone().add(0, 1, 0));
				p.sendMessage(Messages.ABILITY_BLINK);
				Main.cool.add(new Cooldown(30, p, "Blink"));
				return;
			}
		}
		if (Locations.inSafe(from.clone().add(0, 1, 0))) {
			p.sendMessage(Messages.SAFEZONE_TELEPORT);
			return;
		}
		MetaLists.TP_AROUND.add(p);
		p.teleport(to.add(0, 1, 0));
		p.sendMessage(Messages.ABILITY_BLINK);
		Main.cool.add(new Cooldown(30, p, "Blink"));
	}

	private static double distance(Location to, Location pLoc) {
		double x = Math.abs(to.getX() - pLoc.getX());
		double y = Math.abs(to.getY() - pLoc.getY());
		double z = Math.abs(to.getZ() - pLoc.getZ());
		double diagonal = Math.sqrt(x * x + z * z);
		return Math.abs(Math.sqrt(diagonal * diagonal + y * y));
	}

	public static void Boost(final Player p) {
		if (Locations.inSafe(p.getLocation())) {
			p.sendMessage(Messages.SAFEZONE_IN);
			return;
		}
		if (Cooldown.hasCooldown(p, "boost", true))
			return;
		Main.cool.add(new Cooldown(15, p, "boost"));
		p.setFireTicks(0);
		Vector direction = p.getLocation().getDirection().normalize();
		double x = direction.getX() * 3;
		double y = direction.getY() * 10;
		double z = direction.getZ() * 3;
		p.setVelocity(new Vector(x, y, z));
		MetaLists.IGNORE_DAMAGE_FALL.add(p);
		p.getWorld().playEffect(p.getLocation(), Effect.MOBSPAWNER_FLAMES, 0);
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
			@Override
			public void run() {
				BoostLoop(p, 0);
			}
		}, 5L);
	}

	private static void BoostLoop(final Player p, final int time) {
		if (time < 2 * 20)
			p.getWorld().playEffect(p.getLocation(), Effect.MOBSPAWNER_FLAMES, 0);
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
			@Override
			public void run() {
				if ((p.getVelocity().getY() == -0.0784000015258789 && p.getLocation().add(0, -.001, 0).getBlock()
						.getType().isSolid())
						|| time > 20 * 20) {
					MetaLists.IGNORE_DAMAGE_FALL.remove(p);
					return;
				}
				BoostLoop(p, time + 5);
			}
		}, 5L);
	}

	public static void SpeedBoost(Player p) {
		if (Locations.inSafe(p.getLocation())) {
			if (p instanceof Player)
				p.sendMessage(Messages.SAFEZONE_IN);
			return;
		}
		if (Cooldown.hasCooldown(p, "speed boost", true))
			return;
		Main.cool.add(new Cooldown(30, p, "Speed Boost"));
		p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10 * 20, 2));
	}

	public static void Invisibility(Player p) {
		if (Locations.inSafe(p.getLocation())) {
			p.sendMessage(Messages.SAFEZONE_IN);
			return;
		}
		if (Cooldown.hasCooldown(p, "Invisibility", true))
			return;
		Main.cool.add(new Cooldown(60, p, "Invisibility"));
		onCrouch.invisify(p, 15 * 20);
	}

	public static void launchGrenade(final Player p) {
		if (Locations.inSafe(p.getLocation())) {
			p.sendMessage(Messages.SAFEZONE_IN);
			return;
		}
		if (Cooldown.hasCooldown(p, "Grenade Launcher", true))
			return;
		Main.cool.add(new Cooldown(60, p, "Grenade Launcher"));
		ItemStack is = new ItemStack(Material.FIREWORK_CHARGE);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName("Grenade");
		is.setItemMeta(im);
		final Item bomb = p.getEyeLocation().getWorld().dropItem(p.getEyeLocation().add(0, 1, 0), is);
		Location pLoc = p.getLocation();
		pLoc.setYaw(pLoc.getYaw() + new Random().nextInt(40) - 20);
		pLoc.setPitch(pLoc.getPitch() + new Random().nextInt(10) - 5);
		double maxVelocity = .3;
		bomb.setVelocity(pLoc.getDirection().multiply(maxVelocity));
		grenadeLoop(p, bomb, 3 * 20, bomb.getLocation());
	}

	private static void grenadeLoop(final Player p, final Item bomb, final int timeLeft, final Location from) {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
			@Override
			public void run() {
				if (timeLeft > 0) {
					double checkRange = 1;
					if (bomb.getLocation().add(0, -1, 0).getBlock().getType().isSolid()) {
						if (bomb.getVelocity().getY() < 0)
							bomb.setVelocity(bomb.getVelocity().setY(bomb.getVelocity().getY() * -1.1));
					} else if (bomb.getLocation().add(checkRange, 0, 0).getBlock().getType().isSolid()
							|| bomb.getLocation().add(-checkRange, 0, 0).getBlock().getType().isSolid()
							|| bomb.getLocation().add(0, 0, checkRange).getBlock().getType().isSolid()
							|| bomb.getLocation().add(0, 0, -checkRange).getBlock().getType().isSolid()) {
						bomb.setVelocity(bomb.getVelocity().multiply(-1));
					}
					grenadeLoop(p, bomb, timeLeft - 2, bomb.getLocation());
				} else {
					Location loc = bomb.getLocation();
					for (Entity ent : bomb.getNearbyEntities(5, 5, 5))
						if (Main.canDamage(ent)) {
							if (ent instanceof Player && !ent.equals(p) && !CloneManager.isClone(p))
								Death.update((Player) ent, p, "explosion");
							((LivingEntity) ent).damage(99999);
						}
					loc.getWorld().createExplosion(loc, 4F);
					bomb.remove();
				}
			}
		}, 2L);
	}

	@SuppressWarnings("deprecation")
	public static void hounds(Player p) {
		p.setItemInHand(null);
		p.updateInventory();
		for (int i = 0; i < 3; i++) {
			Wolf wolf = (Wolf) p.getWorld().spawnEntity(p.getLocation(), EntityType.WOLF);
			wolf.setAdult();
			wolf.setTamed(true);
			wolf.setOwner(p);
			wolf.setCustomName(ChatColor.RED + p.getName() + "'s Wolf");
			wolf.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 3));
			wolf.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 1));
			wolf.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
		}
	}

	public static void launch(Player p) {
		if (Locations.inSafe(p.getLocation())) {
			p.sendMessage(Messages.SAFEZONE_IN);
			return;
		}
		if (Cooldown.hasCooldown(p, "launch", true))
			return;
		Main.cool.add(new Cooldown(60, p, "launch"));
		p.setFireTicks(0);
		double x = p.getVelocity().getX();
		double y = 10;
		double z = p.getVelocity().getZ();
		p.setVelocity(new Vector(x, y, z));
	}

	@SuppressWarnings("deprecation")
	public static void MachineGun(Player p) {
		if (Locations.inSafe(p.getLocation())) {
			p.sendMessage(Messages.SAFEZONE_IN);
			return;
		}
		if (Cooldown.hasCooldown(p, "Machine Gun", true))
			return;
		Main.cool.add(new Cooldown(2, p, "Machine Gun"));
		if (p.getItemInHand().getAmount() > 5) {
			ItemStack is = p.getItemInHand();
			is.setAmount(p.getItemInHand().getAmount() - 5);
			p.setItemInHand(is);
			MachineGunLoop(p, 5);
		} else {
			MachineGunLoop(p, p.getItemInHand().getAmount());
			p.setItemInHand(null);
		}
		p.updateInventory();
	}

	private static void MachineGunLoop(final Player p, final int times) {
		if (times <= 0)
			return;
		Arrow arrow = p.launchProjectile(Arrow.class);
		arrow.setShooter(p);
		arrow.setVelocity(arrow.getVelocity().multiply(2));
		MetaLists.NO_LAND.add(arrow);
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
			@Override
			public void run() {
				MachineGunLoop(p, times - 1);
			}
		}, 5L);
	}

	public static void Fireball(Player p) {
		if (Locations.inSafe(p.getLocation())) {
			p.sendMessage(Messages.SAFEZONE_IN);
			return;
		}
		if (Cooldown.hasCooldown(p, "The Shadow Blade", true))
			return;
		Main.cool.add(new Cooldown(5, p, "The Shadow Blade"));
		Vector direction = p.getLocation().getDirection();
		Fireball fireball = (Fireball) p.getWorld().spawnEntity(p.getEyeLocation().add(direction), EntityType.FIREBALL);
		fireball.setShooter(p);
		fireball.setVelocity(direction.multiply(2));
		MetaLists.FIREBALLS.add(fireball, p);
	}

	public static void ShapeShift(final Player p) {
		if (Locations.inSafe(p.getLocation())) {
			p.sendMessage(Messages.SAFEZONE_IN);
			return;
		}
		if (Cooldown.hasCooldown(p, "The Shape Shifter", true))
			return;
		Main.cool.add(new Cooldown(30, p, "The Shape Shifter"));
		int random = new Random().nextInt(4);
		EntityType entType = null;
		if (random == 0) {
			entType = EntityType.SILVERFISH;
			p.sendMessage(Messages.PREFIX + "You are a " + ChatColor.GOLD + "SILVERFISH" + ChatColor.GREEN + "!");
		} else if (random == 1) {
			entType = EntityType.BAT;
			p.sendMessage(Messages.PREFIX + "You are a " + ChatColor.GOLD + "BAT" + ChatColor.GREEN + "!");
		} else if (random == 2) {
			entType = EntityType.IRON_GOLEM;
			p.sendMessage(Messages.PREFIX + "You are an " + ChatColor.GOLD + "IRON GOLEM" + ChatColor.GREEN + "!");
		} else {
			entType = EntityType.SHEEP;
			p.sendMessage(Messages.PREFIX + "You are a " + ChatColor.GOLD + "SHEEP" + ChatColor.GREEN + "!");
		}
		DisguiseCraftMethods.disguise(p, entType);
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
			@Override
			public void run() {
				if (DisguiseCraftMethods.isDisguised(p)) {
					p.sendMessage(Messages.PREFIX + ChatColor.RED + "You're shape shifting has worn off");
					DisguiseCraftMethods.unDisguise(p);
				}
			}
		}, 10 * 20L);
	}

	public static void Kamikaze(Player p) {
		if (Locations.inSafe(p.getLocation())) {
			p.sendMessage(Messages.SAFEZONE_IN);
			return;
		}
		p.damage(999999);
		for (Entity ent : p.getNearbyEntities(6, 6, 6))
			if (MetaLists.PLAYERS.contains(ent) && Main.canDamage(ent))
				Death.update((Player) ent, p, "Kamikaze - noboom");
		p.getLocation().getWorld().createExplosion(p.getLocation(), 4);
	}

	public static void Scepter(Player p) {
		if (Locations.inSafe(p.getLocation())) {
			p.sendMessage(Messages.SAFEZONE_IN);
			return;
		}
		int mobs = 0;
		for (Entry<Player, ArrayList<Creature>> entry : minions.entrySet())
			if (entry.getKey().equals(p))
				for (Creature creature : entry.getValue())
					if (creature.isValid())
						mobs++;
		if (mobs >= 3) {
			p.sendMessage(ChatColor.RED + "You have already have 3 followers!");
			return;
		}
		if (Cooldown.hasCooldown(p, "The Scepter", true))
			return;
		Main.cool.add(new Cooldown(20, p, "The Scepter"));
		int randomInt = new Random().nextInt(100);
		Boolean witherSkeleton = false;
		EntityType entType;
		if (randomInt < 10) {
			entType = EntityType.SKELETON;
			witherSkeleton = true;
		} else if (randomInt < 55) {
			entType = EntityType.ZOMBIE;
		} else
			entType = EntityType.SKELETON;
		Location loc = p.getLocation();
		if (!p.getLocation().add(p.getLocation().getDirection().setY(0).multiply(5)).getBlock().getType().isSolid())
			loc = p.getLocation().add(p.getLocation().getDirection().setY(0).multiply(5));
		Entity ent = p.getWorld().spawnEntity(loc, entType);
		((CraftCreature) ent).getHandle().getNavigation().d(false);
		String mobType = "";
		if (ent.getType().equals(EntityType.ZOMBIE))
			mobType = "Zombie";
		else if (witherSkeleton) {
			((Skeleton) ent).setSkeletonType(SkeletonType.WITHER);
			mobType = "Wither Skeleton";
		} else
			mobType = "Skeleton";
		ArrayList<Creature> localMinions = new ArrayList<Creature>();
		if (minions.containsKey(p))
			localMinions = minions.get(p);
		localMinions.add((Creature) ent);
		ent.setMetadata("Minion", new FixedMetadataValue(Main.plugin, p.getName()));
		minions.remove(p);
		minions.put(p, localMinions);
		p.sendMessage(ChatColor.GREEN + "You have summoned a " + mobType);
		((Creature) ent).setCustomName(ChatColor.RED + p.getName() + "'s Follower");
	}

	public static void removeMinions(Player p) {
		for (Entry<Player, ArrayList<Creature>> entry : minions.entrySet())
			if (entry.getKey().equals(p))
				for (Creature creature : entry.getValue())
					if (creature.isValid())
						creature.damage(999);
		minions.remove(p);
	}

	public static void Flinger(Player p) {
		if (Locations.inSafe(p.getLocation())) {
			p.sendMessage(Messages.SAFEZONE_IN);
			return;
		}
		if (Cooldown.hasCooldown(p, "The Flinger", true))
			return;
		Main.cool.add(new Cooldown(6, p, "The Flinger"));
		Egg egg = p.launchProjectile(Egg.class);
		egg.setShooter(p);
		MetaLists.FLUNG.add(egg, p.getLocation().clone());
	}

	public static void Shuriken(Player p) {
		if (Locations.inSafe(p.getLocation())) {
			p.sendMessage(Messages.SAFEZONE_IN);
			return;
		}

		if (Cooldown.hasCooldown(p, "The Shuriken", true))
			return;
		Main.cool.add(new Cooldown(20, p, "The Shuriken"));
		Vector velocity = (p.getLocation().getDirection().multiply(2));
		ArrayList<Entity> ignored = new ArrayList<Entity>();
		ignored.add(p);
		if (CloneManager.ninjaNPCs.containsValue(p)) {
			for (Entry<NPC, Player> entry : CloneManager.ninjaNPCs.entrySet())
				if (entry.getValue().equals(p) && entry.getKey() != null && entry.getKey().getBukkitEntity().isValid()) {

					NPC npc = entry.getKey();
					Location loc = Locations.lookAt(npc.getBukkitEntity().getLocation(),
							p.getLocation().add(p.getLocation().getDirection().multiply(10)));
					npc.getBukkitEntity().teleport(loc);
					// Makes the clone look ~10 blocks in front of the player

					ignored.add(npc.getBukkitEntity());

					Vector cloneVelocity = (loc.getDirection().multiply(2));
					Item cloneShuriken = loc.getWorld().dropItem(loc.add(0, 1, 0), p.getItemInHand());
					cloneShuriken.setVelocity(cloneVelocity);
					ShurikenLoop(cloneShuriken, p, ignored);
				}
		}
		Item shuriken = p.getLocation().getWorld().dropItem(p.getLocation().add(0, 1, 0), p.getItemInHand());
		shuriken.setVelocity(velocity);
		ShurikenLoop(shuriken, p, ignored);
	}

	private static void ShurikenLoop(final Item shuriken, final Player p, final ArrayList<Entity> ignored) {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
			@Override
			public void run() {
				for (Entity ent : shuriken.getNearbyEntities(.5, 2, .5)) {
					if (Locations.inSafe(ent.getLocation())) {
						p.sendMessage(Messages.SAFEZONE_IN);
					} else if (ent instanceof LivingEntity && !ignored.contains(ent)) {
						if (ent instanceof Player)
							Death.update((Player) ent, p, "was shuriken'd");

						((LivingEntity) ent).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 3 * 20, 0));
						((LivingEntity) ent).setVelocity(((LivingEntity) ent).getVelocity().add(new Vector(0, .05, 0)));
						Main.damageIgnoreArmor(ent, 4, true);
						ignored.add(ent);
					}
				}
				if (shuriken != null)
					if (!shuriken.getLocation().add(0, -.5, 0).getBlock().getType().isSolid())
						ShurikenLoop(shuriken, p, ignored);
					else
						shuriken.remove();
			}
		}, 1L);
	}

	public static void NinjaClone(Player p) {
		if (Locations.inSafe(p.getLocation())) {
			p.sendMessage(Messages.SAFEZONE_IN);
			return;
		}
		if (CloneManager.ninjaNPCs.containsValue(p)) {
			for (Entry<NPC, Player> entry : CloneManager.ninjaNPCs.entrySet())
				if (entry.getValue().equals(p)) {
					NPC npc = entry.getKey();
					Location npcLoc = npc.getBukkitEntity().getLocation();
					MetaLists.TP_AROUND.add(p);
					MetaLists.TP_AROUND.add(npc.getBukkitEntity());
					p.teleport(npcLoc);
					MetaLists.TP_AROUND.add(npc.getBukkitEntity());
					npc.getBukkitEntity().getInventory().setArmorContents(p.getInventory().getArmorContents());
					npc.getBukkitEntity().teleport(new Location(npc.getBukkitEntity().getWorld(), 0, -10, 0));
					CloneManager.ninjaNPCs.remove(npc);
					CloneManager.NPCs.remove(npc);
					npc.getBukkitEntity().setHealth(0);
					// ArrayList<PacketPlayOutEntityEquipment> packets = new
					// ArrayList<PacketPlayOutEntityEquipment>();
					// ItemStack[] armor = p.getInventory().getArmorContents();
					// int i = 0;
					// while (i < armor.length) {
					// if (armor[0] == null)
					// packets.add(new
					// PacketPlayOutEntityEquipment(npc.getBukkitEntity().getEntityId(),
					// 1,
					// CraftItemStack.asNMSCopy(armor[i])));
					// i++;
					// }
					// for (Player player : p.getWorld().getPlayers()) {
					// for (PacketPlayOutEntityEquipment packet : packets)
					// ((CraftPlayer)
					// player).getHandle().playerConnection.sendPacket(packet);
					// }
				}
		} else {
			if (Cooldown.hasCooldown(p, "Clone", true))
				return;
			Main.cool.add(new Cooldown(30, p, "Clone"));
			CloneManager.ninjaClone(p, true);
		}
	}

	public static void Force(Player p) {
		if (Locations.inSafe(p.getLocation())) {
			p.sendMessage(Messages.SAFEZONE_IN);
			return;
		}
		if (Cooldown.hasCooldown(p, "The force", true))
			return;
		Main.cool.add(new Cooldown(30, p, "The force"));
		double x = 10.0;
		double maxVelocity = 1.5;
		for (Entity ent : p.getNearbyEntities(x, x, x))
			if (ent instanceof LivingEntity) {
				if (ent instanceof Player) {
					Death.update((Player) ent, p, "was forced");
					if (((Player) ent).getHealth() <= 2.0) {
						((Player) ent).damage(9999);
						return;
					}
					MetaLists.TP_AROUND.add(ent);
				}
				Main.damageIgnoreArmor(ent, 2, true);
				if (ent.isValid()) {
					Vector velocity = Locations.calculateVelocity(p.getLocation().toVector(),
							ent.getLocation().toVector(), 1).normalize();
					double straightVelocity = ((x - (p.getLocation().toVector().setY(0).distance(ent.getLocation()
							.toVector().setY(0)))) / x)
							* maxVelocity;
					velocity = velocity.add(velocity.multiply(straightVelocity));
					velocity = velocity.setY(.5);
					ent.teleport(ent.getLocation().add(0, .3, 0));
					ent.setVelocity(ent.getVelocity().add(velocity));
				}
			}
	}
	public static void ThorsAxe(final Player p, PlayerInteractEvent event) {
		if(event.getClickedBlock() == null) return;
		if(Locations.inSafe(p.getLocation()) || Locations.inSafe(event.getClickedBlock().getLocation())){
			p.sendMessage(Messages.SAFEZONE_IN);
			return;
		}
		if (Cooldown.hasCooldown(p, "Thors Hammer", true))
			return;
		Main.cool.add(new Cooldown(35, p, "Thors Hammer"));
		MetaLists.IGNORE_DAMAGE_LIGHTNING.add( p );
		     event.getPlayer().getWorld().strikeLightning(event.getClickedBlock().getWorld().getHighestBlockAt(event.getClickedBlock().getLocation()).getLocation().clone().add(0, 1, 0)).setFireTicks(0);
		     Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
		    	   public void run() {
		    	    MetaLists.IGNORE_DAMAGE_LIGHTNING.remove( p );
		    	   }
		    	  }, 40L);
	}

	@SuppressWarnings("deprecation")
	public static void Heal(Player p) {
		if (p.getHealth() != p.getMaxHealth()) {
			double health = p.getHealth() + 7;
			if (Shop.effectiveSoupTimeLeft.containsKey(p.getName()) && !MetaLists.ONE_V_ONE_FIGHT.contains(p))
				health = health + 1.0;
			if (health > p.getMaxHealth())
				health = p.getMaxHealth();
			p.setHealth(health);
			if (p.getItemInHand().getType().equals(Material.MUSHROOM_SOUP))
				p.setItemInHand(Kit.easyItem(Messages.ITEM_HEALTH_EMPTY, Material.BOWL, 0, null, 1));
			else
				p.setItemInHand(null);
			p.updateInventory();
		}
	}
}