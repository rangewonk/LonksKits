package me.MnMaxon.LonksKits;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

public class CloneBlock {
	public Material material;
	public BlockState state;

	public CloneBlock(Block b) {
		state = b.getState();
		material = b.getType();
	}

	public CloneBlock(Material material) {
		this.material = material;
		state = null;
	}

	@SuppressWarnings("deprecation")
	public static void set(Location loc, CloneBlock cb) {
		if (cb.material.equals(Material.ENDER_PORTAL_FRAME))
			return;
		loc.getBlock().setType(cb.material);
		if (cb.state != null)
			loc.getBlock().setData(cb.state.getRawData());
	}
}
