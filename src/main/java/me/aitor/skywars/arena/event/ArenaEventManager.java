package me.aitor.skywars.arena.event;

import me.aitor.skywars.arena.Arena;
import java.util.ArrayList;
import java.util.Iterator;

public class ArenaEventManager {
   public static ArenaEvent[] getArenaEvents(Arena var0) {
      ArrayList var1 = new ArrayList();

      String[] var4;
      EventType var5;
      String var6;
      for(Iterator var2 = var0.getConfig().getStringList("events").iterator(); var2.hasNext(); var1.add(new ArenaEvent(var5, var6, Integer.parseInt(var4[1]), var4[2]))) {
         String var3 = (String)var2.next();
         var4 = var3.split(",");
         var5 = EventType.valueOf(var4[0].split(":")[0]);
         var6 = null;
         if (var4[0].split(":").length > 1) {
            var6 = var4[0].split(":")[1];
         }
      }

      return (ArenaEvent[])var1.toArray(new ArenaEvent[var1.size()]);
   }
}
