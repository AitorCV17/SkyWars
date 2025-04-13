package me.aitor.skywars.utils.variable;

import me.aitor.skywars.SkyWars;
import me.aitor.skywars.arena.event.ArenaEvent;
import me.aitor.skywars.player.SkyPlayer;
import me.aitor.skywars.utils.MSG;

public final class VariablesDefault implements VariableReplacer {
   @Variable(
      vars = {"PLAYER", "USERNAME", "NAME"}
   )
   public String nameVar(SkyPlayer var1) {
      return var1.getName();
   }

   @Variable(
      vars = {"COINS", "POINTS"}
   )
   public String coinsVar(SkyPlayer var1) {
      return String.valueOf(var1.getCoins());
   }

   @Variable(
      vars = {"A-NAME", "A_NAME", "ARENA-NAME", "ARENA_NAME"}
   )
   public String arenaNameVar(SkyPlayer var1) {
      return var1.isInArena() ? var1.getArena().getName() : "NONE";
   }

   @Variable(
      vars = {"A-MAX-P", "A_MAX_P", "ARENA-MAX-PLAYERS", "ARENA_MAX_PLAYERS"}
   )
   public String arenaMaxPlayersVar(SkyPlayer var1) {
      return var1.isInArena() ? String.valueOf(var1.getArena().getMaxPlayers()) : "NONE";
   }

   @Variable(
      vars = {"A-INGAME-P", "A_INGAME_P", "ARENA-INGAME-PLAYERS", "ARENA_INGAME_PLAYERS"}
   )
   public String arenaInGamePlayersVar(SkyPlayer var1) {
      return var1.isInArena() ? String.valueOf(var1.getArena().getAlivePlayers()) : "NONE";
   }

   @Variable(
      vars = {"ARENA-EVENT-TITLE", "A-EVENT-TITLE"}
   )
   public String arenaCurrentEventTitleVar(SkyPlayer var1) {
      return var1.isInArena() ? String.valueOf(((ArenaEvent)var1.getArena().getEvents().get(0)).getTitle()) : "NONE";
   }

   @Variable(
      vars = {"ARENA-EVENT-TIME", "A-EVENT-TIME"}
   )
   public String arenaCurrentEventTimeVar(SkyPlayer var1) {
      return var1.isInArena() ? String.valueOf(((ArenaEvent)var1.getArena().getEvents().get(0)).getTime()) : "NONE";
   }

   @Variable(
      vars = {"SW-DEATHS", "STATS_DEATHS", "STATS-DEATHS", "SW-D"}
   )
   public String swDeathsVar(SkyPlayer var1) {
      return String.valueOf(var1.getDeaths());
   }

   @Variable(
      vars = {"SW-KILLS", "STATS_KILLS", "STATS-KILLS", "SW-K"}
   )
   public String swKillsVar(SkyPlayer var1) {
      return String.valueOf(var1.getKills());
   }

   @Variable(
      vars = {"SW-WINS", "STATS_WINS", "STATS-WINS", "SW-W"}
   )
   public String swWinsVar(SkyPlayer var1) {
      return String.valueOf(var1.getWins());
   }

   @Variable(
      vars = {"SW-PLAYED", "STATS_PLAYED", "STATS-PLAYED", "SW-P"}
   )
   public String swPlayedVar(SkyPlayer var1) {
      return String.valueOf(var1.getPlayed());
   }

   @Variable(
      vars = {"SW-ARROW-SHOT", "STATS_ARROW_SHOT", "STATS-ARROW-SHOT", "SW-AS"}
   )
   public String swArrowShotVar(SkyPlayer var1) {
      return String.valueOf(var1.getArrowShot());
   }

   @Variable(
      vars = {"SW-ARROW-HIT", "STATS_ARROW_HIT", "STATS-ARROW-HIT", "SW-AH"}
   )
   public String swArrowHitVar(SkyPlayer var1) {
      return String.valueOf(var1.getArrowHit());
   }

   @Variable(
      vars = {"SW-BLOKS-BROKEN", "STATS_BLOCKS_BROKEN", "STATS-BLOCKS-BROKEN", "SW-BB"}
   )
   public String swBlockBrokenVar(SkyPlayer var1) {
      return String.valueOf(var1.getBlocksBroken());
   }

