package me.aitor.skywars.utils.economy.skyeconomy;

import me.aitor.skywars.SkyWars;
import me.aitor.skywars.utils.economy.SkyEconomy;
import com.greatmancode.craftconomy3.Cause;
import com.greatmancode.craftconomy3.Common;
import com.greatmancode.craftconomy3.account.Account;
import com.greatmancode.craftconomy3.tools.interfaces.Loader;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class CraftconomyUtils implements SkyEconomy {
   private final String currency = SkyWars.getPlugin().getConfig().getString("economy.craftconomy3_currency");
   private Common craftconomy;

   public SkyEconomy loadCraftconomy() {
      Plugin var1 = SkyWars.getPlugin().getServer().getPluginManager().getPlugin("Craftconomy3");
      if (var1 != null) {
         this.craftconomy = (Common)((Loader)var1).getCommon();
         return this;
      } else {
         return null;
      }
   }

   private Account getAccount(Player var1) {
      return this.craftconomy.getAccountManager().getAccount(var1.getName(), false);
   }

   public double getCoins(Player var1) {
      return (double)((int)this.getAccount(var1).getBalance(((World)Bukkit.getWorlds().get(0)).getName(), this.currency));
   }

   public void addCoins(Player var1, double var2, boolean var4) {
      this.getAccount(var1).deposit(var4 ? this.multiply(var1, var2) : var2, ((World)Bukkit.getWorlds().get(0)).getName(), this.currency, Cause.PLUGIN, "SkyWars reward");
   }

   public void removeCoins(Player var1, double var2) {
      this.getAccount(var1).withdraw(var2, ((World)Bukkit.getWorlds().get(0)).getName(), this.currency, Cause.PLUGIN, "SkyWars withdraw");
   }
}
