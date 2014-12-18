package me.MnMaxon.Apis;

import me.MnMaxon.LonksKits.Main;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.kitteh.vanish.VanishPlugin;
import org.kitteh.vanish.event.VanishStatusChangeEvent;
import org.kitteh.vanish.hooks.Hook;

class VanishMethods {
	static VanishPlugin vanishPlugin;

	public static void setup() {
		vanishPlugin = (VanishPlugin) Bukkit.getPluginManager().getPlugin("VanishNoPacket");
		vanishPlugin.getHookManager().registerHook("LuckyItems", new LonksHook(vanishPlugin));
	}

	public static boolean isVanished(Player p) {
		return vanishPlugin.getManager().isVanished(p);
	}

	public static void setVanished(Player p, boolean vanish) {
		if (vanish != isVanished(p))
			vanishPlugin.getManager().toggleVanish(p);
	}
}

final class LonksHook extends Hook implements Listener {

	public LonksHook(VanishPlugin plugin) {
		super(plugin);
		this.plugin.getLogger().info("Now hooking into LonksKits");
		Main.plugin.getServer().getPluginManager().registerEvents(this, Main.plugin);
	}

	@EventHandler
	public void beforeVanishChange(final VanishStatusChangeEvent e) {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
			public void run() {
				for (Player p : Main.plugin.getServer().getOnlinePlayers())
					if (e.isVanishing())
						p.hidePlayer(e.getPlayer());
					else
						p.showPlayer(e.getPlayer());
			}
		}, 20L);
	}
}