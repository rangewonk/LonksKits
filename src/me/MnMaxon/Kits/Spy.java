package me.MnMaxon.Kits;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class Spy extends Kit {
	public Spy() {
		super();
		guiType = Material.CHAINMAIL_HELMET;
	}

	@Override
	public ItemStack[] getInvContents() {
		int size = Bukkit.getServer().createInventory(null, InventoryType.PLAYER).getContents().length;
		ItemStack[] cont = new ItemStack[size];
		cont[0] = unbreak(easyItem(null, Material.DIAMOND_SWORD, 0, null, 1));
		ItemStack bow = unbreak(easyItem(ChatColor.BLUE + "Sneaky Bow", Material.BOW, 0, null, 1));
		bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
		cont[1] = bow;
		cont[17] = easyItem(null, Material.ARROW, 0, null, 1);
		cont = giveSoup(cont);
		return cont;
	}

	@Override
	public ItemStack[] getArmorContents() {
		ItemStack[] cont = new ItemStack[4];
		ItemStack helm = setColor(unbreak(easyItem(null, Material.LEATHER_HELMET, 0, null, 1)), Color.BLACK);
		helm.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
		ItemStack chest = setColor(unbreak(easyItem(null, Material.LEATHER_CHESTPLATE, 0, null, 1)), Color.BLACK);
		chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
		ItemStack pants = setColor(unbreak(easyItem(null, Material.LEATHER_LEGGINGS, 0, null, 1)), Color.BLACK);
		pants.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
		ItemStack boots = setColor(unbreak(easyItem(null, Material.LEATHER_BOOTS, 0, null, 1)), Color.BLACK);
		boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
		cont[3] = helm;
		cont[2] = chest;
		cont[1] = pants;
		cont[0] = boots;
		return cont;
	}
}
