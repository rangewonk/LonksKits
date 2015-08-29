package me.MnMaxon.Listeners;

import java.util.Random;

import me.MnMaxon.Apis.CloneManager;
import me.MnMaxon.Apis.EssentialsMethods;
import me.MnMaxon.Kits.Anvil;
import me.MnMaxon.Kits.BigFoot;
import me.MnMaxon.Kits.Cowboy;
import me.MnMaxon.Kits.Flamer;
import me.MnMaxon.Kits.Jumper;
import me.MnMaxon.Kits.Kangaroo;
import me.MnMaxon.Kits.Ninja;
import me.MnMaxon.Kits.Portastomp;
import me.MnMaxon.Kits.Shark;
import me.MnMaxon.Kits.Turtle;
import me.MnMaxon.LonksKits.Death;
import me.MnMaxon.LonksKits.Locations;
import me.MnMaxon.LonksKits.Main;
import me.MnMaxon.LonksKits.Messages;
import me.MnMaxon.LonksKits.MetaLists;
import me.MnMaxon.LonksKits.Spectator;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class onEntityDamage implements Listener {
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if (e.getCause() == null || e.getEntity() == null || e.isCancelled() || Locations.gameWorld == null
				|| !e.getEntity().getWorld().equals(Locations.gameWorld))
			return;
		if (MetaLists.PIGGY.contains(e.getEntity())) {
			e.setCancelled(true);
		}
		if ((e.getCause().equals(DamageCause.ENTITY_EXPLOSION) || e.getCause().equals(DamageCause.BLOCK_EXPLOSION)
				&& MetaLists.IGNORE_DAMAGE_EXPLOSION.contains(e.getEntity()))) {
			e.setCancelled(true);
			return;
		}
		
		/*if(e.getEntity() instanceof Player && MetaLists.PLAYERS.contains(e.getEntity()))
		{
			if(((Player)e.getEntity()).getInventory().getArmorContents()[0] != null)
			{
				
			}		
		}*/
		
		if (e.getEntity() instanceof Player && Spectator.isSpectator((Player) e.getEntity())) {
			e.setCancelled(true);
			e.getEntity().setFireTicks(0);
			return;
		} else if (MetaLists.PLAYERS.contains(e.getEntity()) && MetaLists.PLAYERS.get(e.getEntity()) instanceof Shark
				&& e.getCause().equals(DamageCause.DROWNING))
			e.setCancelled(true);
		else if (MetaLists.PLAYERS.contains(e.getEntity()) && MetaLists.PLAYERS.get(e.getEntity()) instanceof Turtle
				&& ((Player) e.getEntity()).isSneaking()) {
			if (e.getCause().equals(DamageCause.CONTACT) || e.getCause().equals(DamageCause.ENTITY_ATTACK)
					|| e.getCause().equals(DamageCause.ENTITY_EXPLOSION) || e.getCause().equals(DamageCause.FIRE)
					|| e.getCause().equals(DamageCause.LIGHTNING) || e.getCause().equals(DamageCause.PROJECTILE)
					|| e.getCause().equals(DamageCause.THORNS))
				if (e.getDamage() > 1)
					e.setDamage(1);
		} else if (MetaLists.PLAYERS.contains(e.getEntity()) && MetaLists.PLAYERS.get(e.getEntity()) instanceof Ninja
				&& new Random().nextBoolean())
			e.setCancelled(true);
		else if (MetaLists.IGNORE_DAMAGE_FALL.contains(e.getEntity()) && e.getCause().equals(DamageCause.FALL)) {
			e.setCancelled(true);
		} else if (e.getEntity() instanceof Horse && e.getEntity().getPassenger() != null
				&& MetaLists.PLAYERS.contains(e.getEntity().getPassenger())
				&& MetaLists.PLAYERS.get(e.getEntity().getPassenger()) instanceof Cowboy) {
			if (!e.getCause().equals(DamageCause.CONTACT) && !e.getCause().equals(DamageCause.PROJECTILE))
				e.setCancelled(true);
		}
		if (MetaLists.PLAYERS.contains(e.getEntity())) {
			if (MetaLists.PLAYERS_INVISIBLE.contains(e.getEntity())) {
				final Player fPlayer = ((Player) e.getEntity());
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
					@Override
					public void run() {
						fPlayer.updateInventory();
					}
				}, 5L);
			}
			if (Locations.inSafe(e.getEntity()))
				e.setCancelled(true);
			if (MetaLists.PLAYERS.get(e.getEntity()) instanceof Kangaroo && e.getCause().equals(DamageCause.FALL))
				if (e.getDamage() <= 7)
					e.setCancelled(true);
				else
					e.setDamage(e.getDamage() - 7);
			else if (MetaLists.PLAYERS.get(e.getEntity()) instanceof Jumper && e.getCause().equals(DamageCause.FALL))
				if (e.getDamage() <= 4)
					e.setCancelled(true);
				else
					e.setDamage(e.getDamage() - 4);
			else if (MetaLists.PLAYERS.get(e.getEntity()) instanceof Flamer
					&& (e.getCause().equals(DamageCause.FIRE) || e.getCause().equals(DamageCause.LAVA) || e.getCause()
							.equals(DamageCause.FIRE_TICK))) {
				e.setCancelled(true);
				e.getEntity().setFireTicks(0);
				boolean hasEffect = false;
				for (PotionEffect pot : ((Player) e.getEntity()).getActivePotionEffects())
					if (pot.getType().equals(PotionEffectType.INCREASE_DAMAGE))
						hasEffect = true;
				if (!hasEffect)
					((Player) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,
							10 * 20, 0));
			} else if (!e.isCancelled()
					&& (MetaLists.PLAYERS.get(e.getEntity()) instanceof BigFoot || MetaLists.PLAYERS.get(e.getEntity()) instanceof Portastomp)
					&& e.getCause().equals(DamageCause.FALL)) {
				for (Entity ent : e.getEntity().getNearbyEntities(5, 5, 5))
					if (ent instanceof Player && !Locations.inSafe(ent) && ((Player) ent).getNoDamageTicks() <= 0) {
						Player p = (Player) ent;
						if (!CloneManager.isClone(p))
							Death.update(p, (Player) e.getEntity(), "was stomped on");
						if (!EssentialsMethods.hasGodMode((Player) ent)) {
							if (p.isSneaking()) {
								if (((Player) ent).getHealth() + 1 <= 20)
									((Player) ent).setHealth(((Player) ent).getHealth() + 1);
								if (e.getDamage() / 2 < 8)
									p.damage(e.getDamage() / 2);
								else
									p.damage(8);
							} else {
								p.damage(e.getDamage());
								p.setVelocity(p.getVelocity().add(new Vector(0, 0.5, 0)));
								if (((Player) e.getEntity()).getHealth() - e.getDamage() <= 0)
									p.sendMessage(Messages.HINT_STOMP);
							}
						}
					}
				if (e.getDamage() > 4)
					e.setDamage(4);
				else
					e.setDamage(e.getDamage() - 1);
			}
			if (!Death.getAttacker((Player) e.getEntity()).equals("")
					&& Bukkit.getPlayer(Death.getAttacker((Player) e.getEntity())) != null
					&& MetaLists.DOUBLE_DAMAGE.contains(Bukkit.getPlayer(Death.getAttacker((Player) e.getEntity()))))
				e.setDamage(e.getDamage() * 2.00);
			if (!e.isCancelled() && MetaLists.PLAYERS.contains(e.getEntity())
					&& MetaLists.PLAYERS.get(e.getEntity()) instanceof Anvil) {
				((Player) e.getEntity()).damage(e.getDamage());
				e.setCancelled(true);
			}
			if (!e.isCancelled()
					&& Death.wasHit((Player) e.getEntity())
					&& (e.getCause().equals(DamageCause.BLOCK_EXPLOSION) || e.getCause().equals(
							DamageCause.ENTITY_EXPLOSION))
					&& Death.getMessage((Player) e.getEntity()).contains("Kamikaze - noboom")) {
				Death.getInfo((Player) e.getEntity()).attackReason = "Kamikaze";
				((Player) e.getEntity()).damage(14);
				e.setCancelled(true);
				return;
			} else if (!e.isCancelled()
					&& Death.wasHit((Player) e.getEntity())
					&& (e.getCause().equals(DamageCause.BLOCK_EXPLOSION) || e.getCause().equals(
							DamageCause.ENTITY_EXPLOSION))
					&& Death.getMessage((Player) e.getEntity()).contains("Pigsplosion - noboom")) {
				Death.getInfo((Player) e.getEntity()).attackReason = "Pigsplosion";
				e.setDamage(0);
				Main.damageIgnoreArmor(e.getEntity(), (((Player) e.getEntity()).getMaxHealth() / 2) - 5, true);
				return;
			}
			 if ((e.getCause().equals(DamageCause.LIGHTNING) && MetaLists.IGNORE_DAMAGE_LIGHTNING.contains(e.getEntity()))) {
					e.setCancelled(true);
					return;
				}
		}
	}

}