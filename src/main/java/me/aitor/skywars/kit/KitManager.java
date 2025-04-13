package me.aitor.skywars.kit;

import me.aitor.skywars.SkyWars;
import java.io.File;
import java.util.HashMap;

public class KitManager {
   public static HashMap<String, Kit> kits = new HashMap();

   public static void initKits() {
      kits.clear();
      File var0 = new File(SkyWars.getPlugin().getDataFolder(), SkyWars.kits + File.separator);
      if (var0.exists() && var0.isDirectory()) {
         File[] var1 = var0.listFiles();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            File var4 = var1[var3];
            if (var4.getName().contains(".yml")) {
               String var5 = var4.getName().replace(".yml", "");
               new Kit(var5);
            }
         }
      }

   }

   public static Kit[] getKits() {
      return (Kit[])kits.values().toArray(new Kit[kits.values().size()]);
   }

   public static Kit getKit(String var0) {
      return (Kit)kits.get(var0);
   }
}
