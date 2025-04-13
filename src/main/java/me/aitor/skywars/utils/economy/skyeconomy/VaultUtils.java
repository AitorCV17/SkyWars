package me.aitor.skywars.utils.economy.skyeconomy;

import me.aitor.skywars.SkyWars;
import me.aitor.skywars.utils.economy.SkyEconomy;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultUtils implements SkyEconomy {
   private Economy economy = null;

   public double getCoins(Player var1) {
      return this.economy == null && this.setupEconomy() != null ? 0.0D : (double)((int)this.economy.getBalance(var1));
   }

   public void addCoins(Player var1, double var2, boolean var4) {
      if (this.economy != null) {
         this.economy.depositPlayer(var1, var4 ? this.multiply(var1, var2) : var2);
      }
   }

   public void removeCoins(Player var1, double var2) {
      if (this.economy != null) {
         this.economy.withdrawPlayer(var1, var2);
      }
   }

   public SkyEconomy setupEconomy() {
      if (SkyWars.getPlugin().getServer().getPluginManager().getPlugin("Vault") == null) {
         return null;
      } else {
         RegisteredServiceProvider var1 = SkyWars.getPlugin().getServer().getServicesManager().getRegistration(Economy.class);
         if (var1 == null) {
            return null;
         } else {
            this.economy = (Economy)var1.getProvider();
            return this.economy != null ? this : null;
         }
      }
   }
}
