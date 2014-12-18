package me.MnMaxon.Kits;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class Ninja extends Kit {
	public Ninja() {
		super();
		guiType = Material.LEATHER_HELMET;
		color = Color.BLACK;
	}

	@Override
	public ItemStack[] getInvContents() {
		int size = Bukkit.getServer().createInventory(null, InventoryType.PLAYER).getContents().length;
		ItemStack[] cont = new ItemStack[size];
		List<String> lore = new ArrayList<String>();
		lore.add("Right click to throw a Shuriken!");
		ItemStack shuriken = unbreak(easyItem(ChatColor.BLUE + "Shuriken", Material.NETHER_STAR, 0, lore, 1));
		shuriken.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
		cont[0] = shuriken;
		List<String> cloneLore = new ArrayList<String>();
		lore.add("Left click: Creates a clone for 5 seconds!");
		cloneLore.add("Right click: Teleports you to your clone!");
		cont[1] = easyItem(ChatColor.BLUE + "Clone", Material.SKULL_ITEM, 3, cloneLore, 1);
		cont = giveSoup(cont);
		return cont;
	}

	@Override
	public ItemStack[] getArmorContents() {
		ItemStack[] cont = new ItemStack[4];
		ItemStack helm = setColor(unbreak(easyItem(null, Material.LEATHER_HELMET, 0, null, 1)), Color.BLACK);
		helm.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemStack chest = setColor(unbreak(easyItem(null, Material.LEATHER_CHESTPLATE, 0, null, 1)), Color.BLACK);
		chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemStack pants = setColor(unbreak(easyItem(null, Material.LEATHER_LEGGINGS, 0, null, 1)), Color.BLACK);
		pants.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemStack boots = setColor(unbreak(easyItem(null, Material.LEATHER_BOOTS, 0, null, 1)), Color.BLACK);
		boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		cont[3] = helm;
		cont[2] = chest;
		cont[1] = pants;
		cont[0] = boots;
		return cont;
	}
}