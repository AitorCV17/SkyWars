package me.aitor.skywars.api;

import me.aitor.skywars.SkyWars;
import me.aitor.skywars.database.DatabaseHandler;
import me.aitor.skywars.player.SkyPlayer;
import java.util.List;
import java.util.Map.Entry;
import org.bukkit.entity.Player;

public class SkyWarsAPI {
   public static SkyPlayer getSkyPlayer(Player var0) {
      return SkyWars.getSkyPlayer(var0);
   }

   public static int getWins(Player var0) {
      SkyPlayer var1 = SkyWars.getSkyPlayer(var0);
      return var1.getWins();
   }

   public static int getKills(Player var0) {
      SkyPlayer var1 = SkyWars.getSkyPlayer(var0);
      return var1.getKills();
   }

   public static int getDeaths(Player var0) {
      SkyPlayer var1 = SkyWars.getSkyPlayer(var0);
      return var1.getDeaths();
   }

   public static int getPlayed(Player var0) {
      SkyPlayer var1 = SkyWars.getSkyPlayer(var0);
      return var1.getPlayed();
   }

   public static List<Entry<String, Integer>> getTopWins(int var0) {
      return DatabaseHandler.getDS().getTopStats("wins", var0);
   }

   public static List<Entry<String, Integer>> getTopKills(int var0) {
      return DatabaseHandler.getDS().getTopStats("kills", var0);
   }

   public static List<Entry<String, Integer>> getTopDeaths(int var0) {
      return DatabaseHandler.getDS().getTopStats("deaths", var0);
   }

   public static List<Entry<String, Integer>> getTopPlayed(int var0) {
      return DatabaseHandler.getDS().getTopStats("played", var0);
   }
}
