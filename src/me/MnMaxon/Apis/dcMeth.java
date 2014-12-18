package me.MnMaxon.Apis;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import pgDev.bukkit.DisguiseCraft.DisguiseCraft;
import pgDev.bukkit.DisguiseCraft.api.DisguiseCraftAPI;
import pgDev.bukkit.DisguiseCraft.disguise.Disguise;
import pgDev.bukkit.DisguiseCraft.disguise.DisguiseType;

class dcMeth {

	private static DisguiseCraftAPI dcApi;

	static void setup() {
		dcApi = DisguiseCraft.getAPI();
	}

	static void disguise(Player p, EntityType entType) {
		dcApi.changePlayerDisguise(p, new Disguise(dcApi.newEntityID(), getType(entType)));
	}

	static void unDisguise(Player p) {
		dcApi.undisguisePlayer(p);
	}

	static DisguiseType getType(EntityType entType) {
		return DisguiseType.fromString(entType.name().replace("_", ""));
	}

	static boolean isDisguised(Player p) {
		return dcApi.isDisguised(p);
	}

	static EntityType getDisguiseType(Player p) {
		return EntityType.valueOf(dcApi.getDisguise(p).type.name());
	}
}
