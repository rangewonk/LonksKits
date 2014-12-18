package me.MnMaxon.LonksKits;

import me.MnMaxon.Kits.Blink;
import me.MnMaxon.Kits.Chomp;

import org.bukkit.Material;

public class FakeItem {
	public Material from;
	public Material to;

	public FakeItem(Material from, Material to) {
		this.from = from;
		this.to = to;
	}

	public static void setupFakeItems() {
		Main.fakeItems.put(Blink.class, new FakeItem(Material.COAL, Material.REDSTONE_TORCH_ON));
		Main.fakeItems.put(Chomp.class, new FakeItem(Material.LEATHER_HELMET, Material.CHEST));
	}
}
