package me.MnMaxon.Kits;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class Spider extends Kit {
	public Spider() {
		super();
		guiType = Material.WEB;
		entType = EntityType.SPIDER;
	}

	@Override
	public ItemStack[] getInvContents() {
		int size = Bukkit.getServer().createInventory(null, InventoryType.PLAYER).getContents().length;
		ItemStack[] cont = new ItemStack[size];
		ItemStack sword = unbreak(easyItem(null, Material.IRON_SWORD, 0, null, 1));
		sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
		cont[0] = sword;
		cont = giveSoup(cont);
		return cont;
	}

	@Override
	public ItemStack[] getArmorContents() {
		ItemStack[] cont = new ItemStack[4];
		ItemStack helm = (unbreak(easyItem(null, Material.IRON_HELMET, 0, null, 1)));
		ItemStack chest = setColor(unbreak(easyItem(null, Material.LEATHER_CHESTPLATE, 0, null, 1)), Color.BLACK);
		chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemStack pants = setColor(unbreak(easyItem(null, Material.LEATHER_LEGGINGS, 0, null, 1)), Color.BLACK);
		pants.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemStack boots = setColor(unbreak(easyItem(null, Material.LEATHER_BOOTS, 0, null, 1)), Color.BLACK);
		boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		cont[3] = helm;
		cont[2] = chest;
		cont[1] = pants;
		cont[0] = boots;
		return cont;
	}
}