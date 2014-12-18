package me.MnMaxon.Kits;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class Cowboy extends Kit {
	public Cowboy() {
		super();
		guiType = Material.LEASH;
	}

	@Override
	public ItemStack[] getInvContents() {
		int size = Bukkit.getServer().createInventory(null, InventoryType.PLAYER).getContents().length;
		ItemStack[] cont = new ItemStack[size];
		cont[0] = unbreak(easyItem(null, Material.DIAMOND_SWORD, 0, null, 1));
		cont[1] = unbreak(easyItem(ChatColor.BLUE + "Lasso", Material.LEASH, 0, null, 1));
		cont = giveSoup(cont);
		return cont;
	}

	@Override
	public ItemStack[] getArmorContents() {
		ItemStack[] cont = new ItemStack[4];
		ItemStack helm = setColor(unbreak(easyItem(null, Material.GOLD_HELMET, 0, null, 1)), Color.BLUE);
		helm.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemStack chest = setColor(unbreak(easyItem(null, Material.DIAMOND_CHESTPLATE, 0, null, 1)), Color.BLUE);
		ItemStack pants = setColor(unbreak(easyItem(null, Material.CHAINMAIL_LEGGINGS, 0, null, 1)), Color.BLUE);
		ItemStack boots = unbreak(easyItem(null, Material.LEATHER_BOOTS, 0, null, 1));
		boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
		cont[3] = helm;
		cont[2] = chest;
		cont[1] = pants;
		cont[0] = boots;
		return cont;
	}
}
//hiiiiii
