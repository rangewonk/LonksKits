package me.MnMaxon.LonksKits;

import java.lang.reflect.Field;

import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityPig;
import net.minecraft.server.v1_8_R3.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R3.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_8_R3.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_8_R3.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.v1_8_R3.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;
import net.minecraft.server.v1_8_R3.World;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.util.UnsafeList;
import org.bukkit.entity.Player;

public class CustomEntityPig extends EntityPig {

	public CustomEntityPig(World world) {
		super(world);

		try {
			Field bField = PathfinderGoalSelector.class.getDeclaredField("b");
			bField.setAccessible(true);
			Field cField = PathfinderGoalSelector.class.getDeclaredField("c");
			cField.setAccessible(true);
			bField.set(goalSelector, new UnsafeList<PathfinderGoalSelector>());
			bField.set(targetSelector, new UnsafeList<PathfinderGoalSelector>());
			cField.set(goalSelector, new UnsafeList<PathfinderGoalSelector>());
			cField.set(targetSelector, new UnsafeList<PathfinderGoalSelector>());
		} catch (Exception exc) {
			exc.printStackTrace();
		}

		this.goalSelector.a(0, new PathfinderGoalFloat(this));
		this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, EntityHuman.class, 1.0D, false));
		this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
		this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, 1.0D));
		this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
		this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
		this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true));
		//this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, 0, true));
	}

	/*@Override
	public boolean n(Entity entity) {
		double range = 1.0;
		Location pigLoc = this.getBukkitEntity().getLocation();
		if (pigLoc.distance(entity.getBukkitEntity().getLocation()) > range
				|| this.getBukkitEntity().getTicksLived() < 40)
			return false;
		else {
			return explode();
		}
	}*/

	public boolean explode() {
		Location pigLoc = this.getBukkitEntity().getLocation();
		Player player = (MetaLists.PIGGY.get(this.getBukkitEntity()) == null) ? null : (Player) MetaLists.PIGGY
				.get(this.getBukkitEntity());
		for (org.bukkit.entity.Entity ent : this.getBukkitEntity().getNearbyEntities(6, 6, 6)) {
			if (MetaLists.PLAYERS.contains(ent) && Main.canDamage(ent))
				Death.update((Player) ent, player, "Pigsplosion - noboom");
		}
		pigLoc.getWorld().createExplosion(pigLoc, 4F);
		if (this.getBukkitEntity().isValid())
			this.getBukkitEntity().remove();
		return true;
	}

	/*@Override
	public boolean damageEntity(DamageSource damagesource, float f) {
		if (this.isInvulnerable()) {
			return false;
		} else {
			return super.damageEntity(damagesource, f);
		}
	}*/
}