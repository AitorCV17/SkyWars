package me.aitor.skywars.utils;

import me.aitor.skywars.SkyWars;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationUtil {
   public static Location center(Location var0) {
      return new Location(var0.getWorld(), getRelativeCoord(var0.getBlockX()), getRelativeCoord(var0.getBlockY()), getRelativeCoord(var0.getBlockZ()));
   }

   private static double getRelativeCoord(int var0) {
      double var1 = (double)var0;
      var1 = var1 < 0.0D ? var1 + 0.5D : var1 + 0.5D;
      return var1;
   }

   public static String getString(Location var0, boolean var1) {
      if (var0 == null) {
         SkyWars.logError("Ubicación nula, no se puede convertir a cadena");
         return null;
      } else {
         return var1 ? var0.getWorld().getName() + "," + center(var0).getX() + "," + var0.getY() + "," + center(var0).getZ() + "," + 0 + "," + var0.getYaw() : var0.getWorld().getName() + "," + var0.getX() + "," + var0.getY() + "," + var0.getZ() + "," + var0.getPitch() + "," + var0.getYaw();
      }
   }

   public static Location getLocation(String var0) {
      String[] var1 = var0.split(",");
      Location var2 = null;
      if (var1.length < 4) {
         SkyWars.logError("No se puede obtener la ubicación de '" + var0 + "' (se requieren world,x,y,z)");
      } else if (var1.length < 6) {
         var2 = new Location(Bukkit.getWorld(var1[0]), Double.parseDouble(var1[1]), Double.parseDouble(var1[2]), Double.parseDouble(var1[3]));
      } else {
         try {
            var2 = new Location(Bukkit.getWorld(var1[0]), Double.parseDouble(var1[1]), Double.parseDouble(var1[2]), Double.parseDouble(var1[3]), Float.parseFloat(var1[5]), Float.parseFloat("0"));
         } catch (NullPointerException var4) {
            SkyWars.logError("No se puede obtener la ubicación de '" + var0 + "'");
         }
      }
      return var2;
   }
}