   @Variable(
      vars = {"SW-BLOKS-PLACED", "STATS_BLOCKS_PLACED", "STATS-BLOCKS-PLACED", "SW-BP"}
   )
   public String swBlockPlacedVar(SkyPlayer var1) {
      return String.valueOf(var1.getBlocksPlaced());
   }

   @Variable(
      vars = {"SW-DISTANCE-WALKED", "STATS_DISTANCE_WALKED", "STATS-DISTANCE-WALKED", "SW-DW"}
   )
   public String swDistanceWalkedVar(SkyPlayer var1) {
      return String.valueOf(var1.getDistanceWalked());
   }

   @Variable(
      vars = {"SW-TIME-PLAYED", "STATS_TIME_PLAYED", "STATS-TIME-PLAYED", "SW-TP"}
   )
   public String swTimePlayedVar(SkyPlayer var1) {
      int var2 = var1.getTimePlayed();
      int var3 = var2 % 86400 % 3600 % 60;
      int var4 = var2 % 86400 % 3600 / 60;
      int var5 = var2 % 86400 / 3600;
      int var6 = var2 / 86400;
      boolean var7 = true;
      boolean var8 = true;
      boolean var9 = true;
      boolean var10 = true;
      if (var3 == 1) {
         var7 = false;
      }

      if (var4 == 1) {
         var8 = false;
      }

      if (var5 == 1) {
         var9 = false;
      }

      if (var6 == 1) {
         var10 = false;
      }

      String var11 = var7 ? SkyWars.getMessage(MSG.VARIABLE_TIMEPLAYED_SECONDS) : SkyWars.getMessage(MSG.VARIABLE_TIMEPLAYED_SECOND);
      String var12 = String.format(var11, var3);
      String var13 = var8 ? SkyWars.getMessage(MSG.VARIABLE_TIMEPLAYED_MINUTES) : SkyWars.getMessage(MSG.VARIABLE_TIMEPLAYED_MINUTE);
      String var14 = String.format(var13, var4);
      String var15 = var9 ? SkyWars.getMessage(MSG.VARIABLE_TIMEPLAYED_HOURS) : SkyWars.getMessage(MSG.VARIABLE_TIMEPLAYED_HOUR);
      String var16 = String.format(var15, var5);
      String var17 = var10 ? SkyWars.getMessage(MSG.VARIABLE_TIMEPLAYED_DAYS) : SkyWars.getMessage(MSG.VARIABLE_TIMEPLAYED_DAY);
      String var18 = String.format(var17, var6);
      if (var6 == 0) {
         var18 = "";
      }

      if (var5 == 0) {
         var16 = "";
      }

      if (var4 == 0) {
         var14 = "";
      }

      String var19 = SkyWars.getMessage(MSG.VARIABLE_TIMEPLAYED).replace("{SECONDS}", var12).replace("{MINUTES}", var14).replace("{HOURS}", var16).replace("{DAYS}", var18);
      return var19;
   }

   @Variable(
      vars = {"SW-KILLS-DEATHS", "STATS_KILLS_DEATHS", "STATS-KILLS-DEATHS", "SW-KDR"}
   )
   public String swKDRVar(SkyPlayer var1) {
      double var2 = (double)var1.getKills() / (double)var1.getDeaths();
      return String.valueOf(String.format("%.2f", var2));
   }

   @Variable(
      vars = {"SW-KILL-STREAK", "STATS_KILL-STREAK", "STATS-KILL-STREAK", "SW-KS"}
   )
   public String swKillStreakVar(SkyPlayer var1) {
      return var1.isInArena() ? String.valueOf(var1.getArena().getKillStreak(var1)) : "0";
   }

   @Variable(
      vars = {"GLASS"}
   )
   public String glassSelectedVar(SkyPlayer var1) {
      return SkyWars.boxes.getString("boxes." + var1.getBoxSection() + ".name");
   }

   @Variable(
      vars = {"EMPTY"}
   )
   public String emptyVar(SkyPlayer var1) {
      return " ";
   }
}
