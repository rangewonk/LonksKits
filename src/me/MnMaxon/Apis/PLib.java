package me.MnMaxon.Apis;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.MnMaxon.LonksKits.FakeItem;
import me.MnMaxon.LonksKits.Locations;
import me.MnMaxon.LonksKits.Main;
import me.MnMaxon.LonksKits.MetaLists;
import me.MnMaxon.Kits.Casper;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.NBTTagCompound;
import net.minecraft.server.v1_7_R4.NBTTagList;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_7_R4.PacketPlayOutPlayerInfo;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.Packets;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ConnectionSide;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers.EntityUseAction;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

public class PLib {
	private static Field cC;

	public static boolean hasIntersection(Vector3D p1, Vector3D p2, Vector3D min, Vector3D max) {
		final double epsilon = 0.0001f;

		Vector3D d = p2.subtract(p1).multiply(0.5);
		Vector3D e = max.subtract(min).multiply(0.5);
		Vector3D c = p1.add(d).subtract(min.add(max).multiply(0.5));
		Vector3D ad = d.abs();

		if (Math.abs(c.x) > e.x + ad.x)
			return false;
		if (Math.abs(c.y) > e.y + ad.y)
			return false;
		if (Math.abs(c.z) > e.z + ad.z)
			return false;

		if (Math.abs(d.y * c.z - d.z * c.y) > e.y * ad.z + e.z * ad.y + epsilon)
			return false;
		if (Math.abs(d.z * c.x - d.x * c.z) > e.z * ad.x + e.x * ad.z + epsilon)
			return false;
		if (Math.abs(d.x * c.y - d.y * c.x) > e.x * ad.y + e.y * ad.x + epsilon)
			return false;

		return true;
	}

	public static ItemStack removeAttributes(ItemStack i) {
		if (i == null) {
			return i;
		}
		if (i.getType() == Material.BOOK_AND_QUILL) {
			return i;
		}
		ItemStack item = i.clone();
		net.minecraft.server.v1_7_R4.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
		NBTTagCompound tag;
		if (!nmsStack.hasTag()) {
			tag = new NBTTagCompound();
			nmsStack.setTag(tag);
		} else {
			tag = nmsStack.getTag();
		}
		NBTTagList am = new NBTTagList();
		tag.set("AttributeModifiers", am);
		nmsStack.setTag(tag);
		return CraftItemStack.asCraftMirror(nmsStack);
	}

	public static ItemStack removeUnbreaking(ItemStack i) {
		ItemStack stack = i.clone();
		if (stack == null)
			return i;

		stack.removeEnchantment(Enchantment.DURABILITY);
		return stack;
	}

	public static void toggleVisibility(Player target) {
		if (MetaLists.PLAYERS_INVISIBLE.contains(target))
			toggleVisibility(target, false);
		else
			toggleVisibility(target, true);
	}

