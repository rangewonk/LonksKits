package me.MnMaxon.Kits;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Archer extends Kit {

	public Archer() {
		super();
		guiType = Material.BOW;
	}

	@Override
	public ItemStack[] getInvContents() {
		int size = Bukkit.getServer().createInventory(null, InventoryType.PLAYER).getContents().length;
		ItemStack[] cont = new ItemStack[size];
		ItemStack bow = unbreak(easyItem(null, Material.BOW, 0, null, 1));
		bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
		bow.addEnchantment(Enchantment.ARROW_DAMAGE, 3);
		bow.addEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
		ItemStack sword = unbreak(easyItem(null, Material.STONE_SWORD, 0, null, 1));
		sword.addEnchantment(Enchantment.DAMAGE_ALL, 2);
		cont[0] = sword;
		cont[1] = bow;
		cont[17] = easyItem(null, Material.ARROW, 0, null, 1);
		cont = giveSoup(cont);
		return cont;
	}

	@Override
	public ItemStack[] getArmorContents() {
		ItemStack[] cont = new ItemStack[4];
		ItemStack helm = unbreak(easyItem(null, Material.LEATHER_HELMET, 0, null, 1));
		helm.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemStack chest = unbreak(easyItem(null, Material.LEATHER_CHESTPLATE, 0, null, 1));
		chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemStack pants = unbreak(easyItem(null, Material.LEATHER_LEGGINGS, 0, null, 1));
		pants.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemStack boots = unbreak(easyItem(null, Material.LEATHER_BOOTS, 0, null, 1));
		boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		cont[3] = helm;
		cont[2] = chest;
		cont[1] = pants;
		cont[0] = boots;
		return cont;
	}

	@Override
	public ArrayList<PotionEffect> potionEffects() {
		ArrayList<PotionEffect> potionEffects = new ArrayList<PotionEffect>();
		potionEffects.add(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
		return potionEffects;
	}
}
