package me.MnMaxon.LonksKits;

import java.util.ArrayList;
import java.util.Map.Entry;

import me.MnMaxon.LonksKits.MetaLists;
import me.MnMaxon.Listeners.onInteract;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class Timer {
	private static Boolean running = false;
	private static int minuteTimer = 0;

	public static void startTimer() {
		if (running)
			return;
		running = true;

		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.plugin, new Runnable() {
			@Override
			public void run() {
				cooldownLoop();
				deathLoop();
				tpCooldown();
				minionTimer();
				minuteTimer++;

				if (minuteTimer > 2400) {
					minuteTimer = 0;
					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
						@Override
						public void run() {
							Shop.timer();
							Points.updateSigns(Points.highScore());
						}
					}, 1L);
				}
			}
		}, 0, 20L);
	}

	private static void cooldownLoop() {
		// This is called every second
		if (!Main.cool.isEmpty()) {
			ArrayList<Cooldown> remover = new ArrayList<Cooldown>();
			for (Cooldown cd : Main.cool) {
				cd.time = cd.time - 1;
				if (cd.time <= 0)
					remover.add(cd);
			}
			for (Cooldown remove : remover) {
				if (!remove.ability.equalsIgnoreCase("mount") && !remove.ability.equalsIgnoreCase("dismount")
						&& !remove.ability.equalsIgnoreCase("Machine Gun")
						&& !remove.ability.equalsIgnoreCase("safezone")
						&& !remove.ability.equalsIgnoreCase("safezoneDamage")
						&& !remove.ability.equalsIgnoreCase("assasin") && !remove.ability.equalsIgnoreCase("tpmessage")
						&& !remove.ability.equalsIgnoreCase("CloneKill") && !remove.ability.equalsIgnoreCase("KitZone")
						&& remove.player.isOnline())
					remove.player.sendMessage(ChatColor.GREEN + "You can now use " + ChatColor.GOLD
							+ remove.ability.toUpperCase() + ChatColor.GREEN + " again.");
				Main.cool.remove(remove);
			}
		}
	}

	private static void deathLoop() {
		if (!Death.damageInfo.isEmpty()) {
			ArrayList<HitInfo> remover = new ArrayList<HitInfo>();
			for (HitInfo hi : Death.damageInfo) {
				if (hi == null)
					remover.add(hi);
				else {
					hi.timer = hi.timer - 1;
					if (hi.timer <= 0)
						remover.add(hi);
				}
			}
			for (HitInfo remove : remover)
				Death.damageInfo.remove(remove);
		}
	}

	private static void tpCooldown() {
		if (!TpCountdown.toTp.isEmpty()) {
			ArrayList<TpCountdown> remover = new ArrayList<TpCountdown>();
			for (TpCountdown tpc : TpCountdown.toTp) {
				tpc.time = tpc.time - 1;
				if (tpc.time <= 0)
					remover.add(tpc);
			}
			for (TpCountdown remove : remover) {
				TpCountdown.toTp.remove(remove);
				if (remove.p.isOnline()) {
					remove.p.sendMessage(ChatColor.GOLD + "Teleportation commencing...");
					MetaLists.TP_AROUND.add(remove.p);
					MetaLists.TP_AROUND_PLOT.add(remove.p);
					Main.ClearKit(remove.p);
					if (remove.loc != null)
						remove.p.teleport(remove.loc);
					if (remove.command != null)
						remove.p.chat("/" + remove.command);
				}
			}
		}
	}

	private static void minionTimer() {
		if (onInteract.minions.entrySet().isEmpty())
			return;
		for (Entry<Player, ArrayList<Creature>> entry : onInteract.minions.entrySet()) {
			Player p = entry.getKey();
			ArrayList<Creature> toTarget = new ArrayList<Creature>();
			for (Creature ent : entry.getValue())
				if (ent.isValid())
					if (!ent.getNearbyEntities(15, 5, 15).contains(p) || Locations.inSafe(ent.getLocation())) {
						if (!ent.getNearbyEntities(30, 5, 30).contains(p))
							ent.teleport(p);
						else
							toTarget.add(ent);
					} else if (ent.getTarget() == null) {
						if (!ent.getNearbyEntities(10, 5, 10).contains(p))
							toTarget.add(ent);

					} else if (ent.getTarget().equals(p)) {
						if (ent.getNearbyEntities(3, 5, 3).contains(p))
							ent.setTarget(null);

					} else if (!ent.getNearbyEntities(10, 5, 10).contains(ent.getTarget())) {
						for (Entity closeEnt : ent.getNearbyEntities(10, 5, 10))
							if (closeEnt instanceof Player && !closeEnt.equals(p))
								ent.setTarget((Player) closeEnt);
					}

			for (Creature ent : toTarget) {
				ent.setMetadata("Target", new FixedMetadataValue(Main.plugin, true));
				ent.setTarget(p);
			}
		}
	}
}

// Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new
// Runnable() {
// public void run() {}
// }, 20L);