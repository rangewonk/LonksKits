package me.MnMaxon.Kits;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class BigFoot extends Kit {
	public BigFoot() {
		super();
		guiType = Material.GOLD_BOOTS;
	}

	@Override
	public ItemStack[] getInvContents() {
		int size = Bukkit.getServer().createInventory(null, InventoryType.PLAYER).getContents().length;
		ItemStack[] cont = new ItemStack[size];
		ItemStack sword = unbreak(easyItem(null, Material.IRON_SWORD, 0, null, 1));
		cont[0] = sword;
		cont = giveSoup(cont);
		return cont;
	}

	@Override
	public ItemStack[] getArmorContents() {
		ItemStack[] cont = new ItemStack[4];
		ItemStack helm = setColor(unbreak(easyItem(null, Material.LEATHER_HELMET, 0, null, 1)), Color.BLACK);
		ItemStack chest = unbreak(easyItem(null, Material.CHAINMAIL_CHESTPLATE, 0, null, 1));
		ItemStack pants = unbreak(easyItem(null, Material.CHAINMAIL_LEGGINGS, 0, null, 1));
		ItemStack boots = unbreak(easyItem(null, Material.GOLD_BOOTS, 0, null, 1));
		cont[3] = helm;
		cont[2] = chest;
		cont[1] = pants;
		cont[0] = boots;
		return cont;
	}
}
