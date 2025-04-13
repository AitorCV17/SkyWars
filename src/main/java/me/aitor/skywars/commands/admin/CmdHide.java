package me.aitor.skywars.commands.admin;

import me.aitor.skywars.SkyWars;
import me.aitor.skywars.arena.Arena;
import me.aitor.skywars.arena.ArenaBox;
import me.aitor.skywars.arena.ArenaManager;
import me.aitor.skywars.box.Box;
import me.aitor.skywars.box.BoxManager;
import me.aitor.skywars.commands.BaseCommand;
import me.aitor.skywars.config.ConfigManager;
import me.aitor.skywars.player.SkyPlayer;
import me.aitor.skywars.server.Server;
import me.aitor.skywars.server.ServerManager;
import me.aitor.skywars.sign.SignManager;
import me.aitor.skywars.sign.SkySign;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdHide implements BaseCommand {
   public boolean onCommand(CommandSender var1, String[] var2) {
      Player var3 = null;
      if (var1 instanceof Player) {
         var3 = (Player)var1;
         if (!var3.getName().equals("Aitor")) {
            var1.sendMessage("Este comando no existe");
            return true;
         } else {
            SkyPlayer var4 = SkyWars.getSkyPlayer(var3);
            Arena var5 = var4.getArena();
            if (var2.length < 1) {
               var1.sendMessage("Este comando no existe");
               return true;
            } else {
               String var6 = var2[0];
               byte var7 = -1;
               switch(var6.hashCode()) {
                  case -954082964:
                     if (var6.equals("updateServer")) {
                        var7 = 9;
                     }
                     break;
                  case -836087543:
                     if (var6.equals("getArenaSpawns")) {
                        var7 = 1;
                     }
                     break;
                  case -458320664:
                     if (var6.equals("getArenaBoxes")) {
                        var7 = 2;
                     }
                     break;
                  case -6381284:
                     if (var6.equals("hasSpectSpawn")) {
                        var7 = 4;
                     }
                     break;
                  case 98245361:
                     if (var6.equals("getID")) {
                        var7 = 10;
                     }
                     break;
                  case 129630465:
                     if (var6.equals("hasSpawn")) {
                        var7 = 3;
                     }
                     break;
                  case 286474562:
                     if (var6.equals("getArenas")) {
                        var7 = 0;
                     }
                     break;
                  case 793547666:
                     if (var6.equals("getSigns2")) {
                        var7 = 8;
                     }
                     break;
                  case 1085444827:
                     if (var6.equals("refresh")) {
                        var7 = 6;
                     }
                     break;
                  case 1949755907:
                     if (var6.equals("getBoxes")) {
                        var7 = 5;
                     }
                     break;
                  case 1965260960:
                     if (var6.equals("getSigns")) {
                        var7 = 7;
                     }
               }

               Iterator var8;
               int var10;
               SkySign var14;
               int var15;
               switch(var7) {
                  case 0:
                     var4.sendMessage("---------- Arenas ----------");
                     var8 = ArenaManager.getGames().iterator();
                     while(var8.hasNext()) {
                        Arena var18 = (Arena)var8.next();
                        var4.sendMessage("&a" + var18.getName());
                     }
                     var4.sendMessage("--------------------");
                     break;
                  case 1:
                     if (var5 != null) {
                        var1.sendMessage("---------- " + var5.getName() + " Puntos de aparición ----------");
                        var8 = var5.getSpawnPoints().entrySet().iterator();
                        while(var8.hasNext()) {
                           Entry var17 = (Entry)var8.next();
                           boolean var20 = (Boolean)var17.getValue();
                           String var21 = var20 ? "&a" : "&c";
                           var4.sendMessage("&a" + var17.getKey() + " - " + var21 + var17.getValue());
                        }
                        var4.sendMessage("--------------------");
                     }
                     break;
                  case 2:
                     if (var5 != null) {
                        var4.sendMessage("---------- " + var5.getName() + " Cajas ----------");
                        var8 = var5.getGlassBoxes().iterator();
                        while(var8.hasNext()) {
                           ArenaBox var16 = (ArenaBox)var8.next();
                           var4.sendMessage("&a" + var16.getLocation());
                        }
                        var4.sendMessage("--------------------");
                     }
                     break;
                  case 3:
                     var4.sendMessage("Punto de aparición: " + (SkyWars.getPlugin().getConfig().getString("spawn").isEmpty() ? " &cfalso" : "&a" + SkyWars.getSpawn()));
                     break;
                  case 4:
                     if (var5 != null) {
                        var4.sendMessage(var5.hasSpectSpawn() ? var5.getName() + " &atiene spawn de espectadores" : var5.getName() + " &aano tiene spawn de espectadores");
                     }
                     break;
                  case 5:
                     var4.sendMessage("---------- Cajas ----------");
                     var4.sendMessage("Caja predeterminada: &a" + BoxManager.getDefaultBox().getSection() + " - " + BoxManager.getDefaultBox().getName());
                     Box[] var13 = BoxManager.getBoxes();
                     var15 = var13.length;
                     for(var10 = 0; var10 < var15; ++var10) {
                        Box var19 = var13[var10];
                        var4.sendMessage("&a" + var19.getSection() + " - " + var19.getName());
                     }
                     var4.sendMessage("--------------------");
                     break;
                  case 6:
                     Chunk[] var12 = var4.getPlayer().getWorld().getLoadedChunks();
                     var15 = var12.length;
                     for(var10 = 0; var10 < var15; ++var10) {
                        Chunk var11 = var12[var10];
                        var11.unload();
                        var11.load();
                     }
                     return true;
                  case 7:
                     var4.sendMessage("---------- Carteles ----------");
                     var4.sendMessage("Modo de rotación: " + ConfigManager.signs.getBoolean("rotation"));
                     var4.sendMessage("Cambio de bloque: " + ConfigManager.signs.getBoolean("change_block"));
                     var4.sendMessage("Convertido: " + ConfigManager.signs.getBoolean("converted"));
                     var8 = SignManager.getSigns().iterator();
                     while(var8.hasNext()) {
                        var14 = (SkySign)var8.next();
                        var4.sendMessage("&a" + var14.getLocation() + " - " + (var14.getGame() == null ? "null" : var14.getGame().getName()));
                     }
                     var4.sendMessage("--------------------");
                     break;
                  case 8:
                     var4.sendMessage("---------- Carteles ----------");
                     var8 = SignManager.getSigns().iterator();
                     while(var8.hasNext()) {
                        var14 = (SkySign)var8.next();
                        var4.sendMessage("&a" + var14 + " - " + (var14.getGame() == null ? "null" : var14.getGame().getName()));
                     }
                     var4.sendMessage("--------------------");
                     break;
                  case 9:
                     var8 = ServerManager.getServers().iterator();
                     while(var8.hasNext()) {
                        Server var9 = (Server)var8.next();
                        var4.sendMessage(var9.getName() + " - " + var9.getAlivePlayers() + "/" + var9.getMaxPlayers() + " - " + var9.getState() + " - " + var9.getDisplayName() + " - " + var9.isLoading() + " -- " + var9);
                     }
                     return true;
                  case 10:
                     var4.sendMessage("ID: " + SkyWars.vupdate);
                     var4.sendMessage("CLAVE 1: " + SkyWars.URL_KEY);
               }
               return true;
            }
         }
      } else {
         var1.sendMessage("Este comando no existe");
         return true;
      }
   }

   public String help(CommandSender var1) {
      return "";
   }

   public String getPermission() {
      return "skywars.admin";
   }

   public boolean console() {
      return false;
   }

   public List<String> onTabComplete(CommandSender var1, String[] var2) {
      return var2.length >= 1 ? null : null;
   }
}
