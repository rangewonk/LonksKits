package me.MnMaxon.Kits;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class Snake extends Kit {
	public Snake() {
		super();
		guiType = Material.BAKED_POTATO;
	}

	@Override
	public ItemStack[] getInvContents() {
		int size = Bukkit.getServer().createInventory(null, InventoryType.PLAYER).getContents().length;
		ItemStack[] cont = new ItemStack[size];
		cont[0] = unbreak(easyItem(null, Material.IRON_SWORD, 0, null, 1));
		cont = giveSoup(cont);
		return cont;
	}

	@Override
	public ItemStack[] getArmorContents() {
		ItemStack[] cont = new ItemStack[4];
		ItemStack helm = setColor(unbreak(easyItem(null, Material.LEATHER_HELMET, 0, null, 1)), Color.GREEN);
		helm.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
		ItemStack chest = setColor(unbreak(easyItem(null, Material.LEATHER_CHESTPLATE, 0, null, 1)), Color.GREEN);
		chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
		ItemStack pants = setColor(unbreak(easyItem(null, Material.LEATHER_LEGGINGS, 0, null, 1)), Color.GREEN);
		pants.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
		ItemStack boots = setColor(unbreak(easyItem(null, Material.LEATHER_BOOTS, 0, null, 1)), Color.GREEN);
		boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
		cont[3] = helm;
		cont[2] = chest;
		cont[1] = pants;
		cont[0] = boots;
		return cont;
	}
}
