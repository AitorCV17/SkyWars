package me.aitor.skywars.events;

import me.aitor.skywars.arena.Arena;
import me.aitor.skywars.events.enums.SpectatorReason;
import me.aitor.skywars.player.SkyPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SkyPlayerSpectatorEvent extends Event {
   private static final HandlerList handlers = new HandlerList();
   private SkyPlayer player;
   private Arena game;
   private boolean spectate;
   private SpectatorReason reason;

   public SkyPlayerSpectatorEvent(SkyPlayer var1, Arena var2, boolean var3, SpectatorReason var4) {
      this.player = var1;
      this.game = var2;
      this.spectate = var3;
      this.reason = var4;
   }

   public SkyPlayer getPlayer() {
      return this.player;
   }

   public Arena getGame() {
      return this.game;
   }

   public boolean getSpectate() {
      return this.spectate;
   }

   public SpectatorReason getReason() {
      return this.reason;
   }

   public boolean isDeathReason() {
      return this.reason == SpectatorReason.DEATH;
   }

   public boolean isJoinReason() {
      return this.reason == SpectatorReason.JOIN;
   }

   public boolean isLeaveReason() {
      return this.reason == SpectatorReason.LEAVE;
   }

   public HandlerList getHandlers() {
      return handlers;
   }

   public static HandlerList getHandlerList() {
      return handlers;
   }
}
