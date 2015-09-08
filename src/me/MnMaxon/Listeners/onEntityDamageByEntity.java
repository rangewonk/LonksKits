
package me.MnMaxon.Listeners;

import java.util.Random;

import me.MnMaxon.Kits.Anvil;
import me.MnMaxon.Kits.BeastMaster;
import me.MnMaxon.Kits.Casper;
import me.MnMaxon.Kits.Cowboy;
import me.MnMaxon.Kits.Creeper;
import me.MnMaxon.Kits.Kit;
import me.MnMaxon.Kits.Portastomp;
import me.MnMaxon.Kits.Snake;
import me.MnMaxon.Kits.Snowman;
import me.MnMaxon.LonksKits.Cooldown;
import me.MnMaxon.LonksKits.Death;
import me.MnMaxon.LonksKits.Locations;
import me.MnMaxon.LonksKits.Main;
import me.MnMaxon.LonksKits.Messages;
import me.MnMaxon.LonksKits.MetaLists;
import me.MnMaxon.LonksKits.Spectator;
import me.MnMaxon.LonksKits.TpCountdown;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class onEntityDamageByEntity  implements Listener {
	@EventHandler
	public void onHit(EntityDamageByEntityEvent e) {
		if (Locations.gameWorld == null || !e.getEntity().getWorld().equals(Locations.gameWorld))
			return;
		else if (e.getDamager() instanceof Player){
			Player p=(Player) e.getDamager();
			if(Spectator.isSpectator(p) ||(e.getEntity instanceof ItemFrame&& !MetaLists.BYPASS_BUILD.contains(p))) {
				e.setCancelled(true);
				return;
			} 
		}
		if (e.getEntity() instanceof Player && Locations.inSafe(e.getEntity().getLocation())) {
				if (e.getDamager() instanceof Player)
					((Player) e.getDamager()).sendMessage(Messages.SAFEZONE_IN);
				e.setCancelled(true);
			}

		// Attacker is a revenge zombie ======================================
		if (MetaLists.REVENGE_MOB.contains(e.getDamager())) {
			if (e.getEntity() instanceof Player
					&& Bukkit.getPlayer((String) MetaLists.REVENGE_MOB.get(e.getDamager())) != null)
				Death.update((Player) e.getEntity(),
						Bukkit.getPlayer((String) MetaLists.REVENGE_MOB.get(e.getDamager())), "was revenged upon");
		}

		// Attacker is a minion ==============================================
		else if (e.getDamager().hasMetadata("Minion")) {
			if (e.getEntity() instanceof Player
					&& ((Player) e.getEntity()).getName().equals(e.getDamager().getMetadata("Minion").get(0).value()))
				e.setCancelled(true);
			else if (e.getEntity() instanceof Player
					&& Bukkit.getPlayer((String) e.getDamager().getMetadata("Minion").get(0).value()) != null)
				Death.update((Player) e.getEntity(),
						Bukkit.getPlayer((String) e.getDamager().getMetadata("Minion").get(0).value()),
						"was minioned by");
			else if (e.getEntity().hasMetadata("Minion")
					&& e.getDamager().getMetadata("Minion").get(0).value()
							.equals(e.getEntity().getMetadata("Minion").get(0).value()))
				e.setCancelled(true);
		}

		// Attacker is a player in kitpvp ====================================
		else if (MetaLists.PLAYERS.contains(e.getDamager()) && !e.isCancelled()) {
			Player p = (Player) e.getDamager();
			Kit kit = null;
			if (MetaLists.PLAYERS.get(p) != null)
				kit = (Kit) MetaLists.PLAYERS.get(p);

			if (Locations.inSafe(p.getLocation())) {
				(p).sendMessage(Messages.SAFEZONE_IN);
				e.setCancelled(true);
				return;
			}

			if (p.getItemInHand() != null && p.getItemInHand().hasItemMeta()
					&& p.getItemInHand().getItemMeta().hasDisplayName()) {
				String itemName = ChatColor.stripColor(p.getItemInHand().getItemMeta().getDisplayName());
				if (itemName.equalsIgnoreCase("chomp")) {
					e.setCancelled(true);
					if (Cooldown.hasCooldown(p, "chomp", true))
						return;
					Main.cool.add(new Cooldown(25, p, "chomp"));
					if (MetaLists.PLAYERS.contains(p))
						Death.update((Player) e.getEntity(), p, "was chomped");
					Main.damageIgnoreArmor(e.getEntity(), 14, true);
					p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 6 * 20, 1));
					p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 6 * 20, 1));
				} else if (itemName.equalsIgnoreCase("trolololol")) {
					if (e.getEntity() instanceof Player)
						((Player) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING,
								21 * 4, 0), true);
				}
			}

			if (kit instanceof Creeper) {
				if (new Random().nextDouble() <= .10) {
					((Player) e.getEntity()).setVelocity(new Vector(0, 5, 0));
					MetaLists.CREEPER_JUMP.add(e.getEntity());
				}
			} else if (kit instanceof BeastMaster) {
				if (e.getEntity() instanceof Wolf && ((Wolf) e.getEntity()).getCustomName().contains((p).getName()))
					e.setCancelled(true);
			} else if (kit instanceof Snake) {
				if ((p).getItemInHand().getType().equals(Material.IRON_SWORD))
					((LivingEntity) e.getEntity())
							.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 4 * 20, 0));
			} else if (kit instanceof Anvil) {
				e.setCancelled(true);
				((Player) e.getEntity()).damage(e.getDamage());
			} else if (kit instanceof Casper)
				if (MetaLists.PLAYERS_INVISIBLE.contains(p)) {
					e.setCancelled(true);
					return;

				}

			// The entity getting hit is a player in kitpvp
			if (MetaLists.PLAYERS.contains(e.getEntity())) {

				if (e.getCause().equals(DamageCause.ENTITY_ATTACK) && !Locations.inSafe(p)
						&& !Locations.inSafe(e.getEntity()) && Main.canDamage(p) && Main.canDamage(e.getEntity())) {
					Death.update((Player) e.getEntity(), p, "was beaten to death");
					if (MetaLists.PLAYERS.get(e.getEntity()) instanceof Cowboy && e.getEntity().getVehicle() != null) {
						ItemStack is = (p).getItemInHand();
						if (is != null) {
							Vector velocity = Locations
									.calculateVelocity(p.getLocation().toVector(),
											e.getEntity().getLocation().toVector(), 1).normalize()
									.multiply((is.getEnchantmentLevel(Enchantment.KNOCKBACK) + 1) * 1.2);
							velocity = velocity.setY((is.getEnchantmentLevel(Enchantment.KNOCKBACK) + 1) / 7.0);
							e.getEntity().getVehicle().setVelocity(e.getEntity().getVelocity().add(velocity));
						}
					}
				}
			}
			// The Entity getting hit is a minion
			else if (e.getEntity().hasMetadata("Minion")
					&& (p).getName().equals(e.getEntity().getMetadata("Minion").get(0).value())) {
				e.setCancelled(true);
				return;
			}
		}

		// Attacker is a projectile =====================================
		else if (e.getDamager() instanceof Projectile) {

			if (((Projectile) e.getDamager()).getShooter() != null
					&& ((Projectile) e.getDamager()).getShooter() instanceof Entity
					&& Locations.inSafe(((Entity) ((Projectile) e.getDamager()).getShooter()).getLocation())) {
				if (((Projectile) e.getDamager()).getShooter() instanceof Player)
					((Player) ((Projectile) e.getDamager()).getShooter()).sendMessage(Messages.SAFEZONE_IN);
				e.setCancelled(true);
				return;
			}

			if (e.getDamager() instanceof Arrow && MetaLists.PLAYERS.contains(e.getEntity())) {

				if (((Arrow) e.getDamager()).getShooter() instanceof Player
						&& MetaLists.PLAYERS.contains((Player) ((Arrow) e.getDamager()).getShooter())
						&& !Locations.inSafe(e.getEntity()) && !Locations.inSafe(e.getDamager()))
					Death.update((Player) e.getEntity(), (Player) ((Arrow) e.getDamager()).getShooter(), "was shot");

			} else if (e.getDamager() instanceof Snowball) {

				Snowball snowball = (Snowball) e.getDamager();
				if (snowball.getShooter() instanceof Player) {
					Player p = (Player) snowball.getShooter();
					if (MetaLists.PLAYERS.contains(p) && MetaLists.PLAYERS.get(p) instanceof Snowman
							&& Main.canDamage(e.getEntity()) && MetaLists.PLAYERS.contains(e.getEntity())
							&& !e.getDamager().equals(e.getEntity()) && !p.equals(e.getEntity())) {
						Death.update((Player) e.getEntity(), p, "was frozen by");
						Main.cool.add(new Cooldown(4, (Player) e.getEntity(), "tpMessage"));
						MetaLists.FREEZE.add(e.getEntity());
						((Player) e.getEntity())
								.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 3 * 20, 0));
						final Entity fent = e.getEntity();
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
							@Override
							public void run() {
								MetaLists.FREEZE.remove(fent);
							}
						}, 3 * 20L);
					}
				}
			}

			// The attacker was a "flung" egg
			if (Main.canDamage(e.getEntity()) && MetaLists.FLUNG.contains(e.getDamager())
					&& !((Projectile) e.getDamager()).getShooter().equals(e.getEntity())) {
				Entity ent = e.getEntity();
				int maxDamage = 999999;
				int minDamage = 2;

				if (ent instanceof Horse && ent.getPassenger() != null) {
					ent = ent.getPassenger();
					minDamage = 1;
				}

				Location loc1 = ((Location) MetaLists.FLUNG.get(e.getDamager()));
				e.setCancelled(true);
				loc1.setY(0);
				Location loc2 = (ent.getLocation());
				loc2.setY(0);
				if (MetaLists.PLAYERS.contains(ent))
					Death.update((Player) ent, (Player) ((Projectile) e.getDamager()).getShooter(), "was flung by");
				if (ent instanceof Damageable) {
					if (loc1.distance(loc2) >= 20) {
						((Damageable) ent).damage(maxDamage);
						((Player) ((Projectile) e.getDamager()).getShooter()).playSound(e.getDamager().getLocation(),
								Sound.ORB_PICKUP, 17, 1);
					} else {
						Main.damageIgnoreArmor(ent, minDamage, true);
						if (ent instanceof Player)
							((Player) ((Projectile) e.getDamager()).getShooter()).sendMessage(ChatColor.RED
									+ "You hit " + ((Player) ent).getName()
									+ " but they were less than 20 blocks away so they only took 1 heart damage.");
						else
							((Player) ((Projectile) e.getDamager()).getShooter()).sendMessage(ChatColor.RED
									+ "You hit a " + e.getEntityType().name()
									+ " but it was less than 20 blocks away so it only took 1 heart damage.");
					}
				}
				e.getDamager().remove();
				return;
			}

			if (e.getEntity() instanceof Horse && e.getEntity().getPassenger() != null
					&& e.getEntity().getPassenger() instanceof Player) {
				e.setCancelled(true);
				((Player) e.getEntity().getPassenger()).damage(e.getDamage() / 3.0, e.getDamager());
			}
		}

		if (MetaLists.SWITCHERS.contains(e.getDamager()))
			if (MetaLists.SWITCHERS.get(e.getDamager()).equals(e.getEntity()))
				e.setCancelled(true);
			else {
				Player p = (Player) MetaLists.SWITCHERS.get(e.getDamager());
				Entity ent = e.getEntity();
				MetaLists.SWITCHERS.remove(e.getEntity());
				if (Locations.inSafe(e.getDamager().getLocation())) {
					p.sendMessage(Messages.SAFEZONE_IN);
					return;
				} else if (Locations.inSafe(ent.getLocation())) {
					p.sendMessage(Messages.SAFEZONE_IN);
					return;
				}
				Location loc1 = ent.getLocation().clone();
				Location loc2 = p.getLocation().clone();
				MetaLists.TP_AROUND.add(p);
				if (ent instanceof Player) {
					MetaLists.TP_AROUND.add(ent);
					Death.update((Player) ent, p, "got switched to death");
				}
				p.teleport(loc1);
				ent.teleport(loc2);
				if (ent instanceof Player) {
					p.sendMessage(Messages.PREFIX + ChatColor.RED + "You have switched locations with "
							+ ((Player) ent).getName());
					((Player) ent).sendMessage(Messages.PREFIX + ChatColor.RED + "You have switched locations with "
							+ p.getName());
				}
				return;
			}
		if (!e.isCancelled() && e.getEntity() instanceof Player) {
			TpCountdown.cancel((Player) e.getEntity());
			if (e.getEntity().getVelocity().getY() > 5 && MetaLists.PLAYERS.contains(e.getEntity())
					&& MetaLists.PLAYERS.get(e.getEntity()) instanceof Portastomp
					&& ((Player) e.getEntity()).getHealth() - e.getDamage() > 0) {
				e.setCancelled(true);
				((Player) e.getEntity()).setHealth(((Player) e.getEntity()).getHealth() - e.getDamage());
			}
		}
		if (!e.isCancelled()
				&& MetaLists.PLAYERS.contains(e.getEntity())
				&& e.getDamager() instanceof Wolf
				&& ((Wolf) e.getDamager()).getCustomName() != null
				&& Bukkit.getServer().getPlayer(
						ChatColor.stripColor(((Wolf) e.getDamager()).getCustomName().replace("'s Wolf", ""))) != null)
			Death.update(
					(Player) e.getEntity(),
					Bukkit.getServer().getPlayer(
							ChatColor.stripColor(((Wolf) e.getDamager()).getCustomName().replace("'s Wolf", ""))),
					"was eaten by wolves");
	}
}

