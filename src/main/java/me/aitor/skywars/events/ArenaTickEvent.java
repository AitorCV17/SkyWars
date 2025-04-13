package me.aitor.skywars.events;

import me.aitor.skywars.arena.Arena;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ArenaTickEvent extends Event {
   private static final HandlerList handlers = new HandlerList();
   private Arena arena;

   public ArenaTickEvent(Arena var1) {
      this.arena = var1;
   }

   public Arena getArena() {
      return this.arena;
   }

   public HandlerList getHandlers() {
      return handlers;
   }

   public static HandlerList getHandlerList() {
      return handlers;
   }
}
