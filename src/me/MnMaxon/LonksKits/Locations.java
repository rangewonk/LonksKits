package me.MnMaxon.LonksKits;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class Locations {
	public static Location hub;
	public static Location spawn;

	public static Location loc1;
	public static Location loc2;

	public static World gameWorld;

	public static void reload() {
		hub = Hub();
		spawn = Spawn();
		gameWorld = getGameWorld();
		loc1 = Loc1();
		loc2 = Loc2();
	}

	private static World getGameWorld() {
		return Bukkit.getWorld("kitpvp");
	}

	private static Location Hub() {
		if (Main.data.get("Hub") == null || Bukkit.getWorld(Main.data.getString("Hub.World")) == null)
			return null;
		else {
			Location loc = new Location(Bukkit.getWorld(Main.data.getString("Hub.World")),
					Main.data.getDouble("Hub.X"), Main.data.getDouble("Hub.Y"), Main.data.getDouble("Hub.Z"),
					(float) Main.data.getDouble("Hub.Yaw"), (float) Main.data.getDouble("Hub.Pitch"));
			return loc.add(0, .1, 0);
		}
	}

	private static Location Spawn() {
		if (Main.data.get("Spawn") == null || Locations.getGameWorld() == null)
			return null;
		else {
			Location loc = new Location(Locations.getGameWorld(), Main.data.getDouble("Spawn.X"),
					Main.data.getDouble("Spawn.Y"), Main.data.getDouble("Spawn.Z"),
					(float) Main.data.getDouble("Spawn.Yaw"), (float) Main.data.getDouble("Spawn.Pitch"));
			return loc.add(.0, .1, .0);
		}
	}

	private static Location Loc1() {
		if (Main.data.get("1v1.Loc1") == null || Locations.getGameWorld() == null)
			return null;
		else {
			Location loc = new Location(Locations.getGameWorld(), Main.data.getDouble("1v1.Loc1.X"),
					Main.data.getDouble("1v1.Loc1.Y"), Main.data.getDouble("1v1.Loc1.Z"),
					(float) Main.data.getDouble("1v1.Loc1.Yaw"), (float) Main.data.getDouble("1v1.Loc1.Pitch"));
			return loc.add(0, .1, 0);
		}
	}

	private static Location Loc2() {
		if (Main.data.get("1v1.Loc2") == null || Locations.getGameWorld() == null)
			return null;
		else {
			Location loc = new Location(Locations.getGameWorld(), Main.data.getDouble("1v1.Loc2.X"),
					Main.data.getDouble("1v1.Loc2.Y"), Main.data.getDouble("1v1.Loc2.Z"),
					(float) Main.data.getDouble("1v1.Loc2.Yaw"), (float) Main.data.getDouble("1v1.Loc2.Pitch"));
			return loc.add(0, .1, 0);
		}
	}

	public static Location lookAt(Location loc, Location lookat) {
		// Clone the loc to prevent applied changes to the input loc
		loc = loc.clone();

		// Values of change in distance (make it relative)
		double dx = lookat.getX() - loc.getX();
		double dy = lookat.getY() - loc.getY();
		double dz = lookat.getZ() - loc.getZ();

		// Set yaw
		if (dx != 0) {
			// Set yaw start value based on dx
			if (dx < 0) {
				loc.setYaw((float) (1.5 * Math.PI));
			} else {
				loc.setYaw((float) (0.5 * Math.PI));
			}
			loc.setYaw(loc.getYaw() - (float) Math.atan(dz / dx));
		} else if (dz < 0) {
			loc.setYaw((float) Math.PI);
		}

		// Get the distance from dx/dz
		double dxz = Math.sqrt(Math.pow(dx, 2) + Math.pow(dz, 2));

		// Set pitch
		loc.setPitch((float) -Math.atan(dy / dxz));

		// Set values, convert to degrees (invert the yaw since Bukkit uses a
		// different yaw dimension format)
		loc.setYaw(-loc.getYaw() * 180f / (float) Math.PI);
		loc.setPitch(loc.getPitch() * 180f / (float) Math.PI);

		return loc;
	}

	public static boolean nearishSafe(Location loc) {
		if (Locations.getGameWorld() != null && loc.getWorld().equals(Locations.getGameWorld())
				&& Main.data.get("Safe Zone") != null
				&& loc.getBlockX() >= Main.data.getInt("Safe Zone.Low Point.X") - 3
				&& loc.getBlockZ() >= Main.data.getInt("Safe Zone.Low Point.Z") - 3
				&& loc.getBlockX() <= Main.data.getInt("Safe Zone.Max Point.X") + 3
				&& loc.getBlockZ() <= Main.data.getInt("Safe Zone.Max Point.Z") + 3)
			return true;
		return false;
	}

	public static boolean nearSafe(Location loc) {
		if (Locations.getGameWorld() != null && loc.getWorld().equals(Locations.getGameWorld())
				&& Main.data.get("Safe Zone") != null
				&& loc.getBlockX() >= Main.data.getInt("Safe Zone.Low Point.X") - 2
				&& loc.getBlockZ() >= Main.data.getInt("Safe Zone.Low Point.Z") - 2
				&& loc.getBlockX() <= Main.data.getInt("Safe Zone.Max Point.X") + 2
				&& loc.getBlockZ() <= Main.data.getInt("Safe Zone.Max Point.Z") + 2)
			return true;
		return false;
	}

	public static boolean inSafe(Location loc) {
		if (Locations.getGameWorld() != null && loc.getWorld().equals(Locations.getGameWorld())
				&& Main.data.get("Safe Zone") != null && loc.getBlockX() >= Main.data.getInt("Safe Zone.Low Point.X")
				&& loc.getBlockZ() >= Main.data.getInt("Safe Zone.Low Point.Z")
				&& loc.getBlockX() <= Main.data.getInt("Safe Zone.Max Point.X")
				&& loc.getBlockZ() <= Main.data.getInt("Safe Zone.Max Point.Z"))
			return true;
		return false;
	}

	public static boolean inSafe(Entity ent) {
		return inSafe(ent.getLocation());
	}

	static double distanceSquared(Vector from, Vector to) {
		double dx = to.getBlockX() - from.getBlockX();
		double dz = to.getBlockZ() - from.getBlockZ();
		return dx * dx + dz * dz;
	}

	public static Vector calculateVelocity(Vector from, Vector to, int heightGain) {
		double gravity = 0.115;
		int endGain = to.getBlockY() - from.getBlockY();
		double horizDist = Math.sqrt(distanceSquared(from, to));
		int gain = heightGain;
		double maxGain = gain > (endGain + gain) ? gain : (endGain + gain);
		double a = -horizDist * horizDist / (4 * maxGain);
		double b = horizDist;
		double c = -endGain;
		double slope = -b / (2 * a) - Math.sqrt(b * b - 4 * a * c) / (2 * a);
		double vy = Math.sqrt(maxGain * gravity);
		double vh = vy / slope;
		int dx = to.getBlockX() - from.getBlockX();
		int dz = to.getBlockZ() - from.getBlockZ();
		double mag = Math.sqrt(dx * dx + dz * dz);
		double dirx = dx / mag;
		double dirz = dz / mag;
		double vx = vh * dirx;
		double vz = vh * dirz;
		return new Vector(vx, vy, vz);
	}

}
