package me.MnMaxon.Kits;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class DemoMan extends Kit {
	public DemoMan() {
		super();
		guiType = Material.STONE_PLATE;
	}

	@Override
	public ItemStack[] getInvContents() {
		int size = Bukkit.getServer().createInventory(null, InventoryType.PLAYER).getContents().length;
		ItemStack[] cont = new ItemStack[size];
		cont[0] = unbreak(easyItem(null, Material.IRON_SWORD, 0, null, 1));
		cont[1] = easyItem(ChatColor.BLUE + "Trap", Material.STONE_PLATE, 0, null, 5);
		cont = giveSoup(cont);
		return cont;
	}

	@Override
	public ItemStack[] getArmorContents() {
		ItemStack[] cont = new ItemStack[4];
		ItemStack helm = setColor(unbreak(easyItem(null, Material.LEATHER_HELMET, 0, null, 1)), Color.GRAY);
		helm.addEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 2);
		ItemStack chest = setColor(unbreak(easyItem(null, Material.IRON_CHESTPLATE, 0, null, 1)), null);
		chest.addEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 2);
		ItemStack pants = setColor(unbreak(easyItem(null, Material.IRON_LEGGINGS, 0, null, 1)), null);
		pants.addEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 2);
		ItemStack boots = setColor(unbreak(easyItem(null, Material.IRON_BOOTS, 0, null, 1)), null);
		boots.addEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 2);
		cont[3] = helm;
		cont[2] = chest;
		cont[1] = pants;
		cont[0] = boots;
		return cont;
	}
}
