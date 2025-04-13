package me.aitor.skywars.utils.leaderheads;

import me.aitor.skywars.SkyWars;
import me.aitor.skywars.utils.leaderheads.stats.ArrowHit;
import me.aitor.skywars.utils.leaderheads.stats.ArrowShot;
import me.aitor.skywars.utils.leaderheads.stats.BlocksBroken;
import me.aitor.skywars.utils.leaderheads.stats.BlocksPlaced;
import me.aitor.skywars.utils.leaderheads.stats.Deaths;
import me.aitor.skywars.utils.leaderheads.stats.DistanceWalked;
import me.aitor.skywars.utils.leaderheads.stats.Kills;
import me.aitor.skywars.utils.leaderheads.stats.Played;
import me.aitor.skywars.utils.leaderheads.stats.TimePlayed;
import me.aitor.skywars.utils.leaderheads.stats.Wins;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class LeaderHeadsManager {
   public static void load() {
      Plugin var0 = Bukkit.getPluginManager().getPlugin("LeaderHeads");
      if (var0 != null) {
         new Wins();
         new Kills();
         new Deaths();
         new Played();
         new ArrowShot();
         new ArrowHit();
         new BlocksBroken();
         new BlocksPlaced();
         new TimePlayed();
         new DistanceWalked();
         SkyWars.console(SkyWars.prefix + "&aHook de LeaderHeads habilitado");
      }

   }
}
