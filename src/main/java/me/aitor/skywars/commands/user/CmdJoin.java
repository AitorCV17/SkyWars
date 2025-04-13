package me.aitor.skywars.commands.user;

import me.aitor.skywars.SkyWars;
import me.aitor.skywars.arena.Arena;
import me.aitor.skywars.arena.ArenaManager;
import me.aitor.skywars.arena.ArenaState;
import me.aitor.skywars.arena.GameQueue;
import me.aitor.skywars.commands.BaseCommand;
import me.aitor.skywars.events.enums.ArenaJoinCause;
import me.aitor.skywars.player.SkyPlayer;
import me.aitor.skywars.utils.BungeeUtils;
import me.aitor.skywars.utils.Game;
import me.aitor.skywars.utils.MSG;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

public class CmdJoin implements BaseCommand {
   public boolean onCommand(CommandSender var1, String[] var2) {
      if (!(var1 instanceof Player)) {
         var1.sendMessage("¡No eres un jugador!");
         return true;
      } else {
         Player var3 = (Player)var1;
         SkyPlayer var4 = SkyWars.getSkyPlayer(var3);
         if (var4 == null) {
            return false;
         } else if (!var3.hasPermission(this.getPermission())) {
            var3.sendMessage("§c¡No tienes permiso!");
            return true;
         } else {
            Arena var6;
            if (var2.length == 0) {
               if (!var4.isInArena()) {
                  if (GameQueue.withoutGames()) {
                     GameQueue.addPlayer(var4);
                     var4.sendMessage("&cNo hay juegos disponibles, has sido añadido a la cola");
                     return true;
                  }

                  Game var5 = GameQueue.getJoinableGame();
                  if (var5 == null) {
                     GameQueue.addPlayer(var4);
                     var4.sendMessage("&cNo hay juegos disponibles, has sido añadido a la cola");
                     return true;
                  }

                  if (SkyWars.isMultiArenaMode()) {
                     var6 = (Arena)var5;
                     var6.addPlayer(var4, ArenaJoinCause.COMMAND);
                  } else if (SkyWars.isLobbyMode()) {
                     BungeeUtils.teleToServer(var4.getPlayer(), "", var5.getName());
                  }

                  return true;
               }
            } else if (var2.length == 1 && SkyWars.isMultiArenaMode() && !var4.isInArena()) {
               String var7 = var2[0];
               var6 = ArenaManager.getGame(var7);
               if (var6 == null) {
                  var4.sendMessage("&cThis arena doesn't exists".replace("This arena doesn't exists", "¡Esta arena no existe!"));
                  return false;
               }

               if (var6.getState() == ArenaState.INGAME && !var3.hasPermission("skywars.admin.spectate")) {
                  var4.sendMessage(SkyWars.getMessage(MSG.GAME_INGAME_MESSAGE));
                  return false;
               }

               if (var6.getAlivePlayers() >= var6.getMaxPlayers() && !var3.hasPermission("skywars.admin.spectate")) {
                  var4.sendMessage(SkyWars.getMessage(MSG.GAME_FULL_MESSAGE));
                  return false;
               }

               if (var6.isLoading()) {
                  var4.sendMessage(SkyWars.getMessage(MSG.GAME_LOADING));
                  return false;
               }

               var6.addPlayer(var4, ArenaJoinCause.COMMAND);
               return true;
            }

            return true;
         }
      }
   }

   public String help(CommandSender var1) {
      String var2 = "&a/sw join &e[ArenaName] &a- &bÚnete a una arena aleatoria o específica";
      if (SkyWars.isLobbyMode()) {
         var2 = "&a/sw join &a- &bÚnete a una partida aleatoria";
      }

      return var1.hasPermission(this.getPermission()) ? var2 : "";
   }

   public String getPermission() {
      return "skywars.join";
   }

   public boolean console() {
      return false;
   }

   public List<String> onTabComplete(CommandSender var1, String[] var2) {
      if (var1.hasPermission(this.getPermission()) && !SkyWars.isLobbyMode()) {
         if (var2.length != 1) {
            return null;
         } else {
            ArrayList var3 = new ArrayList();
            ArrayList var4 = new ArrayList();
            Iterator var5 = ArenaManager.getGames().iterator();

            while(true) {
               Arena var6;
               do {
                  if (!var5.hasNext()) {
                     var1.sendMessage("--------------------------------------------");
                     var1.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aJuegos disponibles (&b" + var3.size() + "&a):"));
                     StringUtil.copyPartialMatches(var2[0], var3, var4);
                     Collections.sort(var4);
                     return var4;
                  }

                  var6 = (Arena)var5.next();
               } while(var6.getState() != ArenaState.WAITING && var6.getState() != ArenaState.STARTING);

               if (var6.getAlivePlayers() < var6.getMaxPlayers()) {
                  var3.add(var6.getName());
               }
            }
         }
      } else {
         return null;
      }
   }
}
