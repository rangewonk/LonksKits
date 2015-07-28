package me.MnMaxon.LonksKits;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import me.MnMaxon.LonksKits.MetaLists;
import me.MnMaxon.Apis.CloneManager;
import me.MnMaxon.Apis.DisguiseCraftMethods;
import me.MnMaxon.Apis.EssentialsMethods;
import me.MnMaxon.Apis.GhostManager;
import me.MnMaxon.Apis.PLib;
import me.MnMaxon.Apis.VanishAPI;
import me.MnMaxon.Commands.BuildExecuter;
import me.MnMaxon.Commands.ClearCooldownExecuter;
import me.MnMaxon.Commands.ClearKitExecuter;
import me.MnMaxon.Commands.DonateExecuter;
import me.MnMaxon.Commands.HighscoreExecuter;
import me.MnMaxon.Commands.KitJailExecuter;
import me.MnMaxon.Commands.KitNickExecuter;
import me.MnMaxon.Commands.KitPvPExecuter;
import me.MnMaxon.Commands.KitTpExecuter;
import me.MnMaxon.Commands.KitTpHereExecuter;
import me.MnMaxon.Commands.KitUnjailExecuter;
import me.MnMaxon.Commands.KitsExecuter;
import me.MnMaxon.Commands.LKExecuter;
import me.MnMaxon.Commands.OneVSOneExecuter;
import me.MnMaxon.Commands.PointExecuter;
import me.MnMaxon.Commands.SafeZoneExecuter;
import me.MnMaxon.Commands.SpawnExecuter;
import me.MnMaxon.Commands.WebsiteExecuter;
import me.MnMaxon.Kits.Kit;

import com.comphenix.protocol.ProtocolManager;
import com.lenis0012.bukkit.npc.NPCFactory;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.MnMaxon.Listeners.BlockListener;
import me.MnMaxon.Listeners.EntityListener;
import me.MnMaxon.Listeners.InventoryListener;
import me.MnMaxon.Listeners.PlayerListener;
import me.MnMaxon.Listeners.onCrouch;
import me.MnMaxon.Listeners.onEntityDamage;
import me.MnMaxon.Listeners.onEntityDamageByEntity;
import me.MnMaxon.Listeners.onInteract;
import me.MnMaxon.Listeners.onPlayerDeath;
import me.MnMaxon.Listeners.onShoot;

public final class Main extends JavaPlugin {
	public static String dataFolder;
	public static Main plugin;
	public static ArrayList<Cooldown> cool = new ArrayList<Cooldown>();
	public static Map<Block, String> mines = new HashMap<Block, String>();
	public static Map<Class<?>, FakeItem> fakeItems = new HashMap<Class<?>, FakeItem>();
	public static Map<Location, CloneBlock> changeBack = new HashMap<Location, CloneBlock>();
	public static Location loc1;
	public static Location loc2;
	public static ProtocolManager protocolManager;
	public static SuperYaml config;
	public static SuperYaml data;
	public static SuperYaml bookData;
	public static SuperYaml signData;
	public static SuperYaml playerData;
	public static SuperYaml log;
	public static SuperYaml kitData;
	public static SuperYaml uuidData;
	public static SuperYaml pointShop;
	public static GhostManager ghostManager;
	public static NPCFactory npcFactory;
	public static HashMap<String, CommandExecutor> commands = new HashMap<String, CommandExecutor>();
	public static HashMap<Integer, ArrayList<Location>> signs = new HashMap<Integer, ArrayList<Location>>();

