package me.aitor.skywars.utils;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionType;

public class Utils {
   public static String color(String var0) {
      ChatColor[] var1 = ChatColor.values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         ChatColor var4 = var1[var3];
         var0 = var0.replaceAll("\\[" + var4.getChar() + "\\]", var4.toString());
      }

      return var0;
   }

   public static boolean isNumeric(String var0) {
      try {
         Integer.parseInt(var0);
         return true;
      } catch (NumberFormatException var2) {
         return false;
      }
   }

   public static ItemBuilder readItem(String var0) {
      ItemBuilder var1 = null;
      String[] var2 = var0.split(",");
      String var3 = var2[0];
      String[] var4 = var3.split(":");
      if (isNumeric(var4[0])) {
         var1 = new ItemBuilder(Material.getMaterial(Integer.parseInt(var4[0])));
      } else {
         var1 = new ItemBuilder(Material.matchMaterial(var4[0]));
      }

      if (var4.length >= 2) {
         var1.setData(Short.parseShort(var4[1]));
      }

      if (var2.length >= 2) {
         int var5 = 1;
         if (isNumeric(var2[1])) {
            var1.setAmount(Integer.parseInt(var2[1]) <= 0 ? 1 : Integer.parseInt(var2[1]));
            ++var5;
         }

         if (var2.length >= 3 && isNumeric(var2[2])) {
            var1.setData(Short.parseShort(var2[2]));
         }

         for(int var6 = var5; var6 < var2.length; ++var6) {
            if (var2[var6] != null && !var2[var6].isEmpty()) {
               String var7;
               if (var2[var6].startsWith("lore:")) {
                  var7 = var2[var6].replace("lore:", "");
                  var1.addLore(var7);
               } else {
                  String[] var8;
                  int var10;
                  int var11;
                  if (var2[var6].startsWith("potion:")) {
                     var7 = var2[var6].replace("potion:", "");
                     var8 = var7.split(":");
                     PotionType[] var17 = PotionType.values();
                     var10 = var17.length;

                     for(var11 = 0; var11 < var10; ++var11) {
                        PotionType var12 = var17[var11];
                        if (var8[0].equalsIgnoreCase(var12.toString())) {
                           boolean var13;
                           boolean var14;
                           if (var8.length == 3) {
                              var13 = Boolean.parseBoolean(var8[1]);
                              var14 = Boolean.parseBoolean(var8[2]);
                           } else {
                              var13 = false;
                              var14 = false;
                           }

                           var1.setPotion(var12.toString(), var1.getType(), var13, var14);
                        }
                     }
                  } else if (var2[var6].startsWith("name:")) {
                     var7 = var2[var6].replace("name:", "");
                     var1.setTitle(var7);
                  } else {
                     int var9;
                     if (var2[var6].startsWith("leather_color:")) {
                        var7 = var2[var6].replace("leather_color:", "");
                        var8 = var7.split("-");
                        if (var8.length == 3) {
                           var9 = isNumeric(var8[0]) ? Integer.parseInt(var8[0]) : 0;
                           var10 = isNumeric(var8[1]) ? Integer.parseInt(var8[1]) : 0;
                           var11 = isNumeric(var8[2]) ? Integer.parseInt(var8[2]) : 0;
                        } else {
                           var9 = 0;
                           var10 = 0;
                           var11 = 0;
                        }

                        var1.setColor(Color.fromRGB(var9, var10, var11));
                     } else if (var2[var6].equalsIgnoreCase("glowing")) {
                        var1.setGlow(true);
                     } else {
                        Enchantment var15 = Enchantment.getByName(var2[var6].toUpperCase().split(":")[0]);
                        if (var15 != null) {
                           String var16 = var2[var6].replace(var15.getName().toUpperCase() + ":", "");
                           var9 = 1;
                           if (isNumeric(var16)) {
                              var9 = Integer.parseInt(var16);
                           }

                           var1.addEnchantment(var15, var9);
                        }
                     }
                  }
               }
            }
         }
      }

      return var1;
   }
}
