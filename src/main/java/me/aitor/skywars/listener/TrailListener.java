package me.aitor.skywars.listener;

import me.aitor.skywars.SkyWars;
import me.aitor.skywars.arena.Arena;
import me.aitor.skywars.arena.ArenaState;
import me.aitor.skywars.player.SkyPlayer;
import me.aitor.skywars.utils.Utils19;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;

public class TrailListener implements Listener {
   @EventHandler
   public void trail(EntityShootBowEvent var1) {
      LivingEntity var2 = var1.getEntity();
      Entity var3 = var1.getProjectile();
      if (var3 instanceof Projectile) {
         Projectile var4 = (Projectile)var3;
         if (var4 instanceof Arrow && var2 instanceof Player) {
            Player var5 = (Player)var2;
            SkyPlayer var6 = SkyWars.getSkyPlayer(var5);
            if (var6.isInArena()) {
               Arena var7 = var6.getArena();
               if (var7.getState() == ArenaState.INGAME && var6.hasTrail()) {
                  this.trail(var6.getTrail(), var4);
               }
            }
         }
      }

   }

   private void trail(String var1, Projectile var2) {
      if (!var2.isOnGround() && !var2.isDead()) {
         Location var3 = var2.getLocation();
         Bukkit.getScheduler().runTaskLater(SkyWars.getPlugin(), () -> {
            if (SkyWars.is19orHigher()) {
               Utils19.spawnParticle(var1, var3, 5, 0.1D, 0.1D, 0.1D, 0.3D);
            } else {
               Effect var4 = Effect.getByName(var1);
               if (var4 == null) {
                  String var5 = var1.toUpperCase();
                  byte var6 = -1;
                  switch(var5.hashCode()) {
                  case 2402290:
                     if (var5.equals("NOTE")) {
                        var6 = 5;
                     }
                     break;
                  case 15786612:
                     if (var5.equals("REDSTONE")) {
                        var6 = 4;
                     }
                     break;
                  case 62462639:
                     if (var5.equals("WATER_SPLASH")) {
                        var6 = 2;
                     }
                     break;
                  case 66975507:
                     if (var5.equals("FLAME")) {
                        var6 = 1;
                     }
                     break;
                  case 78988968:
                     if (var5.equals("SLIME")) {
                        var6 = 0;
                     }
                     break;
                  case 1272362666:
                     if (var5.equals("DRIP_LAVA")) {
                        var6 = 3;
                     }
                  }

                  switch(var6) {
                  case 0:
                     var4 = Effect.SLIME;
                     break;
                  case 1:
                     var4 = Effect.FLAME;
                     break;
                  case 2:
                     var4 = Effect.WATERDRIP;
                     break;
                  case 3:
                     var4 = Effect.LAVADRIP;
                     break;
                  case 4:
                     var4 = Effect.COLOURED_DUST;
                     break;
                  case 5:
                     var4 = Effect.NOTE;
                  }
               }

               if (var4 == null) {
                  return;
               }

               var3.getWorld().spigot().playEffect(var3, var4, 0, 0, 0.1F, 0.1F, 0.1F, 0.3F, 5, 100);
            }

            this.trail(var1, var2);
         }, 1L);
      }
   }
}
