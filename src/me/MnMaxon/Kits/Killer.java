package me.MnMaxon.Kits;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class Killer extends Kit {
	public Killer() {
		super();
		guiType = Material.TNT;
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
		ItemStack pants = unbreak(easyItem(null, Material.IRON_LEGGINGS, 0, null, 1));
		pants.addEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 2);
		ItemStack chest = unbreak(easyItem(null, Material.LEATHER_CHESTPLATE, 0, null, 1));
		chest.addEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 2);
		ItemStack boots = unbreak(easyItem(null, Material.IRON_BOOTS, 0, null, 1));
		boots.addEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 2);
		cont[3] = easyItem(null, Material.TNT, 0, null, 1);
		cont[2] = chest;
		cont[1] = pants;
		cont[0] = boots;
		return cont;
	}
}
