package me.aitor.skywars.events;

import me.aitor.skywars.SkyWars;
import me.aitor.skywars.arena.Arena;
import me.aitor.skywars.events.enums.SpectatorReason;
import me.aitor.skywars.player.SkyPlayer;
import me.aitor.skywars.utils.Utils19;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class EventsManager implements Listener {
   @EventHandler
   public void onPlayerDeath(PlayerDeathEvent var1) {
      Player var2 = var1.getEntity();
      SkyPlayer var3 = SkyWars.getSkyPlayer(var2);
      Player var4 = var1.getEntity().getKiller();
      SkyPlayer var5;
      if (var4 == null) {
         var5 = null;
      } else {
         var5 = SkyWars.getSkyPlayer(var4);
      }

      if (var3 != null) {
         if (var3.isInArena()) {
            Arena var6 = var3.getArena();
            var1.setDeathMessage((String)null);
            var3.setSpectating(true, SpectatorReason.DEATH);
            if (SkyWars.is18orHigher()) {
               var2.setHealth(SkyWars.is19orHigher() ? Utils19.getMaxHealth(var2) : var2.getMaxHealth());
               Bukkit.getScheduler().runTaskLater(SkyWars.getPlugin(), () -> {
                  var2.spigot().respawn();
               }, 1L);
            }

            SkyPlayerDeathEvent var7 = new SkyPlayerDeathEvent(var3, var5, var6, var1);
            Bukkit.getServer().getPluginManager().callEvent(var7);
         }

      }
   }

   @EventHandler
   public void onPlayerRespawn(PlayerRespawnEvent var1) {
      Player var2 = var1.getPlayer();
      SkyPlayer var3 = SkyWars.getSkyPlayer(var2);
      if (var3 != null) {
         if (var3.isInArena()) {
            Arena var4 = var3.getArena();
            var1.setRespawnLocation(var4.getSpawn());
         } else {
            var1.setRespawnLocation(SkyWars.getSpawn());
         }

      }
   }
}
