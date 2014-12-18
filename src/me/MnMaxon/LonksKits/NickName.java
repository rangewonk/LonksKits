package me.MnMaxon.LonksKits;

import me.MnMaxon.Apis.EssentialsMethods;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class NickName {
	public static void set(Player p, String nickName) {
		Main.playerData.set("Players." + p.getName() + ".NickName", nickName);
		Main.playerData.save();
		if (nickName == null)
			p.sendMessage(ChatColor.RED + "Your nickname has been removed.");
		else
			p.sendMessage(ChatColor.GREEN + "Your nickname is now " + Main.addColor(nickName) + ChatColor.GREEN + ".");
		if (Locations.gameWorld != null && Locations.gameWorld.equals(p.getWorld()))
			setCustomName(p);
	}

	public static void setCustomName(Player p) {
		String nickName = get(p);
		if (nickName == null) {
			nickName = p.getName();
			if (p.isOp())
				nickName = ChatColor.RED + nickName;
		}
		nickName = nickName + ChatColor.RESET;
		p.setDisplayName(EssentialsMethods.getPrefix(p) + nickName);
		String tabNick = "";
		try {
			tabNick = nickName.substring(0, 16);
		} catch (StringIndexOutOfBoundsException ex) {
			tabNick = nickName;
		}
		if (!p.getPlayerListName().equals(tabNick))
			p.setPlayerListName(tabNick);
	}

	public static String get(Player p) {
		return Main.addColor(Main.playerData.getString("Players." + p.getName() + ".NickName"));
	}
}