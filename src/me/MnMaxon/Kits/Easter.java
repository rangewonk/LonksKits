package me.MnMaxon.Kits;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Easter extends Kit {
	public Easter() {
		super();
		guiType = Material.EGG;
	}

	@Override
	public ItemStack[] getInvContents() {
		int size = Bukkit.getServer().createInventory(null, InventoryType.PLAYER).getContents().length;
		ItemStack[] cont = new ItemStack[size];
		ItemStack carrot = easyItem(ChatColor.GOLD + "Carrot", Material.CARROT, 0, null, 1);
		carrot.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 3);
		carrot.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 1);
		ItemStack droppings = easyItem(ChatColor.GREEN + "Rabbit Droppings", Material.INK_SACK, 3, null, 1);
		ItemStack egg1 = easyItem(ChatColor.YELLOW + "Easter Egg", Material.EGG, 0, null, 20);
		droppings.addUnsafeEnchantment(Enchantment.KNOCKBACK, 2);
//		egg.addEnchantment(Enchantment.KNOCKBACK, 2);
		cont[0] = carrot;
		cont[1] = droppings;
		cont[2] = egg1;
		cont = giveSoup(cont);
		return cont;
	}

	@Override
	public ItemStack[] getArmorContents() {
		ItemStack[] cont = new ItemStack[4];
		ItemStack helm = setColor(unbreak(easyItem(ChatColor.LIGHT_PURPLE + "Bunny Ears", Material.LEATHER_HELMET, 0, null, 1)), Color.FUCHSIA);
		helm.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
		ItemStack chest = setColor(unbreak(easyItem(ChatColor.LIGHT_PURPLE + "Bunny Body", Material.LEATHER_CHESTPLATE, 0, null, 1)), Color.FUCHSIA);
		chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
		ItemStack pants = setColor(unbreak(easyItem(ChatColor.LIGHT_PURPLE + "Bunny Legs", Material.LEATHER_LEGGINGS, 0, null, 1)), Color.FUCHSIA);
		pants.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
		ItemStack boots = setColor(unbreak(easyItem(ChatColor.LIGHT_PURPLE + "Bunny Feet", Material.LEATHER_BOOTS, 0, null, 1)), Color.FUCHSIA);
		boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
		cont[3] = helm;
		cont[2] = chest;
		cont[1] = pants;
		cont[0] = boots;
		return cont;
	}
	@Override
	public ArrayList<PotionEffect> potionEffects() {
		ArrayList<PotionEffect> potionEffects = new ArrayList<PotionEffect>();
		potionEffects.add(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 4));
		return potionEffects;
	}
}
