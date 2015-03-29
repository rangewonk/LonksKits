package me.MnMaxon.Kits;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import me.MnMaxon.Apis.DisguiseCraftMethods;
import me.MnMaxon.LonksKits.Locations;
import me.MnMaxon.LonksKits.Main;
import me.MnMaxon.LonksKits.Messages;
import me.MnMaxon.LonksKits.MetaLists;
import me.MnMaxon.LonksKits.Points;
import me.MnMaxon.LonksKits.Spectator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;

public class Kit {
	public static LinkedHashMap<String, Kit> kits = new LinkedHashMap<String, Kit>();

	public Color color = null;
	public EntityType entType = null;
	public Material guiType = null;
	public String buyLore = null;
	public String rentLore1 = null;
	public String rentLore2 = null;

	public Kit() {
		if (getBuyPrice() > 0) {
			buyLore = ChatColor.GREEN + "Right Click to " + ChatColor.BOLD + "BUY " + ChatColor.RESET + ChatColor.GREEN
					+ "for: " + ChatColor.LIGHT_PURPLE + getBuyPrice() + " Points";
		}
		if (getRentPrice() > 0) {
			rentLore1 = ChatColor.GREEN + "Left Click to " + ChatColor.BOLD + "RENT " + ChatColor.RESET
					+ ChatColor.GREEN + getName() + " for " + getRentAmount() + " Lives";
			rentLore2 = ChatColor.GREEN + "for: " + ChatColor.LIGHT_PURPLE + getRentPrice() + " Points";
		}
	}

	public ItemStack[] getArmorContents() {
		return null;
	}

	public ItemStack[] getInvContents() {
		return Bukkit.getServer().createInventory(null, InventoryType.PLAYER).getContents();
	}

	public static void openGui(Player p, int page) {
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		Inventory inv = Bukkit.createInventory(null, 45, Messages.GUI_SELECTOR);
		for (Kit kit : kits.values())
			if (kit.guiType != null)
				items.add(setColor(guiItem(kit.guiType, kit.getName(), p), kit.color));
		for (int x = (page - 1) * 36; x < page * 36 && x < items.size(); x++)
			inv.addItem(items.get(x));

		if (page > 1)
			inv.setItem(36, easyItem(ChatColor.GOLD + "Page " + (page - 1), Material.SNOW, 0, null, 1));
		if (items.size() >= page * 36)
			inv.setItem(44, easyItem(ChatColor.GOLD + "Page " + (page + 1), Material.SNOW, 4, null, 1));
		if (p.getOpenInventory() != null && p.getOpenInventory().getTitle().equals(Messages.GUI_SELECTOR))
			for (int i = 0; i < inv.getContents().length; i++)
				p.getOpenInventory().setItem(i, inv.getContents()[i]);
		else
			p.openInventory(inv);
	}

	public int getRentPrice() {
		if (Main.kitData.get(getName() + ".Price.Rent") == null
				|| !(Main.kitData.get(getName() + ".Price.Rent") instanceof Integer)) {
			Main.kitData.set(getName() + ".Price.Rent", 0);
			Main.kitData.save();
		}
		return (Main.kitData.getInt(getName() + ".Price.Rent") <= 0) ? 0 : Main.kitData.getInt(getName()
				+ ".Price.Rent");
	}

	public int getRentAmount() {
		if (Main.kitData.get(getName() + ".Amount.Rent") == null
				|| !(Main.kitData.get(getName() + ".Amount.Rent") instanceof Integer)
				|| Main.kitData.getInt(getName() + ".Amount.Rent") <= 0) {
			Main.kitData.set(getName() + ".Amount.Rent", 5);
			Main.kitData.save();
		}
		return Main.kitData.getInt(getName() + ".Amount.Rent");
	}

	public int getBuyPrice() {
		if (Main.kitData.get(getName() + ".Price.Buy") == null
				|| !(Main.kitData.get(getName() + ".Price.Buy") instanceof Integer)) {
			Main.kitData.set(getName() + ".Price.Buy", 0);
			Main.kitData.save();
		}
		return (Main.kitData.getInt(getName() + ".Price.Buy") <= 0) ? 0 : Main.kitData.getInt(getName() + ".Price.Buy");
	}

