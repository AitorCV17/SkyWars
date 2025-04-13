package me.aitor.skywars.listener;

import me.aitor.skywars.SkyWars;
import me.aitor.skywars.arena.Arena;
import me.aitor.skywars.arena.ArenaManager;
import me.aitor.skywars.config.ConfigManager;
import me.aitor.skywars.events.enums.ArenaLeaveCause;
import me.aitor.skywars.player.SkyPlayer;
import me.aitor.skywars.utils.Console;
import me.aitor.skywars.utils.ItemBuilder;
import me.aitor.skywars.utils.Utils;
import java.util.Iterator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.event.world.WorldUnloadEvent;

public class WorldListener implements Listener {
   @EventHandler
   public void onWeather(WeatherChangeEvent var1) {
      Iterator var2 = ArenaManager.getGames().iterator();

      while(var2.hasNext()) {
         Arena var3 = (Arena)var2.next();
         if (var3.getWorld() == var1.getWorld() && !SkyWars.getPlugin().getConfig().getBoolean("options.weather")) {
            if (!var1.isCancelled()) {
               var1.setCancelled(var1.toWeatherState());
            }

            if (var1.getWorld().hasStorm()) {
               var1.getWorld().setWeatherDuration(0);
            }
         }
      }

   }

   @EventHandler
   public void onWorldChange(PlayerChangedWorldEvent var1) {
      Player var2 = var1.getPlayer();
      if (SkyWars.isMultiArenaMode()) {
         SkyPlayer var3 = SkyWars.getSkyPlayer(var2);
         if (var3 != null && var3.isSpectating()) {
            Arena var4 = var3.getArena();
            if (var4 != null) {
               var4.removePlayer(var3, ArenaLeaveCause.WORLD_CHANGE);
            }
         }
      }

      if ((SkyWars.isMultiArenaMode() || SkyWars.isLobbyMode()) && SkyWars.getSpawn().getWorld().getName().equals(var2.getWorld().getName()) && ConfigManager.shop.getBoolean("item.enabled")) {
         ItemBuilder var5 = Utils.readItem(ConfigManager.shop.getString("item.item"));
         var5.setTitle(ConfigManager.shop.getString("item.name")).setLore(ConfigManager.shop.getStringList("item.lore"));
         var2.getInventory().setItem(ConfigManager.shop.getInt("item.inventorySlot"), var5.build());
      }

   }

   @EventHandler
   public void onSaveWorld(WorldSaveEvent var1) {
      Arena var2 = ArenaManager.getGame(var1.getWorld().getName());
      if (var2 != null) {
         if (!var2.isHardReset()) {
            Console.debugWarn(var1.getWorld().getName() + " has forced to hard world reset (due to something is saving the world)");
            var2.setHardReset(true);
         }
      }
   }

   @EventHandler
   public void onMobSpawn(CreatureSpawnEvent var1) {
      Iterator var2 = ArenaManager.getGames().iterator();

      while(true) {
         Arena var3;
         do {
            if (!var2.hasNext()) {
               return;
            }

            var3 = (Arena)var2.next();
         } while(var3.getWorld() != var1.getLocation().getWorld());

         if (var1.getSpawnReason() == SpawnReason.EGG || var1.getSpawnReason() == SpawnReason.DISPENSE_EGG) {
            var1.setCancelled(true);
         }

         if (!SkyWars.getPlugin().getConfig().getBoolean("options.creaturespawn")) {
            var1.setCancelled(true);
         }
      }
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onWorldUnload(WorldUnloadEvent var1) {
      if (var1.isCancelled() && ArenaManager.getGame(var1.getWorld().getName()) != null) {
         var1.setCancelled(false);
      }

   }
}
