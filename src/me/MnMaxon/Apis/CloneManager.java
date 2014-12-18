package me.MnMaxon.Apis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import me.MnMaxon.Kits.Anvil;
import me.MnMaxon.LonksKits.Cooldown;
import me.MnMaxon.LonksKits.Death;
import me.MnMaxon.LonksKits.Locations;
import me.MnMaxon.LonksKits.Main;
import me.MnMaxon.LonksKits.MetaLists;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.lenis0012.bukkit.npc.EquipmentSlot;
import com.lenis0012.bukkit.npc.NPC;
import com.lenis0012.bukkit.npc.NPCAnimation;
import com.lenis0012.bukkit.npc.NPCProfile;

public class CloneManager {
	public static Map<NPC, Player> NPCs = new HashMap<NPC, Player>();
	public static Map<NPC, Player> ninjaNPCs = new HashMap<NPC, Player>();

	public static void add(final Player p) {
		if (Locations.inSafe(p.getLocation())) {
			p.sendMessage(ChatColor.GOLD + "[" + ChatColor.AQUA + "LonksKits" + ChatColor.GOLD + "]"
					+ ChatColor.DARK_RED + "You can't do that in the safe zone!");
			return;
		}
		int cloneAmount = 0;
		for (Entry<NPC, Player> entry : NPCs.entrySet())
			if (p.equals(entry.getValue()))
				cloneAmount++;
		if (cloneAmount >= 5) {
			p.sendMessage(ChatColor.RED + "You have already cloned yourself 5 times!");
			return;
		}
		for (Cooldown cd : Main.cool)
			if (cd.player.equals(p) && cd.ability.equalsIgnoreCase("clone")) {
				p.sendMessage(ChatColor.DARK_RED + "You are on cooldown for another " + cd.time + " seconds");
				return;
			}
		Main.cool.add(new Cooldown(5, p, "clone"));
		ninjaClone(p, false);
	}

