package me.aitor.skywars.utils.variable;

import me.aitor.skywars.SkyWars;
import me.aitor.skywars.player.SkyPlayer;
import me.aitor.skywars.utils.MSG;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class VariablesPlaceholder extends PlaceholderExpansion {
   private SkyWars plugin;

   public VariablesPlaceholder(SkyWars var1) {
      this.plugin = var1;
   }

   public boolean persist() {
      return true;
   }

   public boolean canRegister() {
      return true;
   }

   public String getAuthor() {
      return this.plugin.getDescription().getAuthors().toString();
   }

   public String getIdentifier() {
      return "skywars";
   }

   public String getVersion() {
      return this.plugin.getDescription().getVersion();
   }

   public String onPlaceholderRequest(Player var1, String var2) {
      if (var1 == null) {
         return "";
      } else {
         SkyPlayer var3 = SkyWars.getSkyPlayer(var1);
         if (var3 == null) {
            return "";
         } else if (var2.equals("coins")) {
            return String.valueOf(var3.getCoins());
         } else if (var2.equals("stats_deaths")) {
            return String.valueOf(var3.getDeaths());
         } else if (var2.equals("stats_kills")) {
            return String.valueOf(var3.getKills());
         } else if (var2.equals("stats_wins")) {
            return String.valueOf(var3.getWins());
         } else if (var2.equals("stats_played")) {
            return String.valueOf(var3.getPlayed());
         } else if (var2.equals("stats_arrow_shot")) {
            return String.valueOf(var3.getArrowShot());
         } else if (var2.equals("stats_arrow_hit")) {
            return String.valueOf(var3.getArrowHit());
         } else if (var2.equals("stats_blocks_broken")) {
            return String.valueOf(var3.getBlocksBroken());
         } else if (var2.equals("stats_blocks_placed")) {
            return String.valueOf(var3.getBlocksPlaced());
         } else if (var2.equals("stats_distance_walked")) {
            return String.valueOf(var3.getDistanceWalked());
         } else if (var2.equals("stats_time_played")) {
            int var4 = var3.getTimePlayed();
            int var5 = var4 % 86400 % 3600 % 60;
            int var6 = var4 % 86400 % 3600 / 60;
            int var7 = var4 % 86400 / 3600;
            int var8 = var4 / 86400;
            boolean var9 = true;
            boolean var10 = true;
            boolean var11 = true;
            boolean var12 = true;
            if (var5 == 1) {
               var9 = false;
            }

            if (var6 == 1) {
               var10 = false;
            }

            if (var7 == 1) {
               var11 = false;
            }

            if (var8 == 1) {
               var12 = false;
            }

            String var13 = var9 ? SkyWars.getMessage(MSG.VARIABLE_TIMEPLAYED_SECONDS) : SkyWars.getMessage(MSG.VARIABLE_TIMEPLAYED_SECOND);
            String var14 = String.format(var13, var5);
            String var15 = var10 ? SkyWars.getMessage(MSG.VARIABLE_TIMEPLAYED_MINUTES) : SkyWars.getMessage(MSG.VARIABLE_TIMEPLAYED_MINUTE);
            String var16 = String.format(var15, var6);
            String var17 = var11 ? SkyWars.getMessage(MSG.VARIABLE_TIMEPLAYED_HOURS) : SkyWars.getMessage(MSG.VARIABLE_TIMEPLAYED_HOUR);
            String var18 = String.format(var17, var7);
            String var19 = var12 ? SkyWars.getMessage(MSG.VARIABLE_TIMEPLAYED_DAYS) : SkyWars.getMessage(MSG.VARIABLE_TIMEPLAYED_DAY);
            String var20 = String.format(var19, var8);
            if (var8 == 0) {
               var20 = "";
            }

            if (var7 == 0) {
               var18 = "";
            }

            if (var6 == 0) {
               var16 = "";
            }

            String var21 = SkyWars.getMessage(MSG.VARIABLE_TIMEPLAYED).replace("{SECONDS}", var14).replace("{MINUTES}", var16).replace("{HOURS}", var18).replace("{DAYS}", var20);
            return var21;
         } else if (var2.equals("glass")) {
            return SkyWars.boxes.getString("boxes." + var3.getBoxSection() + ".name");
         } else if (var2.equals("arena_name")) {
            return var3.isInArena() ? var3.getArena().getName() : "none";
         } else if (var2.equals("arena_max_players")) {
            return var3.isInArena() ? String.valueOf(var3.getArena().getMaxPlayers()) : "none";
         } else if (var2.equals("arena_ingame_players")) {
            return var3.isInArena() ? String.valueOf(var3.getArena().getAlivePlayers()) : "none";
         } else {
            return null;
         }
      }
   }
}
