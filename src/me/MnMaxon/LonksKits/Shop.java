package me.MnMaxon.LonksKits;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import me.MnMaxon.Kits.Kit;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Shop {
	public static int vampPrice = 9999;
	public static int doubleSoupPrice = 9999;
	public static int nightPrice = 9999;
	public static int effectiveSoupPrice = 9999;
	public static int revengeZombiePrice = 9999;
	public static int vampTime = 9999;
	public static int doubleSoupTime = 9999;
	public static int nightTime = 9999;
	public static int effectiveSoupTime = 9999;
	public static int revengeZombieTime = 9999;

	public static Map<String, Integer> vampTimeLeft = new HashMap<String, Integer>();
	public static Map<String, Integer> doubleSoupTimeLeft = new HashMap<String, Integer>();
	public static int nightTimeLeft = 0;
	public static Map<String, Integer> effectiveSoupTimeLeft = new HashMap<String, Integer>();
	public static Map<String, Integer> revengeZombieTimeLeft = new HashMap<String, Integer>();

	public static void openGui(Player p) {
		Inventory inv = Bukkit.createInventory(p, 9, Messages.GUI_SHOP);
		List<String> vampLore = new ArrayList<String>();
		vampLore.add("Gives you full health");
		vampLore.add("for killing players");
		vampLore.add("");
		if (vampTime == -1)
			vampLore.add("Lasts forever");
		else
			vampLore.add("Lasts for " + vampTime + " Minutes");
		vampLore.add("Price: " + vampPrice + " Points");
		if (vampTimeLeft.containsKey(p.getName()))
			if (vampTimeLeft.get(p.getName()) == -1)
				vampLore.add(Messages.GUI_LORE_OWN);
			else
				vampLore.add("You have this for " + vampTimeLeft.get(p.getName()) + " more minutes");

		List<String> doubleSoupLore = new ArrayList<String>();
		doubleSoupLore.add("Every mushroom soup you pick");
		doubleSoupLore.add("up will give you two soup");
		doubleSoupLore.add("");
		if (doubleSoupTime == -1)
			doubleSoupLore.add("Lasts forever");
		else
			doubleSoupLore.add("Lasts for " + doubleSoupTime + " Minutes");
		doubleSoupLore.add("Price: " + doubleSoupPrice + " Points");
		if (doubleSoupTimeLeft.containsKey(p.getName()))
			if (doubleSoupTimeLeft.get(p.getName()) == -1)
				doubleSoupLore.add(Messages.GUI_LORE_OWN);
			else
				doubleSoupLore.add("You have this for " + doubleSoupTimeLeft.get(p.getName()) + " more minutes");

		List<String> nightLore = new ArrayList<String>();
		nightLore.add("Makes it night in KitPvP");
		nightLore.add("");
		if (nightTime == -1)
			nightLore.add("Lasts forever");
		else
			nightLore.add("Lasts for " + nightTime + " Minutes");
		nightLore.add("Price: " + nightPrice + " Points");
		if (nightTimeLeft == -1)
			nightLore.add(Messages.GUI_LORE_OWN);
		else if (nightTimeLeft != 0)
			nightLore.add("You have this for " + nightTimeLeft + " more minutes");

		List<String> effectiveSoupLore = new ArrayList<String>();
		effectiveSoupLore.add("Mushroom soup will heal you");
		effectiveSoupLore.add("4 hearts instead of 3.5");
		effectiveSoupLore.add("");
		if (effectiveSoupTime == -1)
			effectiveSoupLore.add("Lasts forever");
		else
			effectiveSoupLore.add("Lasts for " + effectiveSoupTime + " Minutes");
		effectiveSoupLore.add("Price: " + effectiveSoupPrice + " Points");
		if (effectiveSoupTimeLeft.containsKey(p.getName()))
			if (effectiveSoupTimeLeft.get(p.getName()) == -1)
				effectiveSoupLore.add(Messages.GUI_LORE_OWN);
			else
				effectiveSoupLore.add("You have this for " + effectiveSoupTimeLeft.get(p.getName()) + " more minutes");

		List<String> revengeZombieLore = new ArrayList<String>();
		revengeZombieLore.add("A zombie will spawn");
		revengeZombieLore.add("where you die");
		revengeZombieLore.add("");
		if (revengeZombieTime == -1)
			revengeZombieLore.add("Lasts forever");
		else
			revengeZombieLore.add("Lasts for " + revengeZombieTime + " Minutes");
		revengeZombieLore.add("Price: " + revengeZombiePrice + " Points");
		if (revengeZombieTimeLeft.containsKey(p.getName()))
			if (revengeZombieTimeLeft.get(p.getName()) == -1)
				revengeZombieLore.add(Messages.GUI_LORE_OWN);
			else
				revengeZombieLore.add("You have this for " + revengeZombieTimeLeft.get(p.getName()) + " more minutes");

		inv.addItem(Kit.easyItem(Messages.GUI_ITEM_VAMPIRE, Material.WOOD_SWORD, 0, vampLore, 1));
		inv.addItem(Kit.easyItem(Messages.GUI_ITEM_DOUBLE_SOUP, Material.MUSHROOM_SOUP, 0, doubleSoupLore, 2));
		inv.addItem(Kit.easyItem(Messages.GUI_ITEM_NIGHT, Material.NETHER_STAR, 0, nightLore, 1));
		inv.addItem(Kit.easyItem(Messages.GUI_ITEM_EFFECTIVE_SOUP, Material.MUSHROOM_SOUP, 0, effectiveSoupLore, 1));
		inv.addItem(Kit.easyItem(Messages.GUI_ITEM_REVENGE_ZOMBIE, Material.SKULL_ITEM, 2, revengeZombieLore, 1));
		p.openInventory(inv);
	}

	public static void reload() {
		boolean save = false;
		if (setConfig("Vampire", 9999999, -1))
			save = true;
		if (setConfig("Double Soup", 9999999, -1))
			save = true;
		if (setConfig("Night", 9999999, -1))
			save = true;
		if (setConfig("Effective Soup", 9999999, -1))
			save = true;
		if (setConfig("Revenge Zombie", 9999999, -1))
			save = true;
		if (save)
			Main.pointShop.save();
		vampPrice = Main.pointShop.getInt("Vampire.Price");
		doubleSoupPrice = Main.pointShop.getInt("Double Soup.Price");
		nightPrice = Main.pointShop.getInt("Night.Price");
		effectiveSoupPrice = Main.pointShop.getInt("Effective Soup.Price");
		revengeZombiePrice = Main.pointShop.getInt("Revenge Zombie.Price");
		vampTime = Main.pointShop.getInt("Vampire.Time");
		doubleSoupTime = Main.pointShop.getInt("Double Soup.Time");
		nightTime = Main.pointShop.getInt("Night.Time");
		effectiveSoupTime = Main.pointShop.getInt("Effective Soup.Time");
		revengeZombieTime = Main.pointShop.getInt("Revenge Zombie.Time");

		vampTimeLeft = new HashMap<String, Integer>();
		doubleSoupTimeLeft = new HashMap<String, Integer>();
		nightTimeLeft = 0;
		effectiveSoupTimeLeft = new HashMap<String, Integer>();
		revengeZombieTimeLeft = new HashMap<String, Integer>();
		// TODO
		if (Main.data.get("vamp") != null)
			for (String name : Main.data.getConfigurationSection("vamp").getKeys(false))
				if (Main.data.getInt("vamp." + name) == -1 && vampTime != -1)
					vampTimeLeft.put(name, vampTime);
				else
					vampTimeLeft.put(name, Main.data.getInt("vamp." + name));

		if (Main.data.get("Shop.doubleSoup") != null)
			for (String name : Main.data.getConfigurationSection("Shop.doubleSoup").getKeys(false))
				if (Main.data.getInt("Shop.doubleSoup." + name) == -1 && doubleSoupTime != -1)
					doubleSoupTimeLeft.put(name, doubleSoupTime);
				else
					doubleSoupTimeLeft.put(name, Main.data.getInt("Shop.doubleSoup." + name));

		nightTimeLeft = Main.data.getInt("Shop.night");
		if (Locations.gameWorld != null)
			if (nightTimeLeft == 0)
				Locations.gameWorld.setTime(6000);
			else if (nightTimeLeft == -1) {
				if (nightTime != -1)
					nightTimeLeft = nightTime;
				Locations.gameWorld.setTime(18000);
			} else
				Locations.gameWorld.setTime(18000);

		if (Main.data.get("Shop.effectiveSoup") != null)
			for (String name : Main.data.getConfigurationSection("Shop.effectiveSoup").getKeys(false))
				if (Main.data.getInt("Shop.effectiveSoup." + name) == -1 && effectiveSoupTime != -1)
					effectiveSoupTimeLeft.put(name, effectiveSoupTime);
				else
					effectiveSoupTimeLeft.put(name, Main.data.getInt("Shop.effectiveSoup." + name));

		if (Main.data.get("Shop.revengeZombie") != null)
			for (String name : Main.data.getConfigurationSection("Shop.revengeZombie").getKeys(false))
				if (Main.data.getInt("Shop.revengeZombie." + name) == -1 && revengeZombieTime != -1)
					revengeZombieTimeLeft.put(name, revengeZombieTime);
				else
					revengeZombieTimeLeft.put(name, Main.data.getInt("Shop.revengeZombie." + name));
	}

	public static boolean setConfig(String name, int price, int time) {
		boolean save = false;
		if (Main.pointShop.get(name + ".Price") == null) {
			save = true;
			Main.pointShop.set(name + ".Price", price);
		}
		if (Main.pointShop.get(name + ".Time") == null) {
			save = true;
			Main.pointShop.set(name + ".Time", time);
		}
		return save;
	}

	public static void timer() {
		// TODO
		if (vampTime != -1) {
			ArrayList<String> remover = new ArrayList<String>();
			for (Entry<String, Integer> entry : vampTimeLeft.entrySet()) {
				entry.setValue(entry.getValue() - 1);
				if (entry.getValue() < 0)
					remover.add(entry.getKey());
			}
			for (String name : remover) {
				vampTimeLeft.remove(name);
				Main.data.set("Shop.vamp." + name, null);
				Main.data.save();
				Player p = Bukkit.getPlayer(name);
				if (p != null)
					p.sendMessage(Messages.PREFIX + "Your vamparism has worn off");
			}
		}
		if (doubleSoupTime != -1) {
			ArrayList<String> remover = new ArrayList<String>();
			for (Entry<String, Integer> entry : doubleSoupTimeLeft.entrySet()) {
				entry.setValue(entry.getValue() - 1);
				if (entry.getValue() < 0)
					remover.add(entry.getKey());
			}
			for (String name : remover) {
				doubleSoupTimeLeft.remove(name);
				Main.data.set("Shop.doubleSoup." + name, null);
				Main.data.save();
				Player p = Bukkit.getPlayer(name);
				if (p != null)
					p.sendMessage(Messages.PREFIX + "Double soup has worn off");
			}
		}
		if (nightTime != -1) {
			nightTimeLeft--;
			if (nightTimeLeft <= 0) {
				nightTimeLeft = 0;
				if (Locations.gameWorld != null) {
					Locations.gameWorld.setTime(6000);
				}
				String pName = Main.data.getString("Shop.NightPlayer");
				if (pName != null && Bukkit.getPlayer(pName) != null)
					Bukkit.getPlayer(pName).sendMessage(Messages.PREFIX + "Night has worn off");
				Main.data.set("Shop.NightPlayer", null);
				Main.data.set("Shop.night", 0);
				Main.data.save();
			}
		}
		if (effectiveSoupTime != -1) {
			ArrayList<String> remover = new ArrayList<String>();
			for (Entry<String, Integer> entry : effectiveSoupTimeLeft.entrySet()) {
				entry.setValue(entry.getValue() - 1);
				if (entry.getValue() < 0)
					remover.add(entry.getKey());
			}
			for (String name : remover) {
				effectiveSoupTimeLeft.remove(name);
				Main.data.set("Shop.effectiveSoup." + name, null);
				Main.data.save();
				Player p = Bukkit.getPlayer(name);
				if (p != null)
					p.sendMessage(Messages.PREFIX + "Effective soup has worn off");
			}
		}
		if (revengeZombieTime != -1) {
			ArrayList<String> remover = new ArrayList<String>();
			for (Entry<String, Integer> entry : revengeZombieTimeLeft.entrySet()) {
				entry.setValue(entry.getValue() - 1);
				if (entry.getValue() < 0)
					remover.add(entry.getKey());
			}
			for (String name : remover) {
				revengeZombieTimeLeft.remove(name);
				Main.data.set("Shop.revengeZombie." + name, null);
				Main.data.save();
				Player p = Bukkit.getPlayer(name);
				if (p != null)
					p.sendMessage(Messages.PREFIX + "Revenge zombie has worn off");
			}
		}
	}

	public static ArrayList<String> getAbilities(OfflinePlayer p) {
		ArrayList<String> abilities = new ArrayList<String>();
		if (vampTimeLeft.containsKey(p.getName()))
			abilities.add(Messages.GUI_ITEM_VAMPIRE);
		if (effectiveSoupTimeLeft.containsKey(p.getName()))
			abilities.add(Messages.GUI_ITEM_EFFECTIVE_SOUP);
		if (doubleSoupTimeLeft.containsKey(p.getName()))
			abilities.add(Messages.GUI_ITEM_DOUBLE_SOUP);
		if (revengeZombieTimeLeft.containsKey(p.getName()))
			abilities.add(Messages.GUI_ITEM_REVENGE_ZOMBIE);
		return abilities;
	}

	public static void remove(OfflinePlayer p) {
		vampTimeLeft.remove(p.getName());
		effectiveSoupTimeLeft.remove(p.getName());
		doubleSoupTimeLeft.remove(p.getName());
		revengeZombieTimeLeft.remove(p.getName());
	}
}
