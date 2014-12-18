package me.MnMaxon.Kits;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Stimpy extends Kit {
	public Stimpy() {
		super();
		guiType = Material.WOOD_SWORD;
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
		ItemStack pants = unbreak(easyItem(null, Material.LEATHER_LEGGINGS, 0, null, 1));
		LeatherArmorMeta pantsMeta = (LeatherArmorMeta) pants.getItemMeta();
		pantsMeta.setColor(Color.ORANGE);
		pants.setItemMeta(pantsMeta);
		pants.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemStack chest = unbreak(easyItem(null, Material.LEATHER_CHESTPLATE, 0, null, 1));
		LeatherArmorMeta chestMeta = (LeatherArmorMeta) chest.getItemMeta();
		chestMeta.setColor(Color.ORANGE);
		chest.setItemMeta(chestMeta);
		chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		cont[3] = unbreak(easyItem(null, Material.IRON_HELMET, 0, null, 1));
		cont[2] = chest;
		cont[1] = pants;
		cont[0] = unbreak(easyItem(null, Material.IRON_BOOTS, 0, null, 1));
		return cont;
	}

	@Override
	public ArrayList<PotionEffect> potionEffects() {
		ArrayList<PotionEffect> potionEffects = new ArrayList<PotionEffect>();
		potionEffects.add(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 0));
		return potionEffects;
	}
}
