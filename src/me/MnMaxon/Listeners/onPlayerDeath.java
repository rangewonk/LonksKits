package me.MnMaxon.Listeners;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.MnMaxon.Apis.CloneManager;
import me.MnMaxon.Apis.DisguiseCraftMethods;
import me.MnMaxon.Kits.Assassin;
import me.MnMaxon.Kits.Berserker;
import me.MnMaxon.Kits.Ghost;
import me.MnMaxon.Kits.Killer;
import me.MnMaxon.Kits.Kit;
import me.MnMaxon.Kits.Shooter;
import me.MnMaxon.Kits.Switcher;
import me.MnMaxon.LonksKits.Death;
import me.MnMaxon.LonksKits.IP;
import me.MnMaxon.LonksKits.Killstreak;
import me.MnMaxon.LonksKits.Locations;
import me.MnMaxon.LonksKits.Main;
import me.MnMaxon.LonksKits.Messages;
import me.MnMaxon.LonksKits.MetaLists;
import me.MnMaxon.LonksKits.Points;
import me.MnMaxon.LonksKits.Shop;
import me.MnMaxon.LonksKits.Spectator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftCreature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class onPlayerDeath implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGH)
	public void onDeath(PlayerDeathEvent e) {
		if (e.getEntity().getWorld().equals(Locations.gameWorld))
			e.setDeathMessage(null);
		if (CloneManager.isClone(e.getEntity()))
			return;
		
		/*if(PlayerListener.skipKill.containsKey(e.getEntity()))
		{
			PlayerListener.skipKill.remove(e.getEntity());
			return;
		}*/
		
		boolean oneVOne = false;
		if (MetaLists.ONE_V_ONE_FIGHT.contains(e.getEntity()))
			oneVOne = true;

		if (oneVOne)
			Main.endOneVOne(e.getEntity(), (String) MetaLists.ONE_V_ONE_FIGHT.get(e.getEntity()));
		
		if (Death.getInfo(e.getEntity()) != null)
			Death.getInfo(e.getEntity()).timer = 0;
		MetaLists.IGNORE_DAMAGE_FALL.remove(e.getEntity());
		for (Entity ent : e.getEntity().getWorld().getEntities())
			if (ent instanceof Wolf && ((Wolf) ent).getCustomName() != null
					&& ((Wolf) ent).getCustomName().contains(e.getEntity().getName()))
				((Wolf) ent).damage(999);
		CloneManager.remove(e.getEntity());
		onInteract.removeMinions(e.getEntity());
		MetaLists.CAN_JUMP.remove(e.getEntity());
		DisguiseCraftMethods.unDisguise(e.getEntity());
		Spectator.remove(e.getEntity());
		if (Locations.gameWorld != null && e.getEntity().getLocation().getWorld().equals(Locations.gameWorld)) {
			MetaLists.SPAWN_TO.add(e.getEntity());
			for (Entity ent : e.getEntity().getNearbyEntities(5, 5, 5))
				if (ent instanceof Horse && (ent.getPassenger() == null || ent.getPassenger().equals(e.getEntity())))
					((Horse) ent).damage(9999);
			if (Death.getInfo(e.getEntity()) != null
					&& !e.getEntity().getName().equalsIgnoreCase(Death.getAttacker(e.getEntity()))) {
				if (Death.getInfo(e.getEntity()).attackerName.equals("")) {
					e.getEntity().sendMessage(ChatColor.DARK_RED + "You have been killed");
				} else {
					if (Shop.revengeZombieTimeLeft.containsKey(e.getEntity().getName()) && !oneVOne) {
						Zombie zombie = e.getEntity().getWorld().spawn(e.getEntity().getLocation(), Zombie.class);
						((CraftCreature) zombie).getHandle().getNavigation().d(false);
						zombie.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 0, Integer.MAX_VALUE));
						zombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1, Integer.MAX_VALUE));
						zombie.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 1, Integer.MAX_VALUE));
						MetaLists.REVENGE_MOB.add(zombie, e.getEntity().getName());
					}
					int transferredPoints = (int) (Points.get(e.getEntity().getName()) * .03);
					if (transferredPoints > 50)
						transferredPoints = 50;
					Points.remove(e.getEntity().getName(), transferredPoints);
					Points.add(Death.getInfo(e.getEntity()).attackerName, transferredPoints + 2);
					if (transferredPoints == 0)
						e.getEntity().sendMessage(
								ChatColor.AQUA + "You were killed by " + ChatColor.YELLOW
										+ Death.getInfo(e.getEntity()).attackerName + ChatColor.AQUA
										+ ", but were too poor to pay them!");
					else
						e.getEntity().sendMessage(
								ChatColor.AQUA + "You were killed by " + ChatColor.YELLOW
										+ Death.getInfo(e.getEntity()).attackerName + ChatColor.AQUA
										+ ", and so you lost " + ChatColor.YELLOW + transferredPoints + ChatColor.AQUA
										+ " Points!");
					if (Bukkit.getPlayer(Death.getInfo(e.getEntity()).attackerName) != null) {
						Player attacker = Bukkit.getPlayer(Death.getInfo(e.getEntity()).attackerName);
						if (transferredPoints == 0)
							attacker.sendMessage(ChatColor.AQUA + "You killed " + ChatColor.YELLOW
									+ e.getEntity().getName() + ChatColor.AQUA
									+ ", but they are too nooby to pay you, therefore you got " + ChatColor.YELLOW
									+ (transferredPoints + 2) + ChatColor.AQUA + " Points!");
						else
							attacker.sendMessage(ChatColor.AQUA + "You killed " + ChatColor.YELLOW
									+ e.getEntity().getName() + ChatColor.AQUA + ", so you earned " + ChatColor.YELLOW
									+ (transferredPoints + 2) + ChatColor.AQUA + " Points!");
						Date now = new Date();
						SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
						SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
						String path = "Kills.[" + formatDate.format(now) + "]";
						List<String> list = new ArrayList<String>();
						if (Main.log.get(path) != null)
							list = Main.log.getStringList(path);
						String matchMessage = "";
						if (IP.match(attacker, e.getEntity()))
							matchMessage = "        ===IP MATCH===";
						list.add("[" + formatTime.format(now) + "] " + e.getEntity().getName() + " was killed by "
								+ attacker.getName() + " (" + (transferredPoints + 2) + ")" + matchMessage);
						Main.log.set(path, list);
						Main.log.save();
						Death.update(attacker, e.getEntity(), "You killed");
						if (Shop.vampTimeLeft.containsKey(attacker.getName()) && attacker.isValid() || oneVOne)
							attacker.setHealth(attacker.getMaxHealth());
						if (MetaLists.PLAYERS.get(attacker) instanceof Switcher) {
							if (attacker.getInventory().contains(Material.SNOW_BALL)
									&& attacker.getInventory()
											.getItem(attacker.getInventory().first(Material.SNOW_BALL)).getAmount() < 64) {
								attacker.getInventory()
										.getItem(attacker.getInventory().first(Material.SNOW_BALL))
										.setAmount(
												attacker.getInventory()
														.getItem(attacker.getInventory().first(Material.SNOW_BALL))
														.getAmount() + 4);
								e.getEntity().updateInventory();
							} else
								attacker.getInventory().addItem(
										Kit.easyItem("Switcher", Material.SNOW_BALL, 0, null, 4));
						} else if (MetaLists.PLAYERS.get(attacker) instanceof Shooter) {
							int x = 0, arrows = 10;
							while (arrows > 0 && x < attacker.getInventory().getSize()) {
								if (attacker.getInventory().getContents()[x] != null) {
									ItemStack is = attacker.getInventory().getContents()[x].clone();
									is.setAmount(attacker.getInventory().getContents()[x].getAmount() + 1);
									if (is.getType().equals(Material.ARROW)
											&& attacker.getInventory().getContents()[x].getAmount() < 64) {
										attacker.getInventory().setItem(x, is);
										arrows--;
									} else
										x++;
								} else
									x++;
							}
							x = 0;
							while (arrows > 0 && x < attacker.getInventory().getSize()) {
								if (attacker.getInventory().getContents()[x] == null) {
									ItemStack is = Kit.easyItem(ChatColor.BLUE + "Machine Gun", Material.ARROW, 0,
											null, arrows);
									attacker.getInventory().setItem(x, is);
									arrows = 0;
								}
								x++;
							}
							if (arrows != 10)
								attacker.updateInventory();
						} else if (MetaLists.PLAYERS.get(attacker) instanceof Assassin)
							e.getEntity().sendMessage(
									ChatColor.RED + "HINT: " + ChatColor.GRAY
											+ "To see invisible assassins, turn particle effects on.");
						else if (MetaLists.PLAYERS.get(attacker) instanceof Ghost)
							e.getEntity().sendMessage(
									ChatColor.RED + "HINT: " + ChatColor.GRAY
											+ "To see invisible ghosts, turn particle effects on.");
						else if (MetaLists.PLAYERS.get(attacker) instanceof Berserker) {
							attacker.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 16 * 20, 0));
						}
					}
					Killstreak.end(Death.getAttacker(e.getEntity()), e.getEntity().getName());
					Killstreak.add(Death.getAttacker(e.getEntity()), 1);
				}
			} else
				e.getEntity().sendMessage(Messages.PREFIX + ChatColor.DARK_RED + "You have been killed");
		}
		if (MetaLists.PLAYERS.contains(e.getEntity())) {
			if (MetaLists.PLAYERS.get(e.getEntity()) instanceof Killer) {
				TNTPrimed tnt = (TNTPrimed) e.getEntity().getLocation().getWorld()
						.spawnEntity(e.getEntity().getLocation(), EntityType.PRIMED_TNT);
				tnt.setFuseTicks(5);
			}
			e.getDrops().clear();
			if (!Spectator.isSpectator(e.getEntity()) && !Locations.inSafe(e.getEntity())
					&& MetaLists.PLAYERS.get(e.getEntity()) != null && !oneVOne) {
				ItemStack soup = Kit.easyItem(Messages.ITEM_HEALTH, Material.MUSHROOM_SOUP, 0, null, 1);
				e.getDrops().add(soup);
				e.getDrops().add(soup);
				e.getDrops().add(soup);
				e.getDrops().add(soup);
				e.getDrops().add(soup);
			}
			e.setDeathMessage(null);
		}
		if (MetaLists.CAN_FLY.contains(e.getEntity()))
			MetaLists.CAN_FLY.remove(e.getEntity());
		Main.kick(e.getEntity());
		Spectator.remove(e.getEntity());
		MetaLists.BYPASS_SAFEZONE.remove(e.getEntity());
		Death.damageInfo.remove(Death.getInfo(e.getEntity()));
	}

}
