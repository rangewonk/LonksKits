package me.MnMaxon.Listeners;

import me.MnMaxon.LonksKits.Cooldown;
import me.MnMaxon.LonksKits.Locations;
import me.MnMaxon.LonksKits.Main;
import me.MnMaxon.LonksKits.Messages;
import me.MnMaxon.LonksKits.MetaLists;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class onShoot implements Listener {

	@EventHandler
	public void onBowShoot(final EntityShootBowEvent e) {
		if (Locations.inSafe(e.getEntity().getLocation())) {
			if (e.getEntity() instanceof Player)
				((Player) e.getEntity()).sendMessage(Messages.SAFEZONE_IN);
			e.setCancelled(true);
			return;
		}

		if (e.getEntity().hasMetadata("Minion")) {
			if (e.getEntity() instanceof Skeleton
					&& ((Skeleton) e.getEntity()).getTarget() instanceof Player
					&& ((Player) ((Skeleton) e.getEntity()).getTarget()).getName().equals(
							e.getEntity().getMetadata("Minion").get(0).value()))
				e.setCancelled(true);
			else
				e.getProjectile().setMetadata("Minion",
						new FixedMetadataValue(Main.plugin, e.getEntity().getMetadata("Minion").get(0).value()));
		}

		if (MetaLists.PLAYERS.contains(e.getEntity()) && e.getBow().hasItemMeta()
				&& e.getBow().getItemMeta().hasDisplayName()
				&& ChatColor.stripColor(e.getBow().getItemMeta().getDisplayName()).equalsIgnoreCase("Sneaky Bow")) {
			e.getProjectile().remove();
			if (Cooldown.hasCooldown((Player) e.getEntity(), "Sneaky Bow", false))
				return;
			Main.cool.add(new Cooldown(10, (Player) e.getEntity(), "Sneaky Bow"));
			e.getEntity().setVelocity(
					((Player) e.getEntity()).getLocation().getDirection().multiply(5.0 * e.getForce()));
			e.getEntity().setNoDamageTicks(10);
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
				@Override
				public void run() {
					onShoot.track((Player) e.getEntity(), 0);
				}
			}, 5L);
		}
	}

	public static void track(final Player p, final int time) {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
			@Override
			public void run() {
				if (p.getLocation().add(0, -.001, 0).getBlock().getType().isSolid() && time > 20) {
					p.setNoDamageTicks(0);
					p.setFallDistance(0);
					return;
				}
				p.setNoDamageTicks(10);
				p.setFallDistance(0);
				track(p, time + 2);
			}
		}, 2L);
	}

}
