package me.aitor.skywars.arena;

import me.aitor.skywars.SkyWars;
import com.google.common.collect.Sets;
import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class ArenaManager {
   public static HashMap<String, Arena> games = new HashMap<>();

   public static void initGames() {
      games.clear();
      File var0 = new File(SkyWars.getPlugin().getDataFolder(), SkyWars.arenas);
      if (var0.exists() && var0.listFiles().length > 0) {
         int var2;
         if (SkyWars.isBungeeMode()) {
            File var1 = null;
            if (SkyWars.isRandomMap()) {
               var2 = (new Random()).nextInt(var0.listFiles().length);
               File[] var3 = var0.listFiles();
               var1 = var3[var2];
            } else {
               var1 = new File(SkyWars.getPlugin().getDataFolder(), SkyWars.arenas + File.separator + SkyWars.getMapSet() + ".yml");
               if (!var1.exists()) {
                  SkyWars.log("ArenaManager.initGames - El conjunto de mapas no existe");
                  return;
               }
            }

            if (!var1.getName().contains(".yml")) {
               SkyWars.log("ArenaManager.initGames - El archivo existente no es un archivo YML");
               return;
            }

            String var8 = var1.getName().replace(".yml", "");
            File var9 = new File(SkyWars.maps + File.separator + var8);
            if (var9.isDirectory()) {
               arenaClone(var9, var8);
               new Arena(var8);
               SkyWars.log("ArenaManager.initGames - " + var8 + " ha sido establecido como arena en modo Bungee");
            }
         } else if (SkyWars.isMultiArenaMode()) {
            File[] var7 = var0.listFiles();
            var2 = var7.length;

            for (int var10 = 0; var10 < var2; ++var10) {
               File var4 = var7[var10];
               if (var4.getName().contains(".yml")) {
                  String var5 = var4.getName().replace(".yml", "");
                  File var6 = new File(SkyWars.maps + File.separator + var5);
                  if (var6.isDirectory()) {
                     arenaClone(var6, var5);
                     new Arena(var5);
                     SkyWars.log("ArenaManager.initGames - " + var5 + " ha sido añadido como arena en modo Normal");
                  }
               }
            }

            GameQueue.check();
         }
      }
   }

   private static void arenaClone(File var0, String var1) {
      try {
         delete(new File(var0, "uid.dat"));
         delete(new File(var0, "session.lock"));
         delete(new File(var1));
         copyFolder(var0, new File(var1));
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      World var2 = Bukkit.getWorld(var1);
      if (var2 != null) {
         Bukkit.unloadWorld(var2, false);
      }
   }

   public static Set<Arena> getGames() {
      return Collections.unmodifiableSet(Sets.newHashSet(games.values()));
   }

   public static Arena getGame(String var0) {
      return games.get(var0);
   }

   public static void addGame(String var0) {
      File var1 = new File(SkyWars.getPlugin().getDataFolder(), SkyWars.arenas + File.separator + var0 + ".yml");
      if (!var1.exists()) {
         SkyWars.log("ArenaManager.addGame - La arena no existe");
      } else {
         new Arena(var0);
      }
   }

   public static void copyFolder(File var0, File var1) throws IOException {
      int var5;
      if (var0.isDirectory()) {
         if (!var1.exists()) {
            var1.mkdir();
            SkyWars.log("Directorio copiado desde " + var0 + " hasta " + var1);
         }

         String[] var2 = var0.list();
         if (var2 == null) return;
         for (String var6 : var2) {
            File var7 = new File(var0, var6);
            File var8 = new File(var1, var6);
            copyFolder(var7, var8);
         }
      } else {
         try (FileInputStream var9 = new FileInputStream(var0);
              FileOutputStream var10 = new FileOutputStream(var1)) {
            byte[] var11 = new byte[1024];
            while ((var5 = var9.read(var11)) > 0) {
               var10.write(var11, 0, var5);
            }
         }

         SkyWars.log("Archivo copiado desde " + var0 + " hasta " + var1);
      }
   }

   public static void delete(File var0) {
      if (var0 == null) return;

      if (var0.isDirectory()) {
         File[] files = var0.listFiles();
         if (files != null) {
            for (File file : files) {
               delete(file);
            }
         }

         if (var0.listFiles() == null || var0.listFiles().length == 0) {
            var0.delete();
            SkyWars.log("Directorio eliminado : " + var0.getAbsolutePath());
         }
      } else {
         if (var0.exists()) {
            var0.delete();
         }
      }
   }
}
