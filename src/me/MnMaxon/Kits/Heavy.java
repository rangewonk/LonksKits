package me.MnMaxon.Kits;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Heavy extends Kit {
	public Heavy() {
		super();
		guiType = Material.DIAMOND_CHESTPLATE;
	}

	@Override
	public ItemStack[] getInvContents() {
		int size = Bukkit.getServer().createInventory(null, InventoryType.PLAYER).getContents().length;
		ItemStack[] cont = new ItemStack[size];
		ItemStack sword = unbreak(easyItem(null, Material.WOOD_SWORD, 0, null, 1));
		sword.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
		sword.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 2);
		cont[0] = sword;
		cont = giveSoup(cont);
		return cont;
	}

	@Override
	public ItemStack[] getArmorContents() {
		ItemStack[] cont = new ItemStack[4];
		ItemStack helm = setColor(unbreak(easyItem(null, Material.DIAMOND_HELMET, 0, null, 1)), Color.BLUE);
		ItemStack chest = setColor(unbreak(easyItem(null, Material.DIAMOND_CHESTPLATE, 0, null, 1)), Color.BLUE);
		chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemStack pants = setColor(unbreak(easyItem(null, Material.DIAMOND_LEGGINGS, 0, null, 1)), Color.BLUE);
		pants.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemStack boots = setColor(unbreak(easyItem(null, Material.DIAMOND_BOOTS, 0, null, 1)), Color.BLUE);
		cont[3] = helm;
		cont[2] = chest;
		cont[1] = pants;
		cont[0] = boots;
		return cont;
	}

	@Override
	public ArrayList<PotionEffect> potionEffects() {
		ArrayList<PotionEffect> potionEffects = new ArrayList<PotionEffect>();
		potionEffects.add(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 0));
		return potionEffects;
	}
}
