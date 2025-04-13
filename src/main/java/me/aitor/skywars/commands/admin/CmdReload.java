package me.aitor.skywars.commands.admin;

import me.aitor.skywars.SkyWars;
import me.aitor.skywars.commands.BaseCommand;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

public class CmdReload implements BaseCommand {
   public boolean onCommand(CommandSender var1, String[] var2) {
      Player var3 = null;
      if (!(var1 instanceof Player)) {
         var1.sendMessage("¡No eres un jugador!");
         return true;
      } else {
         var3 = (Player)var1;
         if (!var3.hasPermission(this.getPermission())) {
            var3.sendMessage("§c¡No tienes permiso!");
            return true;
         } else if (var2.length == 0) {
            this.helpDefault(var1);
            return true;
         } else {
            if (var2.length >= 1) {
               String var4 = var2[0].toLowerCase();
               byte var5 = -1;
               switch(var4.hashCode()) {
                  case -1354792126:
                     if (var4.equals("config")) {
                        var5 = 0;
                     }
                     break;
                  case -462094004:
                     if (var4.equals("messages")) {
                        var5 = 1;
                     }
                     break;
                  case 96673:
                     if (var4.equals("all")) {
                        var5 = 4;
                     }
                     break;
                  case 109264530:
                     if (var4.equals("score")) {
                        var5 = 2;
                     }
                     break;
                  case 109413437:
                     if (var4.equals("shops")) {
                        var5 = 3;
                     }
               }

               switch(var5) {
                  case 0:
                     if (!var1.hasPermission("skywars.admin.reload.config")) {
                        return false;
                     }
                     this.reloadConfig(var1, true);
                     break;
                  case 1:
                     if (!var1.hasPermission("skywars.admin.reload.messages")) {
                        return false;
                     }
                     this.reloadMessages(var1, true);
                     break;
                  case 2:
                     if (!var1.hasPermission("skywars.admin.reload.score")) {
                        return false;
                     }
                     this.reloadScoreboard(var1, true);
                     break;
                  case 3:
                     if (!var1.hasPermission("skywars.admin.reload.shops")) {
                        return false;
                     }
                     this.reloadShops(var1, true);
                     break;
                  case 4:
                     if (!var1.hasPermission("skywars.admin.reload.all")) {
                        return false;
                     }
                     var1.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a" + SkyWars.prefix + "&eRecargando todos los archivos"));
                     this.reloadConfig(var1, false);
                     this.reloadMessages(var1, false);
                     this.reloadScoreboard(var1, false);
                     this.reloadShops(var1, false);
                     var1.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a" + SkyWars.prefix + "Todos los archivos recargados"));
                     break;
                  default:
                     this.helpDefault(var1);
               }
            }
            return true;
         }
      }
   }

   public String help(CommandSender var1) {
      String var2 = "&a/sw reload - &bPara obtener más ayuda sobre el comando reload";
      return var1.hasPermission(this.getPermission()) ? var2 : "";
   }

   public String getPermission() {
      return "skywars.admin.reload";
   }

   public boolean console() {
      return false;
   }

