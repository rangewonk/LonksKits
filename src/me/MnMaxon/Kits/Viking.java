package me.MnMaxon.Kits;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class Viking extends Kit {
	public Viking() {
		super();
		guiType = Material.DIAMOND_AXE;
	}

	@Override
	public ItemStack[] getInvContents() {
		int size = Bukkit.getServer().createInventory(null, InventoryType.PLAYER).getContents().length;
		ItemStack[] cont = new ItemStack[size];
		ItemStack axe = unbreak(easyItem(null, Material.IRON_AXE, 0, null, 1));
		axe.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 2);
		axe.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 3);
		cont[0] = axe;
		cont = giveSoup(cont);
		return cont;
	}

	@Override
	public ItemStack[] getArmorContents() {
		ItemStack[] cont = new ItemStack[4];
		ItemStack helm = unbreak(easyItem(null, Material.LEATHER_HELMET, 0, null, 1));
		ItemStack chest = unbreak(easyItem(null, Material.CHAINMAIL_CHESTPLATE, 0, null, 1));
		ItemStack pants = unbreak(easyItem(null, Material.CHAINMAIL_LEGGINGS, 0, null, 1));
		ItemStack boots = unbreak(easyItem(null, Material.CHAINMAIL_BOOTS, 0, null, 1));
		cont[3] = helm;
		cont[2] = chest;
		cont[1] = pants;
		cont[0] = boots;
		return cont;
	}
}
