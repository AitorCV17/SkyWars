package me.aitor.skywars.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class Cuboid implements Iterable<Block>, Cloneable, ConfigurationSerializable {
   protected final String worldName;
   protected final int x1;
   protected final int y1;
   protected final int z1;
   protected final int x2;
   protected final int y2;
   protected final int z2;

   public Cuboid(Location var1, Location var2) {
      if (!var1.getWorld().equals(var2.getWorld())) {
         throw new IllegalArgumentException("Las ubicaciones deben estar en el mismo mundo");
      } else {
         this.worldName = var1.getWorld().getName();
         this.x1 = Math.min(var1.getBlockX(), var2.getBlockX());
         this.y1 = Math.min(var1.getBlockY(), var2.getBlockY());
         this.z1 = Math.min(var1.getBlockZ(), var2.getBlockZ());
         this.x2 = Math.max(var1.getBlockX(), var2.getBlockX());
         this.y2 = Math.max(var1.getBlockY(), var2.getBlockY());
         this.z2 = Math.max(var1.getBlockZ(), var2.getBlockZ());
      }
   }

   public Cuboid(Location var1) {
      this(var1, var1);
   }

   public Cuboid(Cuboid var1) {
      this(var1.getWorld().getName(), var1.x1, var1.y1, var1.z1, var1.x2, var1.y2, var1.z2);
   }

   public Cuboid(World var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      this.worldName = var1.getName();
      this.x1 = Math.min(var2, var5);
      this.x2 = Math.max(var2, var5);
      this.y1 = Math.min(var3, var6);
      this.y2 = Math.max(var3, var6);
      this.z1 = Math.min(var4, var7);
      this.z2 = Math.max(var4, var7);
   }

   private Cuboid(String var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      this.worldName = var1;
      this.x1 = Math.min(var2, var5);
      this.x2 = Math.max(var2, var5);
      this.y1 = Math.min(var3, var6);
      this.y2 = Math.max(var3, var6);
      this.z1 = Math.min(var4, var7);
      this.z2 = Math.max(var4, var7);
   }

   public Cuboid(Map<String, Object> var1) {
      this.worldName = (String)var1.get("worldName");
      this.x1 = (Integer)var1.get("x1");
      this.x2 = (Integer)var1.get("x2");
      this.y1 = (Integer)var1.get("y1");
      this.y2 = (Integer)var1.get("y2");
      this.z1 = (Integer)var1.get("z1");
      this.z2 = (Integer)var1.get("z2");
   }

   public Map<String, Object> serialize() {
      HashMap var1 = new HashMap();
      var1.put("worldName", this.worldName);
      var1.put("x1", this.x1);
      var1.put("y1", this.y1);
      var1.put("z1", this.z1);
      var1.put("x2", this.x2);
      var1.put("y2", this.y2);
      var1.put("z2", this.z2);
      return var1;
   }

   public Location getLowerNE() {
      return new Location(this.getWorld(), (double)this.x1, (double)this.y1, (double)this.z1);
   }

   public Location getUpperSW() {
      return new Location(this.getWorld(), (double)this.x2, (double)this.y2, (double)this.z2);
   }

   public List<Block> getBlocks() {
      Iterator var1 = this.iterator();
      ArrayList var2 = new ArrayList();

      while(var1.hasNext()) {
         var2.add(var1.next());
      }

      return var2;
   }

   public Location getCenter() {
      int var1 = this.getUpperX() + 1;
      int var2 = this.getUpperY() + 1;
      int var3 = this.getUpperZ() + 1;
      return new Location(this.getWorld(), (double)this.getLowerX() + (double)(var1 - this.getLowerX()) / 2.0D, (double)this.getLowerY() + (double)(var2 - this.getLowerY()) / 2.0D, (double)this.getLowerZ() + (double)(var3 - this.getLowerZ()) / 2.0D);
   }

   public World getWorld() {
      World var1 = Bukkit.getWorld(this.worldName);
      if (var1 == null) {
         throw new IllegalStateException("El mundo '" + this.worldName + "' no está cargado");
      } else {
         return var1;
      }
   }

   public int getSizeX() {
      return this.x2 - this.x1 + 1;
   }

   public int getSizeY() {
      return this.y2 - this.y1 + 1;
   }

   public int getSizeZ() {
      return this.z2 - this.z1 + 1;
   }

   public int getLowerX() {
      return this.x1;
   }

   public int getLowerY() {
      return this.y1;
   }

   public int getLowerZ() {
      return this.z1;
   }

   public int getUpperX() {
      return this.x2;
   }

   public int getUpperY() {
      return this.y2;
   }

   public int getUpperZ() {
      return this.z2;
   }

   public Block[] corners() {
      Block[] var1 = new Block[8];
      World var2 = this.getWorld();
      var1[0] = var2.getBlockAt(this.x1, this.y1, this.z1);
      var1[1] = var2.getBlockAt(this.x1, this.y1, this.z2);
      var1[2] = var2.getBlockAt(this.x1, this.y2, this.z1);
      var1[3] = var2.getBlockAt(this.x1, this.y2, this.z2);
      var1[4] = var2.getBlockAt(this.x2, this.y1, this.z1);
      var1[5] = var2.getBlockAt(this.x2, this.y1, this.z2);
      var1[6] = var2.getBlockAt(this.x2, this.y2, this.z1);
      var1[7] = var2.getBlockAt(this.x2, this.y2, this.z2);
      return var1;
   }

   public Cuboid expand(Cuboid.CuboidDirection var1, int var2) {
      switch(var1) {
         case North:
            return new Cuboid(this.worldName, this.x1 - var2, this.y1, this.z1, this.x2, this.y2, this.z2);
         case South:
            return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2 + var2, this.y2, this.z2);
         case East:
            return new Cuboid(this.worldName, this.x1, this.y1, this.z1 - var2, this.x2, this.y2, this.z2);
         case West:
            return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2, this.y2, this.z2 + var2);
         case Down:
            return new Cuboid(this.worldName, this.x1, this.y1 - var2, this.z1, this.x2, this.y2, this.z2);
         case Up:
            return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2, this.y2 + var2, this.z2);
         default:
            throw new IllegalArgumentException("Dirección inválida " + var1);
      }
   }

   public Cuboid shift(Cuboid.CuboidDirection var1, int var2) {
      return this.expand(var1, var2).expand(var1.opposite(), -var2);
   }

   public Cuboid outset(Cuboid.CuboidDirection var1, int var2) {
      Cuboid var3;
      switch(var1) {
         case Horizontal:
            var3 = this.expand(Cuboid.CuboidDirection.North, var2).expand(Cuboid.CuboidDirection.South, var2).expand(Cuboid.CuboidDirection.East, var2).expand(Cuboid.CuboidDirection.West, var2);
            break;
         case Vertical:
            var3 = this.expand(Cuboid.CuboidDirection.Down, var2).expand(Cuboid.CuboidDirection.Up, var2);
            break;
         case Both:
            var3 = this.outset(Cuboid.CuboidDirection.Horizontal, var2).outset(Cuboid.CuboidDirection.Vertical, var2);
            break;
         default:
            throw new IllegalArgumentException("Dirección inválida " + var1);
      }

      return var3;
   }

   public Cuboid inset(Cuboid.CuboidDirection var1, int var2) {
      return this.outset(var1, -var2);
   }

   public boolean contains(int var1, int var2, int var3) {
      return var1 >= this.x1 && var1 <= this.x2 && var2 >= this.y1 && var2 <= this.y2 && var3 >= this.z1 && var3 <= this.z2;
   }

   public boolean contains(Block var1) {
      return this.contains(var1.getLocation());
   }

   public boolean contains(Location var1) {
      return !this.worldName.equals(var1.getWorld().getName()) ? false : this.contains(var1.getBlockX(), var1.getBlockY(), var1.getBlockZ());
   }

   public int getVolume() {
      return this.getSizeX() * this.getSizeY() * this.getSizeZ();
   }

   public byte getAverageLightLevel() {
      long var1 = 0L;
      int var3 = 0;
      Iterator var4 = this.iterator();

      while(var4.hasNext()) {
         Block var5 = (Block)var4.next();
         if (var5.isEmpty()) {
            var1 += (long)var5.getLightLevel();
            ++var3;
         }
      }

      return var3 > 0 ? (byte)((int)(var1 / (long)var3)) : 0;
   }

   public Cuboid contract() {
      return this.contract(Cuboid.CuboidDirection.Down).contract(Cuboid.CuboidDirection.South).contract(Cuboid.CuboidDirection.East).contract(Cuboid.CuboidDirection.Up).contract(Cuboid.CuboidDirection.North).contract(Cuboid.CuboidDirection.West);
   }

   public Cuboid contract(Cuboid.CuboidDirection var1) {
      Cuboid var2 = this.getFace(var1.opposite());
      switch(var1) {
         case North:
            while(var2.containsOnly(0) && var2.getLowerX() > this.getLowerX()) {
               var2 = var2.shift(Cuboid.CuboidDirection.North, 1);
            }
            return new Cuboid(this.worldName, this.x1, this.y1, this.z1, var2.getUpperX(), this.y2, this.z2);
         case South:
            while(var2.containsOnly(0) && var2.getUpperX() < this.getUpperX()) {
               var2 = var2.shift(Cuboid.CuboidDirection.South, 1);
            }
            return new Cuboid(this.worldName, var2.getLowerX(), this.y1, this.z1, this.x2, this.y2, this.z2);
         case East:
            while(var2.containsOnly(0) && var2.getLowerZ() > this.getLowerZ()) {
               var2 = var2.shift(Cuboid.CuboidDirection.East, 1);
            }
            return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2, this.y2, var2.getUpperZ());
         case West:
            while(var2.containsOnly(0) && var2.getUpperZ() < this.getUpperZ()) {
               var2 = var2.shift(Cuboid.CuboidDirection.West, 1);
            }
            return new Cuboid(this.worldName, this.x1, this.y1, var2.getLowerZ(), this.x2, this.y2, this.z2);
         case Down:
            while(var2.containsOnly(0) && var2.getLowerY() > this.getLowerY()) {
               var2 = var2.shift(Cuboid.CuboidDirection.Down, 1);
            }
            return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2, var2.getUpperY(), this.z2);
         case Up:
            while(var2.containsOnly(0) && var2.getUpperY() < this.getUpperY()) {
               var2 = var2.shift(Cuboid.CuboidDirection.Up, 1);
            }
            return new Cuboid(this.worldName, this.x1, var2.getLowerY(), this.z1, this.x2, this.y2, this.z2);
         default:
            throw new IllegalArgumentException("Dirección inválida " + var1);
      }
   }

   public Cuboid getFace(Cuboid.CuboidDirection var1) {
      switch(var1) {
         case North:
            return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x1, this.y2, this.z2);
         case South:
            return new Cuboid(this.worldName, this.x2, this.y1, this.z1, this.x2, this.y2, this.z2);
         case East:
            return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2, this.y2, this.z1);
         case West:
            return new Cuboid(this.worldName, this.x1, this.y1, this.z2, this.x2, this.y2, this.z2);
         case Down:
            return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2, this.y1, this.z2);
         case Up:
            return new Cuboid(this.worldName, this.x1, this.y2, this.z1, this.x2, this.y2, this.z2);
         default:
            throw new IllegalArgumentException("Dirección inválida " + var1);
      }
   }

   public boolean containsOnly(int var1) {
      Iterator var2 = this.iterator();
      Block var3;
      do {
         if (!var2.hasNext()) {
            return true;
         }
         var3 = (Block)var2.next();
      } while(var3.getTypeId() == var1);
      return false;
   }

   public Cuboid getBoundingCuboid(Cuboid var1) {
      if (var1 == null) {
         return this;
      } else {
         int var2 = Math.min(this.getLowerX(), var1.getLowerX());
         int var3 = Math.min(this.getLowerY(), var1.getLowerY());
         int var4 = Math.min(this.getLowerZ(), var1.getLowerZ());
         int var5 = Math.max(this.getUpperX(), var1.getUpperX());
         int var6 = Math.max(this.getUpperY(), var1.getUpperY());
         int var7 = Math.max(this.getUpperZ(), var1.getUpperZ());
         return new Cuboid(this.worldName, var2, var3, var4, var5, var6, var7);
      }
   }

   public Block getRelativeBlock(int var1, int var2, int var3) {
      return this.getWorld().getBlockAt(this.x1 + var1, this.y1 + var2, this.z1 + var3);
   }

   public Block getRelativeBlock(World var1, int var2, int var3, int var4) {
      return var1.getBlockAt(this.x1 + var2, this.y1 + var3, this.z1 + var4);
   }

   public List<Chunk> getChunks() {
      ArrayList var1 = new ArrayList();
      World var2 = this.getWorld();
      int var3 = this.getLowerX() & -16;
      int var4 = this.getUpperX() & -16;
      int var5 = this.getLowerZ() & -16;
      int var6 = this.getUpperZ() & -16;

      for(int var7 = var3; var7 <= var4; var7 += 16) {
         for(int var8 = var5; var8 <= var6; var8 += 16) {
            var1.add(var2.getChunkAt(var7 >> 4, var8 >> 4));
         }
      }
      return var1;
   }

   public Iterator<Block> iterator() {
      return new Cuboid.CuboidIterator(this.getWorld(), this.x1, this.y1, this.z1, this.x2, this.y2, this.z2);
   }

   public Cuboid clone() {
      return new Cuboid(this);
   }

   public String toString() {
      return new String("Cuboide: " + this.worldName + "," + this.x1 + "," + this.y1 + "," + this.z1 + "=>" + this.x2 + "," + this.y2 + "," + this.z2);
   }

   public static enum CuboidDirection {
      North,
      East,
      South,
      West,
      Up,
      Down,
      Horizontal,
      Vertical,
      Both,
      Unknown;

      public Cuboid.CuboidDirection opposite() {
         switch(this) {
            case North:
               return South;
            case South:
               return North;
            case East:
               return West;
            case West:
               return East;
            case Down:
               return Up;
            case Up:
               return Down;
            case Horizontal:
               return Vertical;
            case Vertical:
               return Horizontal;
            case Both:
               return Both;
            default:
               return Unknown;
         }
      }
   }

   public class CuboidIterator implements Iterator<Block> {
      private World w;
      private int baseX;
      private int baseY;
      private int baseZ;
      private int x;
      private int y;
      private int z;
      private int sizeX;
      private int sizeY;
      private int sizeZ;

      public CuboidIterator(World var2, int var3, int var4, int var5, int var6, int var7, int var8) {
         this.w = var2;
         this.baseX = var3;
         this.baseY = var4;
         this.baseZ = var5;
         this.sizeX = Math.abs(var6 - var3) + 1;
         this.sizeY = Math.abs(var7 - var4) + 1;
         this.sizeZ = Math.abs(var8 - var5) + 1;
         this.x = this.y = this.z = 0;
      }

      public boolean hasNext() {
         return this.x < this.sizeX && this.y < this.sizeY && this.z < this.sizeZ;
      }

      public Block next() {
         Block var1 = this.w.getBlockAt(this.baseX + this.x, this.baseY + this.y, this.baseZ + this.z);
         if (++this.x >= this.sizeX) {
            this.x = 0;
            if (++this.y >= this.sizeY) {
               this.y = 0;
               ++this.z;
            }
         }
         return var1;
      }

      public void remove() {
      }
   }
}