	public static void ninjaClone(final Player p, final boolean ninja) {
		final Location location = p.getLocation();
		final String playerName = p.getName();
		new Thread() {
			@Override
			public void run() {
				NPCProfile unFinalProfile = null;
				if (StatusChecker.SKINS.getStatus().equals(StatusChecker.Status.ONLINE))
					unFinalProfile = NPCProfile.loadProfile(playerName, playerName);
				else
					unFinalProfile = new NPCProfile(playerName);
				final NPCProfile profile = unFinalProfile;
				Bukkit.getScheduler().runTask(Main.plugin, new Runnable() {
					@Override
					public void run() {
						final NPC npc = Main.npcFactory.spawnHumanNPC(location, profile);
						npc.setInvulnerable(false);
						npc.setEntityCollision(false);
						npc.setYaw(location.getYaw());
						NPCs.put(npc, p);
						Main.ghostManager.removePlayer(npc.getBukkitEntity());
						if (ninja) {
							ninjaNPCs.put(npc, p);
							Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
								@Override
								public void run() {
									if (npc != null && npc.getBukkitEntity().isValid()) {
										NPCs.remove(npc);
										ninjaNPCs.remove(npc);
										for (Entity ent : npc.getBukkitEntity().getNearbyEntities(5, 5, 5))
											if (ent instanceof Player && !ent.equals(p))
												Death.update((Player) ent, p, "nin-splosion");
											else if (ent.equals(p)) {
												MetaLists.IGNORE_DAMAGE_EXPLOSION.add(p);
												Bukkit.getServer().getScheduler()
														.scheduleSyncDelayedTask(Main.plugin, new Runnable() {
															@Override
															public void run() {
																MetaLists.IGNORE_DAMAGE_EXPLOSION.remove(p);
															}
														}, 20L);
											}
										Location loc = npc.getBukkitEntity().getLocation();
										npc.getBukkitEntity().addPotionEffect(
												new PotionEffect(PotionEffectType.INVISIBILITY, 20, 1));
										npc.getBukkitEntity().setHealth(0);
										loc.getWorld().createExplosion(loc, 4F);
									}
								}
							}, 5 * 20L);
						}
						NpcLoop(p, npc, p.getInventory().getArmorContents(), ninja);
					}
				});
			}
		}.start();
	}

	private static void NpcLoop(final Player p, final NPC npc, final ItemStack[] armor, final boolean ninja) {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
			@Override
			public void run() {
				if (p.isOnline() && p.isValid() && npc.getBukkitEntity().isValid()) {
					npc.setEquipment(EquipmentSlot.BOOTS, armor[0]);
					npc.setEquipment(EquipmentSlot.LEGGINGS, armor[1]);
					npc.setEquipment(EquipmentSlot.CHESTPLATE, armor[2]);
					npc.setEquipment(EquipmentSlot.HELMET, armor[3]);
					if (ninja) {
						NpcLoop(p, npc, armor, ninja);
						return;
					}
					Entity target = null;
					if (npc.getTarget() != null
							&& npc.getBukkitEntity().getNearbyEntities(4, 4, 4).contains(npc.getTarget())
							&& npc.getTarget() instanceof LivingEntity && npc.getTarget().isValid()
							&& !Locations.inSafe(npc.getTarget().getLocation())
							&& !Locations.inSafe(npc.getBukkitEntity().getLocation()) && !NPCs.containsKey(npc.getTarget())
							&& Main.canDamage(npc.getTarget())) {
						target = npc.getTarget();
						npc.setTarget(target);
						if (((LivingEntity) target).getHealth() > 1) {
							((LivingEntity) target).setHealth(((LivingEntity) target).getHealth() - 1);
							npc.playAnimation(NPCAnimation.SWING_ARM);
							if (!MetaLists.PLAYERS.contains(target) || MetaLists.PLAYERS.get(target) == null
									|| !(MetaLists.PLAYERS.get(target) instanceof Anvil))
								target.setVelocity(npc.getBukkitEntity().getLocation().getDirection().setY(.3)
										.multiply(1));
						} else
							((LivingEntity) target).damage(999999);
					} else
						for (Entity ent : npc.getBukkitEntity().getNearbyEntities(4, 4, 4))
							if (target == null
									&& (!(ent instanceof Player) || !((Player) ent).getName().equals(
											npc.getBukkitEntity().getName())) && !Locations.inSafe(ent.getLocation())
									&& !Locations.inSafe(npc.getBukkitEntity().getLocation()) && !NPCs.containsKey(ent)
									&& Main.canDamage(ent)) {
								target = ent;
								npc.setTarget(target);
								if (((LivingEntity) target).getHealth() > 1) {
									((LivingEntity) target).setHealth(((LivingEntity) target).getHealth() - 1);
									npc.playAnimation(NPCAnimation.SWING_ARM);
									if (!MetaLists.PLAYERS.contains(target) || MetaLists.PLAYERS.get(target) == null
											|| !(MetaLists.PLAYERS.get(target) instanceof Anvil))
										target.setVelocity(npc.getBukkitEntity().getLocation().getDirection().setX(.5)
												.multiply(1));
								} else
									((LivingEntity) target).damage(999999);
							}
					if (target == null
							&& (npc.getTarget() == null || !npc.getBukkitEntity().getNearbyEntities(10, 10, 10)
									.contains(npc.getTarget())))
						for (Entity ent : npc.getBukkitEntity().getNearbyEntities(8, 8, 8))
							if (target == null
									&& (!(ent instanceof Player) || !((Player) ent).getName().equals(
											npc.getBukkitEntity().getName())) && !Locations.inSafe(ent.getLocation())
									&& Main.canDamage(ent)) {
								target = ent;
								npc.setTarget(target);
							}
					NpcLoop(p, npc, armor, ninja);
				}
			}
		}, 30L);
	}

	public static void remove(final Player p) {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
			@Override
			public void run() {
				ArrayList<NPC> removeList = new ArrayList<NPC>();
				for (Entry<NPC, Player> entry : NPCs.entrySet())
					if (p.equals(entry.getValue()))
						removeList.add(entry.getKey());
				for (NPC npc : removeList) {
					if (npc.getBukkitEntity().isValid()) {
						MetaLists.TP_AROUND.add(npc.getBukkitEntity());
						npc.getBukkitEntity().teleport(new Location(npc.getBukkitEntity().getWorld(), 0, -10, 0));
						npc.getBukkitEntity().setHealth(0);
					}
					NPCs.remove(npc);
					ninjaNPCs.remove(npc);
				}
			}
		}, 5L);
	}

	public static boolean isClone(Entity ent) {
		return Main.npcFactory.isNPC(ent);
	}
}