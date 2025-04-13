package me.aitor.skywars.utils;

import me.aitor.skywars.SkyWars;

public class Console {
   public static void info(String var0) {
      SkyWars.getPlugin().getLogger().info(var0);
   }

   public static void warn(String var0) {
      SkyWars.getPlugin().getLogger().warning(var0);
   }

   public static void severe(String var0) {
      SkyWars.getPlugin().getLogger().severe(var0);
   }

   public static void debugInfo(String var0) {
      if (SkyWars.isDebug()) {
         SkyWars.getPlugin().getLogger().info(var0);
      }

   }

   public static void debugWarn(String var0) {
      if (SkyWars.isDebug()) {
         SkyWars.getPlugin().getLogger().warning(var0);
      }

   }

   public static void debugSevere(String var0) {
      if (SkyWars.isDebug()) {
         SkyWars.getPlugin().getLogger().severe(var0);
      }

   }
}