	/*public static void toggleVisibility(final Player target, boolean invisible) {
		if (invisible) {
			target.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 15 * 20, 1));
			MetaLists.PLAYERS_INVISIBLE.add(target);
			Main.ghostManager.removePlayer(target);

			PacketPlayOutEntityEquipment pack1 = new PacketPlayOutEntityEquipment(target.getEntityId(), 1, null);
			PacketPlayOutEntityEquipment pack2 = new PacketPlayOutEntityEquipment(target.getEntityId(), 2, null);
			PacketPlayOutEntityEquipment pack3 = new PacketPlayOutEntityEquipment(target.getEntityId(), 3, null);
			PacketPlayOutEntityEquipment pack4 = new PacketPlayOutEntityEquipment(target.getEntityId(), 4, null);
			for (Player player : target.getWorld().getPlayers()) {
				if (!player.equals(target)) {
					((CraftPlayer) player).getHandle().playerConnection.sendPacket(pack1);
					((CraftPlayer) player).getHandle().playerConnection.sendPacket(pack2);
					((CraftPlayer) player).getHandle().playerConnection.sendPacket(pack3);
					((CraftPlayer) player).getHandle().playerConnection.sendPacket(pack4);
				}*/
	public static void toggleVisibility(final Player target, boolean invisible) {
		if (invisible) {
			MetaLists.PLAYERS_INVISIBLE.add(target);
			Main.ghostManager.removePlayer(target);

			for (Player player : target.getWorld().getPlayers()) {
				if (!player.equals(target)) {
		//			hidePlayer(player, target);
					player.hidePlayer(target);
				}
			}
		} else {
			MetaLists.PLAYERS_INVISIBLE.remove(target);
			Main.ghostManager.addPlayer(target);
			if (MetaLists.PLAYERS.contains(target) && MetaLists.PLAYERS.get(target) != null && target.isValid()) {

				for (Player player : target.getWorld().getPlayers())
					if (!player.equals(target)) {
                                            player.showPlayer(target);
					}
			}
		}
	}/*
}
		} else {
			target.removePotionEffect(PotionEffectType.INVISIBILITY);
			MetaLists.PLAYERS_INVISIBLE.remove(target);
			Main.ghostManager.addPlayer(target);
			if (MetaLists.PLAYERS.contains(target) && MetaLists.PLAYERS.get(target) != null && target.isValid()) {

				PacketPlayOutEntityEquipment pack1 = new PacketPlayOutEntityEquipment(target.getEntityId(), 1,
						CraftItemStack.asNMSCopy(target.getInventory().getBoots()));
				PacketPlayOutEntityEquipment pack2 = new PacketPlayOutEntityEquipment(target.getEntityId(), 2,
						CraftItemStack.asNMSCopy(target.getInventory().getLeggings()));
				PacketPlayOutEntityEquipment pack3 = new PacketPlayOutEntityEquipment(target.getEntityId(), 3,
						CraftItemStack.asNMSCopy(target.getInventory().getChestplate()));
				PacketPlayOutEntityEquipment pack4 = new PacketPlayOutEntityEquipment(target.getEntityId(), 4,
						CraftItemStack.asNMSCopy(target.getInventory().getHelmet()));
				for (Player player : target.getWorld().getPlayers())
					if (!player.equals(target)) {
						((CraftPlayer) player).getHandle().playerConnection.sendPacket(pack1);
						((CraftPlayer) player).getHandle().playerConnection.sendPacket(pack2);
						((CraftPlayer) player).getHandle().playerConnection.sendPacket(pack3);
						((CraftPlayer) player).getHandle().playerConnection.sendPacket(pack4);
					}
			}
		}
	 } */

