package me.aitor.skywars.listener;

import me.aitor.skywars.SkyWars;
import me.aitor.skywars.arena.Arena;
import me.aitor.skywars.arena.ArenaManager;
import me.aitor.skywars.arena.GameQueue;
import me.aitor.skywars.config.ConfigManager;
import me.aitor.skywars.events.enums.ArenaJoinCause;
import me.aitor.skywars.events.enums.ArenaLeaveCause;
import me.aitor.skywars.menus.arena.MenuKitSelector;
import me.aitor.skywars.menus.arena.MenuSettings;
import me.aitor.skywars.menus.arena.MenuTracker;
import me.aitor.skywars.menus.arena.MenuVote;
import me.aitor.skywars.menus.lobby.MenuShop;
import me.aitor.skywars.player.SkyPlayer;
import me.aitor.skywars.sign.SignManager;
import me.aitor.skywars.sign.SkySign;
import me.aitor.skywars.utils.ItemBuilder;
import me.aitor.skywars.utils.Utils;
import me.aitor.skywars.utils.sky.SkyHologram;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;

public class LoginListener implements Listener {
   @EventHandler(
           priority = EventPriority.HIGHEST,
           ignoreCancelled = true
   )
   public void onAsnycPrePlayerLogin(AsyncPlayerPreLoginEvent var1) {
      if (!SkyWars.login) {
         var1.disallow(Result.KICK_OTHER, "Inténtalo más tarde... el servidor se está cargando");
      }

   }

   @EventHandler
   public void onPlayerTimeLogin(PlayerLoginEvent var1) {
      long var2 = SkyWars.seconds;
      long var4 = (new Date()).getTime();
      long var6 = var4 - var2;
      if (var6 < 1500L) {
         var1.setKickMessage("Inténtalo más tarde... el servidor se está cargando");
         var1.setResult(org.bukkit.event.player.PlayerLoginEvent.Result.KICK_OTHER);
      }

   }

   @EventHandler
   public void onPlayerJoin(PlayerJoinEvent var1) {
      if (!SkyWars.firstJoin && !SkyWars.isBungeeMode()) {
         SkyWars.firstJoin = true;
         Bukkit.getScheduler().runTaskTimer(SkyWars.getPlugin(), () -> {
            Iterator<SkySign> signIterator = SignManager.getSigns().iterator();
            while (signIterator.hasNext()) {
               SkySign sign = signIterator.next();
               SignManager.updateSign(sign);
            }
         }, 20L, 6000L);
      }

      Player var2 = var1.getPlayer();
      if (Bukkit.getScoreboardManager().getMainScoreboard() != null && Bukkit.getScoreboardManager().getMainScoreboard() == var2.getScoreboard()) {
         var2.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
      }

      if (var2.getScoreboard() == null) {
         var2.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
      }

      SkyPlayer var3 = new SkyPlayer(var2.getName(), var2.getUniqueId());
      SkyWars.skyPlayers.put(var2.getName(), var3);
      SkyWars.skyPlayersUUID.put(var2.getUniqueId(), var3);
      if (SkyWars.isBungeeMode() && ArenaManager.getGames().size() > 0) {
         Arena var4 = (Arena)ArenaManager.getGames().iterator().next();
         if (var4 != null) {
            var4.addPlayer(var3, ArenaJoinCause.LOGIN);
         } else {
            SkyWars.goToSpawn(var3);
         }
      }

      if (SkyWars.isBungeeMode() && !var2.hasPlayedBefore()) {
         SkyWars.goToSpawn(var3);
      }

      Bukkit.getScheduler().runTaskLater(SkyWars.getPlugin(), () -> {
         if ((SkyWars.isMultiArenaMode() && !var3.isInArena() || SkyWars.isLobbyMode()) && ConfigManager.shop.getBoolean("item.enabled")) {
            ItemBuilder var2x = Utils.readItem(ConfigManager.shop.getString("item.item"));
            var2x.setTitle(ConfigManager.shop.getString("item.name")).setLore(ConfigManager.shop.getStringList("item.lore"));
            var2.getInventory().setItem(ConfigManager.shop.getInt("item.inventorySlot"), var2x.build());
         }

      }, 5L);
      new MenuKitSelector(var2);
      new MenuTracker(var2);
      new MenuVote(var2);
      new MenuSettings(var2);
      new MenuShop(var2);
      if (SkyWars.getUpdate() && var2.hasPermission("skywars.admin")) {
         var2.sendMessage(SkyWars.checkUpdate("6525"));
      }

      if ((SkyWars.isMultiArenaMode() || SkyWars.isLobbyMode()) && SkyWars.getPlugin().getConfig().getBoolean("options.forceLobbySpawn")) {
         SkyWars.goToSpawn(var3);
      }

   }

