package me.MnMaxon.Listeners;

import me.MnMaxon.Apis.CloneManager;
import me.MnMaxon.Kits.Cowboy;
import me.MnMaxon.LonksKits.Cooldown;
import me.MnMaxon.LonksKits.Death;
import me.MnMaxon.LonksKits.Locations;
import me.MnMaxon.LonksKits.Main;
import me.MnMaxon.LonksKits.Messages;
import me.MnMaxon.LonksKits.MetaLists;
import me.MnMaxon.LonksKits.Spectator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;

//import com.lenis0012.bukkit.npc.NPCDamageEvent;

public class EntityListener implements Listener {

	@EventHandler
	public void onExit(VehicleExitEvent e) {
		if (MetaLists.PLAYERS.contains(e.getExited()) && MetaLists.PLAYERS.get(e.getExited()) instanceof Cowboy
				&& !MetaLists.HORSE_AROUND.contains(e.getExited())) {
			e.setCancelled(true);
			if (Cooldown.hasCooldown((Player) e.getExited(), "dismount", false))
				return;
			Main.cool.add(new Cooldown(3, (Player) e.getExited(), "dismount"));
			((Player) e.getExited()).sendMessage(Messages.PREFIX + ChatColor.RED + "You can't leave your horse");
		}
		if (e.getVehicle().getVehicle() != null)
			e.getVehicle().getVehicle().eject();
		MetaLists.HORSE_AROUND.remove(e.getExited());
	}

	@EventHandler
	public void onBoom(EntityExplodeEvent e) {
		if (Locations.gameWorld != null && e.getLocation().getWorld().equals(Locations.gameWorld))
			e.blockList().clear();

		if (e.getEntity() != null && MetaLists.FIREBALLS.contains(e.getEntity()))
			for (Entity ent : e.getEntity().getNearbyEntities(2, 2, 2))
				if (MetaLists.PLAYERS.contains(ent))
					Death.update((Player) ent, (Player) MetaLists.FIREBALLS.get(e.getEntity()), "was shadowbladed");
	}

	@EventHandler
	public void onSpawn(CreatureSpawnEvent e) {
		if (Locations.gameWorld != null
				&& e.getLocation().getWorld().equals(Locations.gameWorld)
				&& !e.getEntityType().equals(EntityType.PLAYER)
				&& (e.getSpawnReason().equals(SpawnReason.NATURAL) || e.getSpawnReason().equals(SpawnReason.JOCKEY) || e
						.getSpawnReason().equals(SpawnReason.valueOf("MOUNT"))))
			e.setCancelled(true);
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent e) {
		if (Locations.gameWorld != null && Locations.gameWorld.equals(e.getEntity().getWorld())) {
			if (!(e.getEntity() instanceof Player))
				e.getDrops().clear();
			e.setDroppedExp(0);
			//TODO: Fix NPCs
			//CloneManager.NPCs.remove(e.getEntity().getEntityId());
		}
	}

	@EventHandler
	public void onEntityInteract(EntityInteractEvent e) {
		if (Main.mines.containsKey(e.getBlock()) && e.getEntity() instanceof LivingEntity) {
			if (e.getEntity() instanceof Player && Spectator.isSpectator((Player) e.getEntity()))
				return;
			if (Bukkit.getPlayer(Main.mines.get(e.getBlock())) != null
					&& Bukkit.getPlayer(Main.mines.get(e.getBlock())).isOnline()) {
				for (Entity ent : e.getEntity().getNearbyEntities(6, 6, 6))
					if (ent instanceof Player)
						Death.update((Player) ent, Bukkit.getPlayer(Main.mines.get(e.getBlock())), "boom");
			}
			if (e.getEntity() instanceof Player) {
				Death.update((Player) e.getEntity(), Bukkit.getPlayer(Main.mines.get(e.getBlock())), "boom");
				((Player) e.getEntity()).sendMessage(ChatColor.DARK_RED + "You have activated "
						+ Main.mines.get(e.getBlock()) + "'s trap.");
			}
			e.getEntity().getLocation().getWorld().createExplosion(e.getEntity().getLocation(), 4f);
			Main.mines.remove(e.getBlock());
			e.getBlock().setType(Material.AIR);
		}
	}

	@EventHandler
	public void onEntityHealthChange(EntityRegainHealthEvent e) {
		if (MetaLists.PLAYERS.contains(e.getEntity()) && e.getRegainReason().equals(RegainReason.SATIATED))
			e.setCancelled(true);
	}

	@EventHandler
	public void onVehicleEnter(VehicleEnterEvent e) {
		if (MetaLists.PLAYERS.contains(e.getEntered()) && !(MetaLists.PLAYERS.get(e.getEntered()) instanceof Cowboy)
				&& e.getVehicle() instanceof Horse)
			e.setCancelled(true);
	}

	//TODO: Fix NPCs
	/*@EventHandler
	public void NPCDamage(NPCDamageEvent e) {
		if (e.getDamager() != null && !Locations.inSafe(e.getNpc().getBukkitEntity().getLocation())
				&& Locations.inSafe(e.getDamager().getLocation())) {
			e.setCancelled(true);
			return;
		}
		if (e.getNpc().getBukkitEntity().getHealth() - e.getDamage() <= 0) {
			if (e.getDamager() instanceof Player) {
				Boolean message = true;
				if (Cooldown.hasCooldown((Player) e.getDamager(), "CloneKill", false))
					message = false;
				if (message) {
					Main.cool.add(new Cooldown(1, (Player) e.getDamager(), "CloneKill"));
					((Player) e.getDamager()).sendMessage(ChatColor.AQUA + "You killed "
							+ e.getNpc().getBukkitEntity().getName() + "'s Clone!");
				}
			}
			e.setCancelled(true);
			e.getNpc().getBukkitEntity().remove();
		}
	}*/

	@EventHandler
	public void onTarget(EntityTargetEvent e) {
		if (MetaLists.PIGGY.contains(e.getEntity())) {
			if (MetaLists.PIGGY.get(e.getEntity()).equals(e.getTarget()) || !Main.canDamage(e.getTarget()))
				e.setCancelled(true);
		} else if (e.getEntity().hasMetadata("Minion") && e.getTarget() != null) {
			if (Locations.inSafe(e.getTarget().getLocation()))
				e.setCancelled(true);
			if (e.getEntity().hasMetadata("Target")) {
				e.getEntity().removeMetadata("Target", Main.plugin);
			} else {
				if (e.getTarget() instanceof Player
						&& ((Player) e.getTarget()).getName()
								.equals(e.getEntity().getMetadata("Minion").get(0).value()))
					e.setCancelled(true);
				if (!e.getEntity().getNearbyEntities(15, 15, 15).contains(e.getTarget()))
					e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onCombustion(EntityCombustEvent e) {
		if (e.getEntity().hasMetadata("Minion"))
			e.setCancelled(true);
	}

	@EventHandler
	public void onArrowHit(ProjectileHitEvent e) {
		if (e.getEntity() instanceof Arrow) {
			Arrow arrow = (Arrow) e.getEntity();
			if (MetaLists.NO_LAND.contains(arrow))
				arrow.remove();
		}
	}
}
