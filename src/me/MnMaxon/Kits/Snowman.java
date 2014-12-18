package me.MnMaxon.Kits;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class Snowman extends Kit {

	public Snowman() {
		super();
		guiType = Material.PUMPKIN;
		entType = EntityType.SNOWMAN;
	}

	@Override
	public ItemStack[] getInvContents() {
		int size = Bukkit.getServer().createInventory(null, InventoryType.PLAYER).getContents().length;
		ItemStack[] cont = new ItemStack[size];
		cont[0] = unbreak(easyItem(null, Material.IRON_SWORD, 0, null, 1));
		cont[1] = easyItem(null, Material.SNOW_BALL, 0, null, 16);
		cont = giveSoup(cont);
		return cont;
	}

	@Override
	public ItemStack[] getArmorContents() {
		ItemStack[] cont = new ItemStack[4];
		ItemStack helm = setColor(unbreak(easyItem(null, Material.LEATHER_HELMET, 0, null, 1)), Color.WHITE);
		helm.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
		ItemStack chest = setColor(unbreak(easyItem(null, Material.LEATHER_CHESTPLATE, 0, null, 1)), Color.WHITE);
		chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
		ItemStack pants = setColor(unbreak(easyItem(null, Material.LEATHER_LEGGINGS, 0, null, 1)), Color.WHITE);
		pants.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
		ItemStack boots = setColor(unbreak(easyItem(null, Material.LEATHER_BOOTS, 0, null, 1)), Color.WHITE);
		boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
		cont[3] = helm;
		cont[2] = chest;
		cont[1] = pants;
		cont[0] = boots;
		return cont;
	}
}
