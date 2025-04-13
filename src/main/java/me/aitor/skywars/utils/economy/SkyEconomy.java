package me.aitor.skywars.utils.economy;

import org.bukkit.entity.Player;

public interface SkyEconomy {
   double getCoins(Player var1);

   void addCoins(Player var1, double var2, boolean var4);

   void removeCoins(Player var1, double var2);

   default double multiply(Player player, double i) {
      if (player.hasPermission("skywars.vip.coin.3")) {
         i *= 3.0D;
      } else if (player.hasPermission("skywars.vip.coin.2")) {
         i *= 2.0D;
      }

      return i;
   }
}
