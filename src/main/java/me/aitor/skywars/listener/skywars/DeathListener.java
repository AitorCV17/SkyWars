package me.aitor.skywars.listener.skywars;

import me.aitor.skywars.SkyWars;
import me.aitor.skywars.arena.Arena;
import me.aitor.skywars.events.SkyPlayerDeathEvent;
import me.aitor.skywars.events.SkyPlayerSpectatorEvent;
import me.aitor.skywars.events.enums.SpectatorReason;
import me.aitor.skywars.listener.DamageListener;
import me.aitor.skywars.player.SkyPlayer;
import me.aitor.skywars.utils.MSG;
import me.aitor.skywars.utils.economy.SkyEconomyManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class DeathListener implements Listener {
   @EventHandler
   public void onSkyPlayerDeath(SkyPlayerDeathEvent event) {
      Bukkit.getScheduler().runTaskAsynchronously(SkyWars.getPlugin(), () -> {
         SkyPlayer player = event.getPlayer();
         SkyPlayer killer = event.getKiller();
         Arena arena = event.getGame();

         DamageListener.lastDamage.remove(player.getUniqueId());
         player.addDeaths(1);
         player.playedTimeEnd();
         player.distanceWalkedConvert();

         if (killer != null) {
            if (!player.getName().equals(killer.getName())) {
               killer.addKills(1);
               arena.broadcast(String.format(SkyWars.getMessage(MSG.GAME_PLAYER_DEATH_PLAYER), player.getName(), killer.getName()));
               SkyEconomyManager.addCoins(killer.getPlayer(), SkyWars.getPlugin().getConfig().getInt("reward.kill"), true);
               arena.addKillStreak(killer);
            } else {
               arena.broadcast(String.format(SkyWars.getMessage(MSG.GAME_PLAYER__DEATH_OTHER), player.getName()));
            }
         } else {
            arena.broadcast(String.format(SkyWars.getMessage(MSG.GAME_PLAYER__DEATH_OTHER), player.getName()));
         }

         player.sendMessage(SkyWars.getMessage(MSG.PLAYER_DEATH));
         arena.broadcast(String.format(SkyWars.getMessage(MSG.GAME_PLAYERS_REMAIN), arena.getAlivePlayers()));

         Bukkit.getScheduler().runTask(SkyWars.getPlugin(), () -> {
            ArenaListener.checkWinner(arena);
         });

         arena.getAlivePlayer().forEach(survivor -> {
            SkyEconomyManager.addCoins(survivor.getPlayer(), SkyWars.getPlugin().getConfig().getInt("reward.death"), true);
         });

         Bukkit.getScheduler().runTask(SkyWars.getPlugin(), () -> {
            SkyPlayerSpectatorEvent specEvent = new SkyPlayerSpectatorEvent(player, arena, true, SpectatorReason.DEATH);
            Bukkit.getPluginManager().callEvent(specEvent);
         });
      });
   }
}
