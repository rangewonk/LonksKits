package me.MnMaxon.Kits;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class Troll extends Kit {
	public Troll() {
		super();
		guiType = Material.GOLD_SWORD;
	}

	@Override
	public ItemStack[] getInvContents() {
		int size = Bukkit.getServer().createInventory(null, InventoryType.PLAYER).getContents().length;
		ItemStack[] cont = new ItemStack[size];
		ItemStack sword = unbreak(easyItem(null, Material.GOLD_SWORD, 0, null, 1));
		sword.addEnchantment(Enchantment.DAMAGE_ALL, 4);
		cont[0] = sword;
		cont[1] = easyItem(ChatColor.BLUE + "Trolololol", Material.WOOD_HOE, 0, null, 1);
		cont = giveSoup(cont);
		return cont;
	}

	@Override
	public ItemStack[] getArmorContents() {
		ItemStack[] cont = new ItemStack[4];
		ItemStack helm = (unbreak(easyItem(null, Material.LEATHER_HELMET, 0, null, 1)));
		helm.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
		ItemStack chest = (unbreak(easyItem(null, Material.CHAINMAIL_CHESTPLATE, 0, null, 1)));
		chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
		ItemStack pants = (unbreak(easyItem(null, Material.LEATHER_LEGGINGS, 0, null, 1)));
		pants.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
		ItemStack boots = (unbreak(easyItem(null, Material.LEATHER_BOOTS, 0, null, 1)));
		boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
		cont[3] = helm;
		cont[2] = chest;
		cont[1] = pants;
		cont[0] = boots;
		return cont;
	}
}
