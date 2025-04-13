package me.aitor.skywars.arena;

import me.aitor.skywars.SkyWars;
import me.aitor.skywars.events.enums.ArenaJoinCause;
import me.aitor.skywars.player.SkyPlayer;
import me.aitor.skywars.server.Server;
import me.aitor.skywars.server.ServerManager;
import me.aitor.skywars.utils.BungeeUtils;
import me.aitor.skywars.utils.Game;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Map.Entry;
import org.bukkit.scheduler.BukkitRunnable;

public class GameQueue {
   private static final List<SkyPlayer> queue_players = new ArrayList();

   public static void check() {
      (new BukkitRunnable() {
         public void run() {
            if (!GameQueue.inQueue().isEmpty()) {
               Game var1 = GameQueue.getGameCalculated();
               if (var1 == null) {
                  return;
               }

               if (SkyWars.isMultiArenaMode()) {
                  Arena var2 = (Arena)var1;

                  for(int var3 = 0; var3 < var2.getAvailableSlots() && !GameQueue.inQueue().isEmpty(); ++var3) {
                     SkyPlayer var4 = (SkyPlayer)GameQueue.inQueue().get(0);
                     var2.addPlayer(var4, ArenaJoinCause.QUEUE);
                     GameQueue.inQueue().remove(0);
                  }
               } else if (SkyWars.isLobbyMode()) {
                  BungeeUtils.teleToServer(((SkyPlayer)GameQueue.inQueue().get(0)).getPlayer(), "", var1.getName());
                  GameQueue.inQueue().remove(0);
               }
            }

         }
      }).runTaskTimer(SkyWars.getPlugin(), 0L, 20L);
   }

   public static boolean withoutGames() {
      Iterator var0;
      if (SkyWars.isMultiArenaMode()) {
         var0 = ArenaManager.getGames().iterator();

         Arena var1;
         do {
            do {
               if (!var0.hasNext()) {
                  return true;
               }

               var1 = (Arena)var0.next();
            } while(var1.getState() != ArenaState.WAITING && var1.getState() != ArenaState.STARTING);
         } while(var1.isFull());

         return false;
      } else if (SkyWars.isLobbyMode()) {
         var0 = ServerManager.getServers().iterator();

         Server var2;
         do {
            do {
               if (!var0.hasNext()) {
                  return true;
               }

               var2 = (Server)var0.next();
            } while(var2.getState() != ArenaState.WAITING && var2.getState() != ArenaState.STARTING);
         } while(var2.isFull());

         return false;
      } else {
         return true;
      }
   }

   public static Game getGameCalculated() {
      HashMap var0 = new HashMap();
      Game[] var1 = getJoinableGames();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Game var4 = var1[var3];
         double var5 = (double)var4.getAlivePlayers() / (double)var4.getMaxPlayers();
         if (var5 < 1.0D) {
            var0.put(var4, var5);
         }
      }

      ArrayList var7 = new ArrayList();
      double var8 = 0.0D;
      Iterator var9 = var0.entrySet().iterator();

      while(var9.hasNext()) {
         Entry var11 = (Entry)var9.next();
         if ((Double)var11.getValue() == var8) {
            var7.add(var11.getKey());
         }

         if ((Double)var11.getValue() > var8) {
            var8 = (Double)var11.getValue();
            var7.clear();
            var7.add(var11.getKey());
         }
      }

      if (var7.size() == 0) {
         return null;
      } else if (var7.size() == 1) {
         return (Game)var7.get(0);
      } else {
         int var10 = (new Random()).nextInt(var7.size());
         return (Game)var7.get(var10);
      }
   }

   public static Game getJoinableGame() {
      return getGameCalculated();
   }

   public static Game[] getJoinableGames() {
      ArrayList var0 = new ArrayList();
      Iterator var1;
      Game var2;
      if (SkyWars.isMultiArenaMode()) {
         var1 = ArenaManager.getGames().iterator();

         while(true) {
            do {
               do {
                  if (!var1.hasNext()) {
                     return (Game[])var0.toArray(new Game[0]);
                  }

                  var2 = (Game)var1.next();
               } while(var2.getState() != ArenaState.WAITING && var2.getState() != ArenaState.STARTING);
            } while(var2.isLoading() && var2.isFull());

            var0.add(var2);
         }
      } else if (SkyWars.isLobbyMode()) {
         var1 = ServerManager.getServers().iterator();

         while(true) {
            do {
               do {
                  if (!var1.hasNext()) {
                     return (Game[])var0.toArray(new Game[0]);
                  }

                  var2 = (Game)var1.next();
               } while(var2.getState() != ArenaState.WAITING && var2.getState() != ArenaState.STARTING);
            } while(var2.isLoading() && var2.isFull());

            var0.add(var2);
         }
      } else {
         return (Game[])var0.toArray(new Game[0]);
      }
   }

   public static List<SkyPlayer> inQueue() {
      return queue_players;
   }

   public static void addPlayer(SkyPlayer var0) {
      if (!queue_players.contains(var0)) {
         queue_players.add(var0);
      }

   }

   public static void removePlayer(SkyPlayer var0) {
      queue_players.remove(var0);
   }
}
