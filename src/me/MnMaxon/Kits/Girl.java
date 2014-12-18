package me.MnMaxon.Kits;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class Girl extends Kit {
	public Girl() {
		super();
		guiType = Material.RED_ROSE;
	}

	@Override
	public ItemStack[] getInvContents() {
		int size = Bukkit.getServer().createInventory(null, InventoryType.PLAYER).getContents().length;
		ItemStack[] cont = new ItemStack[size];
		ItemStack rose = easyItem(ChatColor.RED + "Rose", Material.RED_ROSE, 0, null, 1);
		rose.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 1);
		rose.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
		ItemStack orchid = easyItem(ChatColor.BLUE + "Orchid", Material.RED_ROSE, 1, null, 1);
		orchid.addUnsafeEnchantment(Enchantment.KNOCKBACK, 2);
		orchid.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
		cont[0] = rose;
		cont[1] = orchid;
		cont = giveSoup(cont);
		return cont;
	}

	@Override
	public ItemStack[] getArmorContents() {
		ItemStack[] cont = new ItemStack[4];
		ItemStack helm = setColor(unbreak(easyItem(null, Material.LEATHER_HELMET, 0, null, 1)),
				Color.fromRGB(250, 120, 165));
		ItemStack chest = setColor(unbreak(easyItem(null, Material.LEATHER_CHESTPLATE, 0, null, 1)),
				Color.fromRGB(250, 120, 165));
		ItemStack pants = setColor(unbreak(easyItem(null, Material.LEATHER_LEGGINGS, 0, null, 1)),
				Color.fromRGB(250, 120, 165));
		ItemStack boots = setColor(unbreak(easyItem(null, Material.DIAMOND_BOOTS, 0, null, 1)),
				Color.fromRGB(250, 120, 165));
		cont[3] = helm;
		cont[2] = chest;
		cont[1] = pants;
		cont[0] = boots;
		return cont;
	}
}
