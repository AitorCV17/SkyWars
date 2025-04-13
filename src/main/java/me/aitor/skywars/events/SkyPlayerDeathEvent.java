package me.aitor.skywars.events;

import me.aitor.skywars.arena.Arena;
import me.aitor.skywars.player.SkyPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PlayerDeathEvent;

public class SkyPlayerDeathEvent extends Event {
   private static final HandlerList handlers = new HandlerList();
   private SkyPlayer player;
   private SkyPlayer killer;
   private Arena game;
   private PlayerDeathEvent originalEvent;

   public SkyPlayerDeathEvent(SkyPlayer var1, SkyPlayer var2, Arena var3, PlayerDeathEvent var4) {
      this.player = var1;
      this.killer = var2;
      this.game = var3;
      this.originalEvent = var4;
   }

   public SkyPlayer getPlayer() {
      return this.player;
   }

   public SkyPlayer getKiller() {
      return this.killer;
   }

   public Arena getGame() {
      return this.game;
   }

   public PlayerDeathEvent getDeathEvent() {
      return this.originalEvent;
   }

   public HandlerList getHandlers() {
      return handlers;
   }

   public static HandlerList getHandlerList() {
      return handlers;
   }
}
