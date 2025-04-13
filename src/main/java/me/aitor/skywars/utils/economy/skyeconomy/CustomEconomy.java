package me.aitor.skywars.utils.economy.skyeconomy;

import me.aitor.skywars.SkyWars;
import me.aitor.skywars.database.DatabaseHandler;
import me.aitor.skywars.player.SkyPlayer;
import me.aitor.skywars.utils.economy.SkyEconomy;
import me.aitor.skywars.utils.economy.SkyEconomyManager;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CustomEconomy implements SkyEconomy {
   public static boolean isCustom() {
      return SkyEconomyManager.getEconomy() instanceof CustomEconomy;
   }

   public double getCoins(Player var1) {
      SkyPlayer var2 = SkyWars.getSkyPlayer(var1);
      if (var2 == null) {
         SkyWars.logError("Obteniendo monedas de SkyPlayer nulo");
         return 0.0D;
      } else {
         return var2.getCoins2();
      }
   }

   public void addCoins(Player var1, double var2, boolean var4) {
      final SkyPlayer var5 = SkyWars.getSkyPlayer(var1);
      if (var5 == null) {
         SkyWars.logError("Intentando agregar monedas a SkyPlayer nulo");
      } else {
         final double var6 = (var4 ? this.multiply(var1, var2) : var2) + var5.getCoins2();
         var5.setCoins(var6);
         (new BukkitRunnable() {
            public void run() {
               DatabaseHandler.getDS().modifyCoins(var5, var6);
            }
         }).runTaskAsynchronously(SkyWars.getPlugin());
      }
   }

   public void removeCoins(Player var1, double var2) {
      final SkyPlayer var4 = SkyWars.getSkyPlayer(var1);
      if (var4 == null) {
         SkyWars.logError("Intentando quitar monedas a SkyPlayer nulo");
      } else {
         final double var5 = var4.getCoins2() - var2;
         var4.setCoins(var5);
         (new BukkitRunnable() {
            public void run() {
               DatabaseHandler.getDS().modifyCoins(var4, var5);
            }
         }).runTaskAsynchronously(SkyWars.getPlugin());
      }
   }
}