   @EventHandler
   public void onPlayerLeave(PlayerQuitEvent var1) {
      if (!SkyWars.getPlugin().getConfig().getBoolean("options.leaveMessage")) {
         var1.setQuitMessage((String)null);
      }

      Player var2 = var1.getPlayer();
      var2.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
      SkyPlayer var3 = SkyWars.getSkyPlayer(var2);
      if (var3 != null) {
         GameQueue.removePlayer(var3);
         if (var3.isInArena()) {
            if (DamageListener.lastDamage.containsKey(var2.getUniqueId())) {
               Player var4 = Bukkit.getPlayer((UUID)DamageListener.lastDamage.get(var2.getUniqueId()));
               var2.damage(1000.0D, var4);
               var3.addDeaths(1);
            }

            Arena var5 = var3.getArena();
            var5.removePlayer(var3, ArenaLeaveCause.LEAVE);
            SkyWars.log("LoginListener.onPlayerLeave - " + var3.getName() + " removed in quit");
         }

         var3.upload(false);
         SkyHologram.removeHologram(var3);
         SkyWars.skyPlayers.remove(var2.getName());
         SkyWars.skyPlayersUUID.remove(var2.getUniqueId());
      }
   }

   @EventHandler
   public void onPlayerKick(PlayerKickEvent var1) {
      if (!SkyWars.getPlugin().getConfig().getBoolean("options.leaveMessage")) {
         var1.setLeaveMessage((String)null);
      }

      Player var2 = var1.getPlayer();
      SkyPlayer var3 = SkyWars.getSkyPlayer(var2);
      if (var3 != null) {
         GameQueue.removePlayer(var3);
         if (var3.isInArena()) {
            if (DamageListener.lastDamage.containsKey(var2.getUniqueId())) {
               Player var4 = Bukkit.getPlayer((UUID)DamageListener.lastDamage.get(var2.getUniqueId()));
               var2.damage(1000.0D, var4);
               var3.addDeaths(1);
            }

            Arena var5 = var3.getArena();
            var5.removePlayer(var3, ArenaLeaveCause.KICK);
            SkyWars.log("LoginListener.onPlayerKick - " + var3.getName() + " removed in kick");
         }

         var3.upload(false);
         SkyHologram.removeHologram(var3);
         SkyWars.skyPlayers.remove(var2.getName());
         SkyWars.skyPlayersUUID.remove(var2.getUniqueId());
      }
   }

   @EventHandler(
           priority = EventPriority.MONITOR
   )
   public void disableJoinMessage(PlayerJoinEvent var1) {
      if (!SkyWars.getPlugin().getConfig().getBoolean("options.joinMessage")) {
         var1.setJoinMessage((String)null);
      }

   }

   @EventHandler(
           priority = EventPriority.MONITOR
   )
   public void disableQuitMessage(PlayerQuitEvent var1) {
      if (!SkyWars.getPlugin().getConfig().getBoolean("options.leaveMessage")) {
         var1.setQuitMessage((String)null);
      }

   }
}