	public boolean buy(Player p) {
		if (getBuyPrice() <= 0) {
			p.sendMessage(Messages.NO_BUY);
			return false;
		}
		if (getBuyPrice() > Points.get(p.getName())) {
			p.sendMessage(Messages.NO_MONEY);
			return false;
		}
		Points.remove(p.getName(), getBuyPrice());
		Uses.giveInfinite(p.getName(), getName());
		Main.playerData.save();
		p.sendMessage(Messages.PREFIX + ChatColor.GREEN + "You have successfully bought the kit: " + ChatColor.BLUE
				+ ChatColor.BOLD + getName() + ChatColor.RESET + ChatColor.GREEN + " for " + getBuyPrice() + " points!");
		return true;
	}

	public boolean rent(Player p) {
		if (getRentPrice() <= 0) {
			p.sendMessage(Messages.NO_BUY);
			return false;
		}
		if (getRentPrice() > Points.get(p.getName())) {
			p.sendMessage(Messages.NO_MONEY);
			return false;
		}
		Points.remove(p.getName(), getRentPrice());
		Uses.add(p.getName(), 5, getName());
		p.sendMessage(Messages.PREFIX + ChatColor.GREEN + "You have successfully rented the kit: " + ChatColor.BLUE
				+ ChatColor.BOLD + getName() + ChatColor.RESET + ChatColor.GREEN + " for " + getRentAmount()
				+ " lives, for " + getRentPrice() + " points!");
		return true;
	}

	private static ItemStack guiItem(Material material, String name, Player p) {
		ChatColor cc;
		Kit kit = Kit.matchKit(name);
		if (p.hasPermission(kit.getPermission()) || Uses.hasInfinite(p.getName(), kit.getName()))
			cc = ChatColor.GREEN;
		else if (Uses.hasUses(p.getName(), kit.getName()))
			cc = ChatColor.BLUE;
		else
			cc = ChatColor.RED;
		ItemStack is = new ItemStack(material);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(cc + name);
		List<String> rawLore = new ArrayList<String>();
		if (Main.kitData.get(name + ".lore") == null) {
			rawLore.add("Line 1");
			rawLore.add("Line 2");
			rawLore.add("Line 3");
			rawLore.add("Line 4");
			Main.kitData.set(name + ".lore", rawLore);
			Main.kitData.save();
		}
		rawLore = Main.kitData.getStringList(name + ".lore");
		List<String> lore = new ArrayList<String>();
		for (String desc : rawLore) {
			desc = ChatColor.AQUA + desc;
			desc = Main.addColor(desc);
			lore.add(desc);
		}
		if (!p.hasPermission("Lonkskit." + name) && !Uses.hasInfinite(p.getName(), name)
				&& (kit.buyLore != null || kit.rentLore1 != null) || Uses.getAmount(p.getName(), name) > 0) {
			lore.add("");
			if (!Uses.hasUses(p.getName(), name)) {
				if (kit.buyLore != null)
					lore.add(kit.buyLore);
				if (kit.rentLore1 != null) {
					lore.add(kit.rentLore1);
					lore.add(kit.rentLore2);
				}
			} else
				lore.add(ChatColor.ITALIC + "" + ChatColor.GRAY + "USES: " + Uses.getAmount(p.getName(), name));
		}
		im.setLore(lore);
		is.setItemMeta(im);
		return is;
	}

	public static void setClass(Player p, String name) {
		if (Locations.gameWorld == null || !Locations.gameWorld.equals(p.getWorld())) {
			p.sendMessage(ChatColor.GOLD + "[" + ChatColor.AQUA + "LonksKits" + ChatColor.GOLD + "]" + ChatColor.RED
					+ " You are not in the right world to do this!");
			return;
		}
		if (MetaLists.PLAYERS.contains(p) && MetaLists.PLAYERS.get(p) != null) {
			p.sendMessage(ChatColor.GOLD + "[" + ChatColor.AQUA + "LonksKits" + ChatColor.GOLD + "]" + ChatColor.RED
					+ " You already have a kit!");
			p.closeInventory();
			return;
		}
		Kit kit = matchKit(name);
		if (!p.hasPermission(kit.getPermission())) {
			if (!Uses.hasUses(p.getName(), kit.getName())) {
				p.sendMessage(Messages.PERMISSION_KIT);
				return;
			}
			if (!Uses.hasInfinite(p.getName(), kit.getName()))
				Uses.remove(p.getName(), 1, kit.getName());
		}
		MetaLists.PLAYERS.remove(p);
		MetaLists.PLAYERS.add(p, kit);
		if (Spectator.isSpectator(p)) {
			Spectator.remove(p);
			MetaLists.TP_AROUND.add(p);
			p.teleport(Locations.spawn);
		}
		p.getInventory().setContents(kit.getInvContents());
		p.getInventory().setArmorContents(kit.getArmorContents());
		if (kit.entType != null)
			DisguiseCraftMethods.disguise(p, kit.entType);
		if (kit instanceof Kangaroo)
			MetaLists.CAN_JUMP.add(p);
		for (PotionEffect pot : kit.potionEffects())
			p.addPotionEffect(pot);
		p.closeInventory();
		if (kit instanceof Cowboy && !Locations.nearSafe(p.getLocation())) {
			Horse horse = p.getWorld().spawn(p.getLocation(), Horse.class);
			horse.setAdult();
			horse.setVariant(Variant.HORSE);
			horse.setColor(Horse.Color.BLACK);
			horse.setCanPickupItems(false);
			horse.setTamed(true);
			horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
			horse.getInventory().setArmor(new ItemStack(Material.DIAMOND_BARDING));
			MetaLists.TP_AROUND.add(p);
			horse.setPassenger(p);
		}
		p.sendMessage(ChatColor.GOLD + "[" + ChatColor.AQUA + "LonksKits" + ChatColor.GOLD + "]" + ChatColor.GREEN
				+ " You have chosen the " + name + " Kit!");
	}

