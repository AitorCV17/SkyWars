package me.aitor.skywars.listener.skywars;

import me.aitor.skywars.SkyWars;
import me.aitor.skywars.arena.Arena;
import me.aitor.skywars.events.SkyPlayerSpectatorEvent;
import me.aitor.skywars.events.enums.ArenaLeaveCause;
import me.aitor.skywars.events.enums.SpectatorReason;
import me.aitor.skywars.player.SkyPlayer;
import me.aitor.skywars.utils.BungeeUtils;
import me.aitor.skywars.utils.MSG;
import me.aitor.skywars.utils.LocationUtil;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SpectateListener implements Listener {

   @EventHandler
   public void onSkyPlayerSpectator(SkyPlayerSpectatorEvent event) {
      SkyPlayer player = event.getPlayer();
      Arena arena = event.getGame();

      if (!event.isLeaveReason() && event.getSpectate()) {
         String spawnPath = arena.getSpectatorSpawnRaw();
         Location spectatorSpawn = spawnPath != null ? LocationUtil.getLocation(spawnPath) : null;

         if (event.getReason() == SpectatorReason.DEATH) {
            if (SkyWars.is18orHigher()) {
               setSpectator(player, spectatorSpawn, false);
            } else if (SkyWars.isBungeeMode()) {
               arena.removePlayer(player, ArenaLeaveCause.SPECTATOR_IN_LOWER_VERSION);
               BungeeUtils.teleToServer(player.getPlayer(), SkyWars.getMessage(MSG.PLAYER_TELEPORT_LOBBY), SkyWars.getRandomLobby());
            } else {
               arena.removePlayer(player, ArenaLeaveCause.SPECTATOR_IN_LOWER_VERSION);
            }
         }

         if (event.isJoinReason()) {
            if (SkyWars.is18orHigher()) {
               setSpectator(player, spectatorSpawn, true);
            } else if (SkyWars.isBungeeMode()) {
               arena.removePlayer(player, ArenaLeaveCause.SPECTATOR_JOIN_IN_LOWER_VERSION);
               BungeeUtils.teleToServer(player.getPlayer(), SkyWars.getMessage(MSG.PLAYER_TELEPORT_LOBBY), SkyWars.getRandomLobby());
            } else {
               arena.removePlayer(player, ArenaLeaveCause.SPECTATOR_JOIN_IN_LOWER_VERSION);
            }
         }

      } else if (player.getPlayer().getGameMode() != GameMode.SURVIVAL) {
         player.getPlayer().setGameMode(GameMode.SURVIVAL);
      }
   }

   private void setSpectator(SkyPlayer player, Location spawn, boolean sendMessage) {
      if (player.getPlayer().getGameMode() != GameMode.SPECTATOR) {
         player.getPlayer().setGameMode(GameMode.SPECTATOR);
         if (sendMessage) {
            player.sendMessage(SkyWars.getMessage(MSG.PLAYER_DEATH));
         }
      }
      if (spawn != null) {
         player.getPlayer().teleport(spawn);
      }
   }
}
