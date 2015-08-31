package me.MnMaxon.LonksKits;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityTypes;
import net.minecraft.server.v1_8_R3.World;

public class CustomEntityType {

	@SuppressWarnings({ "unchecked" })
	public static void registerEntity(String name, int id, Class<?> customClass) {
		try {
			// Main.registeredEntities.put(EntityType.valueOf(name),
			// customClass);
			List<Map<?, ?>> dataMaps = new ArrayList<Map<?, ?>>();
			for (Field f : EntityTypes.class.getDeclaredFields()) {
				if (f.getType().getSimpleName().equals(Map.class.getSimpleName())) {
					f.setAccessible(true);
					dataMaps.add((Map<?, ?>) f.get(null));
				}
			}

			((Map<Class<? extends EntityInsentient>, String>) dataMaps.get(1)).put(
					(Class<? extends EntityInsentient>) customClass, name);
			((Map<Class<? extends EntityInsentient>, Integer>) dataMaps.get(3)).put(
					(Class<? extends EntityInsentient>) customClass, id);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void registerEntitiesNoOverride() {
		registerEntity("Pig", 90, CustomEntityPig.class);
	}

	public static org.bukkit.entity.Entity spawnMob(Class<?> clazz, Location loc) {
		try {
			Entity ent = (Entity) clazz.getConstructor(World.class).newInstance(
					((CraftWorld) loc.getWorld()).getHandle());
			ent.setLocation(loc.getX(), loc.getY(), loc.getZ(), 0F, 0F);
			return ent.getBukkitEntity();

		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			return null;
		}
	}
}