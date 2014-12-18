package me.MnMaxon.LonksKits;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config {
	public static YamlConfiguration Load(String FileLocation) {
		if (!new File(Main.dataFolder).exists())
			new File(Main.dataFolder).mkdirs();
		if (!new File(FileLocation).exists())
			try {
				new File(FileLocation).createNewFile();
				Bukkit.getServer().getLogger().log(Level.INFO, "New Config Created at: " + FileLocation);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		YamlConfiguration cfg = new YamlConfiguration();
		try {
			cfg.load(new File(FileLocation));
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
		return cfg;
	}

	public static void Save(YamlConfiguration cfg, String FileLocation) {
		try {
			cfg.save(new File(FileLocation));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
