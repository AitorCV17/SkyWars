package me.aitor.skywars.listener;

import me.aitor.skywars.SkyWars;
import me.aitor.skywars.arena.Arena;
import me.aitor.skywars.config.ConfigManager;
import me.aitor.skywars.player.SkyPlayer;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class WorldTabListener implements Listener {

   @EventHandler
   public void onWorldChange(PlayerChangedWorldEvent event) {
      if (SkyWars.is18orHigher() && SkyWars.isMultiArenaMode()) {
         Player player = event.getPlayer();
         World toWorld = player.getWorld();
         World fromWorld = event.getFrom();
         SkyPlayer skyPlayer = SkyWars.getSkyPlayer(player);

         Bukkit.getScheduler().runTaskLater(SkyWars.getPlugin(), () -> {
            if (skyPlayer != null && skyPlayer.isInArena()) {
               Arena arena = skyPlayer.getArena();
               Iterator<SkyPlayer> aliveIt = arena.getAlivePlayer().iterator();

               while (aliveIt.hasNext()) {
                  SkyPlayer other = aliveIt.next();
                  if (other != skyPlayer) {
                     other.getPlayer().hidePlayer(player);
                     player.hidePlayer(other.getPlayer());
                  }
               }

               Bukkit.getScheduler().runTaskLater(SkyWars.getPlugin(), () -> {
                  Iterator<SkyPlayer> aliveAgain = arena.getAlivePlayer().iterator();

                  while (aliveAgain.hasNext()) {
                     SkyPlayer otherAgain = aliveAgain.next();
                     if (otherAgain != skyPlayer) {
                        otherAgain.getPlayer().showPlayer(player);
                        player.showPlayer(otherAgain.getPlayer());
                     }
                  }

               }, 20L);
            } else {
               for (Player otherPlayer : toWorld.getPlayers()) {
                  if (otherPlayer != player) {
                     otherPlayer.showPlayer(player);
                     player.showPlayer(otherPlayer);
                  }
               }
            }

         }, 20L);

         for (Player otherPlayer : fromWorld.getPlayers()) {
            if (otherPlayer != player) {
               if (!ConfigManager.main.getBoolean("options.perWorldTabBypass")) {
                  otherPlayer.hidePlayer(player);
                  player.hidePlayer(otherPlayer);
               } else {
                  if (!otherPlayer.hasPermission("skywars.tab.bypass")) {
                     otherPlayer.hidePlayer(player);
                  }
                  if (!player.hasPermission("skywars.tab.bypass")) {
                     player.hidePlayer(otherPlayer);
                  }
               }
            }
         }
      }
   }

   @EventHandler
   public void onJoin(PlayerJoinEvent event) {
      Player player = event.getPlayer();

      if (SkyWars.is18orHigher() && SkyWars.isMultiArenaMode()) {
         World world = player.getWorld();

         for (Player other : Bukkit.getOnlinePlayers()) {
            if (other != player) {
               if (other.getWorld() == world) {
                  other.showPlayer(player);
                  player.showPlayer(other);
               } else if (!ConfigManager.main.getBoolean("options.perWorldTabBypass")) {
                  other.hidePlayer(player);
                  player.hidePlayer(other);
               } else {
                  if (!other.hasPermission("skywars.tab.bypass")) {
                     other.hidePlayer(player);
                  }
                  if (!player.hasPermission("skywars.tab.bypass")) {
                     player.hidePlayer(other);
                  }
               }
            }
         }

      } else if (SkyWars.is18orHigher() && SkyWars.isBungeeMode()) {
         SkyPlayer skyPlayer = SkyWars.getSkyPlayer(player);
         if (skyPlayer != null && skyPlayer.isInArena()) {
            Arena arena = skyPlayer.getArena();

            for (SkyPlayer other : arena.getAlivePlayer()) {
               if (other != skyPlayer) {
                  other.getPlayer().hidePlayer(player);
                  player.hidePlayer(other.getPlayer());
               }
            }

            Bukkit.getScheduler().runTaskLater(SkyWars.getPlugin(), () -> {
               for (SkyPlayer other : arena.getAlivePlayer()) {
                  if (other != skyPlayer) {
                     other.getPlayer().showPlayer(player);
                     player.showPlayer(other.getPlayer());
                  }
               }
            }, 20L);
         }
      }
   }
}
