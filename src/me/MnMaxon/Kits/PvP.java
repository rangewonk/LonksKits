package me.MnMaxon.Kits;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class PvP extends Kit {
	public PvP() {
		super();
		guiType = Material.DIAMOND_SWORD;
	}

	@Override
	public ItemStack[] getInvContents() {
		int size = Bukkit.getServer().createInventory(null, InventoryType.PLAYER).getContents().length;
		ItemStack[] cont = new ItemStack[size];
		ItemStack sword = unbreak(easyItem(null, Material.DIAMOND_SWORD, 0, null, 1));
		sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
		cont[0] = sword;
		cont = giveSoup(cont);
		return cont;
	}

	@Override
	public ItemStack[] getArmorContents() {
		ItemStack[] cont = new ItemStack[4];
		cont[3] = unbreak(easyItem(null, Material.IRON_HELMET, 0, null, 1));
		cont[2] = unbreak(easyItem(null, Material.IRON_CHESTPLATE, 0, null, 1));
		cont[1] = unbreak(easyItem(null, Material.IRON_LEGGINGS, 0, null, 1));
		cont[0] = unbreak(easyItem(null, Material.IRON_BOOTS, 0, null, 1));
		return cont;
	}
}
