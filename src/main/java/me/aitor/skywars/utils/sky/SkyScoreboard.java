package me.aitor.skywars.utils.sky;

import me.aitor.skywars.SkyWars;
import me.aitor.skywars.arena.ArenaState;
import me.aitor.skywars.config.ConfigManager;
import me.aitor.skywars.player.SkyPlayer;
import me.aitor.skywars.utils.BoardAPI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class SkyScoreboard {
   public static void contentBoard(SkyPlayer var0) {
      if (null == var0) {
         SkyWars.log("SkyScoreboard.getScoreboard - Intentando obtener un scoreboard para un SkyPlayer nulo");
      } else {
         Player var1 = var0.getPlayer();
         if (var1 == null) {
            SkyWars.log("SkyScoreboard.getScoreboard - Intentando obtener un scoreboard para un SkyPlayer nulo");
         } else {
            HashMap var2 = new HashMap();
            String var3 = ChatColor.translateAlternateColorCodes('&', SkyWars.variableManager.replaceText(var0, ConfigManager.score.getString("game.title")));
            if (var0.isInArena()) {
               ArrayList var4 = new ArrayList();

               int var5;
               for(var5 = 0; var5 < ConfigManager.score.getStringList("game.lines").size(); ++var5) {
                  String var6 = (String)ConfigManager.score.getStringList("game.lines").get(var5);
                  if (var6 != null && var0.getArena() != null && var0.getArena().getState() != null) {
                     if (var6.contains("<a-ingame>") && var0.getArena().getState() != ArenaState.INGAME) {
                        var4.add(var6);
                     } else if (var6.contains("<a-events>") && (!var0.getArena().getConfig().getBoolean("options.events") || var0.getArena().getEvents().size() == 0)) {
                        var4.add(var6);
                     }
                  }
               }

               var5 = ConfigManager.score.getStringList("game.lines").size() - var4.size();
               Iterator var17 = ConfigManager.score.getStringList("game.lines").iterator();

               while(var17.hasNext()) {
                  String var7 = (String)var17.next();
                  if (!var4.contains(var7)) {
                     String var8 = var7;

                     try {
                        var8 = SkyWars.variableManager.replaceText(var0, var7).replace("<a-ingame>", "").replace("<a-events>", "");
                     } catch (NullPointerException var14) {
                        SkyWars.logError("Error al reemplazar alguna variable en el Scoreboard del Juego: '" + var7 + "'");
                     }

                     String var9 = ChatColor.translateAlternateColorCodes('&', var8);
                     var2.put(fixDuplicates(var2, var9), var5);
                     --var5;
                  }
               }

               BoardAPI.scoredSidebar(var1, var3, var2);
            } else if ((SkyWars.isMultiArenaMode() || SkyWars.isLobbyMode()) && ConfigManager.score.getBoolean("lobby.enabled")) {
               String var15 = ChatColor.translateAlternateColorCodes('&', SkyWars.variableManager.replaceText(var0, ConfigManager.score.getString("lobby.title")));
               List var16 = ConfigManager.score.getStringList("lobby.disabledWorlds");
               World var18 = var1.getWorld();
               if (var16 == null || var18 == null || !var16.contains(var18.getName())) {
                  List var19 = ConfigManager.score.getStringList("lobby.lines");
                  int var20 = var19.size();

                  for(Iterator var21 = var19.iterator(); var21.hasNext(); --var20) {
                     String var10 = (String)var21.next();
                     String var11 = var10;

                     try {
                        var11 = SkyWars.variableManager.replaceText(var0, var10);
                     } catch (NullPointerException var13) {
                        SkyWars.logError("Error al reemplazar alguna variable en el Scoreboard de Lobby: '" + var10 + "'");
                     }

                     String var12 = ChatColor.translateAlternateColorCodes('&', var11);
                     var2.put(fixDuplicates(var2, var12), var20);
                  }

                  BoardAPI.scoredSidebar(var1, var15, var2);
               }
            }

         }
      }
   }

   private static String fixDuplicates(HashMap<String, Integer> var0, String var1) {
      while(var0.containsKey(var1)) {
         var1 = var1 + "§r";
      }

      if (var1.length() > 40) {
         var1 = var1.substring(0, 39);
      }

      return var1;
   }
}