	public static void setupProtatocalLib() {
		Main.protocolManager = ProtocolLibrary.getProtocolManager();
		Main.protocolManager.getAsynchronousManager()
				.registerAsyncHandler(new PacketAdapter(Main.plugin,  PacketType.Play.Client.ARM_ANIMATION) {
					@Override
					public void onPacketReceiving(PacketEvent e) {
						if (e == null || e.getPlayer() == null || e.getPlayer().isDead())
							return;
						final int ATTACK_REACH = 4;
						int animationType = e.getPacket().getIntegers().read(1);

						// Only consider swing arm animation
						if (animationType != 1) {
							return;
						}

						Player observer = e.getPlayer();
						Location observerPos = observer.getEyeLocation();
						Vector3D observerDir = new Vector3D(observerPos.getDirection());

						Vector3D observerStart = new Vector3D(observerPos);
						Vector3D observerEnd = observerStart.add(observerDir.multiply(ATTACK_REACH));

						Player hit = null;

						// Get nearby entities
						for (Player target : Main.protocolManager.getEntityTrackers(observer)) {
							// No need to simulate an attack if the player is
							// already visible
							if (!observer.canSee(target)) {
								// Bounding box of the given player
								Vector3D targetPos = new Vector3D(target.getLocation());
								Vector3D minimum = targetPos.add(-0.5, 0, -0.5);
								Vector3D maximum = targetPos.add(0.5, 1.67, 0.5);

								if (PLib.hasIntersection(observerStart, observerEnd, minimum, maximum)) {
									if (hit == null
											|| hit.getLocation().distanceSquared(observerPos) > target.getLocation()
													.distanceSquared(observerPos)) {
										hit = target;
									}
								}
							}
						}

						// Simulate a hit against the closest player
						if (hit != null) {
					/*		PacketContainer useEntity = Main.protocolManager.createPacket(
									PacketType.Play.Client.USE_ENTITY, false);
							useEntity.getIntegers().write(0, hit.getEntityId());
		
							try {
								useEntity.getEntityUseActions().write(0, EntityUseAction.ATTACK);
								Main.protocolManager.recieveClientPacket(e.getPlayer(), useEntity);
							} catch (Exception ex) {
								ex.printStackTrace();
							}*/
							@SuppressWarnings("deprecation")
							//PacketContainer useEntity = Main.protocolManager.createPacket(PacketType.Play.Client.USE_ENTITY, false);
							PacketContainer useEntity = Main.protocolManager.createPacket(Packets.Client.USE_ENTITY, false);

							useEntity.getIntegers().write(0, hit.getEntityId());							
						//	useEntity.getIntegers().
							//	write(0, observer.getEntityId()).
								//write(1, hit.getEntityId()).
							      //  write(2, 1 /* LEFT_CLICK */);
														
							try {
							    //useEntity.getEntityUseActions().write(0, EntityUseAction.ATTACK);
							    Main.protocolManager.recieveClientPacket(e.getPlayer(), useEntity);
							} catch (Exception ex) {
							    ex.printStackTrace();
							}
						}
					}

				}).syncStart();
		try {
			cC = EntityPlayer.class.getDeclaredField("containerCounter");
			cC.setAccessible(true);
		} catch (Exception e) {
			Main.plugin.disable(true);
		}

		Set<PacketType> packets = new HashSet<PacketType>();
		packets.add(PacketType.Play.Server.SET_SLOT);
		packets.add(PacketType.Play.Server.WINDOW_ITEMS);
		packets.add(PacketType.Play.Server.CUSTOM_PAYLOAD);

		ProtocolLibrary.getProtocolManager().addPacketListener(
				new PacketAdapter(Main.plugin, PacketType.Play.Server.CHAT) {
					@Override
					public void onPacketSending(PacketEvent e) {
						WrappedChatComponent chat = e.getPacket().getChatComponents().read(0);
						// Note that you can only get the JSON representation
						// (currently).
						if (Locations.gameWorld != null && e.getPlayer().getWorld().equals(Locations.gameWorld))
							if (chat.getJson().toString().contains("lose any power due to the world")) {
								e.setCancelled(true);
							} else if (chat.getJson().toString().toLowerCase()
									.contains("you have reached the edge of this world")) {
								MetaLists.TP_AROUND.add(e.getPlayer());
								e.getPlayer().teleport(Locations.spawn);
							}
					}
				});
		ProtocolLibrary.getProtocolManager().addPacketListener(
				new PacketAdapter(Main.plugin, PacketType.Play.Server.ENTITY_EQUIPMENT) {
					@Override
					public void onPacketSending(PacketEvent event) {
						PacketContainer packet = event.getPacket();

						ItemStack stack = packet.getItemModifier().read(0);
						Entity ent = packet.getEntityModifier(event).read(0);
						if (MetaLists.PLAYERS.get(ent) == null)
							return;
						for (Entry<Class<?>, FakeItem> entry : Main.fakeItems.entrySet())
							if (MetaLists.PLAYERS.get(ent).getClass().equals(entry.getKey()))
								if (stack != null && stack.getType().equals(entry.getValue().from)) {
									event.setPacket(packet);
									stack = packet.getItemModifier().read(0);
									stack.setType(entry.getValue().to);
									packet.getItemModifier().write(0, stack);
								}
						if (ent != null && MetaLists.PLAYERS_INVISIBLE.contains(ent)) {
							if (stack != null
									&& (stack.getType().name().toLowerCase().contains("cap")
											|| stack.getType().name().toLowerCase().contains("helm")
											|| stack.getType().name().toLowerCase().contains("chestp")
											|| stack.getType().name().toLowerCase().contains("legg")
											|| stack.getType().name().toLowerCase().contains("boots") || MetaLists.PLAYERS
												.get(ent) instanceof Casper)) {
								if (packet != null) {
									event.setPacket(packet);
									stack = packet.getItemModifier().read(0);
									packet.getItemModifier().write(0, null);
								}
							}
						}
					}
				});

		Main.protocolManager.addPacketListener(new PacketAdapter(Main.plugin, PacketType.Play.Server.WINDOW_ITEMS) {
			@Override
			public void onPacketSending(PacketEvent event) {
				Player p = event.getPlayer();
				if (!MetaLists.PLAYERS.contains(p))
					return;
				PacketContainer packet = event.getPacket();
				ItemStack[] elements = packet.getItemArrayModifier().read(0);
				if (p.getOpenInventory() != null && p.getOpenInventory().getTitle() != null
						&& ChatColor.stripColor(p.getOpenInventory().getTitle()).equalsIgnoreCase("Select Kit")) {
					try {
						ItemStack[] read = packet.getItemArrayModifier().read(0);
						for (int i = 0; i < read.length; i++) {
							if (read[i] != null)
								read[i] = PLib.removeAttributes(read[i]);
						}
						packet.getItemArrayModifier().write(0, read);
					} catch (Exception ex) {
						Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, "1)" + ex);
					}
				}
				if (MetaLists.PLAYERS_INVISIBLE.contains(p))
					try {
						ItemStack[] read = packet.getItemArrayModifier().read(0);
						for (int i = 0; i < read.length; i++) {
							if (read[i] != null) {
								if ((read[i].getType().name().toLowerCase().contains("cap")
										|| read[i].getType().name().toLowerCase().contains("helm")
										|| read[i].getType().name().toLowerCase().contains("chestp")
										|| read[i].getType().name().toLowerCase().contains("legg") || read[i].getType()
										.name().toLowerCase().contains("boots")))
									read[i] = null;
							}
						}
						packet.getItemArrayModifier().write(0, read);
					} catch (Exception ex) {
						Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, "1)" + p);
					}
				for (int i = 0; i < elements.length; i++) {
					for (Entry<Class<?>, FakeItem> entry : Main.fakeItems.entrySet())
						if (MetaLists.PLAYERS.contains(p) && MetaLists.PLAYERS.get(p) != null
								&& MetaLists.PLAYERS.contains(p) && MetaLists.PLAYERS.get(p) != null
								&& MetaLists.PLAYERS.get(p).getClass().equals(entry.getKey()))
							if (elements[i] != null && elements[i].getType().equals(entry.getValue().from)) {
								ItemStack stack = elements[i];
								stack.setType(entry.getValue().to);
								i = 10000;
							}
				}
			}
		});
		Main.protocolManager.addPacketListener(new PacketAdapter(Main.plugin, PacketType.Play.Server.SET_SLOT) {
			@Override
			public void onPacketSending(PacketEvent event) {
				PacketContainer packet = event.getPacket();
				ItemStack stack = packet.getItemModifier().read(0);
				if (!MetaLists.PLAYERS.contains(event.getPlayer()) || MetaLists.PLAYERS.get(event.getPlayer()) == null)
					return;
				for (Entry<Class<?>, FakeItem> entry : Main.fakeItems.entrySet())
					if (MetaLists.PLAYERS.get(event.getPlayer()).getClass().equals(entry.getKey()))
						if (stack != null && stack.getType().equals(entry.getValue().from)) {
							event.setPacket(packet);
							stack = packet.getItemModifier().read(0);
							stack.setType(entry.getValue().to);
							packet.getItemModifier().write(0, stack);
						}
			}
		});
	}

	public static void removeArrows(Player p) {
		((CraftPlayer) p).getHandle().getDataWatcher().watch(9, (byte) 0);
	}
	/*public static void hidePlayer(Player hiding, Player from) {
	    from.hidePlayer(hiding);
	    EntityPlayer nmsFrom = ((CraftPlayer) from).getHandle();
	    EntityPlayer nmsHiding = ((CraftPlayer) hiding).getHandle();
	    nmsFrom.playerConnection.sendPacket(new PacketPlayOutPlayerInfo(nmsHiding.getName(), true, 10));
	}*/
}