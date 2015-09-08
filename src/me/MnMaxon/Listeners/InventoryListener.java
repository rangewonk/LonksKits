package me.MnMaxon.Listeners;

import java.util.ArrayList;
import java.util.Arrays;

import me.MnMaxon.Kits.Blink;
import me.MnMaxon.Kits.Hulk;
import me.MnMaxon.Kits.Kit;
import me.MnMaxon.Kits.Shark;
import me.MnMaxon.LonksKits.Locations;
import me.MnMaxon.LonksKits.Main;
import me.MnMaxon.LonksKits.Messages;
import me.MnMaxon.LonksKits.MetaLists;
import me.MnMaxon.LonksKits.Points;
import me.MnMaxon.LonksKits.Shop;
import me.MnMaxon.LonksKits.Spectator;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.HorseInventory;
import org.bukkit.inventory.ItemStack;

public class InventoryListener implements Listener {
	public static final ArrayList<String> ignoredNames = new ArrayList<String>(Arrays.asList("spear", "wand", "lasso",
			"grenade", "bomb", "switcher", "shuriken", "pistol"));

	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		if (MetaLists.PLAYERS.contains(e.getWhoClicked()) && e.getCurrentItem() != null) {
			Player p = (Player) e.getWhoClicked();
			if (MetaLists.PLAYERS.get(p) instanceof Shark && e.getCursor() != null)
				if (e.getCurrentItem().getType().equals(Material.RAW_FISH)
						&& (e.getCursor().getType().equals(Material.RAW_FISH) || e.isShiftClick())
						|| e.getAction().equals(InventoryAction.COLLECT_TO_CURSOR))
					if (e.isShiftClick()) {
						e.setCancelled(true);
						if (e.getSlot() > 9 && e.getWhoClicked().getInventory().firstEmpty() != -1
								&& e.getWhoClicked().getInventory().firstEmpty() <= 9) {
							e.getWhoClicked().getInventory()
									.setItem(e.getWhoClicked().getInventory().firstEmpty(), e.getCurrentItem());
							e.getWhoClicked().getInventory().setItem(e.getSlot(), null);
						}
					}
			if (e.getInventory().getTitle().equals(Messages.GUI_SHOP)) {
				e.setCancelled(true);
				if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta()
						&& e.getCurrentItem().getItemMeta().hasDisplayName()
						&& e.getCurrentItem().getItemMeta().hasLore()) {
					if (e.getCurrentItem().getItemMeta().getLore()
							.get(e.getCurrentItem().getItemMeta().getLore().size() - 1).equals(Messages.GUI_LORE_OWN)) {
						p.sendMessage(ChatColor.RED + Messages.GUI_LORE_OWN);
						return;
					}
					String name = e.getCurrentItem().getItemMeta().getDisplayName();
					if (name.equals(Messages.GUI_ITEM_DOUBLE_SOUP)) {
						if (Shop.doubleSoupPrice > Points.get(e.getWhoClicked().getName())) {
							p.sendMessage(Messages.NO_MONEY);
							return;
						}
						Points.remove(e.getWhoClicked().getName(), Shop.doubleSoupPrice);
						int time = Shop.doubleSoupTime;
						if (Shop.doubleSoupTimeLeft.containsKey(e.getWhoClicked().getName())) {
							time = time + Shop.doubleSoupTimeLeft.get(e.getWhoClicked().getName());
							Shop.doubleSoupTimeLeft.remove(e.getWhoClicked().getName());
						}
						Shop.doubleSoupTimeLeft.put(e.getWhoClicked().getName(), time);
						p.sendMessage(Messages.PREFIX + ChatColor.BLUE + "You have " + ChatColor.GREEN + "DOUBLE SOUP"
								+ ChatColor.BLUE + " for " + time + " minutes!");
					} else if (name.equals(Messages.GUI_ITEM_EFFECTIVE_SOUP)) {
						if (Shop.effectiveSoupPrice > Points.get(e.getWhoClicked().getName())) {
							p.sendMessage(Messages.NO_MONEY);
							return;
						}
						Points.remove(e.getWhoClicked().getName(), Shop.effectiveSoupPrice);
						int time = Shop.effectiveSoupTime;
						if (Shop.effectiveSoupTimeLeft.containsKey(e.getWhoClicked().getName())) {
							time = time + Shop.effectiveSoupTimeLeft.get(e.getWhoClicked().getName());
							Shop.effectiveSoupTimeLeft.remove(e.getWhoClicked().getName());
						}
						Shop.effectiveSoupTimeLeft.put(e.getWhoClicked().getName(), time);
						p.sendMessage(Messages.PREFIX + ChatColor.BLUE + "You have " + ChatColor.GREEN
								+ "EFFECTIVE SOUP" + ChatColor.BLUE + " for " + time + " minutes!");
					} else if (name.equals(Messages.GUI_ITEM_NIGHT)) {
						if (Shop.nightPrice > Points.get(e.getWhoClicked().getName())) {
							p.sendMessage(Messages.NO_MONEY);
							return;
						}
						int time = Shop.nightTimeLeft + Shop.nightTime;
						if (time > 30) {
							p.sendMessage(Messages.PREFIX + ChatColor.RED
									+ "Night cannot be bought for more than 30 minutes!");
							p.closeInventory();
							return;
						}
						Points.remove(e.getWhoClicked().getName(), Shop.nightPrice);
						Shop.nightTimeLeft = time;
						if (Locations.gameWorld != null)
							Locations.gameWorld.setTime(18000);
						p.sendMessage(Messages.PREFIX + ChatColor.BLUE + "You have " + ChatColor.GREEN + "NIGHT TIME"
								+ ChatColor.BLUE + " for " + time + " minutes!");
						Main.data.set("Shop.NightPlayer", p.getName());
					} else if (name.equals(Messages.GUI_ITEM_REVENGE_ZOMBIE)) {
						if (Shop.revengeZombiePrice > Points.get(e.getWhoClicked().getName())) {
							p.sendMessage(Messages.NO_MONEY);
							return;
						}
						Points.remove(e.getWhoClicked().getName(), Shop.revengeZombiePrice);
						int time = Shop.revengeZombieTime;
						if (Shop.revengeZombieTimeLeft.containsKey(e.getWhoClicked().getName())) {
							time = time + Shop.revengeZombieTimeLeft.get(e.getWhoClicked().getName());
							Shop.revengeZombieTimeLeft.remove(e.getWhoClicked().getName());
						}
						Shop.revengeZombieTimeLeft.put(e.getWhoClicked().getName(), time);
						p.sendMessage(Messages.PREFIX + ChatColor.BLUE + "You have " + ChatColor.GREEN
								+ "REVENGE ZOMBIE" + ChatColor.BLUE + " for " + time + " minutes!");
					} else if (name.equals(Messages.GUI_ITEM_VAMPIRE)) {
						if (Shop.vampPrice > Points.get(e.getWhoClicked().getName())) {
							p.sendMessage(Messages.NO_MONEY);
							return;
						}
						Points.remove(e.getWhoClicked().getName(), Shop.vampPrice);
						int time = Shop.vampTime;
						if (Shop.vampTimeLeft.containsKey(e.getWhoClicked().getName())) {
							time = time + Shop.vampTimeLeft.get(e.getWhoClicked().getName());
							Shop.vampTimeLeft.remove(e.getWhoClicked().getName());
						}
						Shop.vampTimeLeft.put(e.getWhoClicked().getName(), time);
						p.sendMessage(Messages.PREFIX + ChatColor.BLUE + "You have " + ChatColor.GREEN + "VAMPIRISM"
								+ ChatColor.BLUE + " for " + time + " minutes!");
					} else
						return;
					e.getWhoClicked().closeInventory();
				}
			} else if (e.getInventory().getTitle().equals(Messages.GUI_SELECTOR)) {
				e.setCancelled(true);
				ItemStack is = e.getCurrentItem();
				if (is != null && is.hasItemMeta() && is.getItemMeta().hasDisplayName())
					if (is.getItemMeta().getDisplayName().contains("Page "))
						Kit.openGui(
								(Player) e.getWhoClicked(),
								Integer.parseInt(ChatColor.stripColor(is.getItemMeta().getDisplayName()
										.replace("Page ", ""))));
					else if (is.getItemMeta().getDisplayName()
							.equals(ChatColor.GREEN + ChatColor.stripColor(is.getItemMeta().getDisplayName()))
							|| is.getItemMeta().getDisplayName()
									.equals(ChatColor.BLUE + ChatColor.stripColor(is.getItemMeta().getDisplayName())))
						Kit.setClass(p, ChatColor.stripColor(is.getItemMeta().getDisplayName()));
					else {
						if(Kit.matchKit(is.getItemMeta().getDisplayName()) != null)
						if (e.getClick().isRightClick()) {
							Kit.matchKit(is.getItemMeta().getDisplayName()).buy(p);
							p.closeInventory();
						} else if (e.getClick().isLeftClick()) {
							Kit.matchKit(is.getItemMeta().getDisplayName()).rent(p);
							p.closeInventory();
						}
						// TODO Add buy/rent stuff!
					}
			} else if (e.getClick().equals(ClickType.CONTROL_DROP) || e.getClick().equals(ClickType.DROP)
					|| e.getClick().equals(ClickType.WINDOW_BORDER_LEFT)
					|| e.getClick().equals(ClickType.WINDOW_BORDER_RIGHT) || e.getSlotType().equals(SlotType.ARMOR))
				e.setCancelled(true);
			if (e.getInventory() instanceof HorseInventory)
				if (e.getCurrentItem().getType().equals(Material.DIAMOND_BARDING)
						|| e.getCurrentItem().getType().equals(Material.SADDLE))
					e.setCancelled(true);
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		if (MetaLists.PLAYERS.contains(e.getPlayer())) {
			if (e.getItemDrop().getItemStack().getType().equals(Material.MUSHROOM_SOUP)
					|| e.getItemDrop().getItemStack().getType().equals(Material.BOWL)
					|| (e.getItemDrop().getItemStack().getType().equals(Material.RAW_FISH) && e.getItemDrop()
							.getItemStack().getDurability() != 3))
				e.getItemDrop().remove();
			else {
				Player p = e.getPlayer();
				if (!p.getInventory().getViewers().isEmpty() && p.getInventory().firstEmpty() != -1) {
					ItemStack item = e.getItemDrop().getItemStack().clone();
					e.getItemDrop().remove();
					p.getInventory().setItem(p.getInventory().firstEmpty(), item);
					e.getPlayer().sendMessage(
							ChatColor.GOLD + "[" + ChatColor.AQUA + "LonksKits" + ChatColor.GOLD + "]" + ChatColor.RED
									+ "You can't drop this");
					p.updateInventory();
				} else {
					ItemStack item = e.getItemDrop().getItemStack().clone();
					item.setAmount(p.getInventory().getItemInHand().getAmount() + 1);
					e.getItemDrop().remove();
					p.getInventory().setItem(p.getInventory().getHeldItemSlot(), item);
					e.getPlayer().sendMessage(
							ChatColor.GOLD + "[" + ChatColor.AQUA + "LonksKits" + ChatColor.GOLD + "]" + ChatColor.RED
									+ "You can't drop this");
				}
			}
		}
	}

	@EventHandler
	public void onPickup(PlayerPickupItemEvent e) {
		if (Spectator.isSpectator(e.getPlayer())) {
			e.setCancelled(true);
			return;
		}
		if (e.getItem().getItemStack().getType().equals(Material.MUSHROOM_SOUP)
				&& MetaLists.PLAYERS.contains(e.getPlayer())) {
			e.getItem().setItemStack(Kit.easyItem(Messages.ITEM_HEALTH, Material.MUSHROOM_SOUP, 0, null, 1));
			if (MetaLists.PLAYERS.get(e.getPlayer()) instanceof Shark) {
				if (e.getPlayer().getInventory().firstEmpty() == -1)
					e.setCancelled(true);
				else {
					e.getItem().remove();
					e.setCancelled(true);
					e.getPlayer()
							.getInventory()
							.setItem(e.getPlayer().getInventory().firstEmpty(),
									Kit.easyItem(Messages.ITEM_HEALTH, Material.RAW_FISH, 0, null, 1));
					if (Shop.doubleSoupTimeLeft.containsKey(e.getPlayer().getName()))
						if (e.getPlayer().getInventory().firstEmpty() != -1)
							e.getPlayer()
									.getInventory()
									.setItem(e.getPlayer().getInventory().firstEmpty(),
											Kit.easyItem(Messages.ITEM_HEALTH, Material.RAW_FISH, 0, null, 1));
				}
			} else if (!(MetaLists.PLAYERS.get(e.getPlayer()) instanceof Hulk)) {
				if (e.getPlayer().getInventory().firstEmpty() == -1)
					e.setCancelled(true);
				else if (Shop.doubleSoupTimeLeft.containsKey(e.getPlayer().getName())) {
					e.getItem().remove();
					e.setCancelled(true);
					e.getPlayer()
							.getInventory()
							.setItem(e.getPlayer().getInventory().firstEmpty(),
									Kit.easyItem(Messages.ITEM_HEALTH, Material.MUSHROOM_SOUP, 0, null, 1));
					if (e.getPlayer().getInventory().firstEmpty() != -1)
						e.getPlayer()
								.getInventory()
								.setItem(e.getPlayer().getInventory().firstEmpty(),
										Kit.easyItem(Messages.ITEM_HEALTH, Material.MUSHROOM_SOUP, 0, null, 1));
				}
			}
		}
		if (MetaLists.PLAYERS.contains(e.getPlayer()) && MetaLists.PLAYERS.get(e.getPlayer()) instanceof Hulk) {
			e.setCancelled(true);
			if (e.getPlayer().getInventory().firstEmpty() != -1) {
				int x = -1;
				for (int i = 0; i < e.getPlayer().getInventory().getSize() && x == -1; i++)
					if (e.getPlayer().getInventory().getItem(i) == null && i != 1)
						x = i;
				if (x >= 0) {
					e.getItem().remove();
					e.getPlayer().getInventory().setItem(x, e.getItem().getItemStack());
				}
				if (Shop.doubleSoupTimeLeft.containsKey(e.getPlayer().getName())
						&& e.getPlayer().getInventory().firstEmpty() != -1) {
					int y = -1;
					for (int i = 0; i < e.getPlayer().getInventory().getSize() && y == -1; i++)
						if (e.getPlayer().getInventory().getItem(i) == null && i != 1)
							y = i;
					if (y >= 0) {
						e.getItem().remove();
						e.getPlayer().getInventory().setItem(y, e.getItem().getItemStack());
					}
				}
			}
		}
		if (MetaLists.PLAYERS.contains(e.getPlayer())
				&& e.getItem().getItemStack() != null
				&& e.getItem().getItemStack().hasItemMeta()
				&& e.getItem().getItemStack().getItemMeta().hasDisplayName()
				&& ignoredNames.contains(ChatColor
						.stripColor(e.getItem().getItemStack().getItemMeta().getDisplayName()).toLowerCase()))
			e.setCancelled(true);
	}

	@EventHandler
	public void onInventoryDragEvent(InventoryDragEvent e) {
		if (MetaLists.PLAYERS.contains(e.getWhoClicked()) && MetaLists.PLAYERS.get(e.getWhoClicked()) instanceof Shark)
			e.setCancelled(true);
	}

	@EventHandler
	public void onInventoryOpenEvent(InventoryOpenEvent e) {
		if (MetaLists.PLAYERS.contains(e.getPlayer()) && MetaLists.PLAYERS.get(e.getPlayer()) instanceof Blink) {
			int first = e.getPlayer().getInventory().first(Material.COAL);
			if (first != -1) {
				ItemStack is = e.getPlayer().getInventory().getItem(first);
				is.setType(Material.REDSTONE_TORCH_ON);
				e.getPlayer().getInventory().setItem(first, is);
			}
		}
	}

	@EventHandler
	public void onInventoryCloseEvent(InventoryOpenEvent e) {
		if (MetaLists.PLAYERS.contains(e.getPlayer()) && MetaLists.PLAYERS.get(e.getPlayer()) instanceof Blink) {
			int first = e.getPlayer().getInventory().first(Material.REDSTONE_TORCH_ON);
			if (first != -1) {
				ItemStack is = e.getPlayer().getInventory().getItem(first);
				is.setType(Material.COAL);
				e.getPlayer().getInventory().setItem(first, is);
			}
		}
	}
}