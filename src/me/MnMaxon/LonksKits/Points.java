package me.MnMaxon.LonksKits;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class Points {
	public static ArrayList<String> lastScores = null;
	
	//Scores every player's score. Indexed by name
	public static HashMap<String, Integer> scoreCache = null;
	//Stores player names in the order of their position in the highscores
	public static ArrayList<String> positions = null;
	
	
	
	public static void add(String playerName, Integer money) {
		set(playerName, get(playerName) + money);
	}

	public static Boolean remove(String playerName, Integer money) {
		money = get(playerName) - money;
		if (money < 0)			return false;
		set(playerName, money);
		return true;
	}

	public static Integer get(String playerName) {
		if(scoreCache == null || positions == null) loadCache();
		
		//If for some reason the player's score was not loaded.
		if(scoreCache.get(playerName) == null 
				&& Main.playerData.getConfigurationSection("Players").isSet(playerName+".Points"))
			if(Main.playerData.getInt("Players."+playerName+".Points") > 0){
				addPlayerToSet(playerName, Main.playerData.getInt("Players."+playerName+".Points"));
				resortPositions();
			}
		if (scoreCache.get(playerName) == null){
			return 0;
		}
		else
			return scoreCache.get(playerName);
	}

	public static void set(String playerName, Integer money) {
		if(scoreCache == null || positions == null) loadCache();
		
		if(scoreCache.get(playerName) == null) addPlayerToSet(playerName, money);
		else if(money == 0) removePlayerFromSet(playerName);
		else scoreCache.put(playerName, money);
		resortPositions();
		
		Main.playerData.set("Players." + playerName + ".Points", money);
		Main.playerData.save();
	}
	
	public static void removePlayerFromSet(String playerName)
	{
		if(scoreCache.get(playerName) != null){
			scoreCache.remove(playerName);
			positions.remove(playerName);
		}
	}
	
	public static void addPlayerToSet(String playerName, Integer money)
	{
		scoreCache.put(playerName, money);
		positions.add(playerName);
	}

	public static void help(CommandSender s) {
		s.sendMessage(ChatColor.AQUA + "========= LonksKits Points Help =========");
		s.sendMessage(ChatColor.DARK_PURPLE + "/Points " + ChatColor.GREEN + "-" + ChatColor.AQUA
				+ " Shows your points");
		if (s instanceof Player && ((Player) s).hasPermission("points.add"))
			s.sendMessage(ChatColor.DARK_PURPLE + "/Points Add <PLAYER_NAME> <POINTS>" + ChatColor.GREEN + "-"
					+ ChatColor.AQUA + " Allows you to add points");
		if (s instanceof Player && ((Player) s).hasPermission("points.remove"))
			s.sendMessage(ChatColor.DARK_PURPLE + "/Points Remove <PLAYER_NAME> <POINTS>" + ChatColor.GREEN + "-"
					+ ChatColor.AQUA + " Allows you to remove points");
		if (s instanceof Player && ((Player) s).hasPermission("points.set"))
			s.sendMessage(ChatColor.DARK_PURPLE + "/Points Set <PLAYER_NAME> <POINTS>" + ChatColor.GREEN + "-"
					+ ChatColor.AQUA + " Allows you to set points");
		s.sendMessage(ChatColor.DARK_PURPLE + "/Points Show <PLAYER_NAME>" + ChatColor.GREEN + "-" + ChatColor.AQUA
				+ " Allows you to see others' points");
		s.sendMessage(ChatColor.DARK_PURPLE + "/Points Highscore" + ChatColor.GREEN + "-" + ChatColor.AQUA
				+ " Shows you the highscore board");
	}

	public static void resortPositions()
	{
		if(positions == null || positions.size() != scoreCache.size()){
			positions = new ArrayList<String>();
			
			for(Entry<String, Integer> entry : scoreCache.entrySet()){
				positions.add(entry.getKey());
			}
		}
		Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, new Runnable() {
			@Override
			public void run() {
		//Cocktail sort algorithm
		int top = positions.size()-1;
		int bottom = 0;
		boolean swapped = true;
		String temp;
		
		do{
			swapped = false;
			for(int i = bottom; i < top; i++)
			{
				if(scoreCache.get(positions.get(i)) > scoreCache.get(positions.get(i + 1)))
				{
					temp = positions.get(i);
					positions.set(i, positions.get(i + 1)); 
					positions.set(i + 1, temp);
					swapped = true;
				}
			}
			top--;
			
			if(swapped)
			{
				swapped = false;
				for(int i = top; i > bottom; i--)
				{
					if(scoreCache.get(positions.get(i)) < scoreCache.get(positions.get(i - 1)))
					{
						temp = positions.get(i);
						positions.set(i, positions.get(i - 1)); 
						positions.set(i - 1, temp);
						swapped = true;
					}
				}
				bottom++;
			}
		}while(swapped);
			}
		});
	}
	public static void loadCache()
	{
		scoreCache = new HashMap<String, Integer>();
		
		ConfigurationSection cfgData = Main.playerData.getConfigurationSection("Players");
		
		//Load all player's scores
		for(String player : cfgData.getKeys(false))
		{
			if(cfgData.isSet(player+".Points"))
				if(cfgData.getInt(player+".Points") > 0) 
					scoreCache.put(player, cfgData.getInt(player+".Points"));
		}
		
		resortPositions();
	}
	
	
	public static ArrayList<String> highScore() {
		
		if(scoreCache == null || positions == null) loadCache();
		
		ArrayList<String> players = new ArrayList<String>();
		
		for(int i = 0; i < 10; i++){
			if(positions.size() <= i) break;
			players.add(positions.get(i));
		}
		
		return players;
	}

	public static void sendHighscore(final CommandSender s) {
		if (s == null)
			return;
		Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, new Runnable() {
			@Override
			public void run() {
				//if (s instanceof Player && ((Player) s).isOnline())
					//return;
				s.sendMessage(ChatColor.AQUA + "========= Highscores =========");
				int x = 0;
				ArrayList<String> hsList = Points.highScore();
				if (hsList != null)
					for (String name : hsList) {
						x++;
						s.sendMessage(ChatColor.DARK_PURPLE + "" + x + ": " + ChatColor.AQUA + name + ChatColor.GREEN
								+ " - " + ChatColor.BLUE + Points.get(name) + " Points");
					}
			}
		});
	}

	public static void updateSigns(final ArrayList<String> scores) {

		Main.plugin.getServer().getScheduler().callSyncMethod(Main.plugin, new Callable<Boolean>() {
			@Override
			public Boolean call() {
				if (lastScores != null)
					if (lastScores == scores)
						return true;
				lastScores = scores;
				if (scores == null)
					return true;
				for (Entry<Integer, ArrayList<Location>> entry : Main.signs.entrySet())
					for (Location loc : entry.getValue()) {
						if (loc.getBlock().getState() instanceof Sign) {
							Sign sign = (Sign) loc.getBlock().getState();
							
							String placeModifier = "";
							placeModifier = getPlaceSuffix(entry.getKey());
							
							sign.setLine(0, ChatColor.DARK_RED + "" + entry.getKey() + placeModifier);
							if (scores.size() >= entry.getKey() && scores.get(entry.getKey() - 1) != null) {
								String name = scores.get(entry.getKey() - 1);
								if (name.length() > 15)
									name = name.substring(0, 14);
								if (name.length() < 14)
									name = ChatColor.DARK_BLUE + name;
								sign.setLine(1, name);
								sign.setLine(2, ChatColor.GREEN + "Points:");
								sign.setLine(3, ChatColor.YELLOW + "" + get(scores.get(entry.getKey() - 1)));
							} else {
								sign.setLine(1, "none");
								sign.setLine(2, "");
								sign.setLine(3, "");
							}
							sign.update();
						}
					}
				return true;
			}
		});
	}

	public static void setScoreSigns() {
		Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, new Runnable() {
			@Override
			public void run() {
				Main.signs = new HashMap<Integer, ArrayList<Location>>();
				Boolean save = false;
				for (String x : Main.signData.getConfigurationSection("Signs").getKeys(false))
					if (Main.signData.get("Signs." + x) != null
							&& Bukkit.getWorld(Main.signData.getString("Signs." + x + ".world")) != null) {
						Location loc = new Location(Bukkit.getWorld(Main.signData.getString("Signs." + x + ".world")),
								Main.signData.getInt("Signs." + x + ".x"), Main.signData.getInt("Signs." + x + ".y"),
								Main.signData.getInt("Signs." + x + ".z"));
						if (loc.getBlock().getState() instanceof Sign) {
							ArrayList<Location> locs = new ArrayList<Location>();
							if (Main.signs.containsKey(Main.signData.getInt("Signs." + x + ".place"))) {
								locs = Main.signs.get(Main.signData.getInt("Signs." + x + ".place"));
								Main.signs.remove(Main.signData.getInt("Signs." + x + ".place"));
							}
							locs.add(loc);
							Main.signs.put(Main.signData.getInt("Signs." + x + ".place"), locs);
						} else {
							save = true;
							Main.signData.set("Signs." + x, null);
						}
					}
				if (save)
					Main.signData.save();
			}
		});
	}
	
	public static String getPlaceSuffix(int place)
	{
		int digit = place % 10;
		int two_digit = place % 100;
		
		if(digit == 1 && two_digit != 11)
			return "st";
		else if(digit == 2 && two_digit != 12)
			return "nd";
		else if(digit == 3 && two_digit != 13)
			return "rd";
		else 
			return "th";
	}
}
