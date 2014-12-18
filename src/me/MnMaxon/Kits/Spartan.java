package me.MnMaxon.Kits;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class Spartan extends Kit {
	public Spartan() {
		super();
		guiType = Material.STICK;
	}

	@Override
	public ItemStack[] getInvContents() {
		int size = Bukkit.getServer().createInventory(null, InventoryType.PLAYER).getContents().length;
		ItemStack[] cont = new ItemStack[size];
		cont[0] = unbreak(easyItem(null, Material.IRON_SWORD, 0, null, 1));
		cont[1] = easyItem(ChatColor.BLUE + "Spear", Material.STICK, 0, null, 1);
		cont = giveSoup(cont);
		return cont;
	}

	@Override
	public ItemStack[] getArmorContents() {
		ItemStack[] cont = new ItemStack[4];
		ItemStack cap = unbreak(easyItem(null, Material.LEATHER_HELMET, 0, null, 1));
		LeatherArmorMeta im = (LeatherArmorMeta) cap.getItemMeta();
		im.setColor(Color.PURPLE);
		cap.setItemMeta(im);
		cont[3] = cap;
		cont[2] = unbreak(easyItem(null, Material.IRON_CHESTPLATE, 0, null, 1));
		cont[1] = unbreak(easyItem(null, Material.IRON_LEGGINGS, 0, null, 1));
		cont[0] = unbreak(easyItem(null, Material.IRON_BOOTS, 0, null, 1));
		return cont;
	}
}
