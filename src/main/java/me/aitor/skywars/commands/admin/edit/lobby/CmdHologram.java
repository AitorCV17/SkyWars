package me.aitor.skywars.commands.admin.edit.lobby;

import me.aitor.skywars.SkyWars;
import me.aitor.skywars.commands.BaseCommand;
import me.aitor.skywars.config.ConfigManager;
import me.aitor.skywars.player.SkyPlayer;
import me.aitor.skywars.utils.LocationUtil;
import me.aitor.skywars.utils.sky.SkyHologram;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

public class CmdHologram implements BaseCommand {
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
               ArrayList var4 = new ArrayList();
               String var5 = var2[0].toLowerCase();
               byte var6 = -1;
               switch(var5.hashCode()) {
                  case -934610812:
                     if (var5.equals("remove")) {
                        var6 = 1;
                     }
                     break;
                  case 96417:
                     if (var5.equals("add")) {
                        var6 = 0;
                     }
               }

               switch(var6) {
                  case 0:
                     var4.addAll(ConfigManager.score.getStringList("hologram.locations"));
                     var4.add(LocationUtil.getString(var3.getLocation(), true));
                     ConfigManager.score.set("hologram.locations", var4);
                     ConfigManager.score.save();
                     SkyWars.hologram.clear();
                     Iterator var11 = ConfigManager.score.getStringList("hologram.locations").iterator();
                     while(var11.hasNext()) {
                        String var12 = (String)var11.next();
                        SkyWars.hologram.add(LocationUtil.getLocation(var12));
                     }
                     var11 = Bukkit.getOnlinePlayers().iterator();
                     while(var11.hasNext()) {
                        Player var13 = (Player)var11.next();
                        SkyPlayer var15 = SkyWars.getSkyPlayer(var13);
                        if (var3.getWorld() == var13.getWorld()) {
                           SkyHologram.createHologram(var15);
                        }
                     }
                     var3.sendMessage("§aHolograma agregado (" + var4.size() + ")");
                     break;
                  case 1:
                     var4.addAll(ConfigManager.score.getStringList("hologram.locations"));
                     if (var4.isEmpty()) {
                        var3.sendMessage("§c¡Este servidor no tiene hologramas!");
                        return true;
                     }
                     int var7;
                     if (var2.length == 1) {
                        var7 = var4.size();
                     } else {
                        var7 = Integer.parseInt(var2[0]);
                     }
                     var4.remove(var7 - 1);
                     ConfigManager.score.set("hologram.locations", var4);
                     ConfigManager.score.save();
                     SkyWars.hologram.clear();
                     Iterator var8 = ConfigManager.score.getStringList("hologram.locations").iterator();
                     while(var8.hasNext()) {
                        String var9 = (String)var8.next();
                        SkyWars.hologram.add(LocationUtil.getLocation(var9));
                     }
                     var8 = Bukkit.getOnlinePlayers().iterator();
                     while(var8.hasNext()) {
                        Player var14 = (Player)var8.next();
                        SkyPlayer var10 = SkyWars.getSkyPlayer(var14);
                        if (var3.getWorld() == var14.getWorld()) {
                           SkyHologram.createHologram(var10);
                        }
                     }
                     var3.sendMessage("§aEl holograma #" + var7 + " ha sido eliminado");
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
      String var2 = "&a/sw hologram &a- &bPara obtener más ayuda sobre configurar hologramas";
      return var1.hasPermission(this.getPermission()) ? var2 : "";
   }

   public String getPermission() {
      return "skywars.admin.hologram";
   }

   public boolean console() {
      return false;
   }

   public void helpDefault(CommandSender var1) {
      HashMap var2 = new HashMap();
      var2.put("add", "&a/sw hologram &eadd &a- &bAgregar nuevo holograma");
      var2.put("remove", "&a/sw hologram &eremove &d[#] &a- &bEliminar el último o un holograma específico");
      var1.sendMessage("--------------------------------------------");
      Iterator var3 = var2.entrySet().iterator();
      while(var3.hasNext()) {
         Entry var4 = (Entry)var3.next();
         if (var1.hasPermission("skywars.admin.hologram." + (String)var4.getKey())) {
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
         var3.put("add", "&a/sw hologram &eadd &a- &bAgregar holograma del lobby");
         var3.put("remove", "&a/sw hologram &eremove &d[#] &a- &bEliminar el último o un holograma específico");
         String[] var4 = new String[]{"add", "remove"};
         ArrayList var5 = new ArrayList(Arrays.asList(var4));
         ArrayList var6 = new ArrayList();
         StringUtil.copyPartialMatches(var2[0], var5, var6);
         Collections.sort(var6);
         var1.sendMessage("--------------------------------------------");
         Iterator var7 = var3.entrySet().iterator();
         while(var7.hasNext()) {
            Entry var8 = (Entry)var7.next();
            if (var6.contains(var8.getKey()) && var1.hasPermission("skywars.admin.hologram." + (String)var8.getKey())) {
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
