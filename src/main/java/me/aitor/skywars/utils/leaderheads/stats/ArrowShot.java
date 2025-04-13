package me.aitor.skywars.utils.leaderheads.stats;

import me.aitor.skywars.SkyWars;
import me.aitor.skywars.player.SkyPlayer;
import java.util.Arrays;
import me.robin.leaderheads.datacollectors.OnlineDataCollector;
import me.robin.leaderheads.objects.BoardType;
import org.bukkit.entity.Player;

public class ArrowShot extends OnlineDataCollector {
   public ArrowShot() {
      super("sw-arrow-shot", "SkyWars", BoardType.DEFAULT, "SkyWars - Top Disparos de Flechas", "swAShot", Arrays.asList(null, "&9{name}", "&6{amount}", null));
   }

   public Double getScore(Player var1) {
      SkyPlayer var2 = SkyWars.getSkyPlayer(var1);
      return var2 != null ? (double)var2.getArrowShot() : null;
   }
}
