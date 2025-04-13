package me.aitor.skywars.utils.economy.skyeconomy;

import me.aitor.skywars.SkyWars;
import me.aitor.skywars.utils.economy.SkyEconomy;
import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PlayerPointsUtils implements SkyEconomy {
   private PlayerPoints playerPoints;

   public SkyEconomy hookPlayerPoints() {
      Plugin var1 = SkyWars.getPlugin().getServer().getPluginManager().getPlugin("PlayerPoints");
      this.playerPoints = (PlayerPoints)PlayerPoints.class.cast(var1);
      return this.playerPoints != null ? this : null;
   }

   public double getCoins(Player var1) {
      return (double)this.playerPoints.getAPI().look(var1.getUniqueId());
   }

   public void addCoins(Player var1, double var2, boolean var4) {
      this.playerPoints.getAPI().give(var1.getUniqueId(), var4 ? (int)this.multiply(var1, var2) : (int)var2);
   }

   public void removeCoins(Player var1, double var2) {
      this.playerPoints.getAPI().take(var1.getUniqueId(), (int)var2);
   }
}
