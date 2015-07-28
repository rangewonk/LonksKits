package me.MnMaxon.LonksKits;

import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.Metadatable;

public enum MetaLists {
	TP_AROUND("tpAround"), TP_AROUND_PLOT("Plotme"),

	CAN_FLY("canFly"), CAN_JUMP("canJump"),

	KICK_LIST("kickList"),

	CREEPER_JUMP("creeperJump"),

	BYPASS_SAFEZONE("safezoneBypass"), BYPASS_BUILD("buildBypass"),

	HORSE_AROUND("horseAround"),

	IGNORE_DAMAGE_FALL("ignoreFallDamage"), IGNORE_DAMAGE_EXPLOSION("ignoreExplosion"),

	CHARGE("charge"),

	FREEZE("freeze"),

	GROUND_BOOST("groundBoost"),

	PLAYERS_INVISIBLE("invisiblePlayers"), PLAYERS("kit"),

	SWITCHERS("switchers"),

	DOUBLE_DAMAGE("doubleDamage"),

	FIREBALLS("fireballs"),

	FLUNG("flung"),

	NO_LAND("noLand"), NO_PICKUP("noPickup"),

	REVENGE_MOB("revenge"),

	SPAWN_TO("toSpawn"), SPAWN_ANTI("antiSpawn"),

	ONE_V_ONE_FIGHT("OneVOneMe"), ONE_V_ONE_REQUEST("ONE_V_ONE_REQUEST"), ONE_V_ONE_WAITING_RESPONSE("ONE_V_ONE_PLAYER"),
	
	IGNORE_DAMAGE_LIGHTNING("ignoreLightning"),

	PIGGY("PIGGY");

	private String name;

	MetaLists(String name) {
		this.name = name;
	}

	public Boolean contains(Metadatable md) {
		if (md == null)
			return false;
		return md.hasMetadata(getName());
	}

	public Object get(Metadatable md) {
		if (md == null || !contains(md))
			return null;
		return md.getMetadata(getName()).get(0).value();
	}

	public void add(Metadatable md) {
		if (md == null)
			return;
		add(md, true);
	}

	public void add(Metadatable md, Object object) {
		if (md == null)
			return;
		md.setMetadata(getName(), new FixedMetadataValue(Main.plugin, object));
	}

	public void remove(Metadatable md) {
		if (md == null)
			return;
		md.removeMetadata(getName(), Main.plugin);
	}

	public String getName() {
		return name;
	}
}