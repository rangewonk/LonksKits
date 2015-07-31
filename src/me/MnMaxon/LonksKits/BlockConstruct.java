package me.MnMaxon.LonksKits;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class BlockConstruct {
	
	public static boolean CanBuildBlock(Block b, Block origin, Material avoid)
	{
		if ( Locations.inSafe(b.getLocation())
				|| b.getRelative(BlockFace.NORTH).getType().equals(Material.CACTUS)
				|| b.getRelative(BlockFace.SOUTH).getType().equals(Material.CACTUS)
				|| b.getRelative(BlockFace.EAST).getType().equals(Material.CACTUS)
				|| b.getRelative(BlockFace.WEST).getType().equals(Material.CACTUS) 
				|| (b.getType().equals(avoid) && !b.equals(origin)))
			return false;
		return true;
	}
	public static boolean CanBuildBlock(Block b, Material avoid)
	{
		if ( Locations.inSafe(b.getLocation())
				|| b.getRelative(BlockFace.NORTH).getType().equals(Material.CACTUS)
				|| b.getRelative(BlockFace.SOUTH).getType().equals(Material.CACTUS)
				|| b.getRelative(BlockFace.EAST).getType().equals(Material.CACTUS)
				|| b.getRelative(BlockFace.WEST).getType().equals(Material.CACTUS) 
				|| b.getType().equals(avoid))
			return false;
		return true;
	}
	
	public static boolean CanBuildWall(Player player, World w, Block origin, Material wallType, int xStart, int xEnd, int yStart, int yEnd, int zStart, int zEnd)
	{		
		for(int i = xStart; i <= xEnd; i++)
			for(int j = yStart; j <= yEnd; j++)
				for(int k = zStart; k <= zEnd; k++)
				{
					if(!CanBuildBlock(w.getBlockAt(i, j, k), origin, wallType))
					{
						origin.setType(Material.AIR);
						return false;
					}
				}
		
		return true;
	}
	
	public static boolean BuildWall(Block origin, Player player, final Material wallType, long timer, final int sizeX, final int sizeY){
		
		if(wallType.equals(Material.AIR))
			return false;
		
		final int xStart, yStart, zStart;
		final int xEnd, yEnd, zEnd;
		final World w = player.getWorld();
		
		yStart = origin.getY();
		yEnd = yStart+sizeY-1;
		int direction = Math.round(player.getLocation().getYaw()/90f);
		
		if(direction % 2 == 0){ // Direction = (0 || 2)
			xStart = origin.getX()-((sizeX-1)/2);
			xEnd = xStart+sizeX-1;
			zStart = origin.getZ();
			zEnd = zStart;	
		}else{ // Direction = (1 || 3)
			zStart = origin.getZ()-((sizeX-1)/2);
			zEnd = zStart+sizeX-1;
			xStart = origin.getX();
			xEnd = xStart;
		}
		
		if(CanBuildWall(player, w, origin, wallType, xStart, xEnd, yStart, yEnd, zStart, zEnd))
		{
			for(int i = xStart; i <= xEnd; i++)
				for(int j = yStart; j <= yEnd; j++)
					for(int k = zStart; k <= zEnd; k++)
					{
						Block b = w.getBlockAt(i, j, k); 
						if(b.getType().equals(Material.AIR))
							b.setType(wallType);	
					}
			
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
				@Override
				public void run() {
					for(int i = xStart; i <= xEnd; i++)
						for(int j = yStart; j <= yEnd; j++)
							for(int k = zStart; k <= zEnd; k++)
							{
								if(w.getBlockAt(i, j, k).getType().equals(wallType))
									(w.getBlockAt(i, j, k)).setType(Material.AIR);	
							}
				}
			}, timer);
			return true;
		}else return false;
		
	}
	
	public static boolean CanBuildDome(World w, Player player, Material domeType, int xStart, int yStart, int zStart)
	{
		//Bottom
		for(int i = xStart-1; i <= xStart+1; i++)
			for(int k = zStart-1; k <= zStart+1; k++)
			{
				int j = yStart-1;
				if(!CanBuildBlock(w.getBlockAt(i, j, k), domeType)) return false;
			}
		//Top
		for(int i = xStart-1; i <= xStart+1; i++)
			for(int k = zStart-1; k <= zStart+1; k++)
			{
				int j = yStart+3;
				if(!CanBuildBlock(w.getBlockAt(i, j, k), domeType)) return false;
			}
		//Side 1
		for(int i = xStart-1; i <= xStart+1; i++)
			for(int j = yStart; j <= yStart+2; j++)
			{
				int k = zStart-2;
				if(!CanBuildBlock(w.getBlockAt(i, j, k), domeType)) return false;
			}
		//Side 3
		for(int i = xStart-1; i <= xStart+1; i++)
			for(int j = yStart; j <= yStart+2; j++)
			{
				int k = zStart+2;
				if(!CanBuildBlock(w.getBlockAt(i, j, k), domeType)) return false;
			}
		//Side 2
		for(int k = zStart-1; k <= zStart+1; k++)
			for(int j = yStart; j <= yStart+2; j++)
			{
				int i = xStart-2;
				if(!CanBuildBlock(w.getBlockAt(i, j, k), domeType)) return false;
			}
		//Side 4
		for(int k = zStart-1; k <= zStart+1; k++)
			for(int j = yStart; j <= yStart+2; j++)
			{
				int i = xStart+2;
				if(!CanBuildBlock(w.getBlockAt(i, j, k), domeType)) return false;
			}
		
		return true;
	}
	
	public static boolean BuildDome(Player player, final Material domeType, long timer)
	{
		final int xStart, yStart, zStart;
		final World w = player.getWorld();
		xStart = player.getLocation().getBlockX();
		yStart = player.getLocation().getBlockY();
		zStart = player.getLocation().getBlockZ();
		
		if(CanBuildDome(w, player, domeType, xStart, yStart, zStart))
		{
			//Bottom
			for(int i = xStart-1; i <= xStart+1; i++)
				for(int k = zStart-1; k <= zStart+1; k++)
				{
					int j = yStart-1;
					Block b = w.getBlockAt(i, j, k); 
					if(b.getType().equals(Material.AIR))
						b.setType(domeType);
				}
			//Top
			for(int i = xStart-1; i <= xStart+1; i++)
				for(int k = zStart-1; k <= zStart+1; k++)
				{
					int j = yStart+3;
					Block b = w.getBlockAt(i, j, k); 
					if(b.getType().equals(Material.AIR))
						b.setType(domeType);
				}
			//Side 1
			for(int i = xStart-1; i <= xStart+1; i++)
				for(int j = yStart; j <= yStart+2; j++)
				{
					int k = zStart-2;
					Block b = w.getBlockAt(i, j, k); 
					if(b.getType().equals(Material.AIR))
						b.setType(domeType);
				}
			//Side 3
			for(int i = xStart-1; i <= xStart+1; i++)
				for(int j = yStart; j <= yStart+2; j++)
				{
					int k = zStart+2;
					Block b = w.getBlockAt(i, j, k); 
					if(b.getType().equals(Material.AIR))
						b.setType(domeType);
				}
			//Side 2
			for(int k = zStart-1; k <= zStart+1; k++)
				for(int j = yStart; j <= yStart+2; j++)
				{
					int i = xStart-2;
					Block b = w.getBlockAt(i, j, k); 
					if(b.getType().equals(Material.AIR))
						b.setType(domeType);
				}
			//Side 4
			for(int k = zStart-1; k <= zStart+1; k++)
				for(int j = yStart; j <= yStart+2; j++)
				{
					int i = xStart+2;
					Block b = w.getBlockAt(i, j, k); 
					if(b.getType().equals(Material.AIR))
						b.setType(domeType);
				}
			
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
				@Override
				public void run() {
					//Bottom
					for(int i = xStart-1; i <= xStart+1; i++)
						for(int k = zStart-1; k <= zStart+1; k++)
						{
							int j = yStart-1;
							Block b = w.getBlockAt(i, j, k); 
							if(b.getType().equals(domeType))
								b.setType(Material.AIR);
						}
					//Top
					for(int i = xStart-1; i <= xStart+1; i++)
						for(int k = zStart-1; k <= zStart+1; k++)
						{
							int j = yStart+3;
							Block b = w.getBlockAt(i, j, k); 
							if(b.getType().equals(domeType))
								b.setType(Material.AIR);
						}
					//Side 1
					for(int i = xStart-1; i <= xStart+1; i++)
						for(int j = yStart; j <= yStart+2; j++)
						{
							int k = zStart-2;
							Block b = w.getBlockAt(i, j, k); 
							if(b.getType().equals(domeType))
								b.setType(Material.AIR);
						}
					//Side 3
					for(int i = xStart-1; i <= xStart+1; i++)
						for(int j = yStart; j <= yStart+2; j++)
						{
							int k = zStart+2;
							Block b = w.getBlockAt(i, j, k); 
							if(b.getType().equals(domeType))
								b.setType(Material.AIR);
						}
					//Side 2
					for(int k = zStart-1; k <= zStart+1; k++)
						for(int j = yStart; j <= yStart+2; j++)
						{
							int i = xStart-2;
							Block b = w.getBlockAt(i, j, k); 
							if(b.getType().equals(domeType))
								b.setType(Material.AIR);
						}
					//Side 4
					for(int k = zStart-1; k <= zStart+1; k++)
						for(int j = yStart; j <= yStart+2; j++)
						{
							int i = xStart+2;
							Block b = w.getBlockAt(i, j, k); 
							if(b.getType().equals(domeType))
								b.setType(Material.AIR);
						}
				}
			}, 200L);
			
			return true;
		}else return false;
	}
}
