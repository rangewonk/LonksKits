package me.MnMaxon.Listeners;

import me.MnMaxon.Apis.PLib;
import me.MnMaxon.Kits.Assassin;
import me.MnMaxon.Kits.Casper;
import me.MnMaxon.Kits.HotHead;
import me.MnMaxon.Kits.Hulk;
import me.MnMaxon.LonksKits.Death;
import me.MnMaxon.LonksKits.Locations;
import me.MnMaxon.LonksKits.MetaLists;
import me.MnMaxon.LonksKits.Cooldown;
import me.MnMaxon.LonksKits.Main;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityEquipment;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class onCrouch implements Listener {
	@EventHandler
	public void onPlayerCrouch(PlayerToggleSneakEvent e) {
		if (MetaLists.PLAYERS.contains(e.getPlayer())) {
			for (Entity ent : e.getPlayer().getNearbyEntities(0.1, 2, 0.1))
				if (ent instanceof Player && MetaLists.PLAYERS.contains(ent) && ent.getPassenger() != null
						&& ent.getPassenger().equals(e.getPlayer()) && MetaLists.PLAYERS.get(ent) instanceof Hulk)
					ent.eject();
			if (MetaLists.PLAYERS.get(e.getPlayer()) instanceof Assassin) {
				onCrouch.Assasin(e.getPlayer());
			} else if (MetaLists.PLAYERS.get(e.getPlayer()) instanceof HotHead) {
				if (e.isSneaking() && !Locations.inSafe(e.getPlayer().getLocation())) {
					e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2 * 20, 1));
					for (Entity ent : e.getPlayer().getNearbyEntities(4, 4, 4))
						if (Main.canDamage(ent)) {
							if (MetaLists.PLAYERS.contains(ent))
								Death.update((Player) ent, e.getPlayer(), "was burned by");
							ent.setFireTicks(20);
						}
				}
			} else if (MetaLists.PLAYERS.get(e.getPlayer()) instanceof Casper) {
				final Player p = e.getPlayer();
				PLib.toggleVisibility(p, e.isSneaking());
				PacketPlayOutEntityEquipment packet;
				if (e.isSneaking())
					packet = new PacketPlayOutEntityEquipment(p.getEntityId(), 0, null);
				else
					packet = new PacketPlayOutEntityEquipment(p.getEntityId(), 0, CraftItemStack.asNMSCopy(p
							.getItemInHand()));
				for (Player player : p.getWorld().getPlayers())
					((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
			}
		}
	}

	public static void Assasin(Player p) {
		if (Locations.inSafe(p.getLocation())) {
			p.sendMessage(ChatColor.DARK_RED + "You can't do that in the safe zone!");
			return;
		}
		if (!p.isSneaking()) {
			if (p.getActivePotionEffects().size() >= 3) {
				p.sendMessage(ChatColor.DARK_RED + "You can't charge this ability while using it!");
				return;
			}
			for (Cooldown cd : Main.cool)
				if (cd.player.equals(p) && cd.ability.equalsIgnoreCase("assasin"))
					return;
			Main.cool.add(new Cooldown(2, p, "assasin"));
			p.sendMessage(ChatColor.GREEN + "Charging for 0%");
			MetaLists.CHARGE.add(p, 0);
			AssasinLoop(p);
		}
	}

	private static void AssasinLoop(final Player p) {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
			@Override
			public void run() {
				if (p.isSneaking()) {
					if (p.getActivePotionEffects().size() >= 3) {
						p.sendMessage(ChatColor.DARK_RED + "You can't charge this ability while using it!");
						return;
					}
					int timer = ((int) MetaLists.CHARGE.get(p)) + 5;
					MetaLists.CHARGE.remove(p);
					MetaLists.CHARGE.add(p, timer);
					if (timer == 1 * 20)
						p.sendMessage(ChatColor.GREEN + "Charging for 20%");
					else if (timer == 2 * 20)
						p.sendMessage(ChatColor.GREEN + "Charging for 40%");
					else if (timer == 3 * 20)
						p.sendMessage(ChatColor.GREEN + "Charging for 60%");
					else if (timer == 4 * 20)
						p.sendMessage(ChatColor.GREEN + "Charging for 80%");
					else if (timer == 5 * 20) {
						MetaLists.CHARGE.remove(p);
						p.sendMessage(ChatColor.GREEN + "Charging for 100%");
						p.sendMessage(ChatColor.GREEN + "You have been given Speed, Invisibilty and Resistance.");
						p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10 * 20, 1));
						p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 10 * 20, 1));
						invisify(p, 10 * 20);
						return;
					}
					AssasinLoop(p);
				} else
					p.sendMessage(ChatColor.DARK_RED + "You stopped crouching!");
			}
		}, 5L);
	}

	public static void invisify(Player p, int ticks) {
		PLib.toggleVisibility(p, true);
		p.getWorld().playEffect(p.getLocation(), Effect.EXTINGUISH, 31);
		p.getWorld().playEffect(p.getLocation(), Effect.MOBSPAWNER_FLAMES, 30);
		p.getWorld().playEffect(p.getLocation(), Effect.MOBSPAWNER_FLAMES, 29);
		p.getWorld().playEffect(p.getLocation(), Effect.MOBSPAWNER_FLAMES, 32);
		p.getWorld().playEffect(p.getLocation(), Effect.MOBSPAWNER_FLAMES, 33);
		EffectLoop(p, ticks);
	}

	private static void EffectLoop(final Player p, final int ticks) {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
			@Override
			public void run() {
				if (p.isOnline()) {
					if (ticks <= 0 || !p.isValid()) {
						PLib.toggleVisibility(p, false);
						return;
					} else {
						p.getWorld().playEffect(p.getLocation(), Effect.SMOKE, 31);
						EffectLoop(p, ticks - 5);
					}
				}
			}
		}, 5L);
	}
}
