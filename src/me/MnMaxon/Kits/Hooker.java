package me.MnMaxon.Kits;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class Hooker extends Kit {
	public Hooker() {
		super();
		guiType = Material.MOB_SPAWNER;
	}

	@Override
	public ItemStack[] getInvContents() {
		int size = Bukkit.getServer().createInventory(null, InventoryType.PLAYER).getContents().length;
		ItemStack[] cont = new ItemStack[size];
		ItemStack sword = unbreak(easyItem(null, Material.DIAMOND_SWORD, 0, null, 1));
		sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
		cont[0] = sword;
		java.util.List<String> lore = new ArrayList<String>();
		lore.add(0, "Right click to Hook people!");
		cont[1] = unbreak(easyItem(ChatColor.BLUE + "Hook", Material.FISHING_ROD, 0, lore, 1));
		cont = giveSoup(cont);
		return cont;
	}

	@Override
	public ItemStack[] getArmorContents() {
		ItemStack[] cont = new ItemStack[4];
		ItemStack chest = setColor(unbreak(easyItem(null, Material.LEATHER_CHESTPLATE, 0, null, 1)), Color.BLUE);
		ItemStack pants = setColor(unbreak(easyItem(null, Material.CHAINMAIL_LEGGINGS, 0, null, 1)), Color.BLUE);
		ItemStack boots = setColor(unbreak(easyItem(null, Material.DIAMOND_BOOTS, 0, null, 1)), Color.BLUE);
		cont[2] = chest;
		cont[1] = pants;
		cont[0] = boots;
		return cont;
	}
}
