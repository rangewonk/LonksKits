package me.MnMaxon.LonksKits;

import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class SuperYaml {
	private String fileLocation;
	public YamlConfiguration config;

	public SuperYaml(String fileLocation) {
		this.fileLocation = fileLocation;
		reload();
	}

	public void reload() {
		config = Config.Load(fileLocation);
	}

	public void save() {
		Config.Save(config, fileLocation);
	}

	public void set(String path, Object value) {
		config.set(path, value);
	}

	public Object get(String path) {
		return config.get(path);
	}

	public int getInt(String path) {
		return config.getInt(path);
	}

	public Boolean getBoolean(String path) {
		return config.getBoolean(path);
	}

	public double getDouble(String path) {
		return config.getDouble(path);
	}

	public ItemStack getItemStack(String path) {
		return config.getItemStack(path);
	}

	public ConfigurationSection getConfigurationSection(String path) {
		return config.getConfigurationSection(path);
	}

	public String getString(String path) {
		return config.getString(path);
	}

	public List<String> getStringList(String path) {
		return config.getStringList(path);
	}
}
