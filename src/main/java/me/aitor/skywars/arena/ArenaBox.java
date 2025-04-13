package me.aitor.skywars.arena;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class ArenaBox {
   private final Location base;
   private final World world;
   private int x;
   private int y;
   private int z;

   public ArenaBox(Location var1) {
      this.base = var1;
      this.world = var1.getWorld();
   }

   public void setBox(int var1, int var2) {
      this.x = this.base.getBlockX();
      this.y = this.base.getBlockY();
      this.z = this.base.getBlockZ();
      Material var3 = Material.getMaterial(var1);
      this.world.getBlockAt(this.x, this.y - 1, this.z).setType(var3);
      this.world.getBlockAt(this.x, this.y - 1, this.z).setData((byte)var2);
      this.world.getBlockAt(this.x, this.y, this.z - 1).setType(var3);
      this.world.getBlockAt(this.x, this.y, this.z - 1).setData((byte)var2);
      this.world.getBlockAt(this.x, this.y + 1, this.z - 1).setType(var3);
      this.world.getBlockAt(this.x, this.y + 1, this.z - 1).setData((byte)var2);
      this.world.getBlockAt(this.x, this.y + 2, this.z - 1).setType(var3);
      this.world.getBlockAt(this.x, this.y + 2, this.z - 1).setData((byte)var2);
      this.world.getBlockAt(this.x, this.y, this.z + 1).setType(var3);
      this.world.getBlockAt(this.x, this.y, this.z + 1).setData((byte)var2);
      this.world.getBlockAt(this.x, this.y + 1, this.z + 1).setType(var3);
      this.world.getBlockAt(this.x, this.y + 1, this.z + 1).setData((byte)var2);
      this.world.getBlockAt(this.x, this.y + 2, this.z + 1).setType(var3);
      this.world.getBlockAt(this.x, this.y + 2, this.z + 1).setData((byte)var2);
      this.world.getBlockAt(this.x + 1, this.y, this.z).setType(var3);
      this.world.getBlockAt(this.x + 1, this.y, this.z).setData((byte)var2);
      this.world.getBlockAt(this.x + 1, this.y + 1, this.z).setType(var3);
      this.world.getBlockAt(this.x + 1, this.y + 1, this.z).setData((byte)var2);
      this.world.getBlockAt(this.x + 1, this.y + 2, this.z).setType(var3);
      this.world.getBlockAt(this.x + 1, this.y + 2, this.z).setData((byte)var2);
      this.world.getBlockAt(this.x - 1, this.y, this.z).setType(var3);
      this.world.getBlockAt(this.x - 1, this.y, this.z).setData((byte)var2);
      this.world.getBlockAt(this.x - 1, this.y + 1, this.z).setType(var3);
      this.world.getBlockAt(this.x - 1, this.y + 1, this.z).setData((byte)var2);
      this.world.getBlockAt(this.x - 1, this.y + 2, this.z).setType(var3);
      this.world.getBlockAt(this.x - 1, this.y + 2, this.z).setData((byte)var2);
      this.world.getBlockAt(this.x, this.y + 3, this.z).setType(Material.AIR);
   }

   public Location getLocation() {
      return this.base;
   }

   public void removeBase() {
      this.world.getBlockAt(this.x, this.y - 1, this.z).setType(Material.AIR);
   }

   public void removeAll() {
      Material var1 = Material.AIR;
      this.world.getBlockAt(this.x, this.y - 1, this.z).setType(var1);
      this.world.getBlockAt(this.x, this.y, this.z - 1).setType(var1);
      this.world.getBlockAt(this.x, this.y + 1, this.z - 1).setType(var1);
      this.world.getBlockAt(this.x, this.y + 2, this.z - 1).setType(var1);
      this.world.getBlockAt(this.x, this.y, this.z + 1).setType(var1);
      this.world.getBlockAt(this.x, this.y + 1, this.z + 1).setType(var1);
      this.world.getBlockAt(this.x, this.y + 2, this.z + 1).setType(var1);
      this.world.getBlockAt(this.x + 1, this.y, this.z).setType(var1);
      this.world.getBlockAt(this.x + 1, this.y + 1, this.z).setType(var1);
      this.world.getBlockAt(this.x + 1, this.y + 2, this.z).setType(var1);
      this.world.getBlockAt(this.x - 1, this.y, this.z).setType(var1);
      this.world.getBlockAt(this.x - 1, this.y + 1, this.z).setType(var1);
      this.world.getBlockAt(this.x - 1, this.y + 2, this.z).setType(var1);
      this.world.getBlockAt(this.x, this.y + 3, this.z).setType(var1);
   }
}
