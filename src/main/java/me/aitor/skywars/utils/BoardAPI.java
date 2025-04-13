package me.aitor.skywars.utils;

import me.aitor.skywars.SkyWars;
import java.util.HashMap;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

public class BoardAPI {
   public static void scoredSidebar(Player var0, String var1, HashMap<String, Integer> var2) {
      if (var1 == null) {
         var1 = "Tablero sin nombre";
      }

      if (var1.length() > 32) {
         var1 = var1.substring(0, 32);
      }

      String var4;
      label39:
      while (var2.size() > 16) {
         var4 = (String) var2.keySet().toArray()[0];
         int var5 = var2.get(var4);
         Iterator<String> var6 = var2.keySet().iterator();

         while (true) {
            String var7;
            do {
               if (!var6.hasNext()) {
                  continue label39;
               }
               var7 = var6.next();
            } while (var2.get(var7) >= var5 && (var2.get(var7) != var5 || var7.compareTo(var4) >= 0));

            var4 = var7;
            var5 = var2.get(var7);
         }
      }

      final String scoreboardName = var1;

      Bukkit.getScheduler().runTask(SkyWars.getPlugin(), () -> {
         if (var0 != null && var0.isOnline()) {
            if (Bukkit.getScoreboardManager().getMainScoreboard() != null &&
                    Bukkit.getScoreboardManager().getMainScoreboard() == var0.getScoreboard()) {
               var0.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            }

            if (var0.getScoreboard() == null) {
               var0.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            }

            Bukkit.getScheduler().runTaskAsynchronously(SkyWars.getPlugin(), () -> {
               Objective var3 = var0.getScoreboard().getObjective(DisplaySlot.SIDEBAR);
               if (var3 == null) {
                  var3 = var0.getScoreboard().registerNewObjective(
                          scoreboardName.length() > 16 ? scoreboardName.substring(0, 15) : scoreboardName,
                          "dummy");
               }

               var3.setDisplayName(scoreboardName);
               if (var3.getDisplaySlot() == null || var3.getDisplaySlot() != DisplaySlot.SIDEBAR) {
                  var3.setDisplaySlot(DisplaySlot.SIDEBAR);
               }

               Iterator<String> keyIterator = var2.keySet().iterator();
               while (keyIterator.hasNext()) {
                  String key = keyIterator.next();
                  if (!var3.getScore(key).isScoreSet() || var3.getScore(key).getScore() != var2.get(key)) {
                     var3.getScore(key).setScore(var2.get(key));
                  }
               }

               Iterator<String> scoreboardEntries = var0.getScoreboard().getEntries().iterator();
               while (scoreboardEntries.hasNext()) {
                  String entry = scoreboardEntries.next();
                  if (var3.getScore(entry).isScoreSet() && !var2.containsKey(entry)) {
                     var0.getScoreboard().resetScores(entry);
                  }
               }
            });
         }
      });
   }
}
