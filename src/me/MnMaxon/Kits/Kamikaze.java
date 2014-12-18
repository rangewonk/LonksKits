package me.MnMaxon.Kits;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class Kamikaze extends Kit {
	public Kamikaze() {
		super();
		guiType = Material.SULPHUR;
	}

	@Override
	public ItemStack[] getInvContents() {
		int size = Bukkit.getServer().createInventory(null, InventoryType.PLAYER).getContents().length;
		ItemStack[] cont = new ItemStack[size];
		ItemStack sword = unbreak(easyItem(null, Material.STONE_SWORD, 0, null, 1));
		sword.addEnchantment(Enchantment.DAMAGE_ALL, 2);
		cont[0] = sword;
		cont[1] = unbreak(easyItem("Kamikaze", Material.SULPHUR, 0, null, 1));
		cont = giveSoup(cont);
		return cont;
	}

	@Override
	public ItemStack[] getArmorContents() {
		ItemStack[] cont = new ItemStack[4];
		ItemStack helm = setColor(unbreak(easyItem(null, Material.CHAINMAIL_HELMET, 0, null, 1)), Color.RED);
		helm.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemStack chest = setColor(unbreak(easyItem(null, Material.LEATHER_CHESTPLATE, 0, null, 1)), Color.RED);
		chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemStack pants = setColor(unbreak(easyItem(null, Material.LEATHER_LEGGINGS, 0, null, 1)), Color.RED);
		pants.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemStack boots = setColor(unbreak(easyItem(null, Material.CHAINMAIL_BOOTS, 0, null, 1)), Color.RED);
		boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		cont[3] = helm;
		cont[2] = chest;
		cont[1] = pants;
		cont[0] = boots;
		return cont;
	}
}