   private void reloadConfig(CommandSender var1, boolean var2) {
      if (var2) {
         var1.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a" + SkyWars.prefix + "&eRecargando archivo de configuración"));
      }
      try {
         SkyWars.reloadConfigMain();
         if (var2) {
            var1.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a" + SkyWars.prefix + "Archivo de configuración recargado"));
         }
      } catch (Exception var4) {
         var1.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + SkyWars.prefix + "Ocurrió un error al intentar recargar el archivo de configuración, por favor revisa el registro de la consola"));
         SkyWars.getPlugin().getLogger().log(Level.SEVERE, "Ocurrió un error en config.yml", var4);
      }
   }

   private void reloadMessages(CommandSender var1, boolean var2) {
      if (var2) {
         var1.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a" + SkyWars.prefix + "&eRecargando archivos de mensajes"));
      }
      try {
         SkyWars.reloadMessages();
         if (var2) {
            var1.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a" + SkyWars.prefix + "Archivos de mensajes recargados"));
         }
      } catch (Exception var4) {
         var1.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + SkyWars.prefix + "Ocurrió un error al intentar recargar los archivos de mensajes, por favor revisa el registro de la consola"));
         SkyWars.getPlugin().getLogger().log(Level.SEVERE, "Ocurrió un error en algún archivo de mensajes", var4);
      }
   }

   private void reloadScoreboard(CommandSender var1, boolean var2) {
      if (var2) {
         var1.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a" + SkyWars.prefix + "&eRecargando archivo de puntuación"));
      }
      try {
         SkyWars.reloadConfigScoreboard();
         if (var2) {
            var1.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a" + SkyWars.prefix + "Archivo de puntuación recargado"));
         }
      } catch (Exception var4) {
         var1.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + SkyWars.prefix + "Ocurrió un error al intentar recargar el archivo de puntuación, por favor revisa el registro de la consola"));
         SkyWars.getPlugin().getLogger().log(Level.SEVERE, "Ocurrió un error en scoreboard.yml", var4);
      }
   }

   private void reloadShops(CommandSender var1, boolean var2) {
      if (var2) {
         var1.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a" + SkyWars.prefix + "&eRecargando archivo de tiendas"));
      }
      try {
         SkyWars.reloadConfigShop();
         if (var2) {
            var1.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a" + SkyWars.prefix + "Archivo de tiendas recargado"));
         }
      } catch (Exception var6) {
         var1.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + SkyWars.prefix + "Ocurrió un error al intentar recargar el archivo de tiendas, por favor revisa el registro de la consola"));
         SkyWars.getPlugin().getLogger().log(Level.SEVERE, "Ocurrió un error en shop.yml", var6);
      }
      var1.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a" + SkyWars.prefix + "&eRecargando archivo de habilidades"));
      try {
         SkyWars.reloadConfigAbilities();
         var1.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a" + SkyWars.prefix + "Archivo de habilidades recargado"));
      } catch (Exception var5) {
         var1.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + SkyWars.prefix + "Ocurrió un error al intentar recargar el archivo de habilidades, por favor revisa el registro de la consola"));
         SkyWars.getPlugin().getLogger().log(Level.SEVERE, "Ocurrió un error en abilities.yml", var5);
      }
      try {
         SkyWars.reloadAbilities();
      } catch (Exception var4) {
         SkyWars.getPlugin().getLogger().log(Level.SEVERE, "Ocurrió un error al intentar recargar los objetos de habilidades", var4);
      }
   }

   public void helpDefault(CommandSender var1) {
      HashMap var2 = new HashMap();
      var2.put("all", "&a/sw reload &call &a- &bEjecuta todos los subcomandos anteriores");
      var2.put("config", "&a/sw reload &econfig &a- &bRecargar archivo config.yml");
      var2.put("messages", "&a/sw reload &emessages &a- &bRecargar todos los archivos de mensajes");
      var2.put("score", "&a/sw reload &escore &a- &bRecargar archivo de puntuación");
      var2.put("shops", "&a/sw reload &eshops &a- &bRecargar menú de tiendas y mensajes");
      var1.sendMessage("------------ §a[Ayuda Recarga SkyWars] §f------------");
      Iterator var3 = var2.entrySet().iterator();
      while(var3.hasNext()) {
         Entry var4 = (Entry)var3.next();
         if (var1.hasPermission("skywars.admin.reload." + (String)var4.getKey())) {
            var1.sendMessage(ChatColor.translateAlternateColorCodes('&', (String)var4.getValue()));
         }
      }
      var1.sendMessage("--------------------------------------------");
   }

   public List<String> onTabComplete(CommandSender var1, String[] var2) {
      if (!var1.hasPermission(this.getPermission())) {
         return null;
      } else if (var2.length == 1) {
         HashMap var3 = new HashMap();
         var3.put("all", "&a/sw reload &call &a- &bEjecuta todos los subcomandos anteriores");
         var3.put("config", "&a/sw reload &econfig &a- &bRecargar archivo config.yml");
         var3.put("messages", "&a/sw reload &emessages &a- &bRecargar todos los archivos de mensajes");
         var3.put("score", "&a/sw reload &escore &a- &bRecargar archivo de puntuación");
         var3.put("shops", "&a/sw reload &eshops &a- &bRecargar menú de tiendas y mensajes");
         String[] var4 = new String[]{"config", "messages", "score", "shops", "all"};
         ArrayList var5 = new ArrayList(Arrays.asList(var4));
         ArrayList var6 = new ArrayList();
         StringUtil.copyPartialMatches(var2[0], var5, var6);
         Collections.sort(var6);
         var1.sendMessage("--------------------------------------------");
         Iterator var7 = var3.entrySet().iterator();
         while(var7.hasNext()) {
            Entry var8 = (Entry)var7.next();
            if (var6.contains(var8.getKey())) {
               var1.sendMessage(ChatColor.translateAlternateColorCodes('&', (String)var8.getValue()));
            }
         }
         var1.sendMessage("--------------------------------------------");
         return var6;
      } else {
         return null;
      }
   }
}
