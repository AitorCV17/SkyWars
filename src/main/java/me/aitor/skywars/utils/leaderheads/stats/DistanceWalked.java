package me.aitor.skywars.utils.leaderheads.stats;

import me.aitor.skywars.SkyWars;
import me.aitor.skywars.player.SkyPlayer;
import java.util.Arrays;
import me.robin.leaderheads.datacollectors.OnlineDataCollector;
import me.robin.leaderheads.objects.BoardType;
import org.bukkit.entity.Player;

public class DistanceWalked extends OnlineDataCollector {
   public DistanceWalked() {
      super("sw-distance-walk", "SkyWars", BoardType.DEFAULT, "SkyWars - Top Distancia Caminada", "swDWalked", Arrays.asList(null, "&9{name}", "&6{amount}", null));
   }

   public Double getScore(Player var1) {
      SkyPlayer var2 = SkyWars.getSkyPlayer(var1);
      return var2 != null ? (double)var2.getDistanceWalked() : null;
   }
}
