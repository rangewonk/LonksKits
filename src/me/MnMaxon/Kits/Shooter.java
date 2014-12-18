package me.MnMaxon.Kits;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class Shooter extends Kit {
	public Shooter() {
		super();
		guiType = Material.ARROW;
	}

	@Override
	public ItemStack[] getInvContents() {
		int size = Bukkit.getServer().createInventory(null, InventoryType.PLAYER).getContents().length;
		ItemStack[] cont = new ItemStack[size];
		cont[0] = unbreak(easyItem(null, Material.IRON_SWORD, 0, null, 1));
		cont[1] = easyItem(ChatColor.BLUE + "Machine Gun", Material.ARROW, 0, null, 64);
		cont[2] = easyItem(ChatColor.BLUE + "Machine Gun", Material.ARROW, 0, null, 64);
		cont = giveSoup(cont);
		return cont;
	}

	@Override
	public ItemStack[] getArmorContents() {
		ItemStack[] cont = new ItemStack[4];
		ItemStack helm = setColor(unbreak(easyItem(null, Material.IRON_HELMET, 0, null, 1)), Color.BLUE);
		ItemStack chest = setColor(unbreak(easyItem(null, Material.IRON_CHESTPLATE, 0, null, 1)), Color.BLUE);
		ItemStack pants = setColor(unbreak(easyItem(null, Material.IRON_LEGGINGS, 0, null, 1)), Color.BLUE);
		ItemStack boots = setColor(unbreak(easyItem(null, Material.IRON_BOOTS, 0, null, 1)), Color.BLUE);
		cont[3] = helm;
		cont[2] = chest;
		cont[1] = pants;
		cont[0] = boots;
		return cont;
	}
}
