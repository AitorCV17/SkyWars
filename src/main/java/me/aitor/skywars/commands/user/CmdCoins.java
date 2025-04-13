package me.aitor.skywars.commands.user;

import me.aitor.skywars.SkyWars;
import me.aitor.skywars.commands.BaseCommand;
import me.aitor.skywars.player.SkyPlayer;
import me.aitor.skywars.utils.economy.SkyEconomyManager;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdCoins implements BaseCommand {
   public boolean onCommand(CommandSender var1, String[] var2) {
      Player var3 = null;
      SkyPlayer var4 = null;
      boolean var5 = false;
      if (var1 instanceof Player) {
         var3 = (Player)var1;
         var4 = SkyWars.getSkyPlayer(var3);
         if (var4 == null) {
            return false;
         }

         var5 = true;
      }

      if (!var1.hasPermission(this.getPermission())) {
         var1.sendMessage("§c¡No tienes permiso!");
         return true;
      } else if (var2.length == 0) {
         if (var5) {
            double var12 = var4.getCoins();
            var4.sendMessage("&aCoins: &e" + var12);
         } else {
            var1.sendMessage(ChatColor.translateAlternateColorCodes('&', this.help(var1)));
         }

         return true;
      } else {
         if (var2.length >= 1 && var1.hasPermission("skywars.admin.coins")) {
            String var6 = var2[0].toLowerCase();
            byte var7 = -1;
            switch(var6.hashCode()) {
               case -934610812:
                  if (var6.equals("remove")) {
                     var7 = 1;
                  }
                  break;
               case 96417:
                  if (var6.equals("add")) {
                     var7 = 0;
                  }
            }

            String var8;
            Player var9;
            int var10;
            SkyPlayer var11;
            int var13;
            switch(var7) {
               case 0:
                  if (var2.length == 1) {
                     var1.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cEscribe: /sw coins add <cantidad> [NombreJugador]"));
                     return true;
                  }

                  if (var2.length == 2) {
                     if (var5) {
                        var13 = Integer.parseInt(var2[1]);
                        SkyEconomyManager.addCoins(var4.getPlayer(), (double)var13, false);
                     } else {
                        var1.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cEscribe: /sw coins add <cantidad> [NombreJugador]"));
                     }

                     return true;
                  }

                  if (var2.length == 3) {
                     var8 = var2[2];
                     var9 = Bukkit.getPlayer(var8);
                     var10 = Integer.parseInt(var2[1]);
                     if (var9 == null) {
                        var1.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + var8 + " no está en línea o no existe"));
                        return false;
                     }

                     var11 = SkyWars.getSkyPlayer(var9);
                     if (var11 == null) {
                        var1.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + var8 + " no está en línea o no existe"));
                        return false;
                     }

                     SkyEconomyManager.addCoins(var11.getPlayer(), (double)var10, false);
                     return true;
                  }
                  break;
               case 1:
                  if (var2.length == 1) {
                     var1.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cEscribe: /sw coins remove <cantidad> [NombreJugador]"));
                     return true;
                  }

                  if (var2.length == 2) {
                     if (var5) {
                        var13 = Integer.parseInt(var2[1]);
                        SkyEconomyManager.removeCoins(var4.getPlayer(), var13);
                     } else {
                        var1.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cEscribe: /sw coins remove <cantidad> [NombreJugador]"));
                     }

                     return true;
                  }

                  if (var2.length == 3) {
                     var8 = var2[2];
                     var9 = Bukkit.getPlayer(var8);
                     var10 = Integer.parseInt(var2[1]);
                     if (var9 == null) {
                        var1.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + var8 + " no está en línea o no existe"));
                        return false;
                     }

                     var11 = SkyWars.getSkyPlayer(var9);
                     if (var11 == null) {
                        var1.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + var8 + " no está en línea o no existe"));
                        return false;
                     }

                     SkyEconomyManager.removeCoins(var11.getPlayer(), var10);
                     return true;
                  }
            }
         }

         return true;
      }
   }

   public String help(CommandSender var1) {
      String var2 = "&a/sw coins &a- &bObtener la cantidad de monedas que tienes \n";
      if (var1.hasPermission("skywars.admin.coins")) {
         var2 = var2 + "&a/sw coins &eadd <cantidad> [nombre] &a- &bAñadir monedas a ti mismo/otro\n&a/sw coins &eremove <cantidad> [nombre] &a- &bQuitar monedas a ti mismo/otro";
      }

      return var1.hasPermission(this.getPermission()) ? var2 : "";
   }

   public String getPermission() {
      return "skywars.user";
   }

   public boolean console() {
      return true;
   }

   public List<String> onTabComplete(CommandSender var1, String[] var2) {
      return null;
   }
}
