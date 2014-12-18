package me.MnMaxon.LonksKits;

import org.bukkit.ChatColor;

public class Messages {
	public static final String PREFIX = ChatColor.GOLD + "[" + ChatColor.AQUA + "LonksKits" + ChatColor.GOLD + "] ";

	public static final String ITEM_SPECTATOR = ChatColor.AQUA + "" + ChatColor.BOLD + "Spectator Mode";
	public static final String ITEM_SELECTOR = ChatColor.GOLD + "" + ChatColor.BOLD + "Kit Selector";
	public static final String ITEM_BOOK = ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Tips";
	public static final String ITEM_SHOP = ChatColor.YELLOW + "" + ChatColor.BOLD + "Point Shop";
	public static final String ITEM_HEALTH = ChatColor.GREEN + "Health Pack";
	public static final String ITEM_HEALTH_EMPTY = ChatColor.RED + "Empty Health Pack";

	public static final String UNABLE_TO_TELEPORT = Messages.PREFIX + ChatColor.RED + "You can't teleport there!";

	public static final String SAFEZONE_NO_KIT = Messages.PREFIX + ChatColor.DARK_RED
			+ "You can't leave without selecting a kit!";
	public static final String SAFEZONE_LEAVE = Messages.PREFIX + ChatColor.DARK_RED
			+ "You have left the safezone, be careful!";
	public static final String SAFEZONE_ENTER = Messages.PREFIX + ChatColor.RED + "You can't go back in there";
	public static final String SAFEZONE_TELEPORT = Messages.PREFIX + ChatColor.DARK_RED
			+ "You can't teleport into the safe zone!";
	public static final String SAFEZONE_IN = PREFIX + ChatColor.DARK_RED + "You can't do that in the safe zone!"
			+ ChatColor.RESET;

	public static final String ABILITY_BLINK = Messages.PREFIX + ChatColor.RED + "You have blinked!";

	public static final String HINT_STOMP = ChatColor.RED + "HINT: " + ChatColor.GRAY
			+ "If you are stomped while shifted, you will take half the damage.";
	public static final String HINT_SUICIDE = ChatColor.RED + "HINT: " + ChatColor.GRAY + "You can use /suicide";

	public static final String PERMISSION_ULTIMATE = ChatColor.RED
			+ "You must be the Ultimate rank or higher to use this!";
	public static final String PERMISSION_COMMAND = ChatColor.RED
			+ "You don't have permission for this command. If you think this is an error please contact the staff.";
	public static final String PERMISSION_KIT = ChatColor.RED + "You do not have permission for this kit!";

	public static final String GUI_SELECTOR = ChatColor.BOLD + "" + ChatColor.DARK_RED + "Select Kit";
	public static final String GUI_SHOP = ChatColor.BOLD + "" + ChatColor.DARK_RED + "Point Shop";
	public static final String GUI_LORE_OWN = "You already own this!";
	public static final String GUI_ITEM_DOUBLE_SOUP = ChatColor.GREEN + "Double Soup";
	public static final String GUI_ITEM_VAMPIRE = ChatColor.GREEN + "Vampirism";
	public static final String GUI_ITEM_NIGHT = ChatColor.GREEN + "Night";
	public static final String GUI_ITEM_EFFECTIVE_SOUP = ChatColor.GREEN + "Effective Soup";
	public static final String GUI_ITEM_REVENGE_ZOMBIE = ChatColor.GREEN + "Revenge Zombie";

	public static final String CLEAR_COOLDOWN = Messages.PREFIX + ChatColor.GREEN + "Cooldown cleared";
	public static final String CLEAR_KIT = Messages.PREFIX + ChatColor.GREEN + "Kit cleared";

	public static final String NO_MONEY = PREFIX + ChatColor.RED + "You do not have enough points!";
	public static final String NO_BUY = PREFIX + ChatColor.RED + "This kit is not for sale!";

	public static final String MUST_BUILD = PREFIX + ChatColor.RED + "To do this, you must be able to build!";

	public static final String WRONG_WORLD = PREFIX + ChatColor.RED + "You must be in KitPvP to do this!";
}
