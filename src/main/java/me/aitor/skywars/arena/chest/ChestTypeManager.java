package me.aitor.skywars.arena.chest;

import me.aitor.skywars.SkyWars;
import me.aitor.skywars.utils.ItemBuilder;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;

public class ChestTypeManager {
   public static HashMap<String, ChestType> chesttypes = new HashMap();

   public static void loadChests() {
      chesttypes.clear();
      File var0 = new File(SkyWars.getPlugin().getDataFolder(), SkyWars.chests + File.separator);
      if (var0.exists() && var0.isDirectory()) {
         File[] var1 = var0.listFiles();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            File var4 = var1[var3];
            if (var4.getName().contains(".yml")) {
               String var5 = var4.getName().replace(".yml", "");
               ChestType var6 = new ChestType(var5);
               FileConfiguration var7 = var6.getConfig();
               Iterator var8 = var7.getStringList("items").iterator();

               int var11;
               while(var8.hasNext()) {
                  String var9 = (String)var8.next();
                  String[] var10 = var9.split(" ");
                  var11 = Integer.parseInt(var10[0]);
                  int var12 = Integer.parseInt(var10[1]);
                  int var13 = Integer.parseInt(var10[2]);
                  String var14 = var10[3];
                  ItemBuilder var15 = readItem(var14, var6.getName());
                  if (var15 != null) {
                     var6.addItem(new RandomItem((double)var11, var12, var13, var15));
                  }
               }

               String[] var16 = var7.getString("item.item").split(":");
               short var17 = 0;
               if (var16.length == 2) {
                  var17 = (short)Integer.parseInt(var16[1]);
               }

               var6.setItem(var16[0], var17);
               String var18 = var7.getString("item.name");
               var6.setTitle(var18);
               var11 = var7.getInt("item.slot");
               var6.setSlot(var11);
               Iterator var19 = var7.getStringList("item.description").iterator();

               while(var19.hasNext()) {
                  String var20 = (String)var19.next();
                  var6.addDescription(ChatColor.translateAlternateColorCodes('&', var20));
               }
            }
         }
      }

   }

   public static ChestType[] getChestTypes() {
      return (ChestType[])chesttypes.values().toArray(new ChestType[chesttypes.values().size()]);
   }

   public static ChestType getChestType(String var0) {
      return (ChestType)chesttypes.get(var0);
   }

   public static ItemBuilder readItem(String var0, String var1) {
      String[] var2 = var0.split(",");
      String[] var3 = var2[0].split(":");
      short var5 = 0;
      Material var6 = null;
      if (isNumeric(var3[0])) {
         int var4 = Integer.parseInt(var3[0]);
         var6 = Material.getMaterial(var4);
      } else {
         var6 = Material.getMaterial(var3[0].toUpperCase());
      }

      if (var3.length == 2) {
         var5 = (short)Integer.parseInt(var3[1]);
      }

      SkyWars.log("ChestTypeManager - " + var6 + " - " + var1);
      if (var6 == null) {
         return null;
      } else {
         ItemBuilder var7 = new ItemBuilder(var6, var5);
         String[] var8 = var2;

         for(int var9 = 1; var9 < var8.length; ++var9) {
            String var10;
            if (var8[var9].startsWith("name:")) {
               var10 = var8[var9].replace("name:", "");
               var7.setTitle(var10);
            }

            if (var8[var9].startsWith("lore:")) {
               var10 = var8[var9].replace("lore:", "");
               var7.addLore(var10);
            }

            Enchantment[] var15 = Enchantment.values();
            int var11 = var15.length;

            for(int var12 = 0; var12 < var11; ++var12) {
               Enchantment var13 = var15[var12];
               if (var8[var9].toUpperCase().startsWith(var13.getName().toUpperCase())) {
                  int var14 = Integer.parseInt(var8[var9].replace(var13.getName().toUpperCase() + ":", ""));
                  var7.addEnchantment(var13, var14);
               }
            }

            if (var8[var9].startsWith("leather_color:")) {
               var10 = var8[var9].replace("leather_color:", "");
               String[] var16 = var10.split("-");
               Color var17 = Color.fromRGB(Integer.parseInt(var16[0]), Integer.parseInt(var16[1]), Integer.parseInt(var16[2]));
               var7.setColor(var17);
            }
         }

         return var7;
      }
   }

   public static boolean isNumeric(String var0) {
      try {
         int var1 = Integer.parseInt(var0);
         return true;
      } catch (NumberFormatException var2) {
         return false;
      }
   }
}
