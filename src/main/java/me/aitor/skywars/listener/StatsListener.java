package me.aitor.skywars.listener;

import me.aitor.skywars.SkyWars;
import me.aitor.skywars.arena.Arena;
import me.aitor.skywars.arena.ArenaState;
import me.aitor.skywars.player.SkyPlayer;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class StatsListener implements Listener {
   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   public void onArrowShot(EntityShootBowEvent var1) {
      if (var1.getEntity() instanceof Player) {
         Player var2 = (Player)var1.getEntity();
         SkyPlayer var3 = SkyWars.getSkyPlayer(var2);
         if (var3 == null) {
            return;
         }

         if (var1.getProjectile() instanceof Arrow && var3.isInArena()) {
            Arena var4 = var3.getArena();
            if (var4.getState() == ArenaState.INGAME) {
               var3.addArrowShot(1);
            }
         }
      }

   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   public void onArrowHit(EntityDamageByEntityEvent var1) {
      if (var1.getDamager() instanceof Arrow) {
         Arrow var2 = (Arrow)var1.getDamager();
         if (var2.getShooter() instanceof Player && var1.getEntity() instanceof Player) {
            Player var3 = (Player)var2.getShooter();
            SkyPlayer var4 = SkyWars.getSkyPlayer(var3);
            if (var4 == null) {
               return;
            }

            if (var4.isInArena()) {
               Arena var5 = var4.getArena();
               if (var5.getState() == ArenaState.INGAME) {
                  var4.addArrowHit(1);
               }
            }
         }
      }

   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   public void onBlockBroken(BlockBreakEvent var1) {
      Player var2 = var1.getPlayer();
      SkyPlayer var3 = SkyWars.getSkyPlayer(var2);
      if (var3 != null) {
         if (var3.isInArena()) {
            Arena var4 = var3.getArena();
            if (var4.getState() == ArenaState.INGAME) {
               var3.addBlocksBroken(1);
            }
         }

      }
   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   public void onBlockPlaced(BlockPlaceEvent var1) {
      Player var2 = var1.getPlayer();
      SkyPlayer var3 = SkyWars.getSkyPlayer(var2);
      if (var3 != null) {
         if (var3.isInArena()) {
            Arena var4 = var3.getArena();
            if (var4.getState() == ArenaState.INGAME) {
               var3.addBlocksPlaced(1);
            }
         }

      }
   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   public void onDistanceWalked(PlayerMoveEvent var1) {
      Player var2 = var1.getPlayer();
      SkyPlayer var3 = SkyWars.getSkyPlayer(var2);
      if (var3 != null) {
         if (var3.isInArena()) {
            Arena var4 = var3.getArena();
            if (var4.getState() == ArenaState.INGAME) {
               double var5 = var1.getFrom().distance(var1.getTo());
               if (var1.getFrom().getX() == var1.getTo().getX() && var1.getFrom().getY() == var1.getTo().getY() && var1.getFrom().getZ() == var1.getTo().getZ()) {
                  return;
               }

               if (var1.getFrom().getY() < var1.getTo().getY() && var1.getFrom().getX() == var1.getTo().getX() && var1.getFrom().getZ() == var1.getTo().getZ()) {
                  return;
               }

               if (var1.getFrom().getY() > var1.getTo().getY()) {
                  return;
               }

               if (var1.getPlayer().isFlying()) {
                  return;
               }

               if (var1.getPlayer().isInsideVehicle()) {
                  return;
               }

               if ((var1.getFrom().getBlock().getType().equals(Material.WATER) || var1.getFrom().getBlock().getType().equals(Material.STATIONARY_WATER)) && var1.getFrom().getY() != var1.getTo().getY() && (var1.getTo().getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.WATER) || var1.getTo().getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.STATIONARY_WATER))) {
                  return;
               }

               var3.addData("local_distance", var3.getDouble("local_distance") + var5);
            }
         }

      }
   }
}
