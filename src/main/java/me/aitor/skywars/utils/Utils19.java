package me.aitor.skywars.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

public class Utils19 {
   public static double getMaxHealth(Player var0) {
      return var0.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
   }

   public static PotionMeta getPotion(Material var0, PotionType var1, boolean var2, boolean var3) {
      ItemStack var4 = new ItemStack(var0, 1);
      PotionMeta var5 = (PotionMeta)var4.getItemMeta();
      var5.setBasePotionData(new PotionData(var1, var2, var3));
      return var5;
   }

   public static PotionData getPotionData(ItemStack var0) {
      PotionMeta var1 = (PotionMeta)var0.getItemMeta();
      return var1.getBasePotionData();
   }

   public static void spawnParticle(String var0, Location var1, int var2, double var3, double var5, double var7, double var9) {
      Particle var11;
      try {
         var11 = Particle.valueOf(var0);
      } catch (IllegalArgumentException var13) {
         return;
      }

      if (var11 != null) {
         var1.getWorld().spawnParticle(var11, var1, var2, var3, var5, var7, var9);
      }
   }
}