	public static ItemStack easyItem(String name, Material material, int durability, List<String> lore, int amount) {
		ItemStack is = new ItemStack(material);
		if (durability > 0)
			is.setDurability((short) durability);
		if (amount > 1)
			is.setAmount(amount);
		if (is.getItemMeta() != null) {
			ItemMeta im = is.getItemMeta();
			if (name != null)
				im.setDisplayName(name);
			if (lore != null)
				im.setLore(lore);
			is.setItemMeta(im);
		}
		return is;
	}

	public static ItemStack unbreak(ItemStack is) {
		is.addUnsafeEnchantment(Enchantment.DURABILITY, 20);
		return is;
	}

	public static ItemStack setColor(ItemStack is, Color color) {
		if (!(is.getItemMeta() instanceof LeatherArmorMeta))
			return is;
		LeatherArmorMeta im = (LeatherArmorMeta) is.getItemMeta();
		im.setColor(color);
		is.setItemMeta(im);
		return is;
	}

	public String getName() {
		return this.getClass().getSimpleName();
	}

	public static Kit matchKit(String name) {
		name = ChatColor.stripColor(name.toLowerCase());
		if (!kits.containsKey(name))
			return null;
		return kits.get(name);
	}

	public static void setUpList() {
		Kit[] kitList = { new Easter(), new PvP(), new Archer(), new Fisherman(), new Girl(), new Porcupine(), new Anvil(),
				new Troll(), new Monkey(), new Casper(), new Switcher(), new Jumper(), new Chomp(), new Stimpy(),
				new Jedi(), new Elder(), new Turtle(), new Spider(), new Spartan(), new Shooter(), new Berserker(),
				new Spy(), new Snake(), new Wizard(), new Hyper(), new Flamer(), new Mooshroom(), new Monk(),
				new Ghost(), new Warrior(), new Shadowblade(), new Necromancer(), new Sonic(), new Wolf(), new Ninja(),
				new Viking(), new Kangaroo(), new BigFoot(), new Hulk(), new Heavy(), new HotHead(), new Grandma(),
				new Superman(), new Tiger(), new Shark(), new Ripper(), new Pig(), new Hawkeye(), new Creeper(),
				new Cowboy(), new Blink(), new Hooker(), new Snowman(), new Clone(), new Maverick(), new Assassin(),
				new Dragon(), new Morter(), new Kamikaze(), new Tele(), new Killer(), new DemoMan(), new Flinger(),
				new Portastomp(), new BeastMaster(), new King(), new Legolas(), new John(), new UltimateDeath(), new Falcon()  };
		for (Kit kit : kitList)
			kits.put(kit.getName().toLowerCase(), kit);
		kits.put(new OneVSOne().getName().toLowerCase(),new OneVSOne());
	}

	public ArrayList<PotionEffect> potionEffects() {
		return new ArrayList<PotionEffect>();
	}

	ItemStack[] giveSoup(ItemStack[] cont) {
		for (int x = 0; x < cont.length; x++)
			if (cont[x] == null)
				cont[x] = easyItem(Messages.ITEM_HEALTH, Material.MUSHROOM_SOUP, 0, null, 1);
		return cont;
	}

	public String getPermission() {
		return "Lonkskit." + getName();
	}
}