	@Override
	public void onEnable() {
		plugin = this;
		dataFolder = this.getDataFolder().getAbsolutePath();
		CustomEntityType.registerEntitiesNoOverride();
		DisguiseCraftMethods.setup();
		VanishAPI.setup();
		reloadConfigs();
		Kit.setUpList();
		ghostManager = new GhostManager(this);
		npcFactory = new NPCFactory(this);
		Listener[] listeners = { new PlayerListener(), new InventoryListener(), new EntityListener(),
				new BlockListener(), new onShoot(), new onInteract(), new onCrouch(), new onEntityDamageByEntity(),
				new onEntityDamage(), new onPlayerDeath() };
		for (Listener l : listeners) {
			getServer().getPluginManager().registerEvents(l, this);
		}
		PLib.setupProtatocalLib();
		Timer.startTimer();
		registerCommands();
		FakeItem.setupFakeItems();
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				if (Locations.gameWorld != null) {
					for (Entity ent : Locations.gameWorld.getEntities())
						if (!(ent instanceof Player))
							ent.remove();
						else {
							Player p = (Player) ent;
							ghostManager.addPlayer(p);
							ClearKit(p);
							MetaLists.TP_AROUND.add(p);
							p.teleport(Locations.spawn);
							MetaLists.PLAYERS.add(p, null);
							giveSpawnItems(p);
						}
				}
			}
		}, 5L);
	}

	private void registerCommands() {
		commands.put("Spawn", new SpawnExecuter());
		commands.put("Kits", new KitsExecuter());
		commands.put("KitPvP", new KitPvPExecuter());
		commands.put("LK", new LKExecuter());
		commands.put("SZ", new SafeZoneExecuter());
		commands.put("Points", new PointExecuter());
		commands.put("Highscore", new HighscoreExecuter());
		commands.put("CK", new ClearKitExecuter());
		commands.put("CC", new ClearCooldownExecuter());
		commands.put("KitTPHere", new KitTpHereExecuter());
		commands.put("Website", new WebsiteExecuter());
		commands.put("Donate", new DonateExecuter());
		commands.put("Build", new BuildExecuter());
		commands.put("KitTp", new KitTpExecuter());
		commands.put("KitJail", new KitJailExecuter());
		commands.put("KitUnjail", new KitUnjailExecuter());
		commands.put("KitNick", new KitNickExecuter());
		commands.put("KitPvP", new KitPvPExecuter());
		commands.put("1v1", new OneVSOneExecuter());
		commands.put("Ability", new AbilityExecuter());
		for (Entry<String, CommandExecutor> entry : commands.entrySet())
			getCommand(entry.getKey()).setExecutor(entry.getValue());
	}

	public static void reloadConfigs() {
		config = new SuperYaml(dataFolder + "/Config.yml");
		data = new SuperYaml(dataFolder + "/Data");
		bookData = new SuperYaml(dataFolder + "/Book.yml");
		signData = new SuperYaml(dataFolder + "/Signs.yml");
		playerData = new SuperYaml(dataFolder + "/Players.yml");
		kitData = new SuperYaml(dataFolder + "/Kits.yml");
		log = new SuperYaml(dataFolder + "/log.yml");
		uuidData = new SuperYaml(dataFolder + "/UUIDs.yml");
		pointShop = new SuperYaml(dataFolder + "/PointShop.yml");

		cfgSetter("World", "kitpvp");
		cfgSetter("Website", "https://www.google.com/");
		cfgSetter("Donate", "https://www.google.com/");

		Shop.reload();
		Points.setScoreSigns();
		Locations.reload();
	}

	@Override
	public void onDisable() {
		for (Entry<Location, CloneBlock> entry : changeBack.entrySet())
			CloneBlock.set(entry.getKey(), entry.getValue());
		for (Entry<String, Integer> entry : Shop.doubleSoupTimeLeft.entrySet())
			data.set("Shop.doubleSoup." + entry.getKey(), entry.getValue());
		for (Entry<String, Integer> entry : Shop.vampTimeLeft.entrySet())
			data.set("Shop.vamp." + entry.getKey(), entry.getValue());
		for (Entry<String, Integer> entry : Shop.effectiveSoupTimeLeft.entrySet())
			data.set("Shop.effectiveSoup." + entry.getKey(), entry.getValue());
		for (Entry<String, Integer> entry : Shop.revengeZombieTimeLeft.entrySet())
			data.set("Shop.revengeZombie." + entry.getKey(), entry.getValue());
		data.set("Shop.night", Shop.nightTimeLeft);
		data.save();
	}

	public static void kick(Player p) {
		if (MetaLists.PLAYERS.contains(p)) {
			if (!p.isDead() && p.getLastDamageCause() != null && p.getLastDamageCause().getEntity() != null) {
				Main.ClearCooldown(p);
				Entity ent = p.getLastDamageCause().getEntity();
				if (ent instanceof Player && !p.equals(ent)) {
					p.damage(1000, p.getLastDamageCause().getEntity());
					if (!MetaLists.SPAWN_ANTI.contains(p))
						MetaLists.SPAWN_TO.add(p);
					else
						MetaLists.SPAWN_ANTI.remove(p);
				} else if (ent instanceof Arrow) {
					Arrow arrow = (Arrow) p.getLastDamageCause().getEntity();
					if (arrow.getShooter() != null && arrow.getShooter() instanceof Player
							&& ((Player) arrow.getShooter()).isOnline()) {
						p.damage(1000, (Player) arrow.getShooter());
						if (!MetaLists.SPAWN_ANTI.contains(p))
							MetaLists.SPAWN_TO.add(p);
						else
							MetaLists.SPAWN_ANTI.remove(p);
					}
				} else
					p.damage(1000);
			} else if (!p.isDead())
				p.damage(1000);
			else {
				if (!MetaLists.SPAWN_ANTI.contains(p))
					MetaLists.SPAWN_TO.add(p);
				else
					MetaLists.SPAWN_ANTI.remove(p);
			}
		} else if (Locations.gameWorld != null && !p.isDead() && p.getWorld().equals(Locations.gameWorld))
			p.damage(1000);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "You need to be a player to do this!");
			return false;
		}
		Kit kit = Kit.matchKit(cmd.getName());
		Player p = (Player) sender;
		if (kit != null) {
			if (p.hasPermission("Lonkskit." + cmd.getName().toLowerCase()))
				Kit.setClass(p, cmd.getName());
			else
				p.sendMessage(Messages.PERMISSION_KIT);
		} else
			displayHelp(p, 1);
		return false;
	}

	public static void ClearKit(Player p) {
		if (p.getVehicle() != null && !(p.getVehicle() instanceof Player))
			p.getVehicle().remove();
		DisguiseCraftMethods.unDisguise(p);
		if(MetaLists.PLAYERS_INVISIBLE.contains( p)) PLib.toggleVisibility(p, false);
		MetaLists.PLAYERS.remove(p);
		MetaLists.PLAYERS.remove(p);
		CloneManager.remove(p);
		onInteract.removeMinions(p);
		if (Locations.gameWorld.equals(p.getWorld())) {
			for (Entity ent : p.getWorld().getEntities())
				if (ent instanceof Wolf && ((Wolf) ent).getCustomName() != null
						&& ((Wolf) ent).getCustomName().contains(p.getName()))
					((Wolf) ent).damage(999);
			p.getInventory().setArmorContents(null);
			p.getInventory().clear();
			ArrayList<PotionEffectType> remover = new ArrayList<PotionEffectType>();
			for (PotionEffect pot : p.getActivePotionEffects())
				remover.add(pot.getType());
			for (PotionEffectType effects : remover)
				p.removePotionEffect(effects);
		}
	}

	public static void displayHelp(CommandSender s, int page) {
		ArrayList<String> messages = new ArrayList<String>();
		messages.add(ChatColor.DARK_PURPLE + "/LK Help [Page#]" + ChatColor.GREEN + "-" + ChatColor.AQUA
				+ " Displays help");
		messages.add(ChatColor.DARK_PURPLE + "/1v1" + ChatColor.GREEN + "-" + ChatColor.AQUA + " Displays 1v1 help");
		for (Entry<String, CommandExecutor> entry : Main.commands.entrySet())
			messages.add(ChatColor.DARK_PURPLE + Main.plugin.getCommand(entry.getKey()).getUsage() + ChatColor.GREEN
					+ " - " + ChatColor.AQUA + plugin.getCommand(entry.getKey()).getDescription());

		int maxPages = (messages.size() + 4) / 5;// +5
		if (page > maxPages)
			page = maxPages;
		if (page < 1)
			page = 1;
		s.sendMessage(ChatColor.AQUA + "========= LonksKits Help (" + page + "/" + maxPages + ") =========");
		for (int i = (page - 1) * 5; i < messages.size() && i < page * 5; i++)
			s.sendMessage(messages.get(i));
	}

	public static void cfgSetter(String path, Object value) {
		if (config.get(path) == null) {
			config.set(path, value);
			config.save();
		}
	}

	public static void giveSpawnItems(Player p) {
		p.getInventory().setItem(0, Kit.easyItem(Messages.ITEM_SELECTOR, Material.NETHER_STAR, 0, null, 1));
		p.getInventory().setItem(1, Kit.easyItem(Messages.ITEM_SHOP, Material.WATCH, 0, null, 1));
		ItemStack book = Kit.easyItem(Messages.ITEM_BOOK, Material.WRITTEN_BOOK, 0, null, 1);

		BookMeta meta = (BookMeta) book.getItemMeta();
		List<String> rawPages = new ArrayList<String>();
		if (bookData.get("Pages") == null) {
			rawPages.add("&cThis is page 1");
			rawPages.add("&1This is page 2");
			bookData.set("Pages", rawPages);
			bookData.save();
		} else
			rawPages = bookData.getStringList("Pages");
		List<String> pages = new ArrayList<String>();
		for (String page : rawPages)
			pages.add(addColor(page));
		meta.setAuthor("LonksKits");
		meta.setTitle("Tips");
		meta.setPages(pages);
		book.setItemMeta(meta);

		p.getInventory().setItem(2, book);
		p.getInventory().setItem(3, Kit.easyItem(Messages.ITEM_SPECTATOR, Material.RAW_FISH, 3, null, 1));
	}

	public static String addColor(String string) {
		if (string == null)
			return null;
		return string.replace("§", "&").replace("&0", "" + ChatColor.BLACK).replace("&1", "" + ChatColor.DARK_BLUE)
				.replace("&2", "" + ChatColor.DARK_GREEN).replace("&3", "" + ChatColor.DARK_AQUA)
				.replace("&4", "" + ChatColor.DARK_RED).replace("&5", "" + ChatColor.DARK_PURPLE)
				.replace("&6", "" + ChatColor.GOLD).replace("&7", "" + ChatColor.GRAY)
				.replace("&8", "" + ChatColor.DARK_GRAY).replace("&9", "" + ChatColor.BLUE)
				.replace("&A", "" + ChatColor.GREEN).replace("&B", "" + ChatColor.AQUA)
				.replace("&C", "" + ChatColor.RED).replace("&D", "" + ChatColor.LIGHT_PURPLE)
				.replace("&E", "" + ChatColor.YELLOW).replace("&F", "" + ChatColor.WHITE)
				.replace("&M", "" + ChatColor.STRIKETHROUGH).replace("&N", "" + ChatColor.UNDERLINE)
				.replace("&L", "" + ChatColor.BOLD).replace("&K", "" + ChatColor.MAGIC)
				.replace("&O", "" + ChatColor.ITALIC).replace("&a", "" + ChatColor.GREEN)
				.replace("&b", "" + ChatColor.AQUA).replace("&c", "" + ChatColor.RED)
				.replace("&d", "" + ChatColor.LIGHT_PURPLE).replace("&e", "" + ChatColor.YELLOW)
				.replace("&f", "" + ChatColor.WHITE).replace("&m", "" + ChatColor.STRIKETHROUGH)
				.replace("&n", "" + ChatColor.UNDERLINE).replace("&l", "" + ChatColor.BOLD)
				.replace("&k", "" + ChatColor.MAGIC).replace("&o", "" + ChatColor.ITALIC)
				+ ChatColor.RESET;
	}

	public void disable(boolean disable) {
		this.setEnabled(!disable);
	}

	public static void ClearCooldown(Player p) {
		if (!cool.isEmpty()) {
			ArrayList<Cooldown> remover = new ArrayList<Cooldown>();
			for (Cooldown cd : cool)
				if (cd.player.equals(p))
					remover.add(cd);
			for (Cooldown cd : remover)
				cool.remove(cd);
		}
	}

	public static boolean canDamage(Entity ent) {
		if (ent instanceof LivingEntity
				&& ent.isValid()
				&& !Locations.inSafe(ent.getLocation())
				&& (!(ent instanceof Player) || (!Spectator.isSpectator((Player) ent)
						&& !EssentialsMethods.hasGodMode((Player) ent) && !VanishAPI.isVanished((Player) ent))))
			return true;
		return false;
	}

	public static void damageIgnoreArmor(Entity ent, double damage, boolean ignoreEnchantments) {
		if (ent instanceof Damageable && ent.isValid())
			if (ignoreEnchantments || !(ent instanceof Player)) {
				Damageable dam = (Damageable) ent;
				dam.damage(0.0);
				if (dam.getHealth() - damage <= 0.0)
					dam.damage(999);
				else
					dam.setHealth(dam.getHealth() - damage);
			} else {
				PlayerInventory inv = ((Player) ent).getInventory();
				ItemStack boots = inv.getBoots();
				ItemStack helmet = inv.getHelmet();
				ItemStack chest = inv.getChestplate();
				ItemStack pants = inv.getLeggings();
				double red = 0.0;
				if (helmet.getType() == Material.LEATHER_HELMET)
					red = red + 0.04;
				else if (helmet.getType() == Material.GOLD_HELMET)
					red = red + 0.08;
				else if (helmet.getType() == Material.CHAINMAIL_HELMET)
					red = red + 0.08;
				else if (helmet.getType() == Material.IRON_HELMET)
					red = red + 0.08;
				else if (helmet.getType() == Material.DIAMOND_HELMET)
					red = red + 0.12;

				if (boots.getType() == Material.LEATHER_BOOTS)
					red = red + 0.04;
				else if (boots.getType() == Material.GOLD_BOOTS)
					red = red + 0.04;
				else if (boots.getType() == Material.CHAINMAIL_BOOTS)
					red = red + 0.04;
				else if (boots.getType() == Material.IRON_BOOTS)
					red = red + 0.08;
				else if (boots.getType() == Material.DIAMOND_BOOTS)
					red = red + 0.12;

				if (pants.getType() == Material.LEATHER_LEGGINGS)
					red = red + 0.08;
				else if (pants.getType() == Material.GOLD_LEGGINGS)
					red = red + 0.12;
				else if (pants.getType() == Material.CHAINMAIL_LEGGINGS)
					red = red + 0.16;
				else if (pants.getType() == Material.IRON_LEGGINGS)
					red = red + 0.20;
				else if (pants.getType() == Material.DIAMOND_LEGGINGS)
					red = red + 0.24;

				if (chest.getType() == Material.LEATHER_CHESTPLATE)
					red = red + 0.12;
				else if (chest.getType() == Material.GOLD_CHESTPLATE)
					red = red + 0.20;
				else if (chest.getType() == Material.CHAINMAIL_CHESTPLATE)
					red = red + 0.20;
				else if (chest.getType() == Material.IRON_CHESTPLATE)
					red = red + 0.24;
				else if (chest.getType() == Material.DIAMOND_CHESTPLATE)
					red = red + 0.32;
				((Player) ent).damage(damage + red);
			}
	}

	public static boolean hasPermision(CommandSender s, String perm) {
		if (s instanceof Player)
			return ((Player) s).hasPermission(perm);
		else
			return true;
	}

	public static void endOneVOne(Player loser, String winnerName) {
		MetaLists.ONE_V_ONE_FIGHT.remove(loser);
		Player winner = Bukkit.getPlayer(winnerName);
		if (winner != null) {
			Death.update(loser, winner, "1v1");
			MetaLists.ONE_V_ONE_FIGHT.remove(winner);
			MetaLists.TP_AROUND.add(winner);
			winner.teleport(Locations.spawn);
			Main.ClearKit(winner);
			MetaLists.PLAYERS.add(winner, null);
			winner.setHealth(winner.getMaxHealth());
			Main.giveSpawnItems(winner);
			for (Player player : Bukkit.getServer().getOnlinePlayers()) {
				if (!VanishAPI.isVanished(winner))
					player.showPlayer(winner);
				if (!VanishAPI.isVanished(player))
					winner.showPlayer(player);
			}
		}
	}
}